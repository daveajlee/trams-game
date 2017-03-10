package de.davelee.trams.services;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import de.davelee.trams.dao.GameDao;
import de.davelee.trams.data.Game;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.DifficultyLevel;

public class GameService {

    private GameDao gameDao;
	
	public GameService() {
	}

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

	public Game createGame ( String playerName, String scenarioName ) {
        return createGame(playerName, scenarioName, 80000.00, 100, DifficultyLevel.EASY);
	}

    public Game createGame ( String playerName, String scenarioName, double balance, int passengerSatisfaction, DifficultyLevel difficultyLevel ) {
        Game game = new Game();
        game.setPlayerName(playerName);
        game.setBalance(balance);
        game.setPassengerSatisfaction(passengerSatisfaction);
        game.setScenarioName(scenarioName);
        game.setDifficultyLevel(difficultyLevel);
        game.setCurrentTime(new GregorianCalendar(2009,Calendar.AUGUST,20,5,0,0));
        game.setTimeIncrement(15);
        game.setPreviousTime((Calendar) game.getCurrentTime().clone());
        return game;
    }

    public Game getGame ( )  {
        return gameDao.getCurrentGame();
    }
    
    /**
     * Deduct money from the balance.
     * @param amount a <code>double</code> with the amount to deduct from the balance.
     */
    public void withdrawBalance ( double amount ) {
        getGame().setBalance(getGame().getBalance()-amount);
    }
    
    /**
     * Add money to the balance.
     * @param amount a <code>double</code> with the amount to credit the balance.
     */
    public void creditBalance ( double amount ) {
        getGame().setBalance(getGame().getBalance()+amount);
    }

    /**
     * Compute passenger satisfaction for the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @param difficultyLevel a <code>String</code> with the difficulty level.
     */
    public int computeAndReturnPassengerSatisfaction ( final int numSmallLateSchedules, final int numMediumLateSchedules, final int numLargeLateSchedules ) {
        int totalSubtract = 0; Game game = getGame();

        //Easy: numSmallLateSchedules / 2 and numMediumLateSchedules and numLargeLateSchedules*2.
        if ( game.getDifficultyLevel() == DifficultyLevel.EASY ) {
            totalSubtract = (numSmallLateSchedules/2) + numMediumLateSchedules + (numLargeLateSchedules*2);
        }
        else if ( game.getDifficultyLevel() == DifficultyLevel.INTERMEDIATE ) {
            totalSubtract = (numSmallLateSchedules) + (numMediumLateSchedules*2) + (numLargeLateSchedules*3);
        }
        else if ( game.getDifficultyLevel() == DifficultyLevel.MEDIUM ) {
            totalSubtract = (numSmallLateSchedules*2) + (numMediumLateSchedules*3) + (numLargeLateSchedules*4);
        }
        else if ( game.getDifficultyLevel() == DifficultyLevel.HARD ) {
            totalSubtract = (numSmallLateSchedules*3) + (numMediumLateSchedules*4) + (numLargeLateSchedules*5);
        }
        //Subtract from passenger satisfaction.
        game.setPassengerSatisfaction(game.getPassengerSatisfaction() - totalSubtract);
        //After all of this check that passenger satisfaction is greater than 0.
        if ( game.getPassengerSatisfaction() < 0 ) { game.setPassengerSatisfaction(0); }
        return game.getPassengerSatisfaction();
    }

    //TODO: Remove once File Service no longer needed.
    public Hashtable<String, String> getGameAsString ( ) {
        Game game = getGame();
        Hashtable<String, String> gameTable = new Hashtable<String, String>();
        gameTable.put("DifficultyLevel", game.getDifficultyLevel().name());
        gameTable.put("PassengerSatisfaction", "" + game.getPassengerSatisfaction());
        gameTable.put("PlayerName", game.getPlayerName());
        gameTable.put("scenarioName", game.getScenarioName());
        gameTable.put("Balance", "" + game.getBalance());
        return gameTable;
    }

    public DifficultyLevel getDifficultyLevel ( ) {
        return getGame().getDifficultyLevel();
    }

    public void setDifficultyLevel ( DifficultyLevel difficultyLevel ) {
        getGame().setDifficultyLevel(difficultyLevel);
    }

    public double getCurrentBalance ( ) {
        return getGame().getBalance();
    }

    public String getScenarioName ( ) {
        return getGame().getScenarioName();
    }

    public String getPlayerName ( ) {
        return getGame().getPlayerName();
    }


    /**
     * Increment the current time.
     */
    public void incrementTime ( ) {
        Game game = getGame();
        //Copy previous time first.
        game.setPreviousTime(getCurrentTime());
        //Increment time.
        Calendar newCurrentTime = game.getCurrentTime();
        newCurrentTime.add(Calendar.MINUTE, game.getTimeIncrement());
        game.setCurrentTime(newCurrentTime);
        gameDao.createAndStoreGame(game);
    }

    /**
     * Return the supplied calendar object as a formatted string.
     * @param calDate a <code>Calendar</code> object to format.
     * @return a <code>String</code> with the formatted string.
     */
     public String formatDateString ( Calendar calDate, DateFormats dateFormat ) {
        return dateFormat.getFormat().format(calDate.getTime());
     }

     public Calendar getCurrentTime ( ) {
        return (Calendar) getGame().getCurrentTime().clone();
     }

     public int getTimeIncrement ( ) {
        return getGame().getTimeIncrement();
     }

     public void setTimeIncrement ( final int timeIncrement ) {
        getGame().setTimeIncrement(timeIncrement);
     }

     public Calendar getPreviousTime ( ) {
        return (Calendar) getGame().getPreviousTime().clone();
     }
    
}
