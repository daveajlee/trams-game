package de.davelee.trams.gui;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.model.ScenarioModel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class to display the scenario description screen to the TraMS program.
 * @author Dave Lee
 */
public class ScenarioDescriptionScreen extends JFrame {

	private static final long serialVersionUID = 1L;

    private JLabel welcomeLabel;
    private ImageDisplay logoDisplay;
    private JLabel mDLabel;
    private JTextArea scenarioDescriptionArea;
    private JButton continueButton;

    private ControllerHandler controllerHandler;
    
    /**
     * Create a new scenario description screen.
     */
    public ScenarioDescriptionScreen (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    public void displayScreen (  final ScenarioModel scenarioModel ) {
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(ScenarioDescriptionScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                boolean wasSimulationRunning = controllerHandler.getGameController().pauseSimulation();
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(ScenarioDescriptionScreen.this);
                if (wasSimulationRunning) { controllerHandler.getGameController().resumeSimulation(); }
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        c.setBackground(Color.WHITE);
        c.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create a screen panel in box layout to store everything.
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout ( new BoxLayout ( screenPanel, BoxLayout.PAGE_AXIS ) );
        screenPanel.setBackground(Color.WHITE);
        
        //Construct logo panel to add to the centre panel.
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.WHITE);
        welcomeLabel = new JLabel("Welcome to ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 40));
        welcomePanel.add(welcomeLabel);
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoDisplay = new ImageDisplay("TraMSlogo.png", 0, 0);
        logoDisplay.setSize(157,92);
        logoDisplay.setBackground(Color.WHITE);
        logoPanel.add(logoDisplay);
        welcomePanel.add(logoPanel);
        screenPanel.add(welcomePanel);
        
        //Create the MDLabelPanel first of all.
        JPanel MDLabelPanel = new JPanel();
        MDLabelPanel.setBackground(Color.WHITE);
        mDLabel = new JLabel(controllerHandler.getGameController().getCurrentPlayerName() + " appointed Managing Director of " + scenarioModel.getName());
        mDLabel.setFont(new Font("Arial", Font.BOLD, 18));
        MDLabelPanel.add(mDLabel);
        screenPanel.add(MDLabelPanel);
        
        //Create the descriptionPanel.
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBackground(Color.WHITE);
        scenarioDescriptionArea = new JTextArea(controllerHandler.getGameController().getCurrentPlayerName() + " " + scenarioModel.getDescription());
        scenarioDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        scenarioDescriptionArea.setLineWrap(true);
        scenarioDescriptionArea.setWrapStyleWord(true);
        scenarioDescriptionArea.setColumns(50);
        descriptionPanel.add(scenarioDescriptionArea);
        screenPanel.add(descriptionPanel);
        
        //Create the buttonPanel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                ControlScreen controlScreen = new ControlScreen(controllerHandler);
                controlScreen.displayScreen("", 0, 4, false);
                controlScreen.setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(continueButton);
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

