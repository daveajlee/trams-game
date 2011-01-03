package trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import trams.main.*;

/**
 * Class to display the new game screen to the TraMS program.
 * @author Dave Lee
 */
public class NewGameScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private JLabel theWelcomeLabel;
    private ImageDisplay theLogoDisplay;
    private JLabel thePlayerNameLabel;
    private JTextField thePlayerNameField;
    private JLabel theScenarioLabel;
    private ButtonGroup theScenarioButtonGroup;
    private JRadioButton theLanduffTownScenario;
    private JTextArea theLanduffTownDescription;
    private JRadioButton theMDorfCityScenario;
    private JTextArea theMDorfCityDescription;
    private JRadioButton theLongtsCityScenario;
    private JTextArea theLongtsCityDescription;
    
    private JButton theCreateGameButton;
    private JButton theWelcomeScreenButton;
    
    /**
     * Create a new new game screen.
     * @param ui a <code>UserInterface</code> object with the current user interface.
     */
    public NewGameScreen ( UserInterface ui ) {
        
        //Initialise user interface variable.
        theInterface = ui;
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        theInterface.setFrame ( this );
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/trams/images/TraMSlogo.png");
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
        
        //Create a screen panel in box layout to store everything.
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout ( new BoxLayout ( screenPanel, BoxLayout.PAGE_AXIS ) );
        screenPanel.setBackground(Color.WHITE);
        
        //Create welcome panel.
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
        
        //Create a new player name panel.
        JPanel newPlayerPanel = new JPanel();
        newPlayerPanel.setBackground(Color.WHITE);
        thePlayerNameLabel = new JLabel("Player Name:");
        thePlayerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        thePlayerNameField = new JTextField("");
        thePlayerNameField.setFont(new Font("Arial", Font.PLAIN, 18));
        thePlayerNameField.setColumns(25);
        thePlayerNameField.addKeyListener( new KeyListener() {
            public void keyReleased(KeyEvent e) {
                if ( thePlayerNameField.getText().length() > 0 ) {
                    theCreateGameButton.setEnabled(true);
                }
                else {
                    theCreateGameButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        newPlayerPanel.add(thePlayerNameLabel);
        newPlayerPanel.add(thePlayerNameField);
        screenPanel.add(newPlayerPanel);
        
        //Create the scenario panel.
        JPanel scenarioPanel = new JPanel(new BorderLayout());
        scenarioPanel.setBackground(Color.WHITE);
        theScenarioLabel = new JLabel("Choose a Fictional Scenario:");
        theScenarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        theScenarioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scenarioPanel.add(theScenarioLabel, BorderLayout.NORTH);
        
        //Create the actual scenario radio buttons.
        JPanel scenarioRadioPanel = new JPanel(new GridLayout(3,1,5,5));
        scenarioRadioPanel.setBackground(Color.WHITE);
        theScenarioButtonGroup = new ButtonGroup();
        //Landuff Town Scenario.
        JPanel landuffTownPanel = new JPanel(new BorderLayout());
        landuffTownPanel.setBackground(Color.WHITE);
        theLanduffTownScenario = new JRadioButton("Landuff Town (Easy)");
        theLanduffTownScenario.setSelected(true);
        theLanduffTownScenario.setBackground(Color.WHITE);
        theLanduffTownScenario.setFont(new Font("Arial", Font.BOLD, 16));
        theScenarioButtonGroup.add(theLanduffTownScenario);
        landuffTownPanel.add(theLanduffTownScenario, BorderLayout.NORTH);
        theLanduffTownDescription = new JTextArea("Landuff Town is a small town with a very friendly town council. They want to work with you in providing an efficient and effective transport service for Landuff Town.");
        theLanduffTownDescription.setRows(2);
        theLanduffTownDescription.setLineWrap(true);
        theLanduffTownDescription.setWrapStyleWord(true);
        theLanduffTownDescription.setFont(new Font("Arial", Font.ITALIC, 14));
        landuffTownPanel.add(theLanduffTownDescription, BorderLayout.SOUTH);
        scenarioRadioPanel.add(landuffTownPanel);
        //Millenium Dorf City Scenario.
        JPanel millDorfPanel = new JPanel(new BorderLayout());
        millDorfPanel.setBackground(Color.WHITE);
        theMDorfCityScenario = new JRadioButton("Millenium Dorf City (Intermediate)");
        theMDorfCityScenario.setBackground(Color.WHITE);
        theMDorfCityScenario.setFont(new Font("Arial", Font.BOLD, 16));
        theScenarioButtonGroup.add(theMDorfCityScenario);
        millDorfPanel.add(theMDorfCityScenario, BorderLayout.NORTH);
        theMDorfCityDescription = new JTextArea("Millenium Dorf City is a small city. The city council are prepared to work with you providing that you can meet their targets within their timescales.");
        theMDorfCityDescription.setRows(2);
        theMDorfCityDescription.setLineWrap(true);
        theMDorfCityDescription.setWrapStyleWord(true);
        theMDorfCityDescription.setFont(new Font("Arial", Font.ITALIC, 14));
        millDorfPanel.add(theMDorfCityDescription, BorderLayout.SOUTH);
        scenarioRadioPanel.add(millDorfPanel);
        //Longts City Scenario.
        JPanel longtsCityPanel = new JPanel(new BorderLayout());
        longtsCityPanel.setBackground(Color.WHITE);
        theLongtsCityScenario = new JRadioButton("Longts City (Hard)");
        theLongtsCityScenario.setBackground(Color.WHITE);
        theLongtsCityScenario.setFont(new Font("Arial", Font.BOLD, 16));
        theScenarioButtonGroup.add(theLongtsCityScenario);
        longtsCityPanel.add(theLongtsCityScenario, BorderLayout.NORTH);
        theLongtsCityDescription = new JTextArea("Longts City is a very large city. The city council are suspicious of your new company and you will need to impress them very quickly in order to establish a good working relationship.");
        theLongtsCityDescription.setRows(2);
        theLongtsCityDescription.setLineWrap(true);
        theLongtsCityDescription.setWrapStyleWord(true);
        theLongtsCityDescription.setFont(new Font("Arial", Font.ITALIC, 14));
        longtsCityPanel.add(theLongtsCityDescription, BorderLayout.SOUTH);
        scenarioRadioPanel.add(longtsCityPanel);
        //Add scenarioRadioPanel to scenarioPanel.
        scenarioPanel.add(scenarioRadioPanel, BorderLayout.SOUTH);
        //Add scenarioPanel to screenPanel.
        screenPanel.add(scenarioPanel);
        
        //Create button panel to display create game and back to welcome screen buttons.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        theCreateGameButton = new JButton("Create Game");
        theCreateGameButton.setEnabled(false);
        theCreateGameButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                if ( theLanduffTownScenario.isSelected() ) {
                    theInterface.loadScenario("Landuff Transport Company", thePlayerNameField.getText());
                }
                else if ( theMDorfCityScenario.isSelected() ) {
                    theInterface.loadScenario("MDorf Transport Company", thePlayerNameField.getText());
                }
                else {
                    theInterface.loadScenario("Longts Transport Company", thePlayerNameField.getText());
                }
                new ScenarioDescriptionScreen(theInterface);
                dispose();
            }
        });
        buttonPanel.add(theCreateGameButton);
        theWelcomeScreenButton = new JButton("Back to Welcome Screen");
        theWelcomeScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                new WelcomeScreen(theInterface);
                dispose();
            }
        });
        buttonPanel.add(theWelcomeScreenButton);
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

