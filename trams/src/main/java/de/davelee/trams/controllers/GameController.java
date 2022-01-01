package de.davelee.trams.controllers;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import de.davelee.trams.api.request.CompanyRequest;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.gui.ControlScreen;
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
	private VehicleController vehicleController;

	private boolean end = false;
    private Thread runningThread;
    private boolean simulationRunning = false;

	/**
	 * Withdraw the specified amount from the balance of the current game.
	 * @param amount a <code>double</code> with the amount to withdraw from the balance.
	 */
	public void withdrawBalance ( final double amount, final String playerName ) {
		gameService.withdrawOrCreditBalance(-amount, playerName);
	}

	/**
	 * Credit the specified amount to the balance of the current game.
	 * @param amount a <code>double</code> with the amount to credit to the balance
	 */
	public void creditBalance ( final double amount, final String playerName ) {
		gameService.withdrawOrCreditBalance(amount, playerName);
	}

	/**
	 * Create a new game for the specified player running the specified scenario.
	 * @param playerName a <code>String</code> with the player name for this new game.
	 * @param scenarioName a <code>String</code> with the scenario to use for this new game.
	 * @param company a <code>String</code> with the name of the company to use for this new game.
	 * @return a <code>GameModel</code> representing the new game which was created.
	 */
	public CompanyResponse createGameModel ( final String playerName, final String scenarioName, final String company ) {
		//Create game.
		LocalDateTime startDateTime = LocalDateTime.of(2017, Month.MARCH,1,4,0);
		//Save game to db, update cache and return it.
		gameService.saveGame(CompanyRequest.builder()
				.name(company)
				.startingBalance(80000.00)
				.startingTime("01-03-2017 04:00")
				.playerName(playerName)
				.difficultyLevel(DifficultyLevel.EASY.name())
				.scenarioName(scenarioName)
				.build());
		return gameService.getGameByPlayerName(company, playerName);
	}

	/**
	 * This method loads a game model from a saved file. It overwrites any existing game models in the database!
	 * @param companyResponse a <code>CompanyResponse</code> containing the game to load.
	 */
	public void loadGameModel ( final CompanyResponse companyResponse ) {
		//Save game to db and update cache.
		gameService.saveGame(CompanyRequest.builder()
				.name(companyResponse.getName())
				.startingBalance(companyResponse.getBalance())
				.startingTime(companyResponse.getTime())
				.playerName(companyResponse.getPlayerName())
				.difficultyLevel(companyResponse.getDifficultyLevel())
				.scenarioName(companyResponse.getScenarioName())
				.build());
	}

	/**
	 * This method returns the current game model from the cache.
	 * @return a <code>CompanyResponse</code> representing the current game being played.
	 */
	public CompanyResponse getGameModel (final String company, final String playerName ) {
		return gameService.getGameByPlayerName(company, playerName);
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
	 * @return a <code>String</code> with the new time after incrementing it.
	 */
	public String incrementTime ( final String company ) {
		return gameService.incrementTime(company, 15).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
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
	public double computeAndReturnPassengerSatisfaction ( final String company, final String difficultyLevel ) {
		//Essentially satisfaction is determined by the route schedules that are running on time.
		//Now count number of route schedules into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
		int numSmallLateSchedules = 0; int numMediumLateSchedules = 0; int numLargeLateSchedules = 0;
		//Now go through all vehicles.
		for ( VehicleResponse vehicleModel : vehicleController.getVehicleModels(company) ) {
			//Running... 1 - 5 minutes late.
			if ( vehicleModel.getDelayInMinutes() > 0 && vehicleModel.getDelayInMinutes() < 6 ) {
				numSmallLateSchedules++;
			}
			//Running... 6 - 15 minutes late.
			else if ( vehicleModel.getDelayInMinutes() > 5 && vehicleModel.getDelayInMinutes() < 16 ) {
				numMediumLateSchedules++;
			}
			//Running... 16+ minutes late.
			else if ( vehicleModel.getDelayInMinutes() > 15 ) {
				numLargeLateSchedules++;
			}
		}
		return gameService.computeAndReturnPassengerSatisfaction(company, DifficultyLevel.valueOf(difficultyLevel), numSmallLateSchedules, numMediumLateSchedules, numLargeLateSchedules);
	}

}
