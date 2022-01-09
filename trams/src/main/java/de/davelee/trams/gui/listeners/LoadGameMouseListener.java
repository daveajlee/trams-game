package de.davelee.trams.gui.listeners;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.FileDialog;
import de.davelee.trams.gui.WelcomeScreen;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This record class covers the action to be taken when the user clicks on the Load Game button.
 * @author Dave Lee
 */
public record LoadGameMouseListener (WelcomeScreen welcomeScreen, ControllerHandler controllerHandler) implements MouseListener {

    public void mouseClicked(MouseEvent e) {
        FileDialog fileDialog = new FileDialog();
        if ( fileDialog.createLoadFileDialog(welcomeScreen, controllerHandler) ) {
            welcomeScreen.dispose();
        }
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
