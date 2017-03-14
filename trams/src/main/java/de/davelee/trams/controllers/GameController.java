package de.davelee.trams.controllers;

import java.util.Calendar;

import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.GameThread;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.GameService;

public class GameController {
	
	@Autowired
	private GameService gameService;

	private boolean end = false;
    private Thread runningThread;
    private boolean simulationRunning = false;
	
	public void withdrawBalance ( double amount ) {
		gameService.withdrawBalance(amount);
	}
	
	/**
     * Get current simulated time. 
     * @return a <code>Calendar</code> representing the current simulated time.
     */
    public Calendar getCurrentSimTime ( ) {
        return gameService.getCurrentTime();
    }

	public String getScenarioName ( ) {
		return gameService.getScenarioName();
	}

	public void creditBalance ( double amount ) {
		gameService.creditBalance(amount);
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

	public void incrementTime ( ) {
		gameService.incrementTime();
	}

	public void runSimulation ( ) {
		//Finally, run simulation
		end = false;
		runningThread = new GameThread("simThread");
		runningThread.start();
	}

	public DifficultyLevel getDifficultyLevel ( ) {
		return gameService.getDifficultyLevel();
	}

}
