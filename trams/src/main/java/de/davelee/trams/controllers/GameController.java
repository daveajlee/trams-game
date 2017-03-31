package de.davelee.trams.controllers;

import java.util.Calendar;

import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.GameThread;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.GameService;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
	
	@Autowired
	private GameService gameService;

	@Autowired
	private RouteController routeController;

	@Autowired
	private RouteScheduleController routeScheduleController;

	private boolean end = false;
    private Thread runningThread;
    private boolean simulationRunning = false;

	public void withdrawBalance ( final double amount, final String playerName ) {
		gameService.withdrawBalance(amount, playerName);
	}

	public void creditBalance ( final double amount, final String playerName ) {
		gameService.creditBalance(amount, playerName);
	}

	public GameModel getGameModel() {
		return getGameModelByPlayerName(getCurrentPlayerName());
	}

	public GameModel createGameModel ( final String playerName, final String scenarioName ) {
		GameModel gameModel = new GameModel();
		gameModel.setBalance(80000.00);
		gameModel.setCurrentTime(Calendar.getInstance());
		gameModel.setDifficultyLevel(DifficultyLevel.EASY);
		gameModel.setPlayerName(playerName);
		gameModel.setScenarioName(scenarioName);
		gameModel.setPreviousTime(Calendar.getInstance());
		gameModel.setTimeIncrement(15);
		gameModel.setPassengerSatisfaction(100);
		gameService.saveGame(gameModel);
		return getGameModel();
	}

	public GameModel getGameModelByPlayerName ( final String playerName ) {
		return gameService.getGameByPlayerName(playerName);
	}

	public String getCurrentPlayerName ( ) {
		return gameService.getCurrentPlayerName();
	}

	/**
	 * Resume the simulation!
	 */
    public void resumeSimulation ( ) {
		simulationRunning = true;
		//logger.debug("Resuming - Setting isEnd to false");
		end = false;
		runningThread = new Thread("SimThread");
		runningThread.start();
		//runSimulation(theControlScreen, theOperations.getSimulator());
	}

	/**
	 * Pause the simulation!
	 * @return a <code>boolean</code> which is true iff the simulation was successfully paused.
	 */
	public boolean pauseSimulation ( ) {
		if ( simulationRunning ) {
			simulationRunning = false;
			//logger.debug("Pausing - Setting isEnd to true in " + this.toString());
			end = true;
			return true;
		}
		return false;
	}

	public boolean stillRunning ( ) {
		return end;
	}

	public void incrementTime ( final String playerName ) {
		gameService.incrementTime(playerName);
	}

	public void runSimulation ( ) {
		//Finally, run simulation
		end = false;
		runningThread = new GameThread("simThread");
		runningThread.start();
	}

	public String formatDateString ( final Calendar currentTime, final DateFormats dateFormat ) {
		return gameService.formatDateString(currentTime, dateFormat);
	}

	public int computeAndReturnPassengerSatisfaction ( final String playerName ) {
		//Essentially satisfaction is determined by the route schedules that are running on time.
		//Now count number of route schedules into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
		int numSmallLateSchedules = 0; int numMediumLateSchedules = 0; int numLargeLateSchedules = 0;
		//Now go through all routes.
		for ( RouteModel myRoute : routeController.getRouteModels() ) {
			for ( RouteScheduleModel mySchedule : routeScheduleController.getRouteSchedulesByRouteNumber(myRoute.getRouteNumber())) {
				//Running... 1 - 5 minutes late.
				if ( mySchedule.getDelay() > 0 && mySchedule.getDelay() < 6 ) {
					numSmallLateSchedules++;
				}
				//Running... 6 - 15 minutes late.
				else if ( mySchedule.getDelay() > 5 && mySchedule.getDelay() < 16 ) {
					numMediumLateSchedules++;
				}
				//Running... 16+ minutes late.
				else if ( mySchedule.getDelay() > 15 ) {
					numLargeLateSchedules++;
				}
			}
		}
		return gameService.computeAndReturnPassengerSatisfaction(playerName, numSmallLateSchedules, numMediumLateSchedules, numLargeLateSchedules);
	}

	public GameModel[] getAllGames () {
		return gameService.getAllGames();
	}

}
