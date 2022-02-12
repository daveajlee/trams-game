package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.util.GuiUtils;

/**
 * Class to display the new game screen to the TraMS program.
 * @author Dave Lee
 */
public class NewGameScreen extends JFrame {

    @Serial
	private static final long serialVersionUID = 1L;

    /**
     * A <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
    private final ControllerHandler controllerHandler;

    /**
     * Create a new game screen.
     * @param controllerHandler a <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
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
        Image img = Toolkit.getDefaultToolkit().getImage(NewGameScreen.class.getResource("/trams-logo.png"));
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
        JPanel screenPanel = createBoxPanel();
        
        //Create welcome panel.
        screenPanel.add(GuiUtils.createWelcomePanel());

        //Create button but do not display it yet.
        JButton createGameButton = new JButton("Create Game");

        //Create a new company name panel.
        JTextField companyNameField = createTextField(createGameButton);
        screenPanel.add(createPanelWithTwoComponent(createLabel("Company Name:"), companyNameField));
        
        //Create a new player name panel.
        JTextField playerNameField = createTextField(createGameButton);
        screenPanel.add(createPanelWithTwoComponent(createLabel("Player Name:"), playerNameField));
        
        //Create the scenario panel.
        JPanel scenarioPanel = new JPanel(new BorderLayout());
        scenarioPanel.setBackground(Color.WHITE);
        JLabel scenarioLabel = new JLabel("Choose a Fictional Scenario:");
        scenarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scenarioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scenarioPanel.add(scenarioLabel, BorderLayout.NORTH);
        
        //Create the actual scenario radio buttons.
        JComboBox<String> availableScenariosComboBox = new JComboBox<>(controllerHandler.getScenarioController().getAvailableScenarios().toArray(new String[0]));
        //Add scenarioRadioPanel to scenarioPanel.
        scenarioPanel.add(availableScenariosComboBox, BorderLayout.SOUTH);
        //scenarioPanel.add(scenarioRadioPanel, BorderLayout.SOUTH);
        //Add scenarioPanel to screenPanel.
        screenPanel.add(scenarioPanel);
        
        //Create button panel to display create game and back to welcome screen buttons.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        createGameButton.setEnabled(false);
        createGameButton.addActionListener (e -> {
            if ( availableScenariosComboBox.getSelectedItem() != null ) {
                Scenario scenario = controllerHandler.getScenarioController().getScenario(availableScenariosComboBox.getSelectedItem().toString());
                //Create Game
                CompanyResponse companyResponse = controllerHandler.getCompanyController().createCompany(playerNameField.getText(), scenario.getScenarioName(), companyNameField.getText());
                if ( companyResponse != null ) {
                    //Create stops.
                    java.util.List<String> stops = scenario.getStopDistances();
                    for ( String stop : stops ) {
                        controllerHandler.getStopController().saveStop(stop.split(":")[0], companyNameField.getText());
                    }
                    //Create supplied vehicles.
                    controllerHandler.getVehicleController().createSuppliedVehicles(scenario.getSuppliedVehicles(), companyResponse.getTime(), companyNameField.getText());
                    //Create supplied drivers.
                    controllerHandler.getDriverController().createSuppliedDrivers(scenario.getSuppliedDrivers(), companyResponse.getTime(), companyNameField.getText());
                    //Create welcome message.
                    controllerHandler.getMessageController().addMessage(companyNameField.getText(), "Welcome Message", "Congratulations on your appointment as Managing Director of the " +
                            scenario.getScenarioName() + "! \n\n Your targets for the coming days and months are: " +
                            scenario.getTargets(), "Council", "INBOX", companyResponse.getTime());
                    ScenarioDescriptionScreen scenarioDescriptionScreen = new ScenarioDescriptionScreen(controllerHandler);
                    scenarioDescriptionScreen.displayScreen(scenario.getDescription(), companyNameField.getText(), playerNameField.getText());
                    dispose();
                } else {
                    //Show error message if this combination already exists.
                    JOptionPane.showMessageDialog(this, "A company with this name already exists. Please choose another name for your company", "ERROR: Company already exists!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(createGameButton);
        JButton welcomeScreenButton = new JButton("Back to Welcome Screen");
        welcomeScreenButton.addActionListener (e -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(controllerHandler);
            welcomeScreen.displayScreen();
            dispose();
        });
        buttonPanel.add(welcomeScreenButton);
        //Add buttonPanel to screenPanel.
        screenPanel.add(buttonPanel);
         
        c.add(screenPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(650,300);
        this.setLocation ( (screenDim.width/2)-(displayDim.width/2), (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(650,300) );
        
    }

    /**
     * This is a private method to create a <code>JPanel</code> with two component.
     * @param component a <code>JComponent</code> object to add to the panel.
     * @param component2 a <code>JComponent</code> object to add to the panel.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    private JPanel createPanelWithTwoComponent (final JComponent component, final JComponent component2) {
        JPanel twoComponentPanel = new JPanel();
        twoComponentPanel.setBackground(Color.WHITE);
        twoComponentPanel.add(component);
        twoComponentPanel.add(component2);
        return twoComponentPanel;
    }

    /**
     * This is a private helper method to create a <code>JPanel</code> with box layout.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    private JPanel createBoxPanel ( ) {
        JPanel boxPanel = new JPanel();
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setLayout ( new BoxLayout ( boxPanel, BoxLayout.PAGE_AXIS ) );
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return boxPanel;
    }

    private JLabel createLabel ( final String text ) {
        JLabel foldersLabel = new JLabel(text);
        foldersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return foldersLabel;
    }

    private JTextField createTextField ( final JButton createGameButton ) {
        JTextField textField = new JTextField("");
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setColumns(25);
        textField.addKeyListener( new KeyListener() {
            public void keyReleased(KeyEvent e) {
                createGameButton.setEnabled(textField.getText().length() > 0);
            }
            public void keyTyped(KeyEvent e) {
                //Nothing happens when key typed.
            }
            public void keyPressed(KeyEvent e) {
                //Nothing happens when key pressed.
            }
        });
        return textField;
    }
    
}

