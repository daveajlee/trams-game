package de.davelee.trams.services;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import de.davelee.trams.data.Game;
import de.davelee.trams.data.Route;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.util.DifficultyLevel;

public class GameService {
	
	private Game game;
    private DatabaseManager databaseManager;
	
	public GameService() {
	}

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

	public void createGame ( String playerName, String scenarioName ) {
        createGame(playerName, scenarioName, 80000.00, 100, DifficultyLevel.EASY);
	}

    public void createGame ( String playerName, String scenarioName, double balance, int passengerSatisfaction, DifficultyLevel difficultyLevel ) {
        game = new Game();
        game.setPlayerName(playerName);
        game.setBalance(balance);
        game.setPassengerSatisfaction(passengerSatisfaction);
        game.setScenarioName(scenarioName);
        game.setDifficultyLevel(difficultyLevel);
    }
    
    /**
     * Deduct money from the balance.
     * @param amount a <code>double</code> with the amount to deduct from the balance.
     */
    public void withdrawBalance ( double amount ) {
    	game.setBalance(game.getBalance()-amount);
    }
    
    /**
     * Add money to the balance.
     * @param amount a <code>double</code> with the amount to credit the balance.
     */
    public void creditBalance ( double amount ) {
        game.setBalance(game.getBalance()+amount);
    }

    /**
     * Compute passenger satisfaction for the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @param difficultyLevel a <code>String</code> with the difficulty level.
     */
    public int computeAndReturnPassengerSatisfaction ( Calendar currentTime, List<Route> routes ) {
        //Essentially satisfaction is determined by the route schedules that are running on time.
        //Now count number of route schedules into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
        int numSmallLateSchedules = 0; int numMediumLateSchedules = 0; int numLargeLateSchedules = 0;
        //Now go through all routes.
        for ( Route myRoute : routes ) {
            for ( RouteSchedule mySchedule : databaseManager.getRouteSchedulesByRouteId(myRoute.getId())) {
        		//Running... 1 - 5 minutes late.
                if ( mySchedule.getDelayInMins() > 0 && mySchedule.getDelayInMins() < 6 ) {
                    numSmallLateSchedules++;
                }
                //Running... 6 - 15 minutes late.
                else if ( mySchedule.getDelayInMins() > 5 && mySchedule.getDelayInMins() < 16 ) {
                    numMediumLateSchedules++;
                }
                //Running... 16+ minutes late.
                else if ( mySchedule.getDelayInMins() > 15 ) {
                    numLargeLateSchedules++;
                }
        	}
        }
        int totalSubtract = 0;

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
        Hashtable<String, String> gameTable = new Hashtable<String, String>();
        gameTable.put("DifficultyLevel", game.getDifficultyLevel().name());
        gameTable.put("PassengerSatisfaction", "" + game.getPassengerSatisfaction());
        gameTable.put("PlayerName", game.getPlayerName());
        gameTable.put("scenarioName", game.getScenarioName());
        gameTable.put("Balance", "" + game.getBalance());
        return gameTable;
    }

    public DifficultyLevel getDifficultyLevel ( ) {
        return game.getDifficultyLevel();
    }

    public void setDifficultyLevel ( DifficultyLevel difficultyLevel ) {
        game.setDifficultyLevel(difficultyLevel);
    }

    public double getCurrentBalance ( ) {
        return game.getBalance();
    }

    public String getScenarioName ( ) {
        return game.getScenarioName();
    }

    public String getPlayerName ( ) {
        return game.getPlayerName();
    }
    
}
