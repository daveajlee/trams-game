package trams.gui;

import java.awt.event.*;

import trams.data.*;
import trams.main.UserInterface;
import trams.main.*;

/**
 * This class represents a listener for when vehicles are clicked on the control screen.
 * @author Dave Lee
 */
public class BusMouseListener implements MouseListener {

    private RouteSchedule theBusDetail;
    private UserInterface theInterface;
    
    /**
     * Create a new BusMouseListener.
     * @param rd a <code>RouteSchedule</code> object with route being run.
     * @param ui a <code>UserInterface</code> object with current user interface.
     */
    public BusMouseListener(RouteSchedule rd, UserInterface ui) {
        theBusDetail = rd;
        theInterface = ui;
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
        theInterface.pauseSimulation();
        new BusInfoScreen(theInterface,  theBusDetail );
    }
    
}
