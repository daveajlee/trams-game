package de.davelee.trams.util;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.gui.ControlScreen;

public class GameThread extends Thread implements Runnable {

	private GameController gameController;
	private ControlScreen controlScreen;

	private int simulationSpeed;
	private String playerName;
	
	public GameThread ( final String name, final GameController gameController, final int simulationSpeed, final String playerName, final ControlScreen controlScreen ) {
		super(name);
		this.gameController = gameController;
		this.simulationSpeed = simulationSpeed;
		this.playerName = playerName;
		this.controlScreen = controlScreen;
	}
	
	/**
     * Run simulation until pause is called.
     */
    @SuppressWarnings("static-access")
    public void run() {
        //First of all, sleep for theSimulationSpeed seconds.
        try { this.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        //Keep running this until pause.
        while ( !gameController.stillRunning() ) {
            //Increment time.
			controlScreen.updateTime(gameController.incrementTime(playerName));
            //Now sleep!
            try { this.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        }
    }

}
