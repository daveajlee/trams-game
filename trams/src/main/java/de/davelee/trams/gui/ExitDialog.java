package de.davelee.trams.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;

public class ExitDialog {
	
	@Autowired
	private GameController gameController;
	
	public void createExitDialog ( final JFrame currentFrame ) {
		//Confirm user did wish to exit.
	    boolean wasSimulationRunning = gameController.pauseSimulation();
	    int result = JOptionPane.showOptionDialog(currentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "No");
	    if ( result == JOptionPane.YES_OPTION ) {
	    	System.exit(0);
	    }
	    if (wasSimulationRunning) { gameController.resumeSimulation(); }
	}

}
