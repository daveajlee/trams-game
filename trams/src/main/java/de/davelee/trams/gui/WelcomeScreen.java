package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.davelee.trams.controllers.ControllerHandler;

/**
 * Class to display the welcome screen to the TraMS program.
 * @author Dave Lee
 */
public class WelcomeScreen extends JFrame {
    
	private static final long serialVersionUID = 1L;

    private ControllerHandler controllerHandler;

    public WelcomeScreen (final ControllerHandler controllerHandler) {
        this.controllerHandler = controllerHandler;
    }

    public void displayScreen ( ) {
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(WelcomeScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(WelcomeScreen.this);
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        c.setBackground(Color.WHITE);
        c.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create top, centre and bottom panels to add things to.
        JPanel topPanel = new JPanel();
        JPanel centrePanel = new JPanel(new GridLayout(1,2,5,5)); JPanel bottomPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        centrePanel.setBackground(Color.WHITE); bottomPanel.setBackground(Color.WHITE);
        
        //Construct logo panel to add to the centre panel.
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Welcome to ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 40));
        welcomePanel.add(welcomeLabel);
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        ImageDisplay logoDisplay = new ImageDisplay("TraMSlogo-small.png", 0, 0);
        logoDisplay.setSize(157,62);
        logoDisplay.setBackground(Color.WHITE);
        logoPanel.add(logoDisplay);
        welcomePanel.add(logoPanel);
        topPanel.add(welcomePanel);
        
        //Create new panel with image and label first.
        JPanel newPanel = new JPanel(); initialiseNewPanel(newPanel);
        newPanel.setLayout ( new BoxLayout ( newPanel, BoxLayout.PAGE_AXIS ) );
        JPanel busPanel = new JPanel();
        initialiseNewPanel(busPanel);
        ImageDisplay newBusDisplay = new ImageDisplay("newgamebuswelcome.png",25,0);
        newBusDisplay.setSize(225,190);
        newBusDisplay.setBackground(Color.WHITE);
        newBusDisplay.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                NewGameScreen newGameScreen = new NewGameScreen(controllerHandler);
                newGameScreen.displayScreen();
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
        });
        busPanel.add(newBusDisplay);
        newPanel.add(busPanel);
        newPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        centrePanel.add(newPanel);
        
        
        //Create load panel with image and label first.
        JPanel loadPanel = new JPanel(); initialiseLoadPanel(loadPanel);
        loadPanel.setLayout ( new BoxLayout ( loadPanel, BoxLayout.PAGE_AXIS ) );
        JPanel busLoadPanel = new JPanel();
        initialiseLoadPanel(busLoadPanel);
        ImageDisplay loadBusDisplay = new ImageDisplay("loadgametramwelcome.png",25,0);
        loadBusDisplay.setSize(240,190);
        loadBusDisplay.setBackground(Color.WHITE);
        loadBusDisplay.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                FileDialog fileDialog = new FileDialog();
                if ( fileDialog.createLoadFileDialog(WelcomeScreen.this, controllerHandler) ) {
                    dispose();
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
        });
        busLoadPanel.add(loadBusDisplay);
        loadPanel.add(busLoadPanel);
        loadPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        centrePanel.add(loadPanel);
        
        //Create exit panel with image and label first.
        JPanel exitPanel = new JPanel(); initialiseExitPanel(exitPanel);
        exitPanel.setLayout ( new BoxLayout ( exitPanel, BoxLayout.PAGE_AXIS ) );
        JPanel busExitPanel = new JPanel();
        initialiseExitPanel(busExitPanel);
        ImageDisplay exitBusDisplay = new ImageDisplay("exitgamebendysmall.png",25,0);
        exitBusDisplay.setSize(240,190);
        exitBusDisplay.setBackground(Color.WHITE);
        exitBusDisplay.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(WelcomeScreen.this);
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
        });
        busExitPanel.add(exitBusDisplay);
        exitPanel.add(busExitPanel);
        exitPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        bottomPanel.add(exitPanel);
        bottomPanel.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(WelcomeScreen.this);
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
        });
        
        //Add centre and bottom panels to container.
        c.add(topPanel, BorderLayout.NORTH);
        c.add(centrePanel, BorderLayout.CENTER);
        c.add(bottomPanel, BorderLayout.SOUTH);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(750,600);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(750,600) );
    }
    
    /**
     * Initialise the panel to create a new game.
     * @param panel a <code>JPanel</code> to diplay the new game panel.
     */
    public void initialiseNewPanel ( JPanel panel ) {
        panel.setBackground(Color.WHITE);
        panel.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                NewGameScreen newGameScreen = new NewGameScreen(controllerHandler);
                newGameScreen.displayScreen();
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
        });
    }
    
    /**
     * Initialise the panel to load a game.
     * @param panel a <code>JPanel</code> to diplay the load panel.
     */
    public void initialiseLoadPanel ( JPanel panel ) {
        panel.setBackground(Color.WHITE);
        panel.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                FileDialog fileDialog = new FileDialog();
                if ( fileDialog.createLoadFileDialog(WelcomeScreen.this, controllerHandler) ) {
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(WelcomeScreen.this,"The selected file is not a valid saved game for Transport Control Simulator. Please either choose another file or create a new game.", "ERROR: Saved Game Could Not Be Loaded", JOptionPane.ERROR_MESSAGE);
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
        });
    }
    
    /**
     * Initialise the panel to exit the game.
     * @param panel a <code>JPanel</code> to diplay the exit panel.
     */
    public void initialiseExitPanel ( JPanel panel ) {
        panel.setBackground(Color.WHITE);
        panel.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(WelcomeScreen.this);
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
        });
    }
    
}

