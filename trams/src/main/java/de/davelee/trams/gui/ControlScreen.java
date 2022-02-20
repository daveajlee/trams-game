package de.davelee.trams.gui;

//Import the Java GUI packages.
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.davelee.trams.api.response.*;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.panels.ManagementPanel;
import de.davelee.trams.util.MessageFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class represents the control screen display for the TraMS program.
 * @author Dave Lee
 */
public class ControlScreen extends ButtonBar {
	
	private static final Logger logger = LoggerFactory.getLogger(ControlScreen.class);

    @Serial
	private static final long serialVersionUID = 1L;

    /**
     * A button to allow the user to pause the simulation.
     */
    private JButton pauseSimulationButton;

    /**
     * A header panel.
     */
    private JPanel topPanel;

    /**
     * A panel of tabs where the user can choose between managing or viewing the live situation.
     */
    private JTabbedPane tabbedPane;

    /**
     * A panel to show the current graphical vehicle situation.
     */
    private JPanel graphicsPanel;

    /**
     * A panel showing the messages to the user.
     */
    private JPanel messagesPanel;

    /**
     * A text area with the current status of vehicles.
     */
    private JTextArea vehiclesStatusArea;

    /**
     * A label with the current time.
     */
    private JLabel timeLabel;

    /**
     * A barometer showing the current passenger satisfaction level.
     */
    private JProgressBar passengerSatisfactionBar;

    /**
     * A list of routes.
     */
    private JList<String> routeList;

    /**
     * The content model for the list of routes.
     */
    private DefaultListModel<String> routeModel;

    /**
     * A panel to add all other panels to in this screen or frame.
     */
    private JPanel dialogPanel;

    /**
     * Show a text and the current balance.
     */
    private JLabel balanceLabel;

    /**
     * The minimum position in the array of vehicles which should be shown.
     */
    private int minVehicle;

    /**
     * The maximum position in the array of vehicles which should be shown.
     */
    private int maxVehicle;

    /**
     * The current page number shown to the user.
     */
    private int currentPage;

    /**
     * A boolean which is true only iff the user has allocated vehicles.
     */
    private boolean doneAllocations = false;

    /**
     * A text with the current route number being displayed to the user.
     */
    private String routeNumber;

    /**
     * A text area showing the content of a particular message.
     */
    private JTextArea messagesArea;

    /**
     * Model controlling the values shown in the list of messages.
     */
    private DefaultListModel<String> messagesModel;

    /**
     * Model controlling the values shown in the drop-down list of dates.
     */
    private DefaultComboBoxModel<String> dateModel;

    /**
     * A list of messages which the user can choose to display.
     */
    private JList<String> messagesList;

    /**
     * A drop-down list of folder names.
     */
    private JComboBox<String> foldersBox;

    /**
     * A drop-down list of dates.
     */
    private JComboBox<String> dateBox;

    /**
     * A boolean which prevents the routes being reloaded if the simulation is running.
     */
    private boolean redrawOnRouteChange = true;

    /**
     * Create a new control screen.
     * @param controllerHandler a <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     * @param company a <code>String</code> with the company currently being played.
     * @param playerName a <code>String</code> with the name of the player currently playing the game.
     */
    public ControlScreen ( final ControllerHandler controllerHandler, final String company, final String playerName ) {
        super(controllerHandler, company, playerName);
    }

    /**
     * Display a control screen.
     * @param routeNumber a <code>String</code> with the route number
     * @param min a <code>int</code> with the minimum number of vehicles to display.
     * @param max a <code>int</code> with the maximum number of vehicles to display.
     * @param allocationsDone a <code>boolean</code> which is true iff all allocations have been done.
     */
    public void displayScreen ( final String routeNumber, final int min, final int max, final boolean allocationsDone ) {

        //Initialise variables
        this.routeNumber = routeNumber;
        minVehicle = min;
        maxVehicle = max;
        doneAllocations = allocationsDone;
        
        //Initialise dialog panel.
        dialogPanel = new JPanel(new BorderLayout());

        final CompanyResponse companyResponse = super.getControllerHandler().getCompanyController().getCompany(getCompany(), getPlayerName());
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Player: " + companyResponse.getPlayerName() + " (" + companyResponse.getScenarioName() + ")");
        this.setResizable (true);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setJMenuBar(menuBar);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(ControlScreen.class.getResource("/trams-logo.png"));
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                boolean wasSimulationRunning = ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(ControlScreen.this);
                if (wasSimulationRunning) { ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this); }
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Temporary put labels indicating where eventual content will go.
        topPanel = new JPanel();
        topPanel.setLayout ( new BorderLayout () );
        topPanel.setBackground(Color.WHITE);
        timeLabel = new JLabel(formatTime(companyResponse.getTime()), SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        topPanel.add(timeLabel, BorderLayout.NORTH);

        //Initialise route list.
        routeModel = new DefaultListModel<>();
        routeList = new JList<>(routeModel);

        //topPanel.add(makeOptionsPanel(gameModel), BorderLayout.CENTER);
        dialogPanel.add(topPanel, BorderLayout.NORTH);
        
        //Create two tabbed panes here.
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        //get count of routes.
        RoutesResponse routesResponse = super.getControllerHandler().getRouteController().getRoutes(companyResponse.getName());
        long numberRoutes = 0;
        if ( routesResponse != null ) {
            numberRoutes = routesResponse.getCount();
        }
        //Create Live Situation tab.
        drawVehicles(companyResponse);
        if ( numberRoutes > 0 ) {
            updateVehicleStatus(companyResponse.getTime(), companyResponse.getDifficultyLevel(), companyResponse.getName());
            tabbedPane.addTab("Live Situation", graphicsPanel);
            tabbedPane.setSelectedIndex(0);
            super.getControllerHandler().getSimulationController().runSimulation(ControlScreen.this);
        }
        else {
            tabbedPane.addTab("Live Situation", graphicsPanel);
        }
        //Create Messages tab.
        drawMessages(companyResponse.getName());
        tabbedPane.addTab("Messages", messagesPanel);
        tabbedPane.addMouseListener(new MouseListener() {
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
            public void mouseClicked ( MouseEvent e ) { 
                if ( tabbedPane.getSelectedIndex() == 1) {
                    logger.debug("You just selected message screen");
                    topPanel.getComponent(1).setVisible(false);
                    ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation(); //Pause simulation for message screen.
                }
                else if ( tabbedPane.getSelectedIndex() == 2 ) {
                    logger.debug("You just selected management screen");
                    topPanel.getComponent(1).setVisible(false);
                    ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation(); //Pause simulation for management screen.
                }
                else {
                    logger.debug("You just selected live screen");
                    redrawOnRouteChange = false;
                    populateRouteList(ControlScreen.super.getControllerHandler().getCompanyController().getCompany(getCompany(), getPlayerName()).getName());
                    redrawOnRouteChange = true;
                    logger.debug("Route list has been re-populated!");
                    ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this); //Resume simulation for live screen.
                }
            }
        });
        //Create manage tab.
        ManagementPanel managementPanel = new ManagementPanel(getControllerHandler());
        tabbedPane.addTab("Management", managementPanel.createPanel(this));
        //Disable the live situation tab if appropriate.
        if ( numberRoutes > 0 ) {
            tabbedPane.setEnabledAt(0, true);
        } else {
            tabbedPane.setSelectedIndex(2);
        }

        //Add it to the dialogPanel.
        dialogPanel.add(tabbedPane, BorderLayout.CENTER);
        
        //Bottom panel consists of passenger satisfactions and buttons.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        //Create passenger satisfaction bar.
        JPanel passengerSatisfactionPanel = new JPanel();
        passengerSatisfactionPanel.setBackground(Color.WHITE);
        passengerSatisfactionBar = new JProgressBar(0, 100);
        passengerSatisfactionBar.setValue((int) Math.round(super.getControllerHandler().getCompanyController().computeAndReturnPassengerSatisfaction(getCompany(), super.getControllerHandler().getCompanyController().getCompany(getCompany(), getPlayerName()).getDifficultyLevel())));
        passengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + passengerSatisfactionBar.getValue() + "%");
        passengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
        passengerSatisfactionBar.setStringPainted(true);
        passengerSatisfactionPanel.add(passengerSatisfactionBar);
        bottomPanel.add(passengerSatisfactionBar, BorderLayout.NORTH);
        //Create bottom info panel with balance + resign + exit game buttons.
        JPanel bottomInfoPanel = new JPanel();
        bottomInfoPanel.setBackground(Color.WHITE);
        DecimalFormat form = new DecimalFormat("0.00");
        balanceLabel = new JLabel("Balance: £" + form.format(companyResponse.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        bottomInfoPanel.add(balanceLabel);
        JButton resignButton = new JButton("Resign");
        resignButton.addActionListener(e -> {
            ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            new WelcomeScreen(ControlScreen.super.getControllerHandler());
            dispose();
        });
        bottomInfoPanel.add(resignButton);
        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> {
            boolean wasSimulationRunning = ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            ExitDialog exitDialog = new ExitDialog();
            exitDialog.createExitDialog(ControlScreen.this);
            if (wasSimulationRunning) { ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this); }
        });
        bottomInfoPanel.add(exitButton);
        bottomPanel.add(bottomInfoPanel, BorderLayout.SOUTH);
        
        //Add bottom panel to dialog panel.
        dialogPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        //Finally, add dialogPanel to container.
        c.add(dialogPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(800,600);
        this.setLocation ( (screenDim.width/2)-(displayDim.width/2), (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        logger.debug("Completing generation of Control Screen!");
        this.pack ();
        this.setVisible (true);
        this.setSize ( 800,600 );
        
        //Help Contents Item.
        helpItem.addActionListener(e -> {
            //thePauseButton.setText("Resume Simulation");
            ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            Thread contentsThread = new Thread(HelpScreen::new);
            contentsThread.start();
        });
        //About Item.
        aboutItem.addActionListener (e -> {
            //thePauseButton.setText("Resume Simulation");
            ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            Thread aboutThread = new Thread(() -> {
                SplashScreen splashScreen = new SplashScreen(ControlScreen.super.getControllerHandler());
                splashScreen.displayScreen(true);
            });
            aboutThread.start();
        });
        
    }

    /**
     * Update the time to the new specified time and update the status of all vehicles.
     * @param currentDateTime a <code>String</code> with the current time in the game in format dd-MM-yyyy HH:mm
     * @param difficultyLevel a <code>String</code> with the difficulty level which can be either EASY, MEDIUM, INTERMEDIATE or HARD.
     * @param company a <code>String</code> with the name of the company currently being played.
     */
    public void updateDateTime (final String currentDateTime, final String difficultyLevel, final String company ) {
        timeLabel.setText(currentDateTime);
        updateVehicleStatus(currentDateTime, difficultyLevel, company);
    }

    /**
     * Update the status of all vehicles.
     * @param time a <code>String</code> with the current time in the game in format dd-MM-yyyy HH:mm
     * @param difficultyLevel a <code>String</code> with the difficulty level which can be either EASY, MEDIUM, INTERMEDIATE or HARD.
     * @param company a <code>String</code> with the name of the company currently being played.
     */
    public void updateVehicleStatus ( final String time, final String difficultyLevel, final String company ) {
        StringBuilder vehicleStatus = new StringBuilder();

        VehicleResponse[] vehicleModels = super.getControllerHandler().getVehicleController().getVehiclesForRoute(company, routeList.getSelectedValue().split(":")[0]).getVehicleResponses();
        for (VehicleResponse vehicleModel : vehicleModels) {
            String vehiclePos = super.getControllerHandler().getVehicleController().getCurrentStopName(vehicleModel, time, difficultyLevel);
            vehicleStatus.append("Schedule ");
            vehicleStatus.append(vehicleModel.getAllocatedTour());
            vehicleStatus.append(" is at ");
            vehicleStatus.append(vehiclePos);
            vehicleStatus.append(" with a delay of ");
            vehicleStatus.append(vehicleModel.getDelayInMinutes());
            vehicleStatus.append(" minutes.\n");
        }

        vehiclesStatusArea.setText(vehicleStatus.toString());

    }

    /**
     * Update the value of the passenger satisfaction bar to the specified value.
     * @param value a <code>int</code> with the new satisfaction value.
     */
    public void updatePassengerBar ( final int value ) {
        passengerSatisfactionBar.setValue(value);
        passengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + value + "%");
    }
    
    /**
     * Draw the vehicle positions.
     * @param companyResponse a <code>CompanyResponse</code> representing the game currently modeled.
     */
    public void drawVehicles ( final CompanyResponse companyResponse ) {
        //Now check if it is past midnight! If it is then dispose, and create Allocation Screen.
        if ( isPastMidnight(companyResponse.getTime(), getControllerHandler().getSimulationSpeed()) && !doneAllocations ) {
            //Now add a message to summarise days events!!!
            super.getControllerHandler().getMessageController().addMessage(companyResponse.getName(),"Passenger Satisfaction for " +
                            DateFormat.getDateInstance(DateFormat.FULL, Locale.UK).format(getPreviousDateTime(companyResponse.getTime(), getControllerHandler().getSimulationSpeed())),
                    "Congratulations you have successfully completed transport operations for " + companyResponse.getScenarioName() + " on " +
                            DateFormat.getDateInstance(DateFormat.FULL, Locale.UK).format(getPreviousDateTime(companyResponse.getTime(), getControllerHandler().getSimulationSpeed())) +
                            " with a passenger satisfaction of " + super.getControllerHandler().getCompanyController().computeAndReturnPassengerSatisfaction(getCompany(), companyResponse.getDifficultyLevel()) +
                            "%.\n\nNow you need to allocate vehicles to routes for " + companyResponse.getTime() + " and keep the passenger satisfaction up! Click on the Management tab and then choose Allocations. Good luck!",
                    "Council", "INBOX", companyResponse.getTime());
            //Refresh messages.
            refreshMessages(companyResponse.getName(), companyResponse.getTime());
            //Then display it to the user.
            doneAllocations = true;
            super.getControllerHandler().getSimulationController().pauseSimulation();
            topPanel.getComponent(1).setVisible(false);
            tabbedPane.setSelectedIndex(1);
            //Now here we need to update satisfaction bar.
            timeLabel.setText(companyResponse.getTime());
            int satValue = (int) Math.round(super.getControllerHandler().getCompanyController().computeAndReturnPassengerSatisfaction(getCompany(), companyResponse.getDifficultyLevel()));
            Scenario scenario = super.getControllerHandler().getScenarioController().getScenario(companyResponse.getScenarioName());
            if ( satValue < scenario.getMinimumSatisfaction() ) {
                super.getControllerHandler().getSimulationController().pauseSimulation();
                JOptionPane.showMessageDialog(ControlScreen.this, companyResponse.getScenarioName() + " have unfortunately decided to relieve you of your duties as managing director as passenger satisfaction is now " + satValue + "%.", "Sorry You Have Been Sacked!", JOptionPane.ERROR_MESSAGE);
                new WelcomeScreen(super.getControllerHandler());
                dispose();
            }
            passengerSatisfactionBar.setValue(satValue);
            passengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + passengerSatisfactionBar.getValue() + "%");
            passengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
            //Repaint the whole interface immediately.
            dialogPanel.paintImmediately(dialogPanel.getBounds());
            return;
        }
        //Set doneAllocations to false.
        doneAllocations = false;

        //Now get the component and replace it if appropriate.
        graphicsPanel = generateNewVehiclePanel(companyResponse);
    }
    
    /**
     * Draw the messages panel.
     * @param company a <code>String</code> with the name of the company.
     */
    public void drawMessages ( final String company ) {
        //Create theMessages panel.
        messagesPanel = new JPanel(new BorderLayout());
        messagesPanel.setBackground(Color.WHITE);
        //Create a list of messages as the north panel.
        messagesModel = new DefaultListModel<>();
        messagesList = new JList<>(messagesModel);
        messagesList.setVisibleRowCount(5);
        messagesArea = new JTextArea();
        MessageFolder[] messageFolders = MessageFolder.values();
        String[] messageFolderNames = new String[messageFolders.length];
        for ( int i = 0; i < messageFolders.length; i++ ) {
            messageFolderNames[i] = messageFolders[i].getDisplayName();
        }
        foldersBox = new JComboBox<>(messageFolderNames);
        //Create combo box with date.
        dateModel = new DefaultComboBoxModel<>();
        dateModel.addElement("All Dates");
        final MessageResponse[] allMessageModels = super.getControllerHandler().getMessageController().getAllMessages(company);
        if ( allMessageModels != null ) {
            for (MessageResponse allMessageModel : allMessageModels) {
                logger.debug("Index of " + dateModel.getIndexOf(allMessageModel.getDateTime()));
                if (dateModel.getIndexOf(allMessageModel.getDateTime()) == -1) {
                    dateModel.addElement(allMessageModel.getDateTime());
                }
            }
        }
        dateBox = new JComboBox<>(dateModel);

        //Create a west panel - it is a box layout.
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(Color.WHITE);
        //westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
        //Date panel.
        JPanel datePanel = new JPanel(new GridBagLayout());
        datePanel.setBackground(Color.WHITE);
        //Create date heading.
        datePanel.add(createLabel("Date:"));
        dateBox.setFont(new Font("Arial", Font.PLAIN, 12));
        dateBox.addItemListener(e -> refreshMessages(company, ""));
        datePanel.add(dateBox);
        //Add datePanel to westPanel.
        westPanel.add(datePanel, BorderLayout.NORTH);
        //Folders panel.
        JPanel foldersPanel = new JPanel(new GridBagLayout());
        foldersPanel.setBackground(Color.WHITE);
        //Create folders heading.
        foldersPanel.add(createLabel("Folders:"));
        //Create combo box with folders list.
        foldersBox.setFont(new Font("Arial", Font.PLAIN, 12));
        foldersBox.addItemListener (e -> refreshMessages(company, ""));
        foldersPanel.add(foldersBox);
        //Add folders panel to west panel.
        westPanel.add(foldersPanel, BorderLayout.SOUTH);
        //Add west panel to the messages panel.
        messagesPanel.add(westPanel, BorderLayout.WEST);
        //Initialise the messages list now.
        MessageResponse[] myMessageModels = foldersBox.getSelectedItem() != null ?
                        super.getControllerHandler().getMessageController().getMessagesByFolder(company, foldersBox.getSelectedItem().toString()) :
                        super.getControllerHandler().getMessageController().getAllMessages(company);
        if ( myMessageModels != null ) {
            for (MessageResponse myMessageModel : myMessageModels) {
                messagesModel.addElement(myMessageModel.getSubject());
            }
            messagesList.setSelectedIndex(0);
        }
        
        //theMessagesPanel.add(makeOptionsPanel(), BorderLayout.NORTH);
        //BoxLayout layout = new BoxLayout(theMessagesPanel, BoxLayout.Y_AXIS);
        //theMessagesPanel.setLayout(layout);
        //Create a text area with a scroll pane and add this to the interface.
        logger.debug("I'm drawing messages....");
        JPanel messageAndButtonPanel = new JPanel(new BorderLayout());
        messageAndButtonPanel.setBackground(Color.WHITE);
        //Message subject.
        final MessageResponse[] messageModels;
        if ( foldersBox.getSelectedItem() != null  && (dateBox.getSelectedItem() != null ) ) {
            messageModels = super.getControllerHandler().getMessageController().getMessagesByFolderDateSender(company, foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council");
        }
        else {
            messageModels = super.getControllerHandler().getMessageController().getAllMessages(company);
        }
        messagesList.addListSelectionListener(lse -> {
            if ( messagesList.getSelectedIndex() != -1 ) {
                messagesArea.setText(messageModels[messagesList.getSelectedIndex()].getText());
            }
        });
        messageAndButtonPanel.add(messagesList, BorderLayout.NORTH);

        JScrollPane messagesPane = new JScrollPane();
        if ( myMessageModels == null || (myMessageModels.length == 0 && (dateBox.getSelectedItem() != null && dateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates")) )) {
            messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder.");
        }
        else if ( myMessageModels.length == 0 ) {
            messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " for the date " + dateBox.getSelectedItem().toString() + " folder.");
        }
        else {
            messagesArea.setText(myMessageModels[messagesList.getSelectedIndex()].getText());
        }
        messagesArea.setFont(new Font("Arial", Font.ITALIC, 12));
        messagesArea.setWrapStyleWord(true);
        messagesArea.setLineWrap(true);
        //messagesArea.setPreferredSize(theGraphicsPanel.getPreferredSize());
        messagesArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagesPane.getViewport().add(messagesArea);
        messagesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messagesPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        messageAndButtonPanel.add(messagesPane, BorderLayout.CENTER);
        messagesPanel.add(messageAndButtonPanel, BorderLayout.CENTER);
    }
    
    /**
     * Redraw the management panel to be replaced as requested.
     * @param newManagePanel a <code>JPanel</code> containing the new management panel.
     * @param companyResponse a <code>CompanyResponse</code> object representing the game currently being modelled.
     */
    public void redrawManagement (final JPanel newManagePanel, final CompanyResponse companyResponse) {
        //Disable the live situation tab if appropriate.
        //Otherwise, re-enable live panel.
        RoutesResponse routesResponse = super.getControllerHandler().getRouteController().getRoutes(companyResponse.getName());
        tabbedPane.setEnabledAt(0, routesResponse != null ? routesResponse.getCount() != 0 : false);

        tabbedPane.setComponentAt(2, newManagePanel);
        dialogPanel.paintImmediately(dialogPanel.getBounds());
        DecimalFormat form = new DecimalFormat("0.00");
        balanceLabel.setText("Balance: £" + form.format(companyResponse.getBalance()));
    }

    /**
     * Redraw the vehicle panel as appropriate.
     * @param newVehiclePanel a <code>JPanel</code> containing the new vehicle panel.
     */
    public void redrawVehicles ( JPanel newVehiclePanel ) {
        tabbedPane.setComponentAt(0, newVehiclePanel);
        dialogPanel.paintImmediately(dialogPanel.getBounds());
    }
    
    /**
     * Generate a new vehicle panel to display the vehicles.
     * @param companyResponse a <code>CompanyResponse</code> object containing the current game information.
     * @return a <code>JPanel</code> object.
     */
    public JPanel generateNewVehiclePanel ( final CompanyResponse companyResponse ) {
        //Now create panel.
        JPanel allVehicleDisplayPanel = new JPanel(new BorderLayout());
        allVehicleDisplayPanel.add(makeOptionsPanel(companyResponse), BorderLayout.NORTH);

        vehiclesStatusArea = new JTextArea();
        vehiclesStatusArea.setFont(new Font("Arial", Font.BOLD, 15));

        allVehicleDisplayPanel.add(vehiclesStatusArea, BorderLayout.CENTER);
        allVehicleDisplayPanel.add(makeVehicleInfoPanel(companyResponse), BorderLayout.SOUTH);
        return allVehicleDisplayPanel;
    }
    
    /**
     * Check if the current time is the first past midnight.
     * @param currentDateTime a <code>String</code> object which represents the current time.
     * @param timeIncrement a <code>int</code> with the time increment in order to calculate previous time.
     * @return a <code>boolean</code> which is true iff this is the first time past midnight.
     */
    private boolean isPastMidnight ( final String currentDateTime, final int timeIncrement ) {
        LocalDateTime myCurrentDateTime = LocalDateTime.parse(currentDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        LocalDateTime previousDateTime = LocalDateTime.parse(currentDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        previousDateTime = previousDateTime.minusMinutes(timeIncrement);
        return previousDateTime.getDayOfWeek() != myCurrentDateTime.getDayOfWeek();
    }

    /**
     * Get the previous day by calculating the current day and subtracting the time increment.
     * @param currentDateTime a <code>String</code> object which represents the current time.
     * @param timeIncrement a <code>int</code> with the time increment in order to calculate previous time.
     * @return a <code>Calendar</code> with the previous day.
     */
    private LocalDateTime getPreviousDateTime ( final String currentDateTime, final int timeIncrement ) {
        LocalDateTime previousDateTime = LocalDateTime.parse(currentDateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return previousDateTime.minusMinutes(timeIncrement);
    }

    /**
     * Populate the list of routes for the current company.
     * @param company a <code>String</code> with the name of the company currently being played.
     */
    public void populateRouteList ( final String company ) {
        routeModel.clear();
        RouteResponse[] routeResponses = super.getControllerHandler().getRouteController().getRoutes(company).getRouteResponses();
        for ( RouteResponse routeResponse : routeResponses ) {
            routeModel.addElement(routeResponse.getRouteNumber());
        }
        logger.debug("Route number in control screen is " + routeNumber);
        if ( !routeNumber.equalsIgnoreCase("") ) {
            routeList.setSelectedValue(routeNumber, true);
        }
        else if ( routeList.getModel().getSize() > 0 ){
            routeList.setSelectedIndex(0);
            routeNumber = routeList.getSelectedValue();
        }
    }

    /**
     * Show to the user a panel which contains options such as pause the simulation.
     * @param companyResponse a <code>CompanyResponse</code> object with current state of game.
     * @return a <code>JPanel</code> object representing the panel to show to the user.
     */
    public JPanel makeOptionsPanel ( final CompanyResponse companyResponse ) {
        logger.debug("Calling makeOptions panel....");
        //Construct options panel and add it to the top panel.
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setLayout(new BorderLayout());
        //Construct route listing box!
        RoutesResponse routesResponse = super.getControllerHandler().getRouteController().getRoutes(companyResponse.getName());
        if ( routesResponse != null && routesResponse.getCount() != 0 ) {
            populateRouteList(companyResponse.getName());
        }
        routeList.addListSelectionListener(ie -> {
            if ( redrawOnRouteChange ) {
                ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
                ControlScreen.this.redrawVehicles(generateNewVehiclePanel(companyResponse));
                ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this);
            }
        });
        optionsPanel.add(routeList, BorderLayout.CENTER);
        //Construct simulator control panel.
        JPanel simulatorControlPanel = new JPanel();
        simulatorControlPanel.setBackground(Color.WHITE);
        //Simulator Options Label.
        JLabel simulatorOptionsLabel = new JLabel("Simulator Options: ");
        simulatorOptionsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        simulatorControlPanel.add(simulatorOptionsLabel);
        //Pause Simulation Button.
        pauseSimulationButton = new JButton("Pause Simulation");
        pauseSimulationButton.addActionListener(e -> {
            if ( pauseSimulationButton.getText().equalsIgnoreCase("Pause Simulation") ) {
                pauseSimulationButton.setText("Resume Simulation");
                ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            }
            else if ( pauseSimulationButton.getText().equalsIgnoreCase("Resume Simulation") ) {
                pauseSimulationButton.setText("Pause Simulation");
                ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this);
            }
        });
        simulatorControlPanel.add(pauseSimulationButton);
        //Add simulator control panel to options panel.
        optionsPanel.add(simulatorControlPanel, BorderLayout.SOUTH);
        //Return optionsPanel.
        return optionsPanel;
    }

    /**
     * Show to the user the current status of each vehicle.
     * @param companyResponse a <code>CompanyResponse</code> object with current state of game.
     * @return a <code>JPanel</code> object representing the panel to show to the user.
     */
    public JPanel makeVehicleInfoPanel ( final CompanyResponse companyResponse ) {
        //Panel containing vehicle info.
        JPanel vehicleInfoPanel = new JPanel();
        vehicleInfoPanel.setBackground(Color.WHITE);
        //Display page label.
        //logger.debug("Min for this page is: " + min);
        currentPage = (minVehicle/4); if ( (minVehicle+1) % 4 !=0 || currentPage == 0 ) { currentPage++; }
        int totalPages;
        final long routeScheduleModelsLength;
        if ( routeList.getModel().getSize() > 0 ) {
            routeScheduleModelsLength = super.getControllerHandler().getVehicleController().getVehiclesForRoute(companyResponse.getName(), routeList.getSelectedValue()).getCount();
            totalPages = ((int) routeScheduleModelsLength/4); if ((routeScheduleModelsLength%4) !=0 || totalPages == 0 ) { totalPages++; }
        }
        else {
            totalPages = 0;
            routeScheduleModelsLength = 0;
        }
        JLabel numPagesLabel = new JLabel("Page " + currentPage + " / " + totalPages);
        logger.debug("This is page " + currentPage + " / " + totalPages);
        vehicleInfoPanel.add(numPagesLabel);
        //Previous vehicle button.
        JButton previousVehiclesButton = new JButton("Previous Vehicles");
        previousVehiclesButton.addActionListener(e -> {
            //Subtract 4 to the min and max.
            currentPage--;
            minVehicle = (currentPage * 4)-4; maxVehicle = (currentPage*4);
            //Now check if min vehicle is less than 0 - set it to 0.
            if ( minVehicle < 0 ) {
                minVehicle = 0;
            }
            ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            ControlScreen.this.redrawVehicles(generateNewVehiclePanel(companyResponse));
            ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this);
        });
        if ( totalPages == 0 || routeScheduleModelsLength < 5 || minVehicle == 0 ) {
            previousVehiclesButton.setEnabled(false);
        }
        vehicleInfoPanel.add(previousVehiclesButton);
        //Button to go to next vehicle.
        JButton nextVehiclesButton = new JButton("Next Vehicles");
        nextVehiclesButton.addActionListener(e -> {
            //Add 4 to the min and max.
            minVehicle += 4; maxVehicle += 5;
            //Now check if max vehicle is bigger than display vehicles - if it is then set it to display vehicles.
            if ( maxVehicle > routeScheduleModelsLength ) {
                maxVehicle = (int) routeScheduleModelsLength;
            }
            ControlScreen.super.getControllerHandler().getSimulationController().pauseSimulation();
            ControlScreen.this.redrawVehicles(generateNewVehiclePanel(companyResponse));
            ControlScreen.super.getControllerHandler().getSimulationController().resumeSimulation(ControlScreen.this);
        });
        if ( totalPages == 0 || routeScheduleModelsLength < 5 || maxVehicle == routeScheduleModelsLength ) {
            nextVehiclesButton.setEnabled(false);
        }
        vehicleInfoPanel.add(nextVehiclesButton);
        //Return vehicleInfoPanel.
        return vehicleInfoPanel;
    }

    private void refreshMessages( final String company, final String time) {
        if ( foldersBox.getSelectedItem() != null && dateBox.getSelectedItem() != null ) {
            MessageResponse[] messageResponses = super.getControllerHandler().getMessageController().getMessagesByFolderDateSender(company, foldersBox.getSelectedItem().toString(), dateBox.getSelectedItem().toString(), "Council");
            messagesModel.removeAllElements();
            for (MessageResponse messageResponse : messageResponses) {
                messagesModel.addElement(messageResponse.getSubject());
            }
            messagesList.setSelectedIndex(0);
            if (messageResponses.length == 0 && dateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates")) {
                messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder.");
            } else if (messageResponses.length == 0) {
                messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder for the date " + dateBox.getSelectedItem().toString() + ".");
            } else {
                messagesArea.setText(messageResponses[messagesList.getSelectedIndex()].getText());
            }
            if ( !time.isEmpty() ) { dateModel.addElement(time); }
        }
    }

    private JLabel createLabel ( final String text ) {
        JLabel foldersLabel = new JLabel(text);
        foldersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return foldersLabel;
    }

    private String formatTime ( final String time ) {
        LocalDateTime currentDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return currentDateTime.format(DateTimeFormatter.ofPattern("EEEE d", Locale.UK)) + getDateEnding(currentDateTime.getDayOfMonth()) +
                " " +  currentDateTime.format(DateTimeFormatter.ofPattern("MMMM y", Locale.UK)) + " at "
                + currentDateTime.format(DateTimeFormatter.ofPattern("H:mm a", Locale.UK));
    }

    private String getDateEnding ( final int dayNum ) {
        if ( dayNum == 1 || dayNum == 21 || dayNum == 31) {
            return "st";
        } else if ( dayNum == 2 || dayNum == 22 ) {
            return "nd";
        } else if ( dayNum == 3 || dayNum == 23) {
            return "rd";
        } else {
            return "th";
        }
    }

}

