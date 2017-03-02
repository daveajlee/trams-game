package de.davelee.trams.services;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.davelee.trams.data.Game;
import de.davelee.trams.data.Route;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.data.Simulator;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.util.DifficultyLevel;

public class GameService {
	
	private Game game;
	
	private RouteScheduleService routeScheduleService;
	
	public GameService() {
		game = new Game();
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void createGame ( String playerName, String scenarioName ) {
        Game game = new Game();
        game.setPlayerName(playerName);
        game.setBalance(80000.00);
        game.setPassengerSatisfaction(100);
        game.setScenarioName(scenarioName);
        setGame(game);
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
    public void computePassengerSatisfaction ( Calendar currentTime, DifficultyLevel difficultyLevel, List<Route> routes ) {
        //Essentially satisfaction is determined by the route schedules that are running on time.
        //Now count number of route schedules into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
        int numSmallLateSchedules = 0; int numMediumLateSchedules = 0; int numLargeLateSchedules = 0;
        //Now go through all routes.
        for ( Route myRoute : routes ) {
        	for ( RouteSchedule mySchedule : myRoute.getRouteSchedules()) {
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
        game.setPassengerSatisfaction(game.getPassengerSatisfaction() - totalSubtract);
        //After all of this check that passenger satisfaction is greater than 0.
        if ( game.getPassengerSatisfaction() < 0 ) { game.setPassengerSatisfaction(0); }
    }
    
    /**
     * Check if any vehicles are presently running based on the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @return a <code>boolean</code> which is true iff at least one vehicle is running.
     */
    public boolean areAnyVehiclesRunning (Calendar currentTime, Simulator simulator, List<Vehicle> vehicles) {
        //Check if any vehicles are running....
        for ( Vehicle myVehicle : vehicles ) {
            //First one that is not in depot indicates that vehicles are running.
            if ( !routeScheduleService.getCurrentStopName(myVehicle.getRouteScheduleId(), currentTime, game.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                return true;
            }
        }
        //Otherwise, return false;
        return false;
    }
    
    /**
     * Check if a particular schedule id exists for a particular route.
     * @param schedId a <code>int</code> with the schedule id.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>boolean</code> which is true iff the schedule does exist.
     */
    public boolean doesSchedIdExist ( int schedId, String routeNumber ) {
        LinkedList<Integer> scheds = getGame().getScheduleIds().get(routeNumber);
        if ( scheds != null ) {
            for ( int i = 0; i < scheds.size(); i++ ) {
                if ( scheds.get(i) == schedId ) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
