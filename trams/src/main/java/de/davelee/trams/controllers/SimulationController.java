package de.davelee.trams.controllers;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.util.GameThread;
import org.springframework.stereotype.Controller;

/**
 * This class controls the simulation of a company as a game in TraMS.
 * @author Dave Lee
 */
@Controller
public class SimulationController {

    private boolean end = false;
    private Thread runningThread;
    private boolean simulationRunning = false;

    /**
     * Resume the simulation!
     * @param controlScreen a <code>ControlScreen</code> to update whilst running the simulation.
     */
    public void resumeSimulation ( final ControlScreen controlScreen ) {
        simulationRunning = true;
        end = false;
        runningThread = new GameThread("SimThread", controlScreen.getControllerHandler().getCompanyController(), this, 2000, controlScreen);
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
     * This method starts running the simulation.
     * @param controlScreen a <code>ControlScreen</code> to update whilst running the simulation.
     */
    public void runSimulation ( final ControlScreen controlScreen ) {
        //Finally, run simulation
        end = false;
        runningThread = new GameThread("simThread", controlScreen.getControllerHandler().getCompanyController(), this, 2000, controlScreen);
        runningThread.start();
    }

}
