package de.davelee.trams.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import de.davelee.trams.api.request.AddTimeRequest;
import de.davelee.trams.api.request.AdjustBalanceRequest;
import de.davelee.trams.api.request.AdjustSatisfactionRequest;
import de.davelee.trams.api.response.BalanceResponse;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.SatisfactionRateResponse;
import de.davelee.trams.api.response.TimeResponse;
import de.davelee.trams.data.Game;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.business.url}")
    private String businessServerUrl;

    public void saveGame ( final GameModel gameModel ) {
        Game game = convertToGame(gameModel);
        gameRepository.deleteAll(); //Only one game at a time.
        gameRepository.saveAndFlush(game);
    }

    private Game convertToGame ( final GameModel gameModel ) {
        Game game = new Game();
        game.setPlayerName(gameModel.getPlayerName());
        game.setBalance(gameModel.getBalance());
        game.setPassengerSatisfaction(gameModel.getPassengerSatisfaction());
        game.setScenarioName(gameModel.getScenarioName());
        game.setDifficultyLevel(gameModel.getDifficultyLevel());
        game.setCurrentDateTime(gameModel.getCurrentDateTime());
        game.setTimeIncrement(gameModel.getTimeIncrement());
        return game;
    }

    public GameModel getGameByPlayerName ( final String company, final String playerName )  {
        CompanyResponse companyResponse = restTemplate.getForObject(businessServerUrl + "company/?name=" + company + "&playerName=" + playerName, CompanyResponse.class);
        if ( companyResponse != null ) {
            return convertToGameModel(companyResponse);
        }
        return null;
    }

     private GameModel convertToGameModel ( final CompanyResponse companyResponse ) {
        return GameModel.builder()
                .playerName(companyResponse.getPlayerName())
                .balance(companyResponse.getBalance())
                .passengerSatisfaction(companyResponse.getSatisfactionRate())
                .scenarioName(game.getScenarioName())
                .difficultyLevel(game.getDifficultyLevel())
                .currentDateTime(companyResponse.getTime())
                .timeIncrement(game.getTimeIncrement())
                .build();
    }
    
    /**
     * Deduct or add money to/from the balance.
     * @param amount a <code>double</code> with the amount to deduct (negative) or credit (positive) from the balance.
     * @param company a <code>String</code> with the company.
     */
    public void withdrawOrCreditBalance ( final double amount, final String company ) {
        BalanceResponse balanceResponse = restTemplate.patchForObject(businessServerUrl + "company/balance",
                AdjustBalanceRequest.builder()
                        .company(company)
                        .value(amount).build(),
                BalanceResponse.class);
    }

    /**
     * Compute passenger satisfaction for the current time.
     * @param company a <code>String</code> with the company.
     * @param difficultyLevel a <code>DifficultyLevel</code> object with the difficulty level.
     * @param numSmallLateSchedules a <code>int</code> with the number of route schedules running marginally late.
     * @param numMediumLateSchedules a <code>int</code> with the number of route schedules running substantially late.
     * @param numLargeLateSchedules a <code>int</code> with the number of route schedules running very late.
     * @return a <code>double</code> with the computed passenger satisfaction.
     */
    public double computeAndReturnPassengerSatisfaction ( final String company, final DifficultyLevel difficultyLevel, final int numSmallLateSchedules, final int numMediumLateSchedules, final int numLargeLateSchedules ) {
        int totalSubtract = 0;

        //Easy: numSmallLateSchedules / 2 and numMediumLateSchedules and numLargeLateSchedules*2.
        if ( difficultyLevel == DifficultyLevel.EASY ) {
            totalSubtract = (numSmallLateSchedules/2) + numMediumLateSchedules + (numLargeLateSchedules*2);
        }
        else if ( difficultyLevel == DifficultyLevel.INTERMEDIATE ) {
            totalSubtract = (numSmallLateSchedules) + (numMediumLateSchedules*2) + (numLargeLateSchedules*3);
        }
        else if ( difficultyLevel == DifficultyLevel.MEDIUM ) {
            totalSubtract = (numSmallLateSchedules*2) + (numMediumLateSchedules*3) + (numLargeLateSchedules*4);
        }
        else if ( difficultyLevel == DifficultyLevel.HARD ) {
            totalSubtract = (numSmallLateSchedules*3) + (numMediumLateSchedules*4) + (numLargeLateSchedules*5);
        }
        //Subtract from passenger satisfaction.
        SatisfactionRateResponse satisfactionRateResponse = restTemplate.patchForObject(businessServerUrl + "company/satisfaction",
                AdjustSatisfactionRequest.builder()
                        .company(company)
                        .satisfactionRate(totalSubtract).build(),
                SatisfactionRateResponse.class);
        return satisfactionRateResponse.getSatisfactionRate();
    }

    /**
     * Increment the current time.
     * @param company a <code>String</code> with the company.
     * @param timeIncrement a <code>int</code> with the number of minutes to increment.
     * @return a <code>LocalDateTime</code> containing the new time.
     */
    public LocalDateTime incrementTime (final String company, final int timeIncrement ) {
        //Get data from API.
        TimeResponse timeResponse = restTemplate.patchForObject(businessServerUrl + "company/time",
                AddTimeRequest.builder()
                        .company(company)
                        .minutes(timeIncrement).build(),
                TimeResponse.class);
        //Convert time.
        try {
            return LocalDateTime.parse(timeResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        } catch ( DateTimeParseException dateTimeParseException ) {
            return null;
        }
    }

    public GameModel[] getAllGames ( ) {
        List<Game> games = gameRepository.findAll();
        GameModel[] gameModels = new GameModel[games.size()];
        for ( int i = 0; i < gameModels.length; i++ ) {
            gameModels[i] = convertToGameModel(games.get(i));
        }
        return gameModels;
    }

    public void deleteAllGames ( ) {
        gameRepository.deleteAll();
    }
    
}
