package de.davelee.trams.util;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;

public class GameThread extends Thread implements Runnable {
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private int simulationSpeed;
	
	public GameThread ( final String name ) {
		super(name);
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
			gameController.incrementTime(gameController.getCurrentPlayerName());
            //controlScreen.drawVehicles(true);
            //Now sleep!
            try { this.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        }
    }

}
