package trams.gui;

//Import java util package.
import java.util.*;
//Import the Java GUI packages.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import trams.data.*;
import trams.main.UserInterface;
import trams.simulation.Simulator;

import java.text.*;

import org.apache.log4j.Logger;

/**
 * This class represents the control screen display for the TraMS program.
 * @author Dave Lee
 */
public class ControlScreen extends ButtonBar {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private Simulator theSimulator;
    private JLabel theSimulatorOptionsLabel;
    private JButton theSlowSimulationButton;
    private JButton thePauseSimulationButton;
    private JButton theSpeedUpSimulationButton;
    private JLabel theTimeIncrementLabel;
    private JSpinner theTimeIncrementSpinner;
    /*** GENERAL CONTROL SCREEN BUTTONS ***/
    private JLabel theNumPagesLabel;
    private JButton thePreviousVehiclesButton;
    private JButton theNextVehiclesButton;
    private JPanel theTopPanel;
    private JTabbedPane theTabbedPane;
    private JPanel theGraphicsPanel;
    private JPanel theMessagesPanel;
    private ManagePanel theManagementPanel;
    private LinkedList<JPanel> theStopPanels;
    private JLabel theTimeLabel;
    private JProgressBar thePassengerSatisfactionBar;
    private JList theRouteList;
    private DefaultListModel theRouteModel;
    private JPanel theDialogPanel;
    private JLabel theBalanceLabel;
    private int theMinVehicle;
    private int theMaxVehicle;
    
    private int theCurrentPage;
    private boolean doneAllocations = false;

    private String theRouteNumber;

    private LinkedList<Message> theMessages = new LinkedList<Message>();
    private JTextArea theMessagesArea;
    private DefaultListModel theMessagesModel;
    private DefaultComboBoxModel theDateModel;
    private JList theMessagesList;
    private JComboBox theFoldersBox;
    private JComboBox theDateBox;
    private JComboBox theMessageTypeBox;

    private boolean redrawOnRouteChange = true;
    
    private Logger logger;

    /**
     * Create a new control screen.
     * @param ui a <code>UserInterface</code> with the current user interface.
     * @param s a <code>Simulator</code> with the current simulation
     * @param routeNumber a <code>String</code> with the route number
     * @param min a <code>int</code> with the minimum number of vehicles to display.
     * @param max a <code>int</code> with the maximum number of vehicles to display.
     * @param allocationsDone a <code>boolean</code> which is true iff all allocations have been done.
     */
    public ControlScreen ( UserInterface ui, Simulator s, String routeNumber, int min, int max, boolean allocationsDone ) {
        
        //Call super constructor.
        super ( ui );
        
        //Initialise user interface variable.
        theInterface = ui;
        theSimulator = s;
        theRouteNumber = routeNumber;
        theMinVehicle = min;
        theMaxVehicle = max;
        doneAllocations = allocationsDone;
        
        //Initialise dialog panel.
        theDialogPanel = new JPanel(new BorderLayout());
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Player: " + theInterface.getScenario().getPlayerName() + " (" + theInterface.getScenario().getScenarioName() + ")");
        this.setResizable (true);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setJMenuBar(theMenuBar);
        theInterface.setFrame ( this );
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(ControlScreen.class.getResource("/trams/images/TraMSlogo.png"));
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                theInterface.exit();
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Temporary put labels indicating where eventual content will go.
        theTopPanel = new JPanel();
        theTopPanel.setLayout ( new BorderLayout () );
        theTopPanel.setBackground(Color.WHITE);
        theTimeLabel = new JLabel(theSimulator.getCurrentDisplaySimTime(), SwingConstants.CENTER);
        theTimeLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        theTopPanel.add(theTimeLabel, BorderLayout.NORTH);

        //Initialise route list.
        theRouteModel = new DefaultListModel();
        theRouteList = new JList(theRouteModel);
        
        logger = Logger.getLogger(ControlScreen.class);
        
        theTopPanel.add(makeOptionsPanel(), BorderLayout.CENTER);
        theDialogPanel.add(theTopPanel, BorderLayout.NORTH);
        
        //Create two tabbed panes here.
        theTabbedPane = new JTabbedPane();
        theTabbedPane.setBackground(Color.WHITE);
        //Create Live Situation tab.
        if ( theSimulator.getScenario().getNumberRoutes() > 0 ) {
            drawVehicles(false);
            theTabbedPane.addTab("Live Situation", theGraphicsPanel);
        }
        else {
            drawVehicles(false);
            theTabbedPane.addTab("Live Situation", theGraphicsPanel);
        }
        //Create Messages tab.
        drawMessages();
        theTabbedPane.addTab("Messages", theMessagesPanel);
        if ( theInterface.getMessageScreen() ) {
            theTopPanel.getComponent(1).setVisible(false);
            theTabbedPane.setSelectedIndex(1);
        }
        theTabbedPane.addMouseListener(new MouseListener() {
            public void mouseExited ( MouseEvent e ) { }
            public void mouseEntered ( MouseEvent e ) { }
            public void mouseReleased ( MouseEvent e ) { }
            public void mousePressed ( MouseEvent e ) { }
            public void mouseClicked ( MouseEvent e ) { 
                if ( theTabbedPane.getSelectedIndex() == 1) {
                    logger.debug("You just selected message screen");
                    theTopPanel.getComponent(1).setVisible(false);
                    theInterface.setMessageScreen(true);
                    theInterface.setManagementScreen(false);
                    theInterface.pauseSimulation(); //Pause simulation for message screen.
                }
                else if ( theTabbedPane.getSelectedIndex() == 2 ) {
                    logger.debug("You just selected management screen");
                    theTopPanel.getComponent(1).setVisible(false);
                    theInterface.setManagementScreen(true);
                    theInterface.setMessageScreen(false);
                    theInterface.pauseSimulation(); //Pause simulation for management screen.
                }
                else {
                    logger.debug("You just selected live screen");
                    redrawOnRouteChange = false;
                    populateRouteList();
                    redrawOnRouteChange = true;
                    logger.debug("Route list has been re-populated!");
                    theTopPanel.getComponent(1).setVisible(true);
                    theInterface.setMessageScreen(false);
                    theInterface.setManagementScreen(false);
                    theInterface.resumeSimulation(); //Resume simulation for live screen.
                }
            }
        });
        //Create manage tab.
        theManagementPanel = new ManagePanel(theInterface, theSimulator, this);
        theTabbedPane.addTab("Management", theManagementPanel.getDisplayPanel());
        if ( theInterface.getManagementScreen() ) {
            theTopPanel.getComponent(1).setVisible(false);
            theTabbedPane.setSelectedIndex(2);
        }
        //Disable the live situation tab if appropriate.
        if ( theSimulator.getScenario().getNumberRoutes() == 0 ) {
            theTabbedPane.setEnabledAt(0, false);
        }
        /*theTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                logger.debug("You just selected the " + theTabbedPane.getSelectedIndex() + " component");
                if ( theTabbedPane.getSelectedIndex() == 1) {
                    theInterface.setMessageScreen(true);
                }
                else {
                    theInterface.setMessageScreen(false);
                }
            }
        });*/
        //Vehicles - the main bit!
        //Centre panel displays control display of vehicle movements.
        //theCentrePanel = new JPanel();
        //theCentrePanel.setLayout ( new BorderLayout() );
        //theCentrePanel.setBackground(Color.WHITE);
        //theCentrePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1), BorderFactory.createEmptyBorder(5,5,5,5)));
        //theCentrePanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //theCentrePanel.add(theGraphicsPanel, BorderLayout.CENTER);
        //Add it to the dialogPanel.
        theDialogPanel.add(theTabbedPane, BorderLayout.CENTER);
        
        //Bottom panel consists of passenger satisfactions and buttons.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        //bottomPanel.add(makeVehicleInfoPanel(), BorderLayout.NORTH);
        //Create passenger satisfaction bar.
        JPanel passengerSatisfactionPanel = new JPanel();
        passengerSatisfactionPanel.setBackground(Color.WHITE);
        thePassengerSatisfactionBar = new JProgressBar(0, 100);
        thePassengerSatisfactionBar.setValue(theInterface.getScenario().getPassengerSatisfaction());
        thePassengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + thePassengerSatisfactionBar.getValue() + "%");
        thePassengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
        thePassengerSatisfactionBar.setStringPainted(true);
        passengerSatisfactionPanel.add(thePassengerSatisfactionBar);
        bottomPanel.add(thePassengerSatisfactionBar, BorderLayout.NORTH);
        //Create bottom info panel with balance + resign + exit game buttons.
        JPanel bottomInfoPanel = new JPanel();
        bottomInfoPanel.setBackground(Color.WHITE);
        DecimalFormat form = new DecimalFormat("0.00");
        theBalanceLabel = new JLabel("Balance: €" + form.format(theInterface.getBalance()));
        theBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        bottomInfoPanel.add(theBalanceLabel);
        JButton resignButton = new JButton("Resign");
        resignButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.pauseSimulation();
                new WelcomeScreen(theInterface);
                dispose();
            }
        });
        bottomInfoPanel.add(resignButton);
        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.exit();
            }
        });
        bottomInfoPanel.add(exitButton);
        bottomPanel.add(bottomInfoPanel, BorderLayout.SOUTH);
        
        //Add bottom panel to dialog panel.
        theDialogPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        //Finally, add dialogPanel to container.
        c.add(theDialogPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(800,600);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        logger.debug("Completing generation of Control Screen!");
        this.pack ();
        this.setVisible (true);
        this.setSize ( 800,600 );
        
        //Help Contents Item.
        theHelpItem.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //thePauseButton.setText("Resume Simulation");
                theInterface.pauseSimulation();
                Thread contentsThread = new Thread() {
                    public void run () {
                        new HelpScreen(theInterface);
                    }
                };
                contentsThread.start();
            }
        });
        //About Item.
        theAboutItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                //thePauseButton.setText("Resume Simulation");
                theInterface.pauseSimulation();
                Thread aboutThread = new Thread() {
                    public void run () {
                        new SplashScreen(true, theInterface);
                    }
                };
                aboutThread.start();
            }
        });
        
    }
    
    /**
     * Draw the vehicle positions.
     * @param isRedraw a <code>boolean</code> which is true iff this is a redraw rather than first draw.
     */
    public void drawVehicles ( boolean isRedraw ) {
        //Now check if it is past midnight! If it is dispose, and create Allocation Screen.
        if ( isPastMidnight(theSimulator.getCurrentSimTime(), theSimulator.getPreviousSimTime()) && !doneAllocations ) {
            //Now add a message to summarise days events!!!
        	Message message = (Message) theInterface.getContext().getBean("CouncilMessage");
        	message.setSubject("Passenger Satisfaction for " + theSimulator.getPreviousDisplaySimDay());
        	message.setText("Congratulations you have successfully completed transport operations for " + theInterface.getScenario().getScenarioName() + " on " + theSimulator.getPreviousDisplaySimDay() + " with a passenger satisfaction of " + theSimulator.getScenario().getPassengerSatisfaction() + "%.\n\nNow you need to allocate vehicles to routes for " + theSimulator.getCurrentDisplaySimDay() + " and keep the passenger satisfaction up! Click on the Management tab and then choose Allocations. Good luck!");
            message.setSender("Council");
            message.setFolder("Inbox");
            message.setDate(theSimulator.getCurrentDisplaySimTime());
            //Refresh messages.
            theFoldersBox.getSelectedItem().toString();
            theDateBox.getSelectedItem().toString();
            theMessageTypeBox.getSelectedItem().toString();
            theMessages = theInterface.getMessages(theFoldersBox.getSelectedItem().toString(),theDateBox.getSelectedItem().toString(),theMessageTypeBox.getSelectedItem().toString());
            theMessagesModel.removeAllElements();
            for ( int i = 0; i < theMessages.size(); i++ ) {
                theMessagesModel.addElement(theMessages.get(i).getSubject());
            }
            theMessagesList.setSelectedIndex(0);
            if ( theMessages.size() == 0 && theDateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder.");
            }
            else if ( theMessages.size() == 0 ) {
                theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder for the date " + theDateBox.getSelectedItem().toString() + ".");
            }
            else {
                theMessagesArea.setText(theMessages.get(theMessagesList.getSelectedIndex()).getText());
            }
            theDateModel.addElement(theSimulator.getCurrentDisplaySimTime().split(" at ")[0]);
            //Then display it to the user.
            doneAllocations = true;
            theInterface.pauseSimulation();
            theTopPanel.getComponent(1).setVisible(false);
            theTabbedPane.setSelectedIndex(1);
            //Now here we need to update satisfaction bar.
            theTimeLabel.setText(theSimulator.getCurrentDisplaySimTime());
            int satValue = theSimulator.getScenario().computePassengerSatisfaction(theSimulator.getCurrentSimTime(), theSimulator.getDifficultyLevel());
            if ( satValue < theSimulator.getScenario().getMinimumSatisfaction() ) {
                theInterface.pauseSimulation();
                JOptionPane.showMessageDialog(ControlScreen.this, theSimulator.getScenario().getScenarioName() + " have relunctanly decided to relieve you of your duties as managing director as passenger satisfaction is now " + satValue + "%.", "Sorry You Have Been Sacked!", JOptionPane.ERROR_MESSAGE);
                new WelcomeScreen(new UserInterface());
                dispose();
            }
            thePassengerSatisfactionBar.setValue(satValue);
            thePassengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + thePassengerSatisfactionBar.getValue() + "%");
            thePassengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
            //Update graphics panel.
            //theDialogPanel.remove(theGraphicsPanel);
            //theDialogPanel.add(theGraphicsPanel);
            //Repaint the whole interface immediately.
            theDialogPanel.paintImmediately(theDialogPanel.getBounds());
            return;
            //logger.debug("I need to do allocations!");
            //AllocationScreen as = new AllocationScreen(theInterface, true, true, theSimulator);
            //dispose();
        }
        //Set doneAllocations to false.
        doneAllocations = false;

        //Now get the component and replace it if appropriate.
        if ( !isRedraw ) {
            logger.debug("I've set graphics panel to something...");
            theGraphicsPanel = generateNewVehiclePanel();
        }
        if ( isRedraw ) {
            theDialogPanel.remove(theDialogPanel.getComponent(1));
            theGraphicsPanel = generateNewVehiclePanel();
            theTabbedPane.removeAll();
            theTabbedPane.addTab("Live Situation", theGraphicsPanel);
            drawMessages();
            theTabbedPane.addTab("Messages", theMessagesPanel);
            //Create manage tab.
            theTabbedPane.addTab("Management", theManagementPanel.getDisplayPanel());
            if ( theInterface.getMessageScreen() ) {
                theTabbedPane.setSelectedIndex(1);
            }
            else if ( theInterface.getManagementScreen() ) {
                theTabbedPane.setSelectedIndex(2);
            }
            theTabbedPane.addMouseListener(new MouseListener() {
                public void mouseExited ( MouseEvent e ) { }
                public void mouseEntered ( MouseEvent e ) { }
                public void mouseReleased ( MouseEvent e ) { }
                public void mousePressed ( MouseEvent e ) { }
                public void mouseClicked ( MouseEvent e ) { 
                    logger.debug("You just selected the " + theTabbedPane.getSelectedIndex() + " component");
                    if ( theTabbedPane.getSelectedIndex() == 1) {
                        theTopPanel.getComponent(1).setVisible(false);
                        theInterface.setMessageScreen(true);
                        theInterface.setManagementScreen(false);
                        theInterface.pauseSimulation(); //Pause simulation for message screen.
                    }
                    else if ( theTabbedPane.getSelectedIndex() == 2 ) {
                        theTopPanel.getComponent(1).setVisible(false);
                        theInterface.setManagementScreen(true);
                        theInterface.setMessageScreen(false);
                        theInterface.pauseSimulation(); //Pause simulation for management screen.
                    }
                    else {
                        theTopPanel.getComponent(1).setVisible(true);
                        theInterface.setMessageScreen(false);
                        theInterface.setManagementScreen(false);
                        theInterface.resumeSimulation(); //Resume simulation for live screen.
                    }
                }
            });
            //Now disable live situation if no routes.
            if ( theSimulator.getScenario().getNumberRoutes() == 0 ) {
                theTabbedPane.setEnabledAt(0, false);
            }
            theDialogPanel.add(theTabbedPane, 1);
            /*for ( int i = 0; i < theDialogPanel.getComponentCount(); i++ ) {
                logger.debug("Going through component " + i + theDialogPanel.getComponent(i));
                logger.debug("Graphics Panel component " + theGraphicsPanel);
                if ( theDialogPanel.getComponent(i) == theGraphicsPanel ) {
                    theDialogPanel.remove(theDialogPanel.getComponent(i));
                    logger.debug("I've generated vehicle panel!");
                    theGraphicsPanel = generateNewVehiclePanel();
                    theDialogPanel.add(theGraphicsPanel, i);
                }
            }*/
            theTimeLabel.setText(theSimulator.getCurrentDisplaySimTime());
            //Now here we need to update satisfaction bar.
            int satValue = theSimulator.getScenario().computePassengerSatisfaction(theSimulator.getCurrentSimTime(), theSimulator.getDifficultyLevel());
            if ( satValue < theSimulator.getScenario().getMinimumSatisfaction() ) {
                theInterface.pauseSimulation();
                JOptionPane.showMessageDialog(ControlScreen.this, theSimulator.getScenario().getScenarioName() + " have relunctanly decided to relieve you of your duties as managing director as passenger satisfaction is now " + satValue + "%.", "Sorry You Have Been Sacked!", JOptionPane.ERROR_MESSAGE);
                new WelcomeScreen(new UserInterface());
                dispose();
            }
            thePassengerSatisfactionBar.setValue(satValue);
            thePassengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + thePassengerSatisfactionBar.getValue() + "%");
            thePassengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
            //Update graphics panel.
            //theDialogPanel.remove(theGraphicsPanel);
            //theDialogPanel.add(theGraphicsPanel);
            //Repaint the whole interface immediately.
            theDialogPanel.paintImmediately(theDialogPanel.getBounds());
        }
    }
    
    /**
     * Draw the messages panel.
     */
    public void drawMessages ( ) {
        //Create theMessages panel.
        theMessagesPanel = new JPanel(new BorderLayout());
        theMessagesPanel.setBackground(Color.WHITE);
        //Create a list of messages as the north panel.
        theMessagesModel = new DefaultListModel();
        theMessagesList = new JList(theMessagesModel);
        theMessagesList.setVisibleRowCount(5);
        theMessagesArea = new JTextArea();
        theMessagesList.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent lse ) {
                if ( theMessagesList.getSelectedIndex() != -1 ) {
                    theMessagesArea.setText(theMessages.get(theMessagesList.getSelectedIndex()).getText());
                }
            }
        });
        theMessagesPanel.add(theMessagesList, BorderLayout.NORTH);
        //Create a west panel - it is a box layout.
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(Color.WHITE);
        //westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
        //Date panel.
        JPanel datePanel = new JPanel(new GridBagLayout());
        datePanel.setBackground(Color.WHITE);
        //Create date heading.
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        datePanel.add(dateLabel);
        //Create combo box with date.
        theDateModel = new DefaultComboBoxModel();
        theDateModel.addElement("All Dates");
        for ( int i = 0; i < theInterface.getNumberMessages(); i++ ) {
            logger.debug("Index of " + theDateModel.getIndexOf(theInterface.getMessage(i).getDate().split(" at")[0]));
            if ( theDateModel.getIndexOf(theInterface.getMessage(i).getDate().split(" at")[0]) == -1 ) {
                theDateModel.addElement(theInterface.getMessage(i).getDate().split(" at")[0]);
            }
        }
        theDateBox = new JComboBox(theDateModel);
        theDateBox.setFont(new Font("Arial", Font.PLAIN, 12));
        theDateBox.addItemListener( new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                theMessages = theInterface.getMessages(theFoldersBox.getSelectedItem().toString(),theDateBox.getSelectedItem().toString(),theMessageTypeBox.getSelectedItem().toString());
                theMessagesModel.removeAllElements();
                for ( int i = 0; i < theMessages.size(); i++ ) {
                    theMessagesModel.addElement(theMessages.get(i).getSubject());
                }
                theMessagesList.setSelectedIndex(0);
                if ( theMessages.size() == 0 && theDateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                    theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder.");
                }
                else if ( theMessages.size() == 0 ) {
                    theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder for the date " + theDateBox.getSelectedItem().toString() + ".");
                }
                else {
                    theMessagesArea.setText(theMessages.get(theMessagesList.getSelectedIndex()).getText());
                }
            }
        });
        datePanel.add(theDateBox);
        //Add datePanel to westPanel.
        westPanel.add(datePanel, BorderLayout.NORTH);
        //Messages type panel.
        JPanel messageTypePanel = new JPanel(new GridBagLayout());
        messageTypePanel.setBackground(Color.WHITE);
        //Create message type heading.
        JLabel messageTypeLabel = new JLabel("Message Type:");
        messageTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageTypePanel.add(messageTypeLabel);
        //Create combo box with message types.
        theMessageTypeBox = new JComboBox(new String[] { "Council", "Vehicle" });
        theMessageTypeBox.setFont(new Font("Arial", Font.PLAIN, 12));
        theMessageTypeBox.addItemListener ( new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                theMessages = theInterface.getMessages(theFoldersBox.getSelectedItem().toString(),theDateBox.getSelectedItem().toString(),theMessageTypeBox.getSelectedItem().toString());
                theMessagesModel.removeAllElements();
                for ( int i = 0; i < theMessages.size(); i++ ) {
                    theMessagesModel.addElement(theMessages.get(i).getSubject());
                }
                theMessagesList.setSelectedIndex(0);
                if ( theMessages.size() == 0 && theDateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                    theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder.");
                }
                else if ( theMessages.size() == 0 ) {
                    theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder for the date " + theDateBox.getSelectedItem().toString() + ".");
                }
                else {
                    theMessagesArea.setText(theMessages.get(theMessagesList.getSelectedIndex()).getText());
                }
            }
        });
        messageTypePanel.add(theMessageTypeBox);
        //Add messageType panel to west panel.
        westPanel.add(messageTypePanel, BorderLayout.CENTER);
        //Folders panel.
        JPanel foldersPanel = new JPanel(new GridBagLayout());
        foldersPanel.setBackground(Color.WHITE);
        //Create folders heading.
        JLabel foldersLabel = new JLabel("Folders:");
        foldersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        foldersPanel.add(foldersLabel);
        //Create combo box with folders list.
        theFoldersBox = new JComboBox(new String[] { "Inbox", "Sent Items" });
        theFoldersBox.setFont(new Font("Arial", Font.PLAIN, 12));
        theFoldersBox.addItemListener ( new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                theMessages = theInterface.getMessages(theFoldersBox.getSelectedItem().toString(),theDateBox.getSelectedItem().toString(),theMessageTypeBox.getSelectedItem().toString());
                theMessagesModel.removeAllElements();
                for ( int i = 0; i < theMessages.size(); i++ ) {
                    theMessagesModel.addElement(theMessages.get(i).getSubject());
                }
                theMessagesList.setSelectedIndex(0);
                if ( theMessages.size() == 0 && theDateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                    theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder.");
                }
                else if ( theMessages.size() == 0 ) {
                    theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder for the date " + theDateBox.getSelectedItem().toString() + ".");
                }
                else {
                    theMessagesArea.setText(theMessages.get(theMessagesList.getSelectedIndex()).getText());
                }
            }
        });
        foldersPanel.add(theFoldersBox);
        //Add folders panel to west panel.
        westPanel.add(foldersPanel, BorderLayout.SOUTH);
        //Add west panel to messages panel.
        theMessagesPanel.add(westPanel, BorderLayout.WEST);
        //Initialise the messages list now.
        theMessages = theInterface.getMessages(theFoldersBox.getSelectedItem().toString(),theDateBox.getSelectedItem().toString(),theMessageTypeBox.getSelectedItem().toString());
        for ( int i = 0; i < theMessages.size(); i++ ) {
            theMessagesModel.addElement(theMessages.get(i).getSubject());
        }
        theMessagesList.setSelectedIndex(0);
        
        //theMessagesPanel.add(makeOptionsPanel(), BorderLayout.NORTH);
        //BoxLayout layout = new BoxLayout(theMessagesPanel, BoxLayout.Y_AXIS);
        //theMessagesPanel.setLayout(layout);
        //Create a text area with a scroll pane and add this to the interface.
        logger.debug("I'm drawing messages....");
        JPanel messageAndButtonPanel = new JPanel(new BorderLayout());
        messageAndButtonPanel.setBackground(Color.WHITE);
        JScrollPane messagesPane = new JScrollPane();
        if ( theMessages.size() == 0 && theDateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
            theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " folder.");
        }
        else if ( theMessages.size() == 0 ) {
            theMessagesArea.setText("There are no " + theMessageTypeBox.getSelectedItem().toString() + " messages in the " + theFoldersBox.getSelectedItem().toString() + " for the date " + theDateBox.getSelectedItem().toString() + " folder.");
        }
        else {
            theMessagesArea.setText(theMessages.get(theMessagesList.getSelectedIndex()).getText());
        }
        theMessagesArea.setFont(new Font("Arial", Font.ITALIC, 12));
        theMessagesArea.setWrapStyleWord(true);
        theMessagesArea.setLineWrap(true);
        //messagesArea.setPreferredSize(theGraphicsPanel.getPreferredSize());
        theMessagesArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagesPane.getViewport().add(theMessagesArea);
        messagesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messagesPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        messageAndButtonPanel.add(messagesPane, BorderLayout.CENTER);
        theMessagesPanel.add(messageAndButtonPanel, BorderLayout.CENTER);
    }
    
    /**
     * Redraw the management panel to be replaced as requested.
     */
    public void redrawManagement ( JPanel newManagePanel ) {
        //Disable the live situation tab if appropriate.
        if ( theSimulator.getScenario().getNumberRoutes() == 0 ) {
            theTabbedPane.setEnabledAt(0, false);
        }
        //Otherwise, re-enable live panel.
        else {
            theTabbedPane.setEnabledAt(0, true);
        }

        theTabbedPane.setComponentAt(2, newManagePanel);
        theDialogPanel.paintImmediately(theDialogPanel.getBounds());
        DecimalFormat form = new DecimalFormat("0.00");
        theBalanceLabel.setText("Balance: €" + form.format(theInterface.getBalance()));
    }

    /**
     * Redraw the vehicle panel as appropriate.
     */
    public void redrawVehicles ( JPanel newVehiclePanel ) {
        theTabbedPane.setComponentAt(0, newVehiclePanel);
        theDialogPanel.paintImmediately(theDialogPanel.getBounds());
    }
    
    /**
     * Generate a new vehicle panel to display the vehicles.
     * @return a <code>JPanel</code> object.
     */
    public JPanel generateNewVehiclePanel ( ) {
        //Repopulate route list first!
        //populateRouteList();
        //Now create panel.
        JPanel allVehicleDisplayPanel = new JPanel(new BorderLayout());
        //allVehicleDisplayPanel.add(makeOptionsPanel(), BorderLayout.NORTH);
        JPanel vehiclePanel;
        //Call set display method first.
        if ( theRouteModel.getSize() > 0) {
            theInterface.setCurrentDisplayMinMax(theMinVehicle, theMaxVehicle,theRouteNumber.split(":")[0]);
        }
        //Create vehicle Panel as a JPanel!
        if ( theInterface.getNumCurrentDisplaySchedules() == 0 ) {
            vehiclePanel = new JPanel(new GridLayout(2, 1));
        }
        else {
            vehiclePanel = new JPanel(new GridLayout(theInterface.getNumCurrentDisplaySchedules()+1, 1));
            logger.debug("Route number in vehicle panel is " + theRouteList.getSelectedValue().toString().split(":")[0]);
        }
        JPanel stopRowPanel;
        if (theRouteModel.getSize() > 0 ) {
            stopRowPanel = new JPanel(new GridLayout(1, theInterface.getNumStops(theRouteList.getSelectedValue().toString().split(":")[0])));
            //First of all, create a first row of panels which is equal to the number of stops!
            theStopPanels = new LinkedList<JPanel>();
            for ( int i = 0; i < theInterface.getNumStops(theRouteList.getSelectedValue().toString().split(":")[0]); i++ ) {
                JPanel stopPanel = new JPanel();
                stopPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1), BorderFactory.createEmptyBorder(5,5,5,5)));
                stopPanel.setBackground(Color.LIGHT_GRAY);
                JLabel stopLabel = new JLabel(theInterface.getStopName(theRouteList.getSelectedValue().toString().split(":")[0], i));
                stopLabel.setFont(new Font("Arial", Font.BOLD, 15));
                stopPanel.add(stopLabel);
                theStopPanels.add(stopPanel);
                stopRowPanel.add(stopPanel);
            }
        }
        else {
            stopRowPanel = new JPanel(new GridLayout(1, 1));
            JLabel noStopsLabel = new JLabel("No Stops");
            noStopsLabel.setFont(new Font("Arial", Font.BOLD, 20));
            stopRowPanel.add(noStopsLabel);
        }
        vehiclePanel.add(stopRowPanel);
        //If there are no current display vehicles then print a message!
        if ( theInterface.getNumCurrentDisplaySchedules() == 0 ) {
            JLabel noVehiclesLabel = new JLabel("This route currently has no vehicles!");
            noVehiclesLabel.setFont(new Font("Arial", Font.BOLD, 20));
            vehiclePanel.add(noVehiclesLabel);
            allVehicleDisplayPanel.add(vehiclePanel, BorderLayout.CENTER);
            allVehicleDisplayPanel.add(makeVehicleInfoPanel(), BorderLayout.SOUTH);
            return allVehicleDisplayPanel;
        }
        //Otherwise add a label with the route schedule number for each potential vehicle.
        for ( int i = 0; i < theInterface.getNumCurrentDisplaySchedules(); i++ ) {
            //Get the schedule id and vehicle position.
            RouteSchedule rs = theInterface.getDisplaySchedule(theRouteList.getSelectedValue().toString().split(":")[0], i);
            String schedId = rs.toString();
            String[] vehiclePos =  rs.getCurrentStop(theSimulator.getCurrentSimTime(), theSimulator);
            String thisVehiclePos = vehiclePos[0];
            long timeSecs = Long.parseLong(vehiclePos[1]);
            logger.debug(schedId + " is at " + thisVehiclePos + " in " + timeSecs + " seconds with delay " + rs.getDelay() + " minutes.");
            /*if ( rs.hasDelay() ) {
                theInterface.addMessage(theSimulator.getMessageDisplaySimTime() + ": Vehicle " + schedId + " is running " + rs.getDelay() + " minutes late.");
            }*/
            //Get direction first of all we are travelling in!
            LinkedList<String> outwardStops = new LinkedList<String>();
            for ( int j = 0; j < theStopPanels.size(); j++ ) {
                outwardStops.add(((JLabel) theStopPanels.get(j).getComponent(0)).getText());
            }
            int direction = DrawingPanel.RIGHTTOLEFT;
            if ( rs.getCurrentService(theSimulator.getCurrentSimTime()) == null ) {
                continue; //Don't print if the vehicle is at a terminus.
            }
            if ( rs.getCurrentService(theSimulator.getCurrentSimTime()).isOutwardService(outwardStops) ) {
                direction = DrawingPanel.LEFTTORIGHT; 
            }
            //Now we want to find the position where we draw the triangle i.e. position of JLabel.
            int xPos = 0;
            String previousStop = "N/A";
            for ( int j = 0; j < theStopPanels.size(); j++ ) {
                //Each stopPanel has one component which is JLabel.
                JLabel myLabel = (JLabel) theStopPanels.get(j).getComponent(0);
                //Now check where this stop is and get its position.
                //logger.debug("Comparing " + myLabel.getText() + " against " + thisVehiclePos);
                if ( myLabel.getText().equalsIgnoreCase(thisVehiclePos) ) {
                    int panelSize = 800 / theStopPanels.size();
                    int startPos = 0 + ( panelSize * j);
                    int endPos = (panelSize * (j+1))-1;
                    if ( j == (theStopPanels.size()-1) ) {
                        endPos = (panelSize * (j+1))-(panelSize/2);
                    }
                    //Debug.
                    logger.debug("This is stop " + myLabel.getText() + " - range is from " + startPos + " to " + endPos);
                    //xPos = startPos + (panelSize/2 - (panelSize/4));
                    if ( previousStop.equalsIgnoreCase("N/A") ) {
                        xPos = startPos;
                    }
                    else {
                        long maxTimeDiff = Math.abs(rs.getCurrentService(theSimulator.getCurrentSimTime()).getStopMaxTimeDiff(previousStop, myLabel.getText()));
                        if ( maxTimeDiff == Integer.MAX_VALUE ) {
                            xPos = startPos;
                        } 
                        double percent = (double)timeSecs/(double)maxTimeDiff;
                        logger.debug("Percentage is " + percent + "% for positioning! - timeSecs = " + timeSecs + " and maxTimeDiff = " + maxTimeDiff);
                        //If inward, then low percentage means close, high means far away.
                        if ( direction == DrawingPanel.RIGHTTOLEFT ) {
                            xPos = (int) Math.round((percent * (endPos - startPos))) + startPos;
                            logger.debug("Recommeding xPos of " + xPos);
                        }
                        //If outward, reverse is true i.e. high means close, low means far away.
                        else {
                            double invertPercent = 1 - percent;
                            xPos = (int) Math.round((invertPercent * (endPos - startPos))) + startPos;
                            logger.debug("Recommeding xPos of " + xPos);
                        }
                        //xPos = startPos + (panelSize/2 - (panelSize/4));
                    }
                }
                else {
                    previousStop = myLabel.getText();
                }
            }
            logger.debug("I'm drawing route schedule " + rs.toString());
            JPanel drawPanel = new DrawingPanel(xPos, direction, rs.hasDelay() );
            drawPanel.addMouseListener(new BusMouseListener(theInterface.getDisplaySchedule(theRouteList.getSelectedValue().toString().split(":")[0], i), theInterface));
            vehiclePanel.add(drawPanel);
            allVehicleDisplayPanel.add(vehiclePanel, BorderLayout.CENTER);
            allVehicleDisplayPanel.add(makeVehicleInfoPanel(), BorderLayout.SOUTH);
            /*JPanel busPanel = new JPanel();
            Random randNumGen = new Random();
            ImageDisplay busDisplay = new ImageDisplay("doubledeckertrans.png",randNumGen.nextInt(200)-100,0);
            busDisplay.setSize(600,150);
            busPanel.add(busDisplay);
            vehiclePanel.add(busPanel);*/
            /*JPanel routeLabelPanel = new JPanel(new GridBagLayout());
            String routeIdAndStop = theInterface.getDisplaySchedule(theRouteBox.getSelectedItem().toString().split(":")[0], i).toString();
            JLabel routeLabel = new JLabel(routeIdAndStop);
            routeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            //routeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            routeLabel.setLocation(40, 0);
            routeLabelPanel.add(routeLabel);
            vehiclePanel.add(routeLabelPanel);*/
        }
        return allVehicleDisplayPanel;
    }
    
    /**
     * Check if the current time is the first past midnight.
     * @param currentTime a <code>Calendar</code> object which represents the current time.
     * @return a <code>boolean</code> which is true iff this is the first time past midnight.
     */
    private boolean isPastMidnight ( Calendar currentTime, Calendar previousTime ) {
        if ( previousTime.get(Calendar.DAY_OF_WEEK) != currentTime.get(Calendar.DAY_OF_WEEK) ) {
            return true;
        }
        /*if ( currentTime.get(Calendar.HOUR)==0 && currentTime.get(Calendar.MINUTE)==0 && currentTime.get(Calendar.AM_PM)==Calendar.AM ) {
            return true;
        }*/
        return false;
    }
    
    /**
     * Get the current user interface object.
     * @return a <code>UserInterface</code> object.
     */
    public UserInterface getInterface ( ) {
        return theInterface;
    }
    
    /**
     * Pause the simulation.
     */
    public void pauseSimulation ( ) {
        theInterface.pauseSimulation();
    }

    public void populateRouteList ( ) {
        theRouteModel.clear();
        //String[] allRouteStr = new String[theSimulator.getScenario().getNumberRoutes()];
        for ( int i = 0; i < theSimulator.getScenario().getNumberRoutes(); i++ ) {
            logger.debug("Adding route " + theSimulator.getScenario().getRoute(i).toString());
            logger.debug("i is " + i + " and numRoutes is " + theSimulator.getScenario().getNumberRoutes());
            theRouteModel.addElement(theSimulator.getScenario().getRoute(i).toString());
            //allRouteStr[i] = theSimulator.getScenario().getRoute(i).toString();
        }
        //theRouteList = new JList(allRouteStr);
        logger.debug("Route number in control screen is " + theRouteNumber);
        if ( !theRouteNumber.equalsIgnoreCase("") ) {
            theRouteList.setSelectedValue(theRouteNumber, true);
        }
        else if ( theRouteList.getModel().getSize() > 0 ){
            theRouteList.setSelectedIndex(0);
            theRouteNumber = theRouteList.getSelectedValue().toString();
        }
    }
    
    public JPanel makeOptionsPanel ( ) {
        logger.debug("Calling makeOptions panel....");
        //Construct options panel and add it to the top panel.
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setLayout(new BorderLayout());
        //Construct route listing box!
        if ( theSimulator.getScenario().getNumberRoutes() == 0 ) {
            //theRouteList = new JList(new String[] { "No Routes Available" });
            //theRouteList.setVisibleRowCount(3);
            logger.debug("Created route list!");
        }
        else {
            populateRouteList();
        }
        theRouteList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged (ListSelectionEvent ie ) {
                if ( redrawOnRouteChange ) {
                    theInterface.pauseSimulation();
                    ControlScreen.this.redrawVehicles(generateNewVehiclePanel());
                    theInterface.resumeSimulation();
                }
                //logger.debug("Moving to route " + theRouteList.getSelectedValue().toString().split(":")[0]);
                //theInterface.setSimulator(theSimulator);
                //theInterface.changeRoute(theRouteList.getSelectedValue().toString());
                //logger.debug("You now want " + theRouteList.getSelectedValue().toString());
                //dispose();
            }
        });
        optionsPanel.add(theRouteList, BorderLayout.CENTER);
        //Construct simulator control panel.
        JPanel simulatorControlPanel = new JPanel();
        simulatorControlPanel.setBackground(Color.WHITE);
        //Simulator Options Label.
        theSimulatorOptionsLabel = new JLabel("Simulator Options: ");
        theSimulatorOptionsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        simulatorControlPanel.add(theSimulatorOptionsLabel);
        //Slow Simulation Button.
        theSlowSimulationButton = new JButton("<<");
        theSlowSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.slowSimulation();
                if ( theInterface.getSimulationSpeed() >= 4000 ) {
                    theSlowSimulationButton.setEnabled(false);
                }
                theSpeedUpSimulationButton.setEnabled(true);
            }
        });
        simulatorControlPanel.add(theSlowSimulationButton);
        //Pause Simulation Button.
        thePauseSimulationButton = new JButton("||");
        thePauseSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                if ( thePauseSimulationButton.getText().equalsIgnoreCase("||") ) {
                    thePauseSimulationButton.setText("|>");
                    theInterface.pauseSimulation();
                }
                else if ( thePauseSimulationButton.getText().equalsIgnoreCase("|>") ) {
                    thePauseSimulationButton.setText("||");
                    theInterface.resumeSimulation();
                }
            }
        });
        simulatorControlPanel.add(thePauseSimulationButton);
        //Speed Up Simulation Button.
        theSpeedUpSimulationButton = new JButton(">>");
        theSpeedUpSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.speedUpSimulation();
                if ( theInterface.getSimulationSpeed() <= 1000 ) {
                    theSpeedUpSimulationButton.setEnabled(false);
                }
                theSlowSimulationButton.setEnabled(true);
            }
        });
        simulatorControlPanel.add(theSpeedUpSimulationButton);
        //Time Increment Label.
        theTimeIncrementLabel = new JLabel("      Time Increment: ");
        theTimeIncrementLabel.setFont(new Font("Arial", Font.BOLD, 15));
        simulatorControlPanel.add(theTimeIncrementLabel);
        //Time Increment Spinner Field.
        theTimeIncrementSpinner = new JSpinner(new SpinnerNumberModel(theSimulator.getIncrement(),5,60,5));
        theTimeIncrementSpinner.setFont(new Font("Arial", Font.BOLD, 15));
        theTimeIncrementSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent ce ) {
                theSimulator.setIncrement(Integer.parseInt(theTimeIncrementSpinner.getValue().toString()));
            }
        });
        simulatorControlPanel.add(theTimeIncrementSpinner);
        //Add simulator control panel to options panel.
        optionsPanel.add(simulatorControlPanel, BorderLayout.SOUTH);
        //Return optionsPanel.
        return optionsPanel;
    }
    
    public JPanel makeVehicleInfoPanel ( ) {
        //Panel containing vehicle info.
        JPanel vehicleInfoPanel = new JPanel();
        vehicleInfoPanel.setBackground(Color.WHITE);
        //Display page label.
        //logger.debug("Min for this page is: " + min);
        theCurrentPage = (theMinVehicle/4); if ( (theMinVehicle+1) % 4 !=0 || theCurrentPage == 0 ) { theCurrentPage++; }
        int totalPages;
        if ( theRouteList.getModel().getSize() > 0 ) {
            totalPages = (theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0])/4); if ((theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0])%4) !=0 || totalPages == 0 ) { totalPages++; }
        }
        else {
            totalPages = 0;
        }
        theNumPagesLabel = new JLabel("Page " + theCurrentPage + " / " + totalPages);
        logger.debug("This is page " + theCurrentPage + " / " + totalPages);
        vehicleInfoPanel.add(theNumPagesLabel);
        //Previous vehicle button.
        thePreviousVehiclesButton = new JButton("Previous Vehicles");
        thePreviousVehiclesButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Subtract 4 to the min and max.
                theCurrentPage--;
                theMinVehicle = (theCurrentPage * 4)-4; theMaxVehicle = (theCurrentPage*4);
                //Now check if min vehicle is less than 0 - set it to 0.
                if ( theMinVehicle < 0 ) {
                    theMinVehicle = 0;
                }
                theInterface.pauseSimulation();
                ControlScreen.this.redrawVehicles(generateNewVehiclePanel());
                theInterface.resumeSimulation();
                //theInterface.changeDisplay(theRouteList.getSelectedValue().toString(), theMinVehicle, theMaxVehicle, false);
                //dispose();
            }
        });
        if ( totalPages == 0 || theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0]) < 5 || theMinVehicle == 0 ) {
            thePreviousVehiclesButton.setEnabled(false);
        }
        vehicleInfoPanel.add(thePreviousVehiclesButton);
        //Next vehicle button.
        theNextVehiclesButton = new JButton("Next Vehicles");
        theNextVehiclesButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Add 4 to the min and max.
                theMinVehicle = theInterface.getCurrentMinVehicle() + 4; theMaxVehicle = theInterface.getCurrentMaxVehicle() + 5;
                //Now check if max vehicle is bigger than display vehicles - if it is then set it to display vehicles.
                if ( theMaxVehicle > theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0]) ) {
                    theMaxVehicle = theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0]);
                }
                theInterface.pauseSimulation();
                ControlScreen.this.redrawVehicles(generateNewVehiclePanel());
                theInterface.resumeSimulation();
                //theInterface.changeDisplay(theRouteNumber, theMinVehicle, theMaxVehicle, false);
                //dispose();
            }
        });
        if ( totalPages == 0 || theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0]) < 5 || theMaxVehicle == theInterface.getNumRouteDisplayVehicles(theRouteList.getSelectedValue().toString().split(":")[0]) ) {
            theNextVehiclesButton.setEnabled(false);
        }
        vehicleInfoPanel.add(theNextVehiclesButton);
        //Return vehicleInfoPanel.
        return vehicleInfoPanel;
    }
    
}

