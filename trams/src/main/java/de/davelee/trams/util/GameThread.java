package de.davelee.trams.util;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.gui.ControlScreen;

public class GameThread extends Thread implements Runnable {

	private GameController gameController;
	private ControlScreen controlScreen;

	private int simulationSpeed;
	
	public GameThread ( final String name, final GameController gameController, final int simulationSpeed, final ControlScreen controlScreen ) {
		super(name);
		this.gameController = gameController;
		this.simulationSpeed = simulationSpeed;
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
            //Increment time and update passenger satisfaction.
			controlScreen.updateDateTime(gameController.incrementTime(), gameController.getGameModel().getDifficultyLevel());
			controlScreen.updatePassengerBar(gameController.computeAndReturnPassengerSatisfaction());
            //Now sleep!
            try { this.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        }
    }

}
