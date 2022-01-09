package de.davelee.trams.gui.listeners;

import de.davelee.trams.gui.ExitDialog;
import de.davelee.trams.gui.WelcomeScreen;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This record class covers the action to be taken when the user clicks on the Exit Game button.
 * @author Dave Lee
 */
public record ExitMouseListener (WelcomeScreen welcomeScreen) implements MouseListener {

    public void mouseClicked(MouseEvent e) {
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.createExitDialog(welcomeScreen);
    }
    public void mousePressed(MouseEvent e) {
        //Nothing happens when mouse pressed.
    }
    public void mouseReleased(MouseEvent e) {
        //Nothing happens when mouse released.
    }
    public void mouseEntered(MouseEvent e) {
        //Nothing happens when mouse entered.
    }
    public void mouseExited(MouseEvent e) {
        //Nothing happens when mouse exited.
    }

}
