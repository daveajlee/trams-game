package de.davelee.trams.gui;

//Import java util package.
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//Import the Java GUI packages.
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.JourneyController;
import de.davelee.trams.controllers.MessageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.*;

import de.davelee.trams.main.UserInterface;
import de.davelee.trams.util.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class represents the control screen display for the TraMS program.
 * @author Dave Lee
 */
public class ControlScreen extends ButtonBar {
	
	private static final Logger logger = LoggerFactory.getLogger(ControlScreen.class);
	private static final long serialVersionUID = 1L;
	
	private UserInterface userInterface;
    private JLabel simulatorOptionsLabel;
    private JButton slowSimulationButton;
    private JButton pauseSimulationButton;
    private JButton speedUpSimulationButton;
    private JLabel timeIncrementLabel;
    private JSpinner timeIncrementSpinner;
    /*** GENERAL CONTROL SCREEN BUTTONS ***/
    private JLabel numPagesLabel;
    private JButton previousVehiclesButton;
    private JButton nextVehiclesButton;
    private JPanel topPanel;
    private JTabbedPane tabbedPane;
    private JPanel graphicsPanel;
    private JPanel messagesPanel;
    private ManagePanel managementPanel;
    private List<JPanel> stopPanels;
    private JLabel timeLabel;
    private JProgressBar passengerSatisfactionBar;
    private JList routeList;
    private DefaultListModel routeModel;
    private JPanel dialogPanel;
    private JLabel balanceLabel;
    private int minVehicle;
    private int maxVehicle;
    
    private int currentPage;
    private boolean doneAllocations = false;

    private String routeNumber;

    private JTextArea messagesArea;
    private DefaultListModel messagesModel;
    private DefaultComboBoxModel dateModel;
    private JList messagesList;
    private JComboBox foldersBox;
    private JComboBox dateBox;

    private boolean redrawOnRouteChange = true;

    @Autowired
    private GameController gameController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private JourneyController journeyController;

    /**
     * Create a new control screen.
     * @param ui a <code>UserInterface</code> with the current user interface.
     * @param routeNumber a <code>String</code> with the route number
     * @param min a <code>int</code> with the minimum number of vehicles to display.
     * @param max a <code>int</code> with the maximum number of vehicles to display.
     * @param allocationsDone a <code>boolean</code> which is true iff all allocations have been done.
     */
    public ControlScreen ( UserInterface ui, String routeNumber, int min, int max, boolean allocationsDone ) {
        
        //Call super constructor.
        super ( ui );
        
        //Initialise user interface variable.
        userInterface = ui;
        this.routeNumber = routeNumber;
        minVehicle = min;
        maxVehicle = max;
        doneAllocations = allocationsDone;
        
        //Initialise dialog panel.
        dialogPanel = new JPanel(new BorderLayout());
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Player: " + userInterface.getPlayerName() + " (" + userInterface.getScenarioName() + ")");
        this.setResizable (true);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setJMenuBar(menuBar);
        userInterface.setFrame ( this );
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(ControlScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Call the Exit method in the UserInterface class if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                userInterface.exit();
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Temporary put labels indicating where eventual content will go.
        topPanel = new JPanel();
        topPanel.setLayout ( new BorderLayout () );
        topPanel.setBackground(Color.WHITE);
        timeLabel = new JLabel(userInterface.formatDateString(gameController.getCurrentSimTime(), DateFormats.FULL_TIME_FORMAT), SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        topPanel.add(timeLabel, BorderLayout.NORTH);

        //Initialise route list.
        routeModel = new DefaultListModel();
        routeList = new JList(routeModel);
        
        topPanel.add(makeOptionsPanel(), BorderLayout.CENTER);
        dialogPanel.add(topPanel, BorderLayout.NORTH);
        
        //Create two tabbed panes here.
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        //Create Live Situation tab.
        if ( userInterface.areRoutesDefined() ) {
            drawVehicles(false);
            tabbedPane.addTab("Live Situation", graphicsPanel);
        }
        else {
            drawVehicles(false);
            tabbedPane.addTab("Live Situation", graphicsPanel);
        }
        //Create Messages tab.
        drawMessages();
        tabbedPane.addTab("Messages", messagesPanel);
        if ( userInterface.getMessageScreen() ) {
            topPanel.getComponent(1).setVisible(false);
            tabbedPane.setSelectedIndex(1);
        }
        tabbedPane.addMouseListener(new MouseListener() {
            public void mouseExited ( MouseEvent e ) { }
            public void mouseEntered ( MouseEvent e ) { }
            public void mouseReleased ( MouseEvent e ) { }
            public void mousePressed ( MouseEvent e ) { }
            public void mouseClicked ( MouseEvent e ) { 
                if ( tabbedPane.getSelectedIndex() == 1) {
                    logger.debug("You just selected message screen");
                    topPanel.getComponent(1).setVisible(false);
                    userInterface.setMessageScreen(true);
                    userInterface.setManagementScreen(false);
                    userInterface.pauseSimulation(); //Pause simulation for message screen.
                }
                else if ( tabbedPane.getSelectedIndex() == 2 ) {
                    logger.debug("You just selected management screen");
                    topPanel.getComponent(1).setVisible(false);
                    userInterface.setManagementScreen(true);
                    userInterface.setMessageScreen(false);
                    userInterface.pauseSimulation(); //Pause simulation for management screen.
                }
                else {
                    logger.debug("You just selected live screen");
                    redrawOnRouteChange = false;
                    populateRouteList();
                    redrawOnRouteChange = true;
                    logger.debug("Route list has been re-populated!");
                    topPanel.getComponent(1).setVisible(true);
                    userInterface.setMessageScreen(false);
                    userInterface.setManagementScreen(false);
                    userInterface.resumeSimulation(); //Resume simulation for live screen.
                }
            }
        });
        //Create manage tab.
        managementPanel = new ManagePanel(userInterface, this);
        tabbedPane.addTab("Management", managementPanel.getDisplayPanel());
        if ( userInterface.getManagementScreen() ) {
            topPanel.getComponent(1).setVisible(false);
            tabbedPane.setSelectedIndex(2);
        }
        //Disable the live situation tab if appropriate.
        if ( userInterface.areRoutesDefined() ) {
            tabbedPane.setEnabledAt(0, false);
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
        dialogPanel.add(tabbedPane, BorderLayout.CENTER);
        
        //Bottom panel consists of passenger satisfactions and buttons.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        //bottomPanel.add(makeVehicleInfoPanel(), BorderLayout.NORTH);
        //Create passenger satisfaction bar.
        JPanel passengerSatisfactionPanel = new JPanel();
        passengerSatisfactionPanel.setBackground(Color.WHITE);
        passengerSatisfactionBar = new JProgressBar(0, 100);
        passengerSatisfactionBar.setValue(userInterface.computeAndReturnPassengerSatisfaction());
        passengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + passengerSatisfactionBar.getValue() + "%");
        passengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
        passengerSatisfactionBar.setStringPainted(true);
        passengerSatisfactionPanel.add(passengerSatisfactionBar);
        bottomPanel.add(passengerSatisfactionBar, BorderLayout.NORTH);
        //Create bottom info panel with balance + resign + exit game buttons.
        JPanel bottomInfoPanel = new JPanel();
        bottomInfoPanel.setBackground(Color.WHITE);
        DecimalFormat form = new DecimalFormat("0.00");
        balanceLabel = new JLabel("Balance: £" + form.format(userInterface.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        bottomInfoPanel.add(balanceLabel);
        JButton resignButton = new JButton("Resign");
        resignButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                userInterface.pauseSimulation();
                new WelcomeScreen(userInterface);
                dispose();
            }
        });
        bottomInfoPanel.add(resignButton);
        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                userInterface.exit();
            }
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
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        logger.debug("Completing generation of Control Screen!");
        this.pack ();
        this.setVisible (true);
        this.setSize ( 800,600 );
        
        //Help Contents Item.
        helpItem.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //thePauseButton.setText("Resume Simulation");
                userInterface.pauseSimulation();
                Thread contentsThread = new Thread() {
                    public void run () {
                        new HelpScreen(userInterface);
                    }
                };
                contentsThread.start();
            }
        });
        //About Item.
        aboutItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                //thePauseButton.setText("Resume Simulation");
                userInterface.pauseSimulation();
                Thread aboutThread = new Thread() {
                    public void run () {
                        new SplashScreen(true, userInterface);
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
        if ( isPastMidnight(gameController.getCurrentSimTime(), userInterface.getPreviousSimTime()) && !doneAllocations ) {
            //Now add a message to summarise days events!!!
            messageController.addMessage("Passenger Satisfaction for " + userInterface.formatDateString(userInterface.getPreviousSimTime(), DateFormats.FULL_FORMAT), "Congratulations you have successfully completed transport operations for " + userInterface.getScenarioName() + " on " + userInterface.formatDateString(userInterface.getPreviousSimTime(), DateFormats.FULL_FORMAT) + " with a passenger satisfaction of " + userInterface.computeAndReturnPassengerSatisfaction() + "%.\n\nNow you need to allocate vehicles to routes for " + userInterface.formatDateString(gameController.getCurrentSimTime(), DateFormats.FULL_FORMAT) + " and keep the passenger satisfaction up! Click on the Management tab and then choose Allocations. Good luck!", "Council", "INBOX", gameController.getCurrentSimTime());
            //Refresh messages.
            String[] messageSubjects = messageController.getMessageSubjects(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council");
            messagesModel.removeAllElements();
            for ( int i = 0; i < messageSubjects.length; i++ ) {
                messagesModel.addElement(messageSubjects[i]);
            }
            messagesList.setSelectedIndex(0);
            if ( messageSubjects.length == 0 && dateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder.");
            }
            else if ( messageSubjects.length == 0 ) {
                messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder for the date " + dateBox.getSelectedItem().toString() + ".");
            }
            else {
                messagesArea.setText(messageController.getMessageText(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(), "Council", messagesList.getSelectedIndex()));
            }
            dateModel.addElement(userInterface.formatDateString(gameController.getCurrentSimTime(), DateFormats.FULL_TIME_FORMAT).split("-")[0]);
            //Then display it to the user.
            doneAllocations = true;
            userInterface.pauseSimulation();
            topPanel.getComponent(1).setVisible(false);
            tabbedPane.setSelectedIndex(1);
            //Now here we need to update satisfaction bar.
            timeLabel.setText(userInterface.formatDateString(gameController.getCurrentSimTime(), DateFormats.FULL_TIME_FORMAT));
            int satValue = userInterface.computeAndReturnPassengerSatisfaction();
            if ( satValue < userInterface.getMinimumSatisfaction() ) {
                userInterface.pauseSimulation();
                JOptionPane.showMessageDialog(ControlScreen.this, userInterface.getScenarioName() + " have relunctanly decided to relieve you of your duties as managing director as passenger satisfaction is now " + satValue + "%.", "Sorry You Have Been Sacked!", JOptionPane.ERROR_MESSAGE);
                new WelcomeScreen(new UserInterface());
                dispose();
            }
            passengerSatisfactionBar.setValue(satValue);
            passengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + passengerSatisfactionBar.getValue() + "%");
            passengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
            //Update graphics panel.
            //theDialogPanel.remove(theGraphicsPanel);
            //theDialogPanel.add(theGraphicsPanel);
            //Repaint the whole interface immediately.
            dialogPanel.paintImmediately(dialogPanel.getBounds());
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
            graphicsPanel = generateNewVehiclePanel();
        }
        if ( isRedraw ) {
            dialogPanel.remove(dialogPanel.getComponent(1));
            graphicsPanel = generateNewVehiclePanel();
            tabbedPane.removeAll();
            tabbedPane.addTab("Live Situation", graphicsPanel);
            drawMessages();
            tabbedPane.addTab("Messages", messagesPanel);
            //Create manage tab.
            tabbedPane.addTab("Management", managementPanel.getDisplayPanel());
            if ( userInterface.getMessageScreen() ) {
                tabbedPane.setSelectedIndex(1);
            }
            else if ( userInterface.getManagementScreen() ) {
                tabbedPane.setSelectedIndex(2);
            }
            tabbedPane.addMouseListener(new MouseListener() {
                public void mouseExited ( MouseEvent e ) { }
                public void mouseEntered ( MouseEvent e ) { }
                public void mouseReleased ( MouseEvent e ) { }
                public void mousePressed ( MouseEvent e ) { }
                public void mouseClicked ( MouseEvent e ) { 
                    logger.debug("You just selected the " + tabbedPane.getSelectedIndex() + " component");
                    if ( tabbedPane.getSelectedIndex() == 1) {
                        topPanel.getComponent(1).setVisible(false);
                        userInterface.setMessageScreen(true);
                        userInterface.setManagementScreen(false);
                        userInterface.pauseSimulation(); //Pause simulation for message screen.
                    }
                    else if ( tabbedPane.getSelectedIndex() == 2 ) {
                        topPanel.getComponent(1).setVisible(false);
                        userInterface.setManagementScreen(true);
                        userInterface.setMessageScreen(false);
                        userInterface.pauseSimulation(); //Pause simulation for management screen.
                    }
                    else {
                        topPanel.getComponent(1).setVisible(true);
                        userInterface.setMessageScreen(false);
                        userInterface.setManagementScreen(false);
                        userInterface.resumeSimulation(); //Resume simulation for live screen.
                    }
                }
            });
            //Now disable live situation if no routes.
            if ( userInterface.areRoutesDefined() ) {
                tabbedPane.setEnabledAt(0, false);
            }
            dialogPanel.add(tabbedPane, 1);
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
            timeLabel.setText(userInterface.formatDateString(gameController.getCurrentSimTime(), DateFormats.FULL_TIME_FORMAT));
            //Now here we need to update satisfaction bar.
            int satValue = userInterface.computeAndReturnPassengerSatisfaction();
            if ( satValue < userInterface.getMinimumSatisfaction() ) {
                userInterface.pauseSimulation();
                JOptionPane.showMessageDialog(ControlScreen.this, userInterface.getScenarioName() + " have relunctanly decided to relieve you of your duties as managing director as passenger satisfaction is now " + satValue + "%.", "Sorry You Have Been Sacked!", JOptionPane.ERROR_MESSAGE);
                new WelcomeScreen(new UserInterface());
                dispose();
            }
            passengerSatisfactionBar.setValue(satValue);
            passengerSatisfactionBar.setString("Passenger Satisfaction Rating - " + passengerSatisfactionBar.getValue() + "%");
            passengerSatisfactionBar.setFont(new Font("Arial", Font.ITALIC, 20));
            //Update graphics panel.
            //theDialogPanel.remove(theGraphicsPanel);
            //theDialogPanel.add(theGraphicsPanel);
            //Repaint the whole interface immediately.
            dialogPanel.paintImmediately(dialogPanel.getBounds());
        }
    }
    
    /**
     * Draw the messages panel.
     */
    public void drawMessages ( ) {
        //Create theMessages panel.
        messagesPanel = new JPanel(new BorderLayout());
        messagesPanel.setBackground(Color.WHITE);
        //Create a list of messages as the north panel.
        messagesModel = new DefaultListModel();
        messagesList = new JList(messagesModel);
        messagesList.setVisibleRowCount(5);
        messagesArea = new JTextArea();
        messagesList.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent lse ) {
                if ( messagesList.getSelectedIndex() != -1 ) {
                    messagesArea.setText(messageController.getMessageText(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council",messagesList.getSelectedIndex()));
                }
            }
        });
        messagesPanel.add(messagesList, BorderLayout.NORTH);
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
        dateModel = new DefaultComboBoxModel();
        dateModel.addElement("All Dates");
        for ( int i = 0; i < messageController.getNumberMessages(); i++ ) {
            logger.debug("Index of " + dateModel.getIndexOf(messageController.getMessageDateByPosition(i)));
            if ( dateModel.getIndexOf(messageController.getMessageDateByPosition(i)) == -1 ) {
                dateModel.addElement(messageController.getMessageDateByPosition(i));
            }
        }
        dateBox = new JComboBox(dateModel);
        dateBox.setFont(new Font("Arial", Font.PLAIN, 12));
        dateBox.addItemListener( new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                String[] subjects = messageController.getMessageSubjects(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(), "Council");
                messagesModel.removeAllElements();
                for ( int i = 0; i < subjects.length; i++ ) {
                    messagesModel.addElement(subjects[i]);
                }
                messagesList.setSelectedIndex(0);
                if ( subjects.length == 0 && dateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                    messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder.");
                }
                else if ( subjects.length == 0 ) {
                    messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder for the date " + dateBox.getSelectedItem().toString() + ".");
                }
                else {
                    messagesArea.setText(messageController.getMessageText(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(), "Council", messagesList.getSelectedIndex()));
                }
            }
        });
        datePanel.add(dateBox);
        //Add datePanel to westPanel.
        westPanel.add(datePanel, BorderLayout.NORTH);
        //Messages type panel.
        JPanel messageTypePanel = new JPanel(new GridBagLayout());
        messageTypePanel.setBackground(Color.WHITE);
        //Create message type heading.
        JLabel messageTypeLabel = new JLabel("Message Type:");
        messageTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageTypePanel.add(messageTypeLabel);
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
        foldersBox = new JComboBox(new String[] { "Inbox", "Sent Items" });
        foldersBox.setFont(new Font("Arial", Font.PLAIN, 12));
        foldersBox.addItemListener ( new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                String[] subjects = messageController.getMessageSubjects(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council");
                messagesModel.removeAllElements();
                for ( int i = 0; i < subjects.length; i++ ) {
                    messagesModel.addElement(subjects[i]);
                }
                messagesList.setSelectedIndex(0);
                if ( subjects.length == 0 && dateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
                    messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder.");
                }
                else if ( subjects.length == 0 ) {
                    messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder for the date " + dateBox.getSelectedItem().toString() + ".");
                }
                else {
                    messagesArea.setText(messageController.getMessageText(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council", messagesList.getSelectedIndex()));
                }
            }
        });
        foldersPanel.add(foldersBox);
        //Add folders panel to west panel.
        westPanel.add(foldersPanel, BorderLayout.SOUTH);
        //Add west panel to messages panel.
        messagesPanel.add(westPanel, BorderLayout.WEST);
        //Initialise the messages list now.
        String[] subjects = messageController.getMessageSubjects(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council");
        for ( int i = 0; i < subjects.length; i++ ) {
            messagesModel.addElement(subjects[i]);
        }
        messagesList.setSelectedIndex(0);
        
        //theMessagesPanel.add(makeOptionsPanel(), BorderLayout.NORTH);
        //BoxLayout layout = new BoxLayout(theMessagesPanel, BoxLayout.Y_AXIS);
        //theMessagesPanel.setLayout(layout);
        //Create a text area with a scroll pane and add this to the interface.
        logger.debug("I'm drawing messages....");
        JPanel messageAndButtonPanel = new JPanel(new BorderLayout());
        messageAndButtonPanel.setBackground(Color.WHITE);
        JScrollPane messagesPane = new JScrollPane();
        if ( subjects.length == 0 && dateBox.getSelectedItem().toString().equalsIgnoreCase("All Dates") ) {
            messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " folder.");
        }
        else if ( subjects.length == 0 ) {
            messagesArea.setText("There are no messages in the " + foldersBox.getSelectedItem().toString() + " for the date " + dateBox.getSelectedItem().toString() + " folder.");
        }
        else {
            messagesArea.setText(messageController.getMessageText(foldersBox.getSelectedItem().toString(),dateBox.getSelectedItem().toString(),"Council", messagesList.getSelectedIndex()));
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
     */
    public void redrawManagement ( JPanel newManagePanel ) {
        //Disable the live situation tab if appropriate.
        if ( !userInterface.areRoutesDefined() ) {
            tabbedPane.setEnabledAt(0, false);
        }
        //Otherwise, re-enable live panel.
        else {
            tabbedPane.setEnabledAt(0, true);
        }

        tabbedPane.setComponentAt(2, newManagePanel);
        dialogPanel.paintImmediately(dialogPanel.getBounds());
        DecimalFormat form = new DecimalFormat("0.00");
        balanceLabel.setText("Balance: £" + form.format(userInterface.getBalance()));
    }

    /**
     * Redraw the vehicle panel as appropriate.
     */
    public void redrawVehicles ( JPanel newVehiclePanel ) {
        tabbedPane.setComponentAt(0, newVehiclePanel);
        dialogPanel.paintImmediately(dialogPanel.getBounds());
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
        if ( routeModel.getSize() > 0) {
            userInterface.setCurrentDisplayMinMax(minVehicle, maxVehicle,routeNumber.split(":")[0]);
        }
        //Create vehicle Panel as a JPanel!
        if ( userInterface.getNumCurrentDisplaySchedules() == 0 ) {
            vehiclePanel = new JPanel(new GridLayout(2, 1));
        }
        else {
            vehiclePanel = new JPanel(new GridLayout(userInterface.getNumCurrentDisplaySchedules()+1, 1));
            logger.debug("Route number in vehicle panel is " + routeList.getSelectedValue().toString().split(":")[0]);
        }
        JPanel stopRowPanel;
        if (routeModel.getSize() > 0 ) {
            stopRowPanel = new JPanel(new GridLayout(1, userInterface.getNumStops(routeList.getSelectedValue().toString().split(":")[0])));
            //First of all, create a first row of panels which is equal to the number of stops!
            stopPanels = new ArrayList<JPanel>();
            for ( int i = 0; i < userInterface.getNumStops(routeList.getSelectedValue().toString().split(":")[0]); i++ ) {
                JPanel stopPanel = new JPanel();
                stopPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1), BorderFactory.createEmptyBorder(5,5,5,5)));
                stopPanel.setBackground(Color.LIGHT_GRAY);
                JLabel stopLabel = new JLabel(userInterface.getStopName(routeList.getSelectedValue().toString().split(":")[0], i));
                stopLabel.setFont(new Font("Arial", Font.BOLD, 15));
                stopPanel.add(stopLabel);
                stopPanels.add(stopPanel);
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
        if ( userInterface.getNumCurrentDisplaySchedules() == 0 ) {
            JLabel noVehiclesLabel = new JLabel("This route currently has no vehicles!");
            noVehiclesLabel.setFont(new Font("Arial", Font.BOLD, 20));
            vehiclePanel.add(noVehiclesLabel);
            allVehicleDisplayPanel.add(vehiclePanel, BorderLayout.CENTER);
            allVehicleDisplayPanel.add(makeVehicleInfoPanel(), BorderLayout.SOUTH);
            return allVehicleDisplayPanel;
        }
        //Otherwise add a label with the route schedule number for each potential vehicle.
        for ( int i = 0; i < userInterface.getNumCurrentDisplaySchedules(); i++ ) {
            //Get the schedule id and vehicle position.
            long routeScheduleId = userInterface.getDisplaySchedule(routeList.getSelectedValue().toString().split(":")[0], i);
            String vehiclePos =  userInterface.getCurrentStopName(routeScheduleId, gameController.getCurrentSimTime(), userInterface.getDifficultyLevel());
            logger.debug(routeScheduleId + " is at " + vehiclePos + " seconds with delay " + userInterface.getDelay(routeScheduleId) + " minutes.");
            /*if ( rs.hasDelay() ) {
                theInterface.addMessage(theSimulator.getMessageDisplaySimTime() + ": Vehicle " + schedId + " is running " + rs.getDelay() + " minutes late.");
            }*/
            //Get direction first of all we are travelling in!
            List<String> outwardStops = new ArrayList<String>();
            for ( int j = 0; j < stopPanels.size(); j++ ) {
                outwardStops.add(((JLabel) stopPanels.get(j).getComponent(0)).getText());
            }
            int direction = DrawingPanel.RIGHT_TO_LEFT;
            if ( journeyController.getCurrentJourney(routeScheduleId, gameController.getCurrentSimTime()) == null ) {
                continue; //Don't print if the vehicle is at a terminus.
            }
            if ( journeyController.isOutwardJourney(routeScheduleId, gameController.getCurrentSimTime(), outwardStops) ) {
                direction = DrawingPanel.LEFT_TO_RIGHT;
                direction = DrawingPanel.LEFT_TO_RIGHT; 
            }
            //Now we want to find the position where we draw the triangle i.e. position of JLabel.
            int xPos = 0;
            String previousStop = "N/A";
            for ( int j = 0; j < stopPanels.size(); j++ ) {
                //Each stopPanel has one component which is JLabel.
                JLabel myLabel = (JLabel) stopPanels.get(j).getComponent(0);
                //Now check where this stop is and get its position.
                //logger.debug("Comparing " + myLabel.getText() + " against " + thisVehiclePos);
                if ( myLabel.getText().equalsIgnoreCase(vehiclePos) ) {
                    int panelSize = 800 / stopPanels.size();
                    int startPos = 0 + ( panelSize * j);
                    int endPos = (panelSize * (j+1))-1;
                    if ( j == (stopPanels.size()-1) ) {
                        endPos = (panelSize * (j+1))-(panelSize/2);
                    }
                    //Debug.
                    logger.debug("This is stop " + myLabel.getText() + " - range is from " + startPos + " to " + endPos);
                    //xPos = startPos + (panelSize/2 - (panelSize/4));
                    if ( previousStop.equalsIgnoreCase("N/A") ) {
                        xPos = startPos;
                    }
                    else {
                        long maxTimeDiff = Math.abs(journeyController.getStopMaxTimeDiff(routeScheduleId, gameController.getCurrentSimTime(), previousStop, myLabel.getText()));
                        if ( maxTimeDiff == Integer.MAX_VALUE ) {
                            xPos = startPos;
                        } 
                        //If inward, then low percentage means close, high means far away.
                        if ( direction == DrawingPanel.RIGHT_TO_LEFT ) {
                            xPos = (int) Math.round((1.0 * (endPos - startPos))) + startPos;
                            logger.debug("Recommeding xPos of " + xPos);
                        }
                        //If outward, reverse is true i.e. high means close, low means far away.
                        else {
                            xPos = (int) Math.round((0.0 * (endPos - startPos))) + startPos;
                            logger.debug("Recommeding xPos of " + xPos);
                        }
                        //xPos = startPos + (panelSize/2 - (panelSize/4));
                    }
                }
                else {
                    previousStop = myLabel.getText();
                }
            }
            logger.debug("I'm drawing route schedule " + routeScheduleId);
            JPanel drawPanel = new DrawingPanel(xPos, direction, userInterface.getDelay(routeScheduleId) > 0 );
            drawPanel.addMouseListener(new BusMouseListener(userInterface.getDisplaySchedule(routeList.getSelectedValue().toString().split(":")[0], i), userInterface));
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
     * Pause the simulation.
     */
    public void pauseSimulation ( ) {
        userInterface.pauseSimulation();
    }

    public void populateRouteList ( ) {
        routeModel.clear();
        //String[] allRouteStr = new String[theSimulator.getScenario().getNumberRoutes()];
        for ( int i = 0; i < userInterface.getNumberRoutes(); i++ ) {
            routeModel.addElement(userInterface.getRouteNumber(userInterface.getRoute(i)));
            //allRouteStr[i] = theSimulator.getScenario().getRoutes().get(i).toString();
        }
        //theRouteList = new JList(allRouteStr);
        logger.debug("Route number in control screen is " + routeNumber);
        if ( !routeNumber.equalsIgnoreCase("") ) {
            routeList.setSelectedValue(routeNumber, true);
        }
        else if ( routeList.getModel().getSize() > 0 ){
            routeList.setSelectedIndex(0);
            routeNumber = routeList.getSelectedValue().toString();
        }
    }
    
    public JPanel makeOptionsPanel ( ) {
        logger.debug("Calling makeOptions panel....");
        //Construct options panel and add it to the top panel.
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setLayout(new BorderLayout());
        //Construct route listing box!
        if ( !userInterface.areRoutesDefined() ) {
            //theRouteList = new JList(new String[] { "No Routes Available" });
            //theRouteList.setVisibleRowCount(3);
            logger.debug("Created route list!");
        }
        else {
            populateRouteList();
        }
        routeList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged (ListSelectionEvent ie ) {
                if ( redrawOnRouteChange ) {
                    userInterface.pauseSimulation();
                    ControlScreen.this.redrawVehicles(generateNewVehiclePanel());
                    userInterface.resumeSimulation();
                }
                //logger.debug("Moving to route " + theRouteList.getSelectedValue().toString().split(":")[0]);
                //theInterface.setSimulator(theSimulator);
                //theInterface.changeRoute(theRouteList.getSelectedValue().toString());
                //logger.debug("You now want " + theRouteList.getSelectedValue().toString());
                //dispose();
            }
        });
        optionsPanel.add(routeList, BorderLayout.CENTER);
        //Construct simulator control panel.
        JPanel simulatorControlPanel = new JPanel();
        simulatorControlPanel.setBackground(Color.WHITE);
        //Simulator Options Label.
        simulatorOptionsLabel = new JLabel("Simulator Options: ");
        simulatorOptionsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        simulatorControlPanel.add(simulatorOptionsLabel);
        //Slow Simulation Button.
        slowSimulationButton = new JButton("<<");
        slowSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                userInterface.slowSimulation();
                if ( userInterface.getSimulationSpeed() >= 4000 ) {
                    slowSimulationButton.setEnabled(false);
                }
                speedUpSimulationButton.setEnabled(true);
            }
        });
        simulatorControlPanel.add(slowSimulationButton);
        //Pause Simulation Button.
        pauseSimulationButton = new JButton("||");
        pauseSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                if ( pauseSimulationButton.getText().equalsIgnoreCase("||") ) {
                    pauseSimulationButton.setText("|>");
                    userInterface.pauseSimulation();
                }
                else if ( pauseSimulationButton.getText().equalsIgnoreCase("|>") ) {
                    pauseSimulationButton.setText("||");
                    userInterface.resumeSimulation();
                }
            }
        });
        simulatorControlPanel.add(pauseSimulationButton);
        //Speed Up Simulation Button.
        speedUpSimulationButton = new JButton(">>");
        speedUpSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                userInterface.speedUpSimulation();
                if ( userInterface.getSimulationSpeed() <= 1000 ) {
                    speedUpSimulationButton.setEnabled(false);
                }
                slowSimulationButton.setEnabled(true);
            }
        });
        simulatorControlPanel.add(speedUpSimulationButton);
        //Time Increment Label.
        timeIncrementLabel = new JLabel("      Time Increment: ");
        timeIncrementLabel.setFont(new Font("Arial", Font.BOLD, 15));
        simulatorControlPanel.add(timeIncrementLabel);
        //Time Increment Spinner Field.
        timeIncrementSpinner = new JSpinner(new SpinnerNumberModel(userInterface.getTimeIncrement(),5,60,5));
        timeIncrementSpinner.setFont(new Font("Arial", Font.BOLD, 15));
        timeIncrementSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent ce ) {
                userInterface.setTimeIncrement(Integer.parseInt(timeIncrementSpinner.getValue().toString()));
            }
        });
        simulatorControlPanel.add(timeIncrementSpinner);
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
        currentPage = (minVehicle/4); if ( (minVehicle+1) % 4 !=0 || currentPage == 0 ) { currentPage++; }
        int totalPages;
        if ( routeList.getModel().getSize() > 0 ) {
            totalPages = (userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0])/4); if ((userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0])%4) !=0 || totalPages == 0 ) { totalPages++; }
        }
        else {
            totalPages = 0;
        }
        numPagesLabel = new JLabel("Page " + currentPage + " / " + totalPages);
        logger.debug("This is page " + currentPage + " / " + totalPages);
        vehicleInfoPanel.add(numPagesLabel);
        //Previous vehicle button.
        previousVehiclesButton = new JButton("Previous Vehicles");
        previousVehiclesButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Subtract 4 to the min and max.
                currentPage--;
                minVehicle = (currentPage * 4)-4; maxVehicle = (currentPage*4);
                //Now check if min vehicle is less than 0 - set it to 0.
                if ( minVehicle < 0 ) {
                    minVehicle = 0;
                }
                userInterface.pauseSimulation();
                ControlScreen.this.redrawVehicles(generateNewVehiclePanel());
                userInterface.resumeSimulation();
                //theInterface.changeDisplay(theRouteList.getSelectedValue().toString(), theMinVehicle, theMaxVehicle, false);
                //dispose();
            }
        });
        if ( totalPages == 0 || userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0]) < 5 || minVehicle == 0 ) {
            previousVehiclesButton.setEnabled(false);
        }
        vehicleInfoPanel.add(previousVehiclesButton);
        //Next vehicle button.
        nextVehiclesButton = new JButton("Next Vehicles");
        nextVehiclesButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Add 4 to the min and max.
                minVehicle = userInterface.getCurrentMinVehicle() + 4; maxVehicle = userInterface.getCurrentMaxVehicle() + 5;
                //Now check if max vehicle is bigger than display vehicles - if it is then set it to display vehicles.
                if ( maxVehicle > userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0]) ) {
                    maxVehicle = userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0]);
                }
                userInterface.pauseSimulation();
                ControlScreen.this.redrawVehicles(generateNewVehiclePanel());
                userInterface.resumeSimulation();
                //theInterface.changeDisplay(theRouteNumber, theMinVehicle, theMaxVehicle, false);
                //dispose();
            }
        });
        if ( totalPages == 0 || userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0]) < 5 || maxVehicle == userInterface.getNumRouteDisplayVehicles(routeList.getSelectedValue().toString().split(":")[0]) ) {
            nextVehiclesButton.setEnabled(false);
        }
        vehicleInfoPanel.add(nextVehiclesButton);
        //Return vehicleInfoPanel.
        return vehicleInfoPanel;
    }
    
}

