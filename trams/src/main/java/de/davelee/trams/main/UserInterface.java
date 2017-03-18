package de.davelee.trams.main;

import javax.swing.*;

import de.davelee.trams.controllers.*;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.SplashScreen;
import de.davelee.trams.gui.WelcomeScreen;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class controls the user interface of the TraMS program. 
 * @author Dave Lee.
 */
public class UserInterface {
    
    //This is to decide which screen to show when we refresh.
    private boolean showingMessages = false;
    private boolean showingManagement = false;

    @Autowired
    private GameController gameController;
    
    /**
     * Create a new user interface - default constructor.
     */
    public UserInterface ( ) {
    }
    
    /**
     * Set the message screen we are using.
     */
    public void setMessageScreen ( boolean flag ) {
        showingMessages = flag;
    }
    
    /**
     * Check if we are using the message screen.
     */
    public boolean getMessageScreen ( ) {
        return showingMessages;
    }
    
    /**
     * Set the management screen we are using.
     */
    public void setManagementScreen ( boolean flag ) {
        showingManagement = flag;
    }
    
    /**
     * Check if we are using the message screen.
     */
    public boolean getManagementScreen ( ) {
        return showingManagement;
    }

    /**
     * Run simulation!
     */
    public void runSimulation (final JFrame currentFrame) {
        currentFrame.dispose();
        ControlScreen cs = new ControlScreen("", 0, 4, false);
        cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
    }
    
    /**
     * Change the selected route.
     * @param routeNumber a <code>String</code> with the new route number.
     */
    public void changeRoute ( String routeNumber, final JFrame currentFrame ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(routeNumber, 0, 4, false);
        cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
        //Resume simulation.
        gameController.resumeSimulation();
    }
    
    /**
     * Change the display to show other vehicles.
     * @param routeNumber a <code>String</code> with the route number.
     * @param min a <code>int</code> with the new min vehicle id.
     * @param max a <code>int</code> with the new max vehicle id.
     * @param allocations a <code>boolean</code> which is true iff allocations have been performed.
     */
    public void changeDisplay ( String routeNumber, int min, int max, boolean allocations, final JFrame currentFrame ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(routeNumber, min, max, allocations);
        //cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
        //Resume simulation.
        gameController.resumeSimulation();
    }

    /**
     * Main method to run the TraMS program.
     * @param args a <code>String</code> array which is not presently being used.
     */
    public static void main ( String[] args ) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch ( Exception e ) { }
        //Display splash screen to the user.
        SplashScreen ss = new SplashScreen(false);
        for ( int i = 12; i > -5; i-- ) {
            try {
                Thread.sleep(200);
                ss.moveImage(10*(i+1),0);
            }
            catch ( InterruptedException ie ) { }
        }
        ss.dispose();
        new WelcomeScreen();
        //LoadingScreen ls = new LoadingScreen();
    }

}
