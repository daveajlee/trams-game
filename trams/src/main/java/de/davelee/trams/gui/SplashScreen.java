package de.davelee.trams.gui;

//Import the Java GUI packages.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.davelee.trams.controllers.ControllerHandler;

/**
 * Splash screen for the TraMS program.
 * @author Dave Lee.
 */
public class SplashScreen extends JFrame {
    
	private static final long serialVersionUID = 1L;

    private ImageDisplay logoDisplay;
    private ImageDisplay busDisplay;
    private JLabel titleLabel;
    private JLabel loadingLabel;
    private JLabel copyrightLabel;

    private ControllerHandler controllerHandler;

    public SplashScreen (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
    
    /**
     * Create a new splash screen.
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
        logoDisplay = new ImageDisplay("TraMSlogo.png", 0, 0);
        logoDisplay.setSize(157,96);
        logoDisplay.setBackground(Color.WHITE);
        logoPanel.add(logoDisplay);
        centrePanel.add(logoPanel);
        
        //Construct title panel to add to the centre panel.
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titleLabel = new JLabel("TraMS - Transport Management Simulator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titlePanel.add(titleLabel);
        centrePanel.add(titlePanel);
        
        //Construct graphics panel to add to the centre panel.
        JPanel busPanel = new JPanel();
        busPanel.setBackground(Color.WHITE);
        if ( isAboutScreen ) { busDisplay = new ImageDisplay("abouttransparent.png",60,0); }
        else { busDisplay = new ImageDisplay("loadingtransparent.png",120,0); }
        busDisplay.setSize(450,340);
        busDisplay.setBackground(Color.WHITE);
        busPanel.add(busDisplay);
        centrePanel.add(busPanel);
        
        //Construct loading panel to add to the centre panel.
        JPanel loadingPanel = new JPanel();
        loadingPanel.setBackground(Color.WHITE);
        if ( isAboutScreen ) { loadingLabel = new JLabel("Version: " + controllerHandler.getVersion()); }
        else { loadingLabel = new JLabel("Loading... Please Wait!"); }
        loadingLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        loadingPanel.add(loadingLabel);
        centrePanel.add(loadingPanel);
        
        //Construct copyright panel to add to the centre panel.
        JPanel copyrightPanel = new JPanel();
        copyrightPanel.setBackground(Color.WHITE);
        copyrightLabel = new JLabel("Copyright 2014 Dr. David A J Lee. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        copyrightPanel.add(copyrightLabel);
        centrePanel.add(copyrightPanel);
        
        c.add(centrePanel, BorderLayout.CENTER);
        
        //Mouse listeners if this is the about screen.
        if ( isAboutScreen ) {
            this.getContentPane().addMouseListener ( new MouseListener () {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
            busDisplay.addMouseListener ( new MouseListener () {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
        }
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = getPreferredSize();
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( getPreferredSize() );
        
    }
    
    public void moveImage ( int width, int height ) {
        busDisplay.moveImage(width, height);
    }
    
}
