package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Game;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.repository.GameRepository;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public void saveGame ( final GameModel gameModel ) {
        Game game = convertToGame(gameModel);
        game.getBalance();
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
        game.setCurrentTime(gameModel.getCurrentTime());
        game.setTimeIncrement(gameModel.getTimeIncrement());
        return game;
    }

    public GameModel getGameByPlayerName ( final String playerName )  {
        Game game = gameRepository.findByPlayerName(playerName);
        if ( game != null ) {
            return convertToGameModel(game);
        }
        return null;
    }

     private GameModel convertToGameModel ( final Game game ) {
        return GameModel.builder()
                .playerName(game.getPlayerName())
                .balance(game.getBalance())
                .passengerSatisfaction(game.getPassengerSatisfaction())
                .scenarioName(game.getScenarioName())
                .difficultyLevel(game.getDifficultyLevel())
                .currentTime(game.getCurrentTime())
                .timeIncrement(game.getTimeIncrement())
                .build();
    }
    
    /**
     * Deduct money from the balance.
     * @param amount a <code>double</code> with the amount to deduct from the balance.
     * @param playerName a <code>String</code> with the player name.
     */
    public void withdrawBalance ( final double amount, final String playerName ) {
        GameModel gameModel = getGameByPlayerName(playerName);
        gameRepository.findByPlayerName(playerName).setBalance(gameModel.getBalance()-amount);
    }
    
    /**
     * Add money to the balance.
     * @param amount a <code>double</code> with the amount to credit the balance.
     * @param playerName a <code>String</code> with the player name.
     */
    public void creditBalance ( final double amount, final String playerName ) {
        GameModel gameModel = getGameByPlayerName(playerName);
        Game game = gameRepository.findByPlayerName(playerName);
        game.setBalance(gameModel.getBalance()+amount);
        gameRepository.save(game);
    }

    /**
     * Compute passenger satisfaction for the current time.
     * @param playerName a <code>String</code> with the player name.
     * @param numSmallLateSchedules a <code>int</code> with the number of route schedules running marginally late.
     * @param numMediumLateSchedules a <code>int</code> with the number of route schedules running substantially late.
     * @param numLargeLateSchedules a <code>int</code> with the number of route schedules running very late.
     * @return a <code>int</code> with the computed passenger satisfaction.
     */
    public int computeAndReturnPassengerSatisfaction ( final String playerName, final int numSmallLateSchedules, final int numMediumLateSchedules, final int numLargeLateSchedules ) {
        int totalSubtract = 0;

        GameModel gameModel = getGameByPlayerName(playerName);

        //Easy: numSmallLateSchedules / 2 and numMediumLateSchedules and numLargeLateSchedules*2.
        if ( gameModel.getDifficultyLevel() == DifficultyLevel.EASY ) {
            totalSubtract = (numSmallLateSchedules/2) + numMediumLateSchedules + (numLargeLateSchedules*2);
        }
        else if ( gameModel.getDifficultyLevel() == DifficultyLevel.INTERMEDIATE ) {
            totalSubtract = (numSmallLateSchedules) + (numMediumLateSchedules*2) + (numLargeLateSchedules*3);
        }
        else if ( gameModel.getDifficultyLevel() == DifficultyLevel.MEDIUM ) {
            totalSubtract = (numSmallLateSchedules*2) + (numMediumLateSchedules*3) + (numLargeLateSchedules*4);
        }
        else if ( gameModel.getDifficultyLevel() == DifficultyLevel.HARD ) {
            totalSubtract = (numSmallLateSchedules*3) + (numMediumLateSchedules*4) + (numLargeLateSchedules*5);
        }
        //Subtract from passenger satisfaction.
        Game game = gameRepository.findByPlayerName(playerName);
        game.setPassengerSatisfaction(gameModel.getPassengerSatisfaction() - totalSubtract);
        //After all of this check that passenger satisfaction is greater than 0.
        if ( game.getPassengerSatisfaction() < 0 ) { game.setPassengerSatisfaction(0); }
        gameRepository.save(game);
        return game.getPassengerSatisfaction();
    }

    /**
     * Increment the current time.
     * @param playerName a <code>String</code> with the player name.
     * @return a <code>Calendar</code> containing the new time.
     */
    public Calendar incrementTime ( final String playerName ) {
        //Retrieve the game.
        Game game = gameRepository.findByPlayerName(playerName);
        //Increment time.
        Calendar newCurrentTime = game.getCurrentTime();
        newCurrentTime.add(Calendar.MINUTE, game.getTimeIncrement());
        game.setCurrentTime(newCurrentTime);
        //Save game and return the new time.
        gameRepository.save(game);
        return newCurrentTime;
    }

    public String getCurrentPlayerName ( ) {
        GameModel[] gameModels = getAllGames();
        if ( gameModels.length > 0 ) {
            return gameModels[0].getPlayerName();
        }
        return null;
    }

    @Transactional
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
