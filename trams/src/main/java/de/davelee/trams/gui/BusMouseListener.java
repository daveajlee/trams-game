package de.davelee.trams.gui;

import java.awt.event.*;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.main.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class represents a listener for when vehicles are clicked on the control screen.
 * @author Dave Lee
 */
public class BusMouseListener implements MouseListener {

    private long routeScheduleId;
    private UserInterface userInterface;

    @Autowired
    private GameController gameController;
    
    /**
     * Create a new BusMouseListener.
     * @param routeScheduleId a <code>long</code> with route schedule id being run.
     * @param ui a <code>UserInterface</code> object with current user interface.
     */
    public BusMouseListener(long routeScheduleId) {
        this.routeScheduleId = routeScheduleId;
    }
    
    /**
     * What to do when the mouse press is revoked.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseExited(MouseEvent e) {}
    
    /**
     * What to do when the mouse press is started.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseEntered(MouseEvent e) {}
    
    /**
     * What to do when the mouse press is released.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseReleased(MouseEvent e) {}
    
    /**
     * What to do when the mouse is pressed.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mousePressed(MouseEvent e) {}
    
    /**
     * What to do when the mouse is clicked.
     * Pause and display vehicle info.
     * @param e a <code>MouseEvent</code> object.
     */
    public void mouseClicked(MouseEvent e) {
        gameController.pauseSimulation();
        new BusInfoScreen(routeScheduleId );
    }
    
}
