package de.davelee.trams.controllers;

import java.util.Calendar;

import de.davelee.trams.gui.ControlScreen;
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
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 3);
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.HOUR_OF_DAY, 4);
		calendar.set(Calendar.MINUTE, 0);
		gameModel.setCurrentTime(calendar);
		gameModel.setDifficultyLevel(DifficultyLevel.EASY);
		gameModel.setPlayerName(playerName);
		gameModel.setScenarioName(scenarioName);
		gameModel.setTimeIncrement(15);
		gameModel.setPassengerSatisfaction(100);
		gameService.saveGame(gameModel);
		return getGameModel();
	}

	/**
	 * This method loads a game model from a saved file. It overwrites an existing game models in the database!
	 * @param gameModel a <code>GameModel</code> containing the game to load.
	 */
	public void loadGameModel ( final GameModel gameModel ) {
		gameService.saveGame(gameModel);
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
    public void resumeSimulation ( final ControlScreen controlScreen ) {
		simulationRunning = true;
		end = false;
		runningThread = new GameThread("SimThread", this, 2000, getCurrentPlayerName(), controlScreen);
		runningThread.start();
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

	public Calendar incrementTime ( final String playerName ) {
		return gameService.incrementTime(playerName);
	}

	public void runSimulation ( final ControlScreen controlScreen ) {
		//Finally, run simulation
		end = false;
		runningThread = new GameThread("simThread", this, 2000, getCurrentPlayerName(), controlScreen);
		runningThread.start();
	}

	/**
	 * Return the supplied calendar object as a formatted string.
	 * @param currentTime a <code>Calendar</code> object to format.
	 * @param dateFormat a <code>DateFormats</code> with the formats.
	 * @return a <code>String</code> with the formatted string.
	 */
	public String formatDateString ( final Calendar currentTime, final DateFormats dateFormat ) {
		return dateFormat.getFormat().format(currentTime.getTime());
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
