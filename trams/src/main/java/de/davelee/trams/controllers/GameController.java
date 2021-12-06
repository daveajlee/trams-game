package de.davelee.trams.controllers;

import java.time.LocalDateTime;
import java.time.Month;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
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

    //Cache game model to improve performance.
	private GameModel cachedGameModel;

	/**
	 * Withdraw the specified amount from the balance of the current game.
	 * @param amount a <code>double</code> with the amount to withdraw from the balance.
	 */
	public void withdrawBalance ( final double amount ) {
		gameService.withdrawOrCreditBalance(-amount, cachedGameModel.getPlayerName());
		//Update cache.
		updateCache();
	}

	/**
	 * Credit the specified amount to the balance of the current game.
	 * @param amount a <code>double</code> with the amount to credit to the balance
	 */
	public void creditBalance ( final double amount ) {
		gameService.withdrawOrCreditBalance(amount, cachedGameModel.getPlayerName());
		//Update cache.
		updateCache();
	}

	/**
	 * This method updates the cache by retrieving the current game model from the database after changes.
	 */
	private void updateCache() {
		cachedGameModel = gameService.getGameByPlayerName(cachedGameModel.getCompany(), cachedGameModel.getPlayerName());
	}

	/**
	 * Create a new game for the specified player running the specified scenario.
	 * @param playerName a <code>String</code> with the player name for this new game.
	 * @param scenarioName a <code>String</code> with the scenario to use for this new game.
	 * @return a <code>GameModel</code> representing the new game which was created.
	 */
	public GameModel createGameModel ( final String playerName, final String scenarioName ) {
		//There can only be one game!
		gameService.deleteAllGames();
		//Create game.
		LocalDateTime startDateTime = LocalDateTime.of(2017, Month.MARCH,1,4,0);
		GameModel gameModel = GameModel.builder()
				.balance(80000.00)
				.currentDateTime(startDateTime)
				.difficultyLevel(DifficultyLevel.EASY)
				.playerName(playerName)
				.scenarioName(scenarioName)
				.timeIncrement(15)
				.passengerSatisfaction(100)
				.build();
		//Save game to db, update cache and return it.
		gameService.saveGame(gameModel);
		cachedGameModel = gameModel;
		return gameModel;
	}

	/**
	 * This method loads a game model from a saved file. It overwrites any existing game models in the database!
	 * @param gameModel a <code>GameModel</code> containing the game to load.
	 */
	public void loadGameModel ( final GameModel gameModel ) {
		//There can only be one game!
		gameService.deleteAllGames();
		//Save game to db and update cache.
		gameService.saveGame(gameModel);
		cachedGameModel = gameModel;
	}

	/**
	 * This method returns the current game model from the cache.
	 * @return a <code>GameModel</code> representing the current game being played.
	 */
	public GameModel getGameModel ( ) {
		return cachedGameModel;
	}

	/**
	 * This method returns the name of the player playing the current game.
	 * @return a <code>String</code> with the player name.
	 */
	public String getCurrentPlayerName ( ) {
		return cachedGameModel.getPlayerName();
	}

	/**
	 * Resume the simulation!
	 * @param controlScreen a <code>ControlScreen</code> to update whilst running the simulation.
	 */
    public void resumeSimulation ( final ControlScreen controlScreen ) {
		simulationRunning = true;
		end = false;
		runningThread = new GameThread("SimThread", this, 2000, controlScreen);
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

	/**
	 * This method checks if the simulation is currently running.
	 * @return a <code>boolean</code> which is true iff the simulation is still running.
	 */
	public boolean stillRunning ( ) {
		return end;
	}

	/**
	 * Increment the time for the currently running simulation.
	 * @return a <code>LocalDateTime</code> with the new time after incrementing it.
	 */
	public LocalDateTime incrementTime ( ) {
		LocalDateTime newTime = gameService.incrementTime(cachedGameModel.getCompany());
		updateCache();
		return newTime;
	}

	/**
	 * This method starts running the simulation.
	 * @param controlScreen a <code>ControlScreen</code> to update whilst running the simulation.
	 */
	public void runSimulation ( final ControlScreen controlScreen ) {
		//Finally, run simulation
		end = false;
		runningThread = new GameThread("simThread", this, 2000, controlScreen);
		runningThread.start();
	}

	/**
	 * Compute and return the passenger satisfaction for the current game.
	 * @return a <code>int</code> with the current level of passenger satisfaction between 0 and 100.
	 */
	public double computeAndReturnPassengerSatisfaction ( ) {
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
		return gameService.computeAndReturnPassengerSatisfaction(cachedGameModel.getPlayerName(), cachedGameModel.getDifficultyLevel(), numSmallLateSchedules, numMediumLateSchedules, numLargeLateSchedules);
	}

}
