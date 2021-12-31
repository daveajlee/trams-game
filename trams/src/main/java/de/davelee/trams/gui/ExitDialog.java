package de.davelee.trams.gui;

import javax.swing.*;

public class ExitDialog {
	
	public void createExitDialog ( final JFrame currentFrame ) {
		//Create image with exit.
		ImageIcon imageIcon = new ImageIcon(ExitDialog.class.getResource("/TraMSlogo-small.png"));

		//Confirm user did wish to exit.
	    int result = JOptionPane.showOptionDialog(currentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, new String[] { "Yes", "No" }, "No");
	    if ( result == JOptionPane.YES_OPTION ) {
	    	System.exit(0);
	    }

	}

}
