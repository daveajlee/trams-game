package trams.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import trams.main.*;

/**
 * Class to display the scenario description screen to the TraMS program.
 * @author Dave Lee
 */
public class ScenarioDescriptionScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private JLabel theWelcomeLabel;
    private ImageDisplay theLogoDisplay;
    private JLabel theMDLabel;
    private JTextArea theScenarioDescriptionArea;
    private JButton theContinueButton;
    
    private static final int DIMENSION_SPACER = 10;
    
    /**
     * Create a new scenario description screen.
     * @param ui a <code>UserInterface</code> object with the current user interface.
     */
    public ScenarioDescriptionScreen ( UserInterface ui ) {
        
        //Initialise user interface variable.
        theInterface = ui;
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        theInterface.setFrame ( this );
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(ScenarioDescriptionScreen.class.getResource("/trams/images/TraMSlogo.png"));
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
        c.add(Box.createRigidArea(new Dimension(0,DIMENSION_SPACER))); //Spacer.
        
        //Create a screen panel in box layout to store everything.
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout ( new BoxLayout ( screenPanel, BoxLayout.PAGE_AXIS ) );
        screenPanel.setBackground(Color.WHITE);
        
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
        screenPanel.add(welcomePanel);
        
        //Create the MDLabelPanel first of all.
        JPanel managingDirectorLabelPanel = new JPanel();
        managingDirectorLabelPanel.setBackground(Color.WHITE);
        theMDLabel = new JLabel(theInterface.getScenario().getPlayerName() + " appointed Managing Director of " + theInterface.getScenario().getScenarioName());
        theMDLabel.setFont(new Font("Arial", Font.BOLD, 18));
        managingDirectorLabelPanel.add(theMDLabel);
        screenPanel.add(managingDirectorLabelPanel);
        
        //Create the descriptionPanel.
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBackground(Color.WHITE);
        theScenarioDescriptionArea = new JTextArea(theInterface.getScenario().getFullDescription());
        theScenarioDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        theScenarioDescriptionArea.setLineWrap(true);
        theScenarioDescriptionArea.setWrapStyleWord(true);
        theScenarioDescriptionArea.setColumns(50);
        descriptionPanel.add(theScenarioDescriptionArea);
        screenPanel.add(descriptionPanel);
        
        //Create the buttonPanel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        theContinueButton = new JButton("Continue");
        theContinueButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.setManagementScreen(true);
                ControlScreen ocs = new ControlScreen(theInterface, theInterface.getSimulator(), "", 0, 4, false);
                theInterface.setControlScreen(ocs);
                dispose();
            }
        });
        buttonPanel.add(theContinueButton);
        screenPanel.add(buttonPanel);
        
        //Add screen panels to container.
        c.add(screenPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(700,500);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(700,500) );
        
    }
    
}

