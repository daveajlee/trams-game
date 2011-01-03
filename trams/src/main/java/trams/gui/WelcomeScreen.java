package trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import trams.main.*;

/**
 * Class to display the welcome screen to the TraMS program.
 * @author Dave Lee
 */
public class WelcomeScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private JLabel theWelcomeLabel;
    private ImageDisplay theLogoDisplay;
    private ImageDisplay theNewBusDisplay;
    private ImageDisplay theLoadBusDisplay;
    private ImageDisplay theExitBusDisplay;
    
    /**
     * Create a new welcome screen.
     * @param ui a <code>UserInterface</code> object with the current user interface.
     */
    public WelcomeScreen ( UserInterface ui ) {
        
        //Initialise user interface variable.
        theInterface = ui;
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        theInterface.setFrame ( this );
        
        //Set image icon.
        //Image img = Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/TraMSlogo.png"));
        Image img = Toolkit.getDefaultToolkit().getImage("trams/images/TraMSlogo.png");
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                theInterface.exit();
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
        theWelcomeLabel = new JLabel("Welcome to ", SwingConstants.CENTER);
        theWelcomeLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 40));
        welcomePanel.add(theWelcomeLabel);
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        theLogoDisplay = new ImageDisplay("TraMSlogo.png", 0, 0);
        theLogoDisplay.setSize(157,92);
        theLogoDisplay.setBackground(Color.WHITE);
        logoPanel.add(theLogoDisplay);
        welcomePanel.add(logoPanel);
        topPanel.add(welcomePanel);
        
        //Create new panel with image and label first.
        JPanel newPanel = new JPanel(); initialiseNewPanel(newPanel);
        newPanel.setLayout ( new BoxLayout ( newPanel, BoxLayout.PAGE_AXIS ) );
        JPanel busPanel = new JPanel();
        initialiseNewPanel(busPanel);
        theNewBusDisplay = new ImageDisplay("newgamebuswelcome.png",25,0);
        theNewBusDisplay.setSize(225,190);
        theNewBusDisplay.setBackground(Color.WHITE);
        theNewBusDisplay.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                new NewGameScreen(theInterface);
                dispose();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        busPanel.add(theNewBusDisplay);
        newPanel.add(busPanel);
        newPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        centrePanel.add(newPanel);
        
        
        //Create load panel with image and label first.
        JPanel loadPanel = new JPanel(); initialiseLoadPanel(loadPanel);
        loadPanel.setLayout ( new BoxLayout ( loadPanel, BoxLayout.PAGE_AXIS ) );
        JPanel busLoadPanel = new JPanel();
        initialiseLoadPanel(busLoadPanel);
        theLoadBusDisplay = new ImageDisplay("loadgametramwelcome.png",25,0);
        theLoadBusDisplay.setSize(240,190);
        theLoadBusDisplay.setBackground(Color.WHITE);
        theLoadBusDisplay.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                if ( theInterface.loadFile() ) {
                    dispose();
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        busLoadPanel.add(theLoadBusDisplay);
        loadPanel.add(busLoadPanel);
        loadPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        centrePanel.add(loadPanel);
        
        //Create exit panel with image and label first.
        JPanel exitPanel = new JPanel(); initialiseExitPanel(exitPanel);
        exitPanel.setLayout ( new BoxLayout ( exitPanel, BoxLayout.PAGE_AXIS ) );
        JPanel busExitPanel = new JPanel();
        initialiseExitPanel(busExitPanel);
        theExitBusDisplay = new ImageDisplay("exitgamebendysmall.png",25,0);
        theExitBusDisplay.setSize(240,190);
        theExitBusDisplay.setBackground(Color.WHITE);
        theExitBusDisplay.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                theInterface.exit();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        busExitPanel.add(theExitBusDisplay);
        exitPanel.add(busExitPanel);
        exitPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        bottomPanel.add(exitPanel);
        bottomPanel.addMouseListener ( new MouseListener () {
            public void mouseClicked(MouseEvent e) {
                theInterface.exit();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
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
                new NewGameScreen(theInterface);
                dispose();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
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
                if ( theInterface.loadFile() ) {
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(WelcomeScreen.this,"The selected file is not a valid saved game for Transport Control Simulator. Please either choose another file or create a new game.", "ERROR: Saved Game Could Not Be Loaded", JOptionPane.ERROR_MESSAGE);
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
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
                theInterface.exit();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }
    
}

