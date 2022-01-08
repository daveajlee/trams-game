package de.davelee.trams.gui;

//Import the Java GUI packages.
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import javax.swing.*;

import de.davelee.trams.controllers.ControllerHandler;

/**
 * Splash screen for the TraMS program.
 * @author Dave Lee.
 */
public class SplashScreen extends JFrame {

    @Serial
	private static final long serialVersionUID = 1L;

    /**
     * A <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
    private final ControllerHandler controllerHandler;

    /**
     * Create a new splash screen.
     * @param controllerHandler a <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
    public SplashScreen (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
    
    /**
     * Display a new splash screen.
     * @param isAboutScreen a <code>boolean</code> which is true iff this is the about screen rather than splash screen at beginning.
     */
    public void displayScreen ( boolean isAboutScreen ) {
        
        //Initialise GUI with resizable, title and decorate methods.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (true);
        this.setUndecorated(true);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Construct centre panel with box layout to display all components.
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout ( new BoxLayout ( centrePanel, BoxLayout.PAGE_AXIS ) );
        centrePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1), BorderFactory.createEmptyBorder(5,5,5,5)));
        centrePanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        centrePanel.setBackground(Color.WHITE);
        
        //Construct logo panel to add to the centre panel.
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        ImageDisplay logoDisplay = new ImageDisplay("TraMSlogo.png", 0, 0);
        logoDisplay.setSize(872,346);
        logoDisplay.setBackground(Color.WHITE);
        logoPanel.add(logoDisplay);
        centrePanel.add(logoPanel);
        
        //Construct loading panel to add to the centre panel.
        JPanel loadingPanel = new JPanel();
        loadingPanel.setBackground(Color.WHITE);
        JLabel loadingLabel = isAboutScreen ? new JLabel("Version: " + controllerHandler.getVersion()) :
                new JLabel("Loading... Please Wait!");
        loadingLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        loadingPanel.add(loadingLabel);
        centrePanel.add(loadingPanel);
        
        //Construct copyright panel to add to the centre panel.
        JPanel copyrightPanel = new JPanel();
        copyrightPanel.setBackground(Color.WHITE);
        JLabel copyrightLabel = new JLabel("Copyright 2014-2020 Dr. David A J Lee. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        copyrightPanel.add(copyrightLabel);
        centrePanel.add(copyrightPanel);
        
        c.add(centrePanel, BorderLayout.CENTER);
        
        //Mouse listeners if this is the about screen.
        if ( isAboutScreen ) {
            MouseListener closeMouseListener = new MouseListener () {
                public void mouseClicked(MouseEvent e) {
                    dispose();
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
            };
            logoDisplay.addMouseListener(closeMouseListener);
            this.getContentPane().addMouseListener ( closeMouseListener);
        }
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = getPreferredSize();
        this.setLocation ( (screenDim.width/2)-(displayDim.width/2),  (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( getPreferredSize() );
        
    }
    
}
