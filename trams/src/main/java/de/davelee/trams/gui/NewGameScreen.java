package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.MessageController;
import de.davelee.trams.controllers.ScenarioController;
import de.davelee.trams.controllers.VehicleController;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.ScenarioModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class to display the new game screen to the TraMS program.
 * @author Dave Lee
 */
public class NewGameScreen extends JFrame {
    
	private static final long serialVersionUID = 1L;

    private JLabel welcomeLabel;
    private ImageDisplay logoDisplay;
    private JLabel playerNameLabel;
    private JTextField playerNameField;
    private JLabel scenarioLabel;
    private ButtonGroup scenarioButtonGroup;
    private JRadioButton[] scenarioButtons;
    private JTextArea[] scenarioDescriptions;
    
    private JButton createGameButton;
    private JButton welcomeScreenButton;

    @Autowired
    private GameController gameController;

    @Autowired
    private ScenarioController scenarioController;

    @Autowired
    private VehicleController vehicleController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private ExitDialog exitDialog;

    @Autowired
    private WelcomeScreen welcomeScreen;

    @Autowired
    private ScenarioDescriptionScreen scenarioDescriptionScreen;

    public ExitDialog getExitDialog() {
        return exitDialog;
    }

    public void setExitDialog(final ExitDialog exitDialog) {
        this.exitDialog = exitDialog;
    }

    public WelcomeScreen getWelcomeScreen() {
        return welcomeScreen;
    }

    public void setWelcomeScreen(final WelcomeScreen welcomeScreen) {
        this.welcomeScreen = welcomeScreen;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public ScenarioController getScenarioController() {
        return scenarioController;
    }

    public void setScenarioController(ScenarioController scenarioController) {
        this.scenarioController = scenarioController;
    }

    public VehicleController getVehicleController() {
        return vehicleController;
    }

    public void setVehicleController(VehicleController vehicleController) {
        this.vehicleController = vehicleController;
    }

    public MessageController getMessageController() {
        return messageController;
    }

  	public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
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
        
        //Create a new player name panel.
        JPanel newPlayerPanel = new JPanel();
        newPlayerPanel.setBackground(Color.WHITE);
        playerNameLabel = new JLabel("Player Name:");
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
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        newPlayerPanel.add(playerNameLabel);
        newPlayerPanel.add(playerNameField);
        screenPanel.add(newPlayerPanel);
        
        //Create the scenario panel.
        JPanel scenarioPanel = new JPanel(new BorderLayout());
        scenarioPanel.setBackground(Color.WHITE);
        scenarioLabel = new JLabel("Choose a Fictional Scenario:");
        scenarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scenarioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scenarioPanel.add(scenarioLabel, BorderLayout.NORTH);

        final ScenarioModel[] scenarioModels = scenarioController.getAvailableScenarios();
        
        //Create the actual scenario radio buttons.
        JPanel scenarioRadioPanel = new JPanel(new GridLayout(3,1,5,5));
        scenarioRadioPanel.setBackground(Color.WHITE);
        scenarioButtonGroup = new ButtonGroup();
        scenarioButtons = new JRadioButton[scenarioModels.length];
        scenarioDescriptions = new JTextArea[scenarioModels.length];
        //Scenarios.
        for ( int i = 0; i < scenarioModels.length; i++ ) {
        	JPanel scenarioTownPanel = new JPanel(new BorderLayout());
        	scenarioTownPanel.setBackground(Color.WHITE);
        	scenarioButtons[i] = new JRadioButton(scenarioModels[i].getName());
        	if ( i == 0 ) { scenarioButtons[i].setSelected(true); }
        	scenarioButtons[i].setBackground(Color.WHITE);
        	scenarioButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
        	scenarioButtonGroup.add(scenarioButtons[i]);
        	scenarioTownPanel.add(scenarioButtons[i], BorderLayout.NORTH);
        	scenarioDescriptions[i] = new JTextArea(scenarioModels[i].getCityDescription());
        	scenarioDescriptions[i].setRows(2);
        	scenarioDescriptions[i].setLineWrap(true);
        	scenarioDescriptions[i].setWrapStyleWord(true);
        	scenarioDescriptions[i].setFont(new Font("Arial", Font.ITALIC, 14));
        	scenarioTownPanel.add(scenarioDescriptions[i], BorderLayout.SOUTH);
        	scenarioRadioPanel.add(scenarioTownPanel);
        }
        //Add scenarioRadioPanel to scenarioPanel.
        scenarioPanel.add(scenarioRadioPanel, BorderLayout.SOUTH);
        //Add scenarioPanel to screenPanel.
        screenPanel.add(scenarioPanel);
        
        //Create button panel to display create game and back to welcome screen buttons.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        createGameButton = new JButton("Create Game");
        createGameButton.setEnabled(false);
        createGameButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Get selected scenario.
                int selectedPosition = 0;
            	for ( int i = 0; i < scenarioButtons.length; i++ ) {
            		if ( scenarioButtons[i].isSelected() ) {
                        selectedPosition = i;
            			break;
            		}
            	}
                //Create Game
                GameModel gameModel = gameController.createGameModel(playerNameField.getText(), scenarioModels[selectedPosition].getName());
                //Create supplied vehicles.
                vehicleController.createSuppliedVehicles(scenarioModels[selectedPosition], gameModel.getCurrentTime());
                //Create welcome message.
                messageController.addMessage("Welcome Message", "Congratulations on your appointment as Managing Director of the " +
                        scenarioModels[selectedPosition].getName() + "! \n\n Your targets for the coming days and months are: " +
                        scenarioModels[selectedPosition].getTargets(),"Council","INBOX",gameModel.getCurrentTime());
                scenarioDescriptionScreen.displayScreen(scenarioModels[selectedPosition]);
                dispose();
            }
        });
        buttonPanel.add(createGameButton);
        welcomeScreenButton = new JButton("Back to Welcome Screen");
        welcomeScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
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
        Dimension displayDim = new Dimension(650,500);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(650,500) );
        
    }
    
}

