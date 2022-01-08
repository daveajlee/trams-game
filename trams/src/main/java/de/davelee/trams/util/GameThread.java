package de.davelee.trams.util;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.controllers.CompanyController;
import de.davelee.trams.controllers.SimulationController;
import de.davelee.trams.gui.ControlScreen;

/**
 * This class represents a thread which runs the simulation independently from the GUI.
 * @author Dave Lee
 */
public class GameThread extends Thread implements Runnable {

	private final CompanyController companyController;
	private final SimulationController simulationController;
	private final ControlScreen controlScreen;

	private final int simulationSpeed;

	/**
	 * Create a new thread to run the simulation.
	 * @param name a <code>String</code> with the name for this thread.
	 * @param companyController a <code>CompanyController</code> object which allows access to company data.
	 * @param simulationController a <code>SimulationController</code> object which controls the simulation.
	 * @param simulationSpeed a <code>int</code> containing the number of milliseconds to pause before running the thread again.
	 * @param controlScreen a <code>ControlScreen</code> object containing the GUI to update.
	 */
	public GameThread (final String name, final CompanyController companyController, final SimulationController simulationController, final int simulationSpeed, final ControlScreen controlScreen ) {
		super(name);
		this.companyController = companyController;
		this.simulationController = simulationController;
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
        while ( !simulationController.stillRunning() ) {
			//Get company response.
			CompanyResponse companyResponse = companyController.getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
            //Increment time and update passenger satisfaction.
			controlScreen.updateDateTime(companyController.incrementTime(controlScreen.getCompany()),
					companyResponse.getDifficultyLevel(), controlScreen.getCompany());
			controlScreen.updatePassengerBar((int) Math.round(companyController.computeAndReturnPassengerSatisfaction(controlScreen.getCompany(), companyResponse.getDifficultyLevel())));
            //Now sleep!
            try { this.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        }
    }

}
