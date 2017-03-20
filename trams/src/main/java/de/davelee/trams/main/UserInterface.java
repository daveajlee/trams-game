package de.davelee.trams.main;

import javax.swing.*;

import de.davelee.trams.controllers.*;

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
