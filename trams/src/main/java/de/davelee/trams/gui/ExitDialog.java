package de.davelee.trams.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ExitDialog {
	
	public void createExitDialog ( final JFrame currentFrame ) {
		//Confirm user did wish to exit.
	    int result = JOptionPane.showOptionDialog(currentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "No");
	    if ( result == JOptionPane.YES_OPTION ) {
	    	System.exit(0);
	    }

	}

}
