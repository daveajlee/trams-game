package de.davelee.trams.gui;

import javax.swing.*;
import java.net.URL;

/**
 * This class represents a dialog which will be shown to confirm that the user would like to exit the game.
 * @author Dave Lee
 */
public class ExitDialog {

	/**
	 * Create a new <code>ExitDialog</code> with an option for the user to confirm that they wish to leave TraMS.
	 * @param currentFrame a <code>JFrame</code> object containing the parent frame of this dialog.
	 */
	public void createExitDialog ( final JFrame currentFrame ) {
		//Create image with exit.
		URL url = ExitDialog.class.getResource("/TraMSlogo-small.png");
		ImageIcon imageIcon = new ImageIcon(url);

		//Confirm user did wish to exit.
	    int result = JOptionPane.showOptionDialog(currentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, new String[] { "Yes", "No" }, "No");
	    if ( result == JOptionPane.YES_OPTION ) {
	    	System.exit(0);
	    }

	}

}
