package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.model.ScenarioModel;

/**
 * Class to display the new game screen to the TraMS program.
 * @author Dave Lee
 */
public class NewGameScreen extends JFrame {
    
	private static final long serialVersionUID = 1L;

    private JTextField companyNameField;

    private JTextField playerNameField;

    private JButton createGameButton;

    private ControllerHandler controllerHandler;

    public NewGameScreen (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Display the new game screen appropriately.
     */
    public void displayScreen ( ) {
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(NewGameScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(NewGameScreen.this);
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
        
        //Create welcome panel.
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
        screenPanel.add(welcomePanel);

        //Create a new company name panel.
        JPanel newCompanyPanel = new JPanel();
        newCompanyPanel.setBackground(Color.WHITE);
        JLabel companyNameLabel = new JLabel("Company Name:");
        companyNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        companyNameField = new JTextField("");
        companyNameField.setFont(new Font("Arial", Font.PLAIN, 18));
        companyNameField.setColumns(25);
        companyNameField.addKeyListener( new KeyListener() {
            public void keyReleased(KeyEvent e) {
                if ( companyNameField.getText().length() > 0 ) {
                    createGameButton.setEnabled(true);
                }
                else {
                    createGameButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) {
                //Nothing happens when key typed.
            }
            public void keyPressed(KeyEvent e) {
                //Nothing happens when key pressed.
            }
        });
        newCompanyPanel.add(companyNameLabel);
        newCompanyPanel.add(companyNameField);
        screenPanel.add(newCompanyPanel);
        
        //Create a new player name panel.
        JPanel newPlayerPanel = new JPanel();
        newPlayerPanel.setBackground(Color.WHITE);
        JLabel playerNameLabel = new JLabel("Player Name:");
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playerNameField = new JTextField("");
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 18));
        playerNameField.setColumns(25);
        playerNameField.addKeyListener( new KeyListener() {
            public void keyReleased(KeyEvent e) {
                if ( playerNameField.getText().length() > 0 ) {
                    createGameButton.setEnabled(true);
                }
                else {
                    createGameButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) {
                //Nothing happens when key typed.
            }
            public void keyPressed(KeyEvent e) {
                //Nothing happens when key pressed.
            }
        });
        newPlayerPanel.add(playerNameLabel);
        newPlayerPanel.add(playerNameField);
        screenPanel.add(newPlayerPanel);
        
        //Create the scenario panel.
        JPanel scenarioPanel = new JPanel(new BorderLayout());
        scenarioPanel.setBackground(Color.WHITE);
        JLabel scenarioLabel = new JLabel("Choose a Fictional Scenario:");
        scenarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scenarioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scenarioPanel.add(scenarioLabel, BorderLayout.NORTH);
        
        //Create the actual scenario radio buttons.
        JComboBox<String> availableScenariosComboBox = new JComboBox<String>(controllerHandler.getScenarioController().getAvailableScenarios().toArray(new String[controllerHandler.getScenarioController().getAvailableScenarios().size()]));
        //Add scenarioRadioPanel to scenarioPanel.
        scenarioPanel.add(availableScenariosComboBox, BorderLayout.SOUTH);
        //scenarioPanel.add(scenarioRadioPanel, BorderLayout.SOUTH);
        //Add scenarioPanel to screenPanel.
        screenPanel.add(scenarioPanel);
        
        //Create button panel to display create game and back to welcome screen buttons.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        createGameButton = new JButton("Create Game");
        createGameButton.setEnabled(false);
        createGameButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                ScenarioModel scenarioModel = controllerHandler.getScenarioController().getScenario(availableScenariosComboBox.getSelectedItem().toString());
                //Create Game
                CompanyResponse companyResponse = controllerHandler.getGameController().createGameModel(playerNameField.getText(), scenarioModel.getName(), companyNameField.getText());
                //Create supplied vehicles.
                controllerHandler.getVehicleController().createSuppliedVehicles(scenarioModel, companyResponse.getTime(), companyNameField.getText());
                //Create supplied drivers.
                controllerHandler.getDriverController().createSuppliedDrivers(scenarioModel, companyResponse.getTime(), companyNameField.getText());
                //Create welcome message.
                controllerHandler.getMessageController().addMessage(companyNameField.getText(), "Welcome Message", "Congratulations on your appointment as Managing Director of the " +
                        scenarioModel.getName() + "! \n\n Your targets for the coming days and months are: " +
                        scenarioModel.getTargets(),"Council","INBOX", companyResponse.getTime());
                ScenarioDescriptionScreen scenarioDescriptionScreen = new ScenarioDescriptionScreen(controllerHandler);
                scenarioDescriptionScreen.displayScreen(scenarioModel, companyNameField.getText(), playerNameField.getText());
                dispose();
            }
        });
        buttonPanel.add(createGameButton);
        JButton welcomeScreenButton = new JButton("Back to Welcome Screen");
        welcomeScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                WelcomeScreen welcomeScreen = new WelcomeScreen(controllerHandler);
                welcomeScreen.displayScreen();
                dispose();
            }
        });
        buttonPanel.add(welcomeScreenButton);
        //Add buttonPanel to screenPanel.
        screenPanel.add(buttonPanel);
         
        c.add(screenPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(650,300);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(650,300) );
        
    }
    
}

