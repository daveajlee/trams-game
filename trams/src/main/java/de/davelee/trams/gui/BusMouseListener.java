package de.davelee.trams.gui;

import java.awt.event.*;

import de.davelee.trams.controllers.GameController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class represents a listener for when vehicles are clicked on the control screen.
 * @author Dave Lee
 */
public class BusMouseListener implements MouseListener {

    @Autowired
    private GameController gameController;

    private String routeNumber;
    private String scheduleNumber;
    
    /**
     * Create a new BusMouseListener.
     * @param routeNumber a <code>String</code> with the route number.
     * @param scheduleNumber a <code>String</code> with the schedule number.
     */
    public BusMouseListener(final String routeNumber, final String scheduleNumber) {
        this.routeNumber = routeNumber;
        this.scheduleNumber = scheduleNumber;
    }
    
    /**
     * What to do when the mouse press is revoked.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseExited(MouseEvent e) {
        //Nothing happens when mouse exits
    }
    
    /**
     * What to do when the mouse press is started.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseEntered(MouseEvent e) {
        //Nothing happens when mouse enters.
    }
    
    /**
     * What to do when the mouse press is released.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseReleased(MouseEvent e) {
        //Nothing happens when mouse released.
    }
    
    /**
     * What to do when the mouse is pressed.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mousePressed(MouseEvent e) {
        //Nothing happens when mouse pressed.
    }
    
    /**
     * What to do when the mouse is clicked.
     * Pause and display vehicle info.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseClicked(MouseEvent e) {
        gameController.pauseSimulation();
        new BusInfoScreen(routeNumber, scheduleNumber);
    }
    
}
