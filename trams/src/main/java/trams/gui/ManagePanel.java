package trams.gui;

import javax.swing.*;
import javax.swing.event.*;

import trams.data.*;
import trams.main.UserInterface;
import trams.simulation.Simulator;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.text.*;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ArrayList;
import trams.main.*;
import trams.simulation.*;

/**
 * This class displays the management options for the control screen in TraMS.
 * @author Dave Lee
 */
public class ManagePanel {

    private JLabel theScenarioLabel;
    private JButton theViewScenarioButton;
    private JLabel theRoutesLabel;
    private JButton theAddRouteButton;
    private JButton theRouteTimetableButton;
    private JLabel theVehiclesLabel;
    private JButton thePurchaseVehicleScreenButton;
    private JButton theViewDepotButton;
    private JLabel theDriversLabel;
    private JButton theEmployDriversButton;
    private JButton theViewDriversButton;
    private JLabel theAllocationsLabel;
    private JButton theChangeAllocationButton;
    
    private UserInterface theInterface;
    private Simulator theSimulator;
    private ControlScreen theControlScreen;
    
    /** THESE VARIABLES ARE NEEDED FOR ACTION LISTENERS ETC. **/
    private JTextField theRouteNumberField;
    private JComboBox[] theStopBoxes;
    private DefaultListModel theTimetableModel;
    private JList theTimetableList;
    private JButton theCreateTimetableButton;
    private JButton theModifyTimetableButton;
    private JButton theDeleteTimetableButton;
    private JButton theCreateRouteButton;
    private Route theSelectedRoute;
    private String theSelectedTimetableName;

    private JTextField theDriverNameField;
    private JSpinner theContractedHoursSpinner;
    private Calendar theStartDate;

    private JTextField theTimetableNameField;
    private DefaultComboBoxModel theValidFromDayModel;
    private int theFromStartDay;
    private JComboBox theValidFromMonthBox;
    private DefaultComboBoxModel theValidToDayModel;
    private int theToStartDay;
    private JComboBox theValidToMonthBox;
    private JComboBox theTerminus1Box;
    private ArrayList<String> theStopNames;
    private JComboBox theTerminus2Box;
    private JSpinner theFromHourSpinner;
    private JSpinner theFromMinuteSpinner;
    private JSpinner theToHourSpinner;
    private JSpinner theToMinuteSpinner;
    private JSpinner theEveryMinuteSpinner;
    private SpinnerNumberModel theEveryMinuteModel;
    private JLabel theMinVehicleLabel;
    
    private DefaultListModel theRoutesModel;
    private JList theRoutesList;

    private DefaultListModel theAllocationsModel;
    private JList theAllocationsList;
    
    private DefaultListModel theServicePatternModel;
    private JList theServicePatternList;
    private ServicePattern theSelectedServicePattern;
    private JButton theCreateServicePatternButton;
    private JButton theModifyServicePatternButton;
    private JButton theDeleteServicePatternButton;
    
    private JButton theCreateSPButton;
    private JCheckBox[] theDaysBox;
    private JTextField theServicePatternNameField;
    
    private int theTypePosition;
    private String theVehicleId;
    
    private DefaultListModel theVehiclesModel;
    private JList theVehiclesList;
    private Vehicle theVehicle;
    private Calendar theDeliveryDate;
    private JSpinner theQuantitySpinner;
    private JLabel theTotalPriceField;
    private DecimalFormat theFormat;
    private JButton thePurchaseVehicleButton;
    
    private String theSelectedRouteStr;
    private int theCurrentMin;
    private JComboBox theDatesComboBox;
    
    public ManagePanel ( UserInterface ui, Simulator sim, ControlScreen cs ) {
        theInterface = ui;
        theSimulator = sim;
        theControlScreen = cs;
    }
    
    public JPanel getDisplayPanel ( ) {
        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
        //Create a label for an information picture and an information area.
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.WHITE);
        ImageDisplay infoDisplay = null;
        if ( theInterface.getNumberRoutes() == 0 || theInterface.getNumberVehicles() == 0 || theInterface.getAllocations().size() == 0 ) {
            infoDisplay = new ImageDisplay("xpic.png",0,0);
        }
        else {
            infoDisplay = new ImageDisplay("infopic.png",0,0);
        }
        infoDisplay.setSize(50,50);
        infoDisplay.setBackground(Color.WHITE);
        informationPanel.add(infoDisplay, BorderLayout.WEST);
        JTextArea informationArea = new JTextArea();
        informationArea.setFont(new Font("Arial", Font.PLAIN, 14));
        if ( theInterface.getNumberRoutes() == 0 ) {
            informationArea.setText("WARNING: No routes have been devised yet. Click 'Create Route' to define a route.");
        }
        else if ( theInterface.getNumberVehicles() == 0 ) {
            informationArea.setText("WARNING: You can't run routes without vehicles. Click 'Purchase Vehicle' to buy a vehicle");
        }
        else if ( theInterface.getAllocations().size() == 0 ) {
            informationArea.setText("WARNING: To successfully run services, you must assign vehicles to route schedules. Click 'Allocations' to match vehicles to route schedules");
        }
        else {
            System.out.println("The allocations size was " + theInterface.getAllocations().size() + " which is " + theInterface.getAllocations().toString());
            informationArea.setText(theInterface.getRandomTipMessage());
        }
        informationArea.setRows(4);
        informationArea.setColumns(50);
        informationArea.setLineWrap(true);
        informationArea.setWrapStyleWord(true);
        informationPanel.add(informationArea);
        overallScreenPanel.add(informationPanel, BorderLayout.NORTH);
        //Panels for routes, vehicles and allocation modifications.
        JPanel gridPanel = new JPanel(new GridLayout(2,3,5,5));
        gridPanel.setBackground(Color.WHITE);
        //Scenario Panel.
        JPanel scenarioPanel = new JPanel(new BorderLayout());
        scenarioPanel.setBackground(Color.WHITE);
        scenarioPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scenarioPanel.setLayout ( new BoxLayout ( scenarioPanel, BoxLayout.PAGE_AXIS ) );
        JPanel scenarioLabelPanel = new JPanel();
        scenarioLabelPanel.setBackground(Color.WHITE);
        theScenarioLabel = new JLabel("Scenario:", SwingConstants.CENTER);
        theScenarioLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scenarioLabelPanel.add(theScenarioLabel);
        scenarioPanel.add(scenarioLabelPanel, BorderLayout.NORTH);
        //Create description panel.
        JPanel scenarioDescriptionPanel = new JPanel(new BorderLayout());
        scenarioDescriptionPanel.setBackground(Color.WHITE);
        JTextArea scenarioDescriptionArea = new JTextArea("View scenario information, game targets and the location map.");
        scenarioDescriptionArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        scenarioDescriptionArea.setWrapStyleWord(true);
        scenarioDescriptionArea.setLineWrap(true);
        scenarioDescriptionArea.setFont(new Font("Arial", Font.ITALIC, 14));
        scenarioDescriptionPanel.add(scenarioDescriptionArea);
        scenarioPanel.add(scenarioDescriptionPanel, BorderLayout.CENTER);
        //Scenario buttons.
        JPanel scenarioButtonPanel = new JPanel();
        scenarioButtonPanel.setLayout ( new BoxLayout ( scenarioButtonPanel, BoxLayout.PAGE_AXIS ) );
        scenarioButtonPanel.setBackground(Color.WHITE);
        JPanel viewScenarioButtonPanel = new JPanel(new GridBagLayout());
        viewScenarioButtonPanel.setBackground(Color.WHITE);
        theViewScenarioButton = new JButton("View Information");
        theViewScenarioButton.addActionListener( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeScenarioPanel());
            }
        });
        viewScenarioButtonPanel.add(theViewScenarioButton);
        scenarioButtonPanel.add(viewScenarioButtonPanel);
        scenarioButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel locationMapButtonPanel = new JPanel(new GridBagLayout());
        locationMapButtonPanel.setBackground(Color.WHITE);
        JButton locationMapButton = new JButton("Location Map");
        locationMapButton.addActionListener( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeLocationMapPanel());
            }
        });
        locationMapButtonPanel.add(locationMapButton);
        scenarioButtonPanel.add(locationMapButtonPanel);
        scenarioPanel.add(scenarioButtonPanel, BorderLayout.SOUTH);
        gridPanel.add(scenarioPanel);
        //Route panel.
        JPanel routePanel = new JPanel();
        routePanel.setBackground(Color.WHITE);
        routePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        routePanel.setLayout ( new BoxLayout ( routePanel, BoxLayout.PAGE_AXIS ) );
        JPanel routeLabelPanel = new JPanel();
        routeLabelPanel.setBackground(Color.WHITE);
        theRoutesLabel = new JLabel("Routes:", SwingConstants.CENTER);
        theRoutesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        routeLabelPanel.add(theRoutesLabel);
        routePanel.add(routeLabelPanel);
        //Create description panel.
        JPanel routeDescriptionPanel = new JPanel(new BorderLayout());
        routeDescriptionPanel.setBackground(Color.WHITE);
        JTextArea routeDescriptionArea = new JTextArea("Create routes, view/amend route timetables and remove routes.");
        routeDescriptionArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        routeDescriptionArea.setWrapStyleWord(true);
        routeDescriptionArea.setLineWrap(true);
        routeDescriptionArea.setFont(new Font("Arial", Font.ITALIC, 14));
        routeDescriptionPanel.add(routeDescriptionArea, BorderLayout.CENTER);
        routePanel.add(routeDescriptionPanel);
        //Route buttons.
        JPanel routeButtonPanel = new JPanel();
        routeButtonPanel.setLayout ( new BoxLayout ( routeButtonPanel, BoxLayout.PAGE_AXIS ) );
        routeButtonPanel.setBackground(Color.WHITE);
        JPanel createRouteButtonPanel = new JPanel(new GridBagLayout());
        createRouteButtonPanel.setBackground(Color.WHITE);
        theAddRouteButton = new JButton("Create Route");
        theAddRouteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeAddRoutePanel(null));
            }
        });
        createRouteButtonPanel.add(theAddRouteButton);
        routeButtonPanel.add(createRouteButtonPanel);
        routeButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel timetableButtonPanel = new JPanel(new GridBagLayout());
        timetableButtonPanel.setBackground(Color.WHITE);
        theRouteTimetableButton = new JButton("View Route Info");
        if ( theInterface.getNumberRoutes() == 0 ) { theRouteTimetableButton.setEnabled(false); }
        theRouteTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeTimetablePanel(theInterface.getRoute(0).getRouteNumber(), 0, 0));
            }
        });
        timetableButtonPanel.add(theRouteTimetableButton);
        routeButtonPanel.add(timetableButtonPanel);
        routePanel.add(routeButtonPanel);
        gridPanel.add(routePanel);
        //Vehicle panel.
        JPanel vehiclePanel = new JPanel();
        vehiclePanel.setBackground(Color.WHITE);
        vehiclePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vehiclePanel.setLayout(new BorderLayout());
        JPanel vehicleLabelPanel = new JPanel();
        vehicleLabelPanel.setBackground(Color.WHITE);
        theVehiclesLabel = new JLabel("Vehicles:", SwingConstants.CENTER);
        theVehiclesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        vehicleLabelPanel.add(theVehiclesLabel);
        vehiclePanel.add(vehicleLabelPanel, BorderLayout.NORTH);
        //Create description panel.
        JPanel vehicleDescriptionPanel = new JPanel(new BorderLayout());
        vehicleDescriptionPanel.setBackground(Color.WHITE);
        JTextArea descriptionArea = new JTextArea("Purchase vehicles, view current vehicles and sell old vehicles");
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("Arial", Font.ITALIC, 14));
        descriptionArea.setColumns(25);
        vehicleDescriptionPanel.add(descriptionArea);
        vehiclePanel.add(vehicleDescriptionPanel, BorderLayout.CENTER);
        //Vehicle buttons.
        JPanel vehicleButtonPanel = new JPanel();
        vehicleButtonPanel.setLayout ( new BoxLayout ( vehicleButtonPanel, BoxLayout.PAGE_AXIS ) );
        vehicleButtonPanel.setBackground(Color.WHITE);
        JPanel purchaseVehicleButtonPanel = new JPanel(new GridBagLayout());
        purchaseVehicleButtonPanel.setBackground(Color.WHITE);
        thePurchaseVehicleScreenButton = new JButton("Purchase");
        thePurchaseVehicleScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makePurchaseVehiclePanel(0));
            }
        });
        purchaseVehicleButtonPanel.add(thePurchaseVehicleScreenButton);
        vehicleButtonPanel.add(purchaseVehicleButtonPanel);
        vehicleButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDepotButtonPanel = new JPanel(new GridBagLayout());
        viewDepotButtonPanel.setBackground(Color.WHITE);
        theViewDepotButton = new JButton("View Depot");
        if ( !theInterface.hasSomeVehiclesBeenDelivered() ) { theViewDepotButton.setEnabled(false); }
        theViewDepotButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeVehicleDepotPanel(""));
            }
        });
        viewDepotButtonPanel.add(theViewDepotButton);
        vehicleButtonPanel.add(viewDepotButtonPanel);
        vehiclePanel.add(vehicleButtonPanel, BorderLayout.SOUTH);
        gridPanel.add(vehiclePanel);
        //Driver panel.
        JPanel driverPanel = new JPanel();
        driverPanel.setBackground(Color.WHITE);
        driverPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        driverPanel.setLayout(new BorderLayout());
        JPanel driverLabelPanel = new JPanel();
        driverLabelPanel.setBackground(Color.WHITE);
        theDriversLabel = new JLabel("", SwingConstants.CENTER);
        //theDriversLabel = new JLabel("Drivers:", SwingConstants.CENTER);
        theDriversLabel.setFont(new Font("Arial", Font.BOLD, 18));
        driverLabelPanel.add(theDriversLabel);
        driverPanel.add(driverLabelPanel, BorderLayout.NORTH);
        //Create description panel.
        JPanel driverDescriptionPanel = new JPanel(new BorderLayout());
        driverDescriptionPanel.setBackground(Color.WHITE);
        //JTextArea driverDescriptionArea = new JTextArea("Employ drivers, view current employees and sack drivers");
        JTextArea driverDescriptionArea = new JTextArea("");
        driverDescriptionArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        driverDescriptionArea.setWrapStyleWord(true);
        driverDescriptionArea.setLineWrap(true);
        driverDescriptionArea.setFont(new Font("Arial", Font.ITALIC, 14));
        driverDescriptionArea.setColumns(25);
        driverDescriptionPanel.add(driverDescriptionArea);
        driverPanel.add(driverDescriptionPanel, BorderLayout.CENTER);
        //Driver buttons.
        JPanel driverButtonPanel = new JPanel();
        driverButtonPanel.setLayout ( new BoxLayout ( driverButtonPanel, BoxLayout.PAGE_AXIS ) );
        driverButtonPanel.setBackground(Color.WHITE);
        JPanel employDriverButtonPanel = new JPanel(new GridBagLayout());
        employDriverButtonPanel.setBackground(Color.WHITE);
        theEmployDriversButton = new JButton("Employ");
        theEmployDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeEmployDriverPanel(0));
            }
        });
        employDriverButtonPanel.add(theEmployDriversButton);
        driverButtonPanel.add(employDriverButtonPanel);
        driverButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDriversButtonPanel = new JPanel(new GridBagLayout());
        viewDriversButtonPanel.setBackground(Color.WHITE);
        theViewDriversButton = new JButton("View Drivers");
        if ( !theInterface.hasSomeDriversBeenEmployed() ) { theViewDriversButton.setEnabled(false); }
        theViewDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeViewDriversPanel(""));
            }
        });
        viewDriversButtonPanel.add(theViewDriversButton);
        driverButtonPanel.add(viewDriversButtonPanel);
        //driverPanel.add(driverButtonPanel, BorderLayout.SOUTH);
        gridPanel.add(driverPanel);
        //Allocation panel.
        JPanel allocationPanel = new JPanel();
        allocationPanel.setBackground(Color.WHITE);
        allocationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        allocationPanel.setLayout ( new BoxLayout ( allocationPanel, BoxLayout.PAGE_AXIS ) );
        JPanel allocationLabelPanel = new JPanel();
        allocationLabelPanel.setBackground(Color.WHITE);
        theAllocationsLabel = new JLabel("Allocations:", SwingConstants.CENTER);
        theAllocationsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        allocationLabelPanel.add(theAllocationsLabel);
        allocationPanel.add(allocationLabelPanel);
        //Create description panel.
        JPanel allocationDescriptionPanel = new JPanel(new BorderLayout());
        allocationDescriptionPanel.setBackground(Color.WHITE);
        JTextArea allocationDescriptionArea = new JTextArea("Allocate or deallocate vehicles to routes.");
        allocationDescriptionArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        allocationDescriptionArea.setWrapStyleWord(true);
        allocationDescriptionArea.setLineWrap(true);
        allocationDescriptionArea.setFont(new Font("Arial", Font.ITALIC, 14));
        allocationDescriptionPanel.add(allocationDescriptionArea, BorderLayout.CENTER);
        allocationPanel.add(allocationDescriptionPanel);
        //Allocation button.
        JPanel allocationButtonPanel = new JPanel();
        allocationButtonPanel.setBackground(Color.WHITE);
        theChangeAllocationButton = new JButton("Change");
        theChangeAllocationButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeAllocationPanel());
            }
        });
        allocationButtonPanel.add(theChangeAllocationButton);
        allocationPanel.add(allocationButtonPanel);
        gridPanel.add(allocationPanel);
        //Blank panel.
        JPanel blank1Panel = new JPanel();
        blank1Panel.setBackground(Color.WHITE);
        blank1Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        blank1Panel.setLayout(new BorderLayout());
        gridPanel.add(blank1Panel);
        
        overallScreenPanel.add(gridPanel, BorderLayout.CENTER);
        
        return overallScreenPanel;
    }
    
    public JPanel makeAddRoutePanel( Route amendRouteObj ) {
        //Initialise selected route.
        theSelectedRoute = amendRouteObj;
        
        //Create routeScreen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout ( new BoxLayout ( routeScreenPanel, BoxLayout.PAGE_AXIS ) );
        routeScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Route", SwingConstants.CENTER);
        if ( amendRouteObj != null ) { topLabel.setText("Amend Route"); }
        topLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        routeScreenPanel.add(topLabelPanel);
                
        //Create panel for route number first of all.
        JPanel routeNumberPanel = new JPanel(new GridBagLayout());
        routeNumberPanel.setBackground(Color.WHITE);
        JLabel routeNumberLabel = new JLabel("Route Number: ", SwingConstants.CENTER);
        routeNumberLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        routeNumberPanel.add(routeNumberLabel);
        theRouteNumberField = new JTextField(10);
        theRouteNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        if ( amendRouteObj != null ) { theRouteNumberField.setText(amendRouteObj.getRouteNumber()); }
        theRouteNumberField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                enableCreateButtons();
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        routeNumberPanel.add(theRouteNumberField);
        
        //Add the routeNumber panel to the screen panel.
        routeScreenPanel.add(routeNumberPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Now create stops - a 2 x 3 grid layout.
        JPanel stopGridPanel = new JPanel(new GridLayout(2,3,5,5));
        stopGridPanel.setBackground(Color.WHITE);
        //Create the boxes and labels as appropriate.
        //Create the stops.
        JPanel[] stopPanels = new JPanel[5];
        theStopBoxes = new JComboBox[5];
        for ( int i = 0; i < stopPanels.length; i++ ) {
            stopPanels[i] = new JPanel(new GridBagLayout());
            stopPanels[i].setBackground(Color.WHITE);
            JLabel stopLabel = new JLabel("Stop " + (i+1) + ":");
            stopLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            stopPanels[i].add(stopLabel);
            theStopBoxes[i] = new JComboBox(theInterface.getScenario().getStopNames());
            theStopBoxes[i].setFont(new Font("Arial", Font.PLAIN, 14));
            theStopBoxes[i].setSelectedIndex(theStopBoxes[i].getItemCount()-1);
            if ( amendRouteObj != null ) { 
                if ( amendRouteObj.getNumStops(Route.OUTWARDSTOPS) > i ) {
                    int findIndexPos = findIndex(amendRouteObj.getStop(Route.OUTWARDSTOPS, i).getStopName(), i);
                    if ( findIndexPos != -1 ) { theStopBoxes[i].setSelectedIndex(findIndexPos); }
                }
            }
            if ( i < 2 ) {
                theStopBoxes[i].addActionListener( new ActionListener() {
                    public void actionPerformed ( ActionEvent e ) {
                        enableCreateButtons();
                    }
                });
            }
            stopPanels[i].add(theStopBoxes[i]);
            stopGridPanel.add(stopPanels[i]);
        }
        //There's no stop 6 so it is just a filler.
        JPanel tempPanel = new JPanel();
        tempPanel.setBackground(Color.WHITE);
        stopGridPanel.add(tempPanel);
        
        //Add the stopGrid panel to the screen panel.
        routeScreenPanel.add(stopGridPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create the timetable list panel and three buttons.
        JPanel timetableListPanel = new JPanel(new BorderLayout());
        timetableListPanel.setBackground(Color.WHITE);
        JPanel routeDetailsLabelPanel = new JPanel();
        routeDetailsLabelPanel.setBackground(Color.WHITE);
        JLabel routeTimetableLabel = new JLabel("Route Timetable:");
        routeTimetableLabel.setFont(new Font("Arial", Font.ITALIC, 17));
        routeDetailsLabelPanel.add(routeTimetableLabel);
        timetableListPanel.add(routeDetailsLabelPanel, BorderLayout.NORTH);
        //Here is the actual timetable list.
        JPanel centreTimetableListPanel = new JPanel(new GridBagLayout());
        centreTimetableListPanel.setBackground(Color.WHITE);
        theTimetableModel = new DefaultListModel();
        //Now get all the timetables which we have at the moment.
        try {
            Iterator<String> timetableKeys = theSelectedRoute.getTimetableNames();
            while ( timetableKeys.hasNext() ) {
                String timetableName = timetableKeys.next();
                theTimetableModel.addElement(theSelectedRoute.getTimetable(timetableName).getName());
            }
        } 
        catch (NullPointerException npe) { }
        theTimetableList = new JList(theTimetableModel);
        if ( theTimetableModel.getSize() != 0 ) {
            theTimetableList.setSelectedIndex(0);
        }
        theTimetableList.setVisibleRowCount(3);
        theTimetableList.setFixedCellWidth(450);
        theTimetableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timetablePane = new JScrollPane(theTimetableList);
        centreTimetableListPanel.add(timetablePane);
        timetableListPanel.add(centreTimetableListPanel, BorderLayout.CENTER);
        //Now the three create, modify and delete button.
        JPanel timetableButtonPanel = new JPanel();
        timetableButtonPanel.setBackground(Color.WHITE);
        theCreateTimetableButton = new JButton("Create");
        theCreateTimetableButton.setEnabled(false);
        theCreateTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //First of all, set the selected route.
                if ( theSelectedRoute == null && theTimetableModel.getSize() == 0 ) {
                    theSelectedRoute = new Route();
                    theSelectedRoute.setRouteNumber(theRouteNumberField.getText());
                    for ( int i = 0; i < theStopBoxes.length; i++ ) {
                        if ( !theStopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                            theSelectedRoute.addStop(theStopBoxes[i].getSelectedItem().toString(), Route.OUTWARDSTOPS);
                        }
                    }
                    for ( int i = (theStopBoxes.length-1); i >=0; i-- ) {
                        if ( !theStopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                            theSelectedRoute.addStop(theStopBoxes[i].getSelectedItem().toString(), Route.RETURNSTOPS);
                        }
                    }
                }
                //Show the actual screen!
                theControlScreen.redrawManagement(makeCreateTimetablePanel(""));
            }
        });
        timetableButtonPanel.add(theCreateTimetableButton);
        theModifyTimetableButton = new JButton("Modify");
        if ( theTimetableModel.getSize() == 0 ) { theModifyTimetableButton.setEnabled(false); }
        theModifyTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e ) {
                theControlScreen.redrawManagement(makeCreateTimetablePanel(theTimetableList.getSelectedValue().toString()));
            }
        });
        timetableButtonPanel.add(theModifyTimetableButton);
        theDeleteTimetableButton = new JButton("Delete");
        if ( theTimetableModel.getSize() == 0 ) { theDeleteTimetableButton.setEnabled(false); }
        theDeleteTimetableButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theSelectedRoute.deleteTimetable(theTimetableList.getSelectedValue().toString());
                theTimetableModel.removeElement(theTimetableList.getSelectedValue());
                if ( theTimetableModel.getSize() == 0 ) {
                    theDeleteTimetableButton.setEnabled(false);
                    theModifyTimetableButton.setEnabled(false);
                }
                else {
                    theTimetableList.setSelectedIndex(0);
                }
            }
        });
        timetableButtonPanel.add(theDeleteTimetableButton);
        timetableListPanel.add(timetableButtonPanel, BorderLayout.SOUTH);
        
        //Add the timetableList panel to the screen panel.
        routeScreenPanel.add(timetableListPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        theCreateRouteButton = new JButton("Create Route");
        theCreateRouteButton.setEnabled(false);
        theCreateRouteButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
               List<Service> outgoingServices = theSelectedRoute.generateServiceTimetables(theInterface.getCurrentSimTime(), theInterface.getScenario(), Route.OUTWARDSTOPS);
               List<Service> returnServices = theSelectedRoute.generateServiceTimetables(theInterface.getCurrentSimTime(), theInterface.getScenario(), Route.RETURNSTOPS);
               theSelectedRoute.generateRouteSchedules(outgoingServices, returnServices);
               theInterface.addNewRoute(theSelectedRoute);
               //Now return to previous screen.
               theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        bottomButtonPanel.add(theCreateRouteButton);
        
        //Create new route button and add it to screen panel.
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Check enabling of buttons.
        enableCreateButtons();
        
        //Add bottom button panel to the screen panel.
        routeScreenPanel.add(bottomButtonPanel);
        
        //Return routeScreenPanel.
        return routeScreenPanel;
    }
    
    public JPanel makeCreateTimetablePanel(String timetableName) {
        //Get the timetable here for initialise data parts.
        Timetable myTimetable = theSelectedRoute.getTimetable(timetableName);
        
        //Create timetableScreen panel to add things to.
        JPanel timetableScreenPanel = new JPanel();
        timetableScreenPanel.setLayout ( new BoxLayout ( timetableScreenPanel, BoxLayout.PAGE_AXIS ) );
        timetableScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Timetable", SwingConstants.CENTER);
        if ( myTimetable != null ) {
            topLabel.setText("Modify Timetable");
        }
        topLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        timetableScreenPanel.add(topLabelPanel);
        
        //Create timetable name panel.
        JPanel timetableNamePanel = new JPanel(new GridBagLayout());
        timetableNamePanel.setBackground(Color.WHITE);
        JLabel timetableNameLabel = new JLabel("Name: ");
        timetableNameLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timetableNamePanel.add(timetableNameLabel);
        theTimetableNameField = new JTextField(20);
        if ( myTimetable != null ) { theTimetableNameField.setText(myTimetable.getName()); }
        theTimetableNameField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                if ( !theTimetableNameField.getText().equalsIgnoreCase("") ) {
                    theCreateServicePatternButton.setEnabled(true);
                }
                else {
                    theCreateServicePatternButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        theTimetableNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        timetableNamePanel.add(theTimetableNameField);
        timetableScreenPanel.add(timetableNamePanel);
                
        //Create panel for validity first of all.
        JPanel validityPanel = new JPanel(new GridBagLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromLabel = new JLabel("Valid From: ", SwingConstants.CENTER);
        validFromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validFromLabel);
        //Get the calendar object with current time.
        Calendar currTime = (Calendar) theInterface.getCurrentSimTime().clone();
        currTime.add(Calendar.HOUR, 0); //Change this to 48!!!!
        //Valid From Day.
        theFromStartDay = currTime.get(Calendar.DAY_OF_MONTH);
        theValidFromDayModel = new DefaultComboBoxModel();
        for ( int i = currTime.get(Calendar.DAY_OF_MONTH); i <= getMonthLen(getMonth(currTime.get(Calendar.MONTH))); i++ ) {
            theValidFromDayModel.addElement(i);
        }
        JComboBox validFromDayBox = new JComboBox(theValidFromDayModel);
        if ( myTimetable != null ) {
            validFromDayBox.setSelectedItem(myTimetable.getValidFrom().get(Calendar.DAY_OF_MONTH));
        }
        validFromDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validFromDayBox);
        //Valid From Month.
        theValidFromMonthBox = new JComboBox();
        for ( int i = 0; i < 4; i++ ) {
            theValidFromMonthBox.addItem(getMonth(currTime.get(Calendar.MONTH)) + " " + currTime.get(Calendar.YEAR));
            currTime.add(Calendar.MONTH, 1);
        }
        if ( myTimetable != null ) {
            theValidFromMonthBox.setSelectedItem(getMonth(myTimetable.getValidFrom().get(Calendar.MONTH)) + " " + myTimetable.getValidFrom().get(Calendar.YEAR));
        }
        theValidFromMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        theValidFromMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                String month = theValidFromMonthBox.getSelectedItem().toString().split(" ")[0];
                if ( theValidFromMonthBox.getSelectedIndex() == 0 ) {
                    theValidFromDayModel.removeAllElements();
                    for ( int i = theFromStartDay; i <= getMonthLen(month); i++ ) {
                        theValidFromDayModel.addElement(i);
                    }
                }
                else {
                    theValidFromDayModel.removeAllElements();
                    for ( int i = 1; i <= getMonthLen(month); i++ ) {
                        theValidFromDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(theValidFromMonthBox);
        //Valid to!!!
        JLabel validToLabel = new JLabel("Valid To: ", SwingConstants.CENTER);
        validToLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validToLabel);
        //Get the calendar object with current time.
        Calendar myCurrTime = (Calendar) theInterface.getCurrentSimTime().clone();
        myCurrTime.add(Calendar.HOUR, 72);
        //Valid To Day.
        theToStartDay = myCurrTime.get(Calendar.DAY_OF_MONTH);
        theValidToDayModel = new DefaultComboBoxModel();
        for ( int i = myCurrTime.get(Calendar.DAY_OF_MONTH); i <= getMonthLen(getMonth(myCurrTime.get(Calendar.MONTH))); i++ ) {
            theValidToDayModel.addElement(i);
        }
        JComboBox validToDayBox = new JComboBox(theValidToDayModel);
        if ( myTimetable != null ) {
            validToDayBox.setSelectedItem(myTimetable.getValidTo().get(Calendar.DAY_OF_MONTH));
        }
        validToDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validToDayBox);
        //Valid To Month.
        theValidToMonthBox = new JComboBox();
        for ( int i = 0; i < 25; i++ ) {
            theValidToMonthBox.addItem(getMonth(myCurrTime.get(Calendar.MONTH)) + " " + myCurrTime.get(Calendar.YEAR));
            myCurrTime.add(Calendar.MONTH, 1);
        }
        if ( myTimetable != null ) {
            theValidToMonthBox.setSelectedItem(getMonth(myTimetable.getValidTo().get(Calendar.MONTH)) + " " + myTimetable.getValidTo().get(Calendar.YEAR));
        }
        theValidToMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        theValidToMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                String month = theValidToMonthBox.getSelectedItem().toString().split(" ")[0];
                if ( theValidToMonthBox.getSelectedIndex() == 0 ) {
                    theValidToDayModel.removeAllElements();
                    for ( int i = theToStartDay; i <= getMonthLen(month); i++ ) {
                        theValidToDayModel.addElement(i);
                    }
                }
                else {
                    theValidToDayModel.removeAllElements();
                    for ( int i = 1; i <= getMonthLen(month); i++ ) {
                        theValidToDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(theValidToMonthBox);
       
        //Add validityPanel to the screen panel.
        timetableScreenPanel.add(validityPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create label in middle of screen in a middleLabelPanel added to screenPanel.
        JPanel middleLabelPanel = new JPanel(new BorderLayout());
        middleLabelPanel.setBackground(Color.WHITE);
        JLabel middleLabel = new JLabel("Service Pattern(s)", SwingConstants.CENTER);
        middleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        middleLabel.setVerticalAlignment(JLabel.CENTER);
        middleLabelPanel.add(middleLabel, BorderLayout.CENTER);
        timetableScreenPanel.add(middleLabelPanel);
        
        //Create the servicePattern list panel and three buttons.
        JPanel servicePatternListPanel = new JPanel(new BorderLayout());
        servicePatternListPanel.setBackground(Color.WHITE);
        //Here is the actual service pattern list.
        JPanel centreServicePatternListPanel = new JPanel(new GridBagLayout());
        centreServicePatternListPanel.setBackground(Color.WHITE);
        theServicePatternModel = new DefaultListModel();
        //Now get all the service pattern which we have at the moment.
        try {
            Iterator<String> patternKeys = theSelectedRoute.getTimetable(theTimetableNameField.getText()).getServicePatternNames().iterator();
            while ( patternKeys.hasNext() ) {
                String servicePatternName = patternKeys.next();
                theServicePatternModel.addElement(theSelectedRoute.getTimetable(theTimetableNameField.getText()).getServicePattern(servicePatternName).getName());
            }
        } 
        catch (NullPointerException npe) { }
        theServicePatternList = new JList(theServicePatternModel);
        if ( theServicePatternModel.getSize() > 0 ) { theServicePatternList.setSelectedIndex(0); } 
        theServicePatternList.setVisibleRowCount(3);
        theServicePatternList.setFixedCellWidth(450);
        theServicePatternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timetablePane = new JScrollPane(theServicePatternList);
        centreServicePatternListPanel.add(timetablePane);
        servicePatternListPanel.add(centreServicePatternListPanel, BorderLayout.CENTER);
        //Now the three create, modify and delete button.
        JPanel servicePatternButtonPanel = new JPanel();
        servicePatternButtonPanel.setBackground(Color.WHITE);
        theCreateServicePatternButton = new JButton("Create");
        if (theTimetableNameField.getText().equalsIgnoreCase("")) { theCreateServicePatternButton.setEnabled(false); }
        theCreateServicePatternButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Create relevant calendar object.
                if ( theSelectedRoute.getTimetable(theTimetableNameField.getText()) == null) {
                    int vfYear = Integer.parseInt(theValidFromMonthBox.getSelectedItem().toString().split(" ")[1]);
                    int vfMonth = getMonthNumber(theValidFromMonthBox.getSelectedItem().toString().split(" ")[0]);
                    int vfDay = Integer.parseInt(theValidFromDayModel.getSelectedItem().toString());
                    GregorianCalendar validFrom = new GregorianCalendar(vfYear, vfMonth, vfDay);
                    int vtYear = Integer.parseInt(theValidToMonthBox.getSelectedItem().toString().split(" ")[1]);
                    int vtMonth = getMonthNumber(theValidToMonthBox.getSelectedItem().toString().split(" ")[0]);
                    int vtDay = Integer.parseInt(theValidToDayModel.getSelectedItem().toString());
                    GregorianCalendar validTo = new GregorianCalendar(vtYear, vtMonth, vtDay);
                    //Save this timetable with valid dates first.
                    theSelectedRoute.addTimetable(theTimetableNameField.getText(), new Timetable(theTimetableNameField.getText(), validFrom, validTo));
                    //System.out.println("Adding timetable with name " + theTimetableNameField.getText() + " to route " + theSelectedRoute.getRouteNumber());
                }
                //Process the stops.
                ArrayList<String> stops = new ArrayList<String>();
                for ( int i = 0; i < theStopBoxes.length; i++ ) {
                    if ( !theStopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        stops.add(theStopBoxes[i].getSelectedItem().toString());
                    }
                }
                //Show the actual screen!
                theControlScreen.redrawManagement(makeServicePatternPanel(stops, theTimetableNameField.getText(), null));
            }
        });
        servicePatternButtonPanel.add(theCreateServicePatternButton);
        theModifyServicePatternButton = new JButton("Modify");
        theModifyServicePatternButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                ServicePattern sp = theSelectedRoute.getTimetable(theTimetableNameField.getText()).getServicePattern(theServicePatternList.getSelectedValue().toString());
                //Process the stops.
                ArrayList<String> stops = new ArrayList<String>();
                for ( int i = 0; i < theStopBoxes.length; i++ ) {
                    if ( !theStopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        stops.add(theStopBoxes[i].getSelectedItem().toString());
                    }
                }
                theControlScreen.redrawManagement(ManagePanel.this.makeServicePatternPanel(stops, theTimetableNameField.getText(), sp));
            }
        });
        if ( theServicePatternModel.getSize() == 0 ) { theModifyServicePatternButton.setEnabled(false); }
        servicePatternButtonPanel.add(theModifyServicePatternButton);
        theDeleteServicePatternButton = new JButton("Delete");
        theDeleteServicePatternButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theSelectedRoute.getTimetable(theTimetableNameField.getText()).deleteServicePattern(theServicePatternList.getSelectedValue().toString());
                theServicePatternModel.removeElement(theServicePatternList.getSelectedValue());
                if ( theServicePatternModel.getSize() == 0 ) {
                    theDeleteServicePatternButton.setEnabled(false);
                    theModifyServicePatternButton.setEnabled(false);
                }
                else {
                    theServicePatternList.setSelectedIndex(0);
                }
            }
        });
        if ( theServicePatternModel.getSize() == 0 ) { theDeleteServicePatternButton.setEnabled(false); }
        servicePatternButtonPanel.add(theDeleteServicePatternButton);
        servicePatternListPanel.add(servicePatternButtonPanel, BorderLayout.SOUTH);
        //Add the servicePatternList panel to the screen panel.
        timetableScreenPanel.add(servicePatternListPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        JButton createTimetableButton = new JButton("Create Timetable");
        if(theServicePatternModel.getSize()==0) { createTimetableButton.setEnabled(false); }
        createTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Timetable is already saved so we just go back to the original screen.
                theControlScreen.redrawManagement(ManagePanel.this.makeAddRoutePanel(theSelectedRoute));
            }
        });
        bottomButtonPanel.add(createTimetableButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Cancel addition.
                theControlScreen.redrawManagement(ManagePanel.this.makeAddRoutePanel(theSelectedRoute));
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Check enabling of buttons.
        enableCreateButtons();
        
        //Add bottom button panel to the screen panel.
        timetableScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return timetableScreenPanel;
    }
    
    public JPanel makeServicePatternPanel ( ArrayList<String> myStopNames, String timetableName, ServicePattern sp ) {
        //Initialise selected timetable.
        theSelectedTimetableName = timetableName;
        //Initialise service pattern.
        theSelectedServicePattern = sp;
        
        //Create servicePatternScreen panel to add things to.
        JPanel servicePatternScreenPanel = new JPanel();
        servicePatternScreenPanel.setLayout ( new BoxLayout ( servicePatternScreenPanel, BoxLayout.PAGE_AXIS ) );
        servicePatternScreenPanel.setBackground(Color.WHITE);
        
        //Initialise stop names.
        theStopNames = myStopNames;
        
        //Create label in middle of screen in a middleLabelPanel added to screenPanel.
        JPanel middleLabelPanel = new JPanel(new BorderLayout());
        middleLabelPanel.setBackground(Color.WHITE);
        JLabel middleLabel = new JLabel("Create Service Pattern", SwingConstants.CENTER);
        if ( sp != null ) { middleLabel.setText("Modify Service Pattern"); }
        middleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        middleLabel.setVerticalAlignment(JLabel.CENTER);
        middleLabelPanel.add(middleLabel, BorderLayout.CENTER);
        servicePatternScreenPanel.add(middleLabelPanel);
        
        //Create service pattern name panel.
        JPanel servicePatternNamePanel = new JPanel(new GridBagLayout());
        servicePatternNamePanel.setBackground(Color.WHITE);
        JLabel servicePatternNameLabel = new JLabel("Name: ");
        servicePatternNameLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        servicePatternNamePanel.add(servicePatternNameLabel);
        theServicePatternNameField = new JTextField(20);
        if ( sp != null ) { theServicePatternNameField.setText(sp.getName()); }
        theServicePatternNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        theServicePatternNameField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                if ( !theServicePatternNameField.getText().equalsIgnoreCase("") ) {
                    for ( int i = 0; i < theDaysBox.length; i++ ) {
                        if ( theDaysBox[i].isSelected() ) {
                            theCreateSPButton.setEnabled(true);
                            return;
                        }
                    }
                    theCreateSPButton.setEnabled(false);
                }
                else {
                    theCreateSPButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        servicePatternNamePanel.add(theServicePatternNameField);
        servicePatternScreenPanel.add(servicePatternNamePanel);
        
        //Create day of week panel with 7 tick boxes.
        JPanel dayOfWeekPanel = new JPanel(new GridBagLayout());
        dayOfWeekPanel.setBackground(Color.WHITE);
        theDaysBox = new JCheckBox[7];
        String[] dayStr = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        for ( int i = 0; i < theDaysBox.length; i++ ) {
            theDaysBox[i] = new JCheckBox(dayStr[i]);
            theDaysBox[i].setFont(new Font("Arial", Font.PLAIN, 14));
            theDaysBox[i].addActionListener(new ActionListener() {
                public void actionPerformed ( ActionEvent e ) {
                    if ( !theServicePatternNameField.getText().equalsIgnoreCase("") ) {
                        for ( int i = 0; i < theDaysBox.length; i++ ) {
                            if ( theDaysBox[i].isSelected() ) {
                                theCreateSPButton.setEnabled(true);
                                return;
                            }
                        }
                        theCreateSPButton.setEnabled(false);
                    }
                    else {
                        theCreateSPButton.setEnabled(false);
                    }
                }
            });
            int addPos = (i+1);
            if ( sp!=null ) { if ( sp.getDaysOfOperation().contains("" + addPos) ) { theDaysBox[i].setSelected(true); } }
            dayOfWeekPanel.add(theDaysBox[i]);
        }
        servicePatternScreenPanel.add(dayOfWeekPanel);
        
        //Create panel with between stops.
        JPanel betweenStopsPanel = new JPanel(new GridBagLayout());
        betweenStopsPanel.setBackground(Color.WHITE);
        //Between label.
        JLabel betweenLabel = new JLabel("Between:");
        betweenLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(betweenLabel);
        //Terminus 1 Combo box.
        theTerminus1Box = new JComboBox();
        for ( int i = 0; i < theStopNames.size()-1; i++ ) {
            theTerminus1Box.addItem(theStopNames.get(i));
        }
        theTerminus1Box.setSelectedIndex(0);
        if ( sp!=null ) { theTerminus1Box.setSelectedItem(sp.getReturnTerminus()); }
        theTerminus1Box.setFont(new Font("Arial", Font.PLAIN, 14));
        theTerminus1Box.addItemListener(new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                //Update terminus 2 box!!!
                theTerminus2Box.removeAllItems();
                for ( int i = (theStopNames.indexOf(theTerminus1Box.getSelectedItem().toString()))+1; i < theStopNames.size(); i++ ) {
                    theTerminus2Box.addItem(theStopNames.get(i));
                }
                //Update spinner!
                theEveryMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(theEveryMinuteSpinner.getValue().toString())));
            }
        });
        betweenStopsPanel.add(theTerminus1Box);
        //And label.
        JLabel andLabel = new JLabel("  and  ");
        andLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(andLabel);
        //Terminus 2 Combo box.
        theTerminus2Box = new JComboBox();
        for ( int i = 1; i < theStopNames.size(); i++ ) {
            theTerminus2Box.addItem(theStopNames.get(i));
        }
        theTerminus2Box.setSelectedIndex(theTerminus2Box.getItemCount() - 1);
        if ( sp!= null) { theTerminus2Box.setSelectedItem(sp.getOutgoingTerminus()); }
        theTerminus2Box.setFont(new Font("Arial", Font.PLAIN, 14));
        theTerminus2Box.addItemListener( new ItemListener () {
            public void itemStateChanged ( ItemEvent e ) {
                //Update spinner!
                if ( theTerminus2Box.getItemCount() != 0 ) {
                    theEveryMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(theEveryMinuteSpinner.getValue().toString())));
                }
            }
        });
        betweenStopsPanel.add(theTerminus2Box);
        //Add betweenStopsPanel.
        servicePatternScreenPanel.add(betweenStopsPanel);
        
        //Create panel with between times and every x frequency - this is bascially full of spinners.
        JPanel timesPanel = new JPanel(new GridBagLayout());
        timesPanel.setBackground(Color.WHITE);
        //From + times.
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(fromLabel);
        theFromHourSpinner = new JSpinner(new SpinnerNumberModel(6,0,23,1));
        if ( sp!=null ) { theFromHourSpinner.setValue(sp.getStartTime().get(Calendar.HOUR_OF_DAY)); }
        theFromHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(theFromHourSpinner);
        theFromMinuteSpinner = new JSpinner(new SpinnerNumberModel(0,0,59,1));
        if ( sp!=null ) { theFromMinuteSpinner.setValue(sp.getStartTime().get(Calendar.MINUTE)); }
        theFromMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(theFromMinuteSpinner);
        //To + times.
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(toLabel);
        theToHourSpinner = new JSpinner(new SpinnerNumberModel(18,0,23,1));
        if ( sp!=null ) { theToHourSpinner.setValue(sp.getEndTime().get(Calendar.HOUR_OF_DAY)); }
        theToHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(theToHourSpinner);
        theToMinuteSpinner = new JSpinner(new SpinnerNumberModel(30,0,59,1));
        if ( sp!=null ) { theToMinuteSpinner.setValue(sp.getEndTime().get(Calendar.MINUTE)); }
        theToMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(theToMinuteSpinner);
        //Every.
        JLabel everyLabel = new JLabel("Every: ");
        everyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(everyLabel);
        int min = 10;
        if ( min > getCurrentRouteDuration(1) ) { min = getCurrentRouteDuration(1); }
        theEveryMinuteModel = new SpinnerNumberModel(min,1,getMaxRouteDuration(),1);
        theEveryMinuteSpinner = new JSpinner(theEveryMinuteModel);
        //Initialise minVehicles label here but then actually place it later.
        theMinVehicleLabel = new JLabel("NOTE: " + getMinVehicles() + " vehicles are required to operate " + theEveryMinuteSpinner.getValue().toString() + " minute frequency!" );
        theEveryMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        theEveryMinuteSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                theMinVehicleLabel.setText("NOTE: " + getMinVehicles() + " vehicles are required to operate " + theEveryMinuteSpinner.getValue().toString() + " minute frequency!");
            }
        });
        if ( sp!=null ) { System.out.println("Frequency is " + sp.getFrequency() + " and duration is " + sp.getDuration()); theEveryMinuteModel.setValue(sp.getFrequency()); }
        timesPanel.add(theEveryMinuteSpinner);
        JLabel minutesLabel = new JLabel("minutes");
        minutesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(minutesLabel);
        
        servicePatternScreenPanel.add(timesPanel);
        
        //Create panel to state vehicles required to maintain frequency.
        JPanel minVehiclePanel = new JPanel(new GridBagLayout());
        minVehiclePanel.setBackground(Color.WHITE);
        
        theMinVehicleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        minVehiclePanel.add(theMinVehicleLabel);
        servicePatternScreenPanel.add(minVehiclePanel);
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        theCreateSPButton = new JButton("Create Service Pattern");
        if ( sp != null ) { theCreateSPButton.setText("Modify Service Pattern"); }
        else { theCreateSPButton.setEnabled(false); }
        theCreateSPButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Create a linked list of days selected.
                String operatingDays = "";
                for ( int i = 0; i < theDaysBox.length; i++ ) {
                    if ( theDaysBox[i].isSelected() ) {
                        operatingDays += (i+1) + ",";
                    }
                }
                //Create time from.
                GregorianCalendar timeFrom = new GregorianCalendar(2009,7,3,Integer.parseInt(theFromHourSpinner.getValue().toString()), Integer.parseInt(theFromMinuteSpinner.getValue().toString()));
                //Create time to.
                GregorianCalendar timeTo = new GregorianCalendar(2009,7,3,Integer.parseInt(theToHourSpinner.getValue().toString()), Integer.parseInt(theToMinuteSpinner.getValue().toString()));
                //Create + add service pattern.
                if ( theSelectedServicePattern != null ) {
                    theSelectedRoute.getTimetable(theSelectedTimetableName).getServicePattern(theSelectedServicePattern.getName()).editServicePattern(theServicePatternNameField.getText(), operatingDays, theTerminus1Box.getSelectedItem().toString(), theTerminus2Box.getSelectedItem().toString(), timeFrom, timeTo, Integer.parseInt(theEveryMinuteSpinner.getValue().toString()), getCurrentRouteDuration(Integer.parseInt(theEveryMinuteSpinner.getValue().toString())));
                }
                else {
                    System.out.println("I am calling add method with timetable name " + theSelectedTimetableName + "!");
                    ServicePattern sp = new ServicePattern(theServicePatternNameField.getText(), operatingDays, theTerminus1Box.getSelectedItem().toString(), theTerminus2Box.getSelectedItem().toString(), timeFrom, timeTo, Integer.parseInt(theEveryMinuteSpinner.getValue().toString()), getCurrentRouteDuration(Integer.parseInt(theEveryMinuteSpinner.getValue().toString())) );
                    theSelectedRoute.getTimetable(theSelectedTimetableName).addServicePattern(theServicePatternNameField.getText(), sp);
                }
                //Now return to the timetable screen.
                theControlScreen.redrawManagement(ManagePanel.this.makeCreateTimetablePanel(theSelectedTimetableName));
            }
        });
        bottomButtonPanel.add(theCreateSPButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Return to the timetable screen.
                theControlScreen.redrawManagement(ManagePanel.this.makeCreateTimetablePanel(theSelectedTimetableName));
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Add bottom button panel to the screen panel.
        servicePatternScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return servicePatternScreenPanel;
        
    }
    
    public int findIndex ( String text, int pos ) {
        for ( int i = 0; i < theStopBoxes[pos].getItemCount(); i++ ) {
            if ( theStopBoxes[pos].getItemAt(i).toString().equalsIgnoreCase(text) ) {
                return i;
            }
        }
        return -1;
    }
    
    public void enableCreateButtons ( ) {
        //To enable create timetable button we need the selected item in stop1Box and stop2Box to not be -.
        if ( !theStopBoxes[0].getSelectedItem().toString().equalsIgnoreCase("-") && !theStopBoxes[1].getSelectedItem().toString().equalsIgnoreCase("-") && !theRouteNumberField.getText().equalsIgnoreCase("") ) {
            theCreateTimetableButton.setEnabled(true);
            //In addition, the timetable model must not be 0 to create a route.
            if ( theTimetableModel.getSize() > 0 ) {
                theCreateRouteButton.setEnabled(true);
            }
        }
    }
    
    /**
     * Get the length of the month based on the supplied month.
     * @param month a <code>String</code> with the month.
     * @return a <code>int</code> with the month length.
     */
    private int getMonthLen ( String month ) {
        if ( month.equalsIgnoreCase("January") ) { return 31; }
        else if ( month.equalsIgnoreCase("February") ) { return 28; }
        else if ( month.equalsIgnoreCase("March") ) { return 31; }
        else if ( month.equalsIgnoreCase("April") ) { return 30; }
        else if ( month.equalsIgnoreCase("May") ) { return 31; }
        else if ( month.equalsIgnoreCase("June") ) { return 30; }
        else if ( month.equalsIgnoreCase("July") ) { return 31; }
        else if ( month.equalsIgnoreCase("August") ) { return 31; }
        else if ( month.equalsIgnoreCase("September") ) { return 30; }
        else if ( month.equalsIgnoreCase("October") ) { return 31; }
        else if ( month.equalsIgnoreCase("November") ) { return 30; }
        else if ( month.equalsIgnoreCase("December") ) { return 31; }
        else { return 0; }
    }
    
    /**
     * Get the month as a String based on the number.
     * @param month a <code>int</code> with the month number.
     * @return a <code>String</code> with the string representation of the month.
     */
    private String getMonth ( int month ) {
        if ( month == Calendar.JANUARY ) { return "January"; }
        else if ( month == Calendar.FEBRUARY ) { return "February"; }
        else if ( month == Calendar.MARCH ) { return "March"; }
        else if ( month == Calendar.APRIL ) { return "April"; }
        else if ( month == Calendar.MAY ) { return "May"; }
        else if ( month == Calendar.JUNE ) { return "June"; }
        else if ( month == Calendar.JULY ) { return "July"; }
        else if ( month == Calendar.AUGUST ) { return "August"; }
        else if ( month == Calendar.SEPTEMBER ) { return "September"; }
        else if ( month == Calendar.OCTOBER ) { return "October"; }
        else if ( month == Calendar.NOVEMBER ) { return "November"; }
        else if ( month == Calendar.DECEMBER ) { return "December"; }
        else { return ""; }
    }
    
    /**
     * Get the month as an int based on the String.
     * @param month a <code>String</code> with the month.
     * @return a <code>int</code> with the int representation of the month.
     */
    private int getMonthNumber ( String month ) {
        if ( month.equalsIgnoreCase("January") ) { return Calendar.JANUARY; }
        else if ( month.equalsIgnoreCase("February") ) { return Calendar.FEBRUARY; }
        else if ( month.equalsIgnoreCase("March") ) { return Calendar.MARCH; }
        else if ( month.equalsIgnoreCase("April") ) { return Calendar.APRIL; }
        else if ( month.equalsIgnoreCase("May") ) { return Calendar.MAY; }
        else if ( month.equalsIgnoreCase("June") ) { return Calendar.JUNE; }
        else if ( month.equalsIgnoreCase("July") ) { return Calendar.JULY; }
        else if ( month.equalsIgnoreCase("August") ) { return Calendar.AUGUST; }
        else if ( month.equalsIgnoreCase("September") ) { return Calendar.SEPTEMBER; }
        else if ( month.equalsIgnoreCase("October") ) { return Calendar.OCTOBER; }
        else if ( month.equalsIgnoreCase("November") ) { return Calendar.NOVEMBER; }
        else if ( month.equalsIgnoreCase("December") ) { return Calendar.DECEMBER; }
        else { return -1; }
    }
    
    private int getCurrentRouteDuration ( int frequency ) {
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 first of all - this is guaranteed.
        cumDistance += theInterface.getScenario().getDistance(theTerminus1Box.getSelectedItem().toString(), theTerminus2Box.getItemAt(0).toString());
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = theTerminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            int myDistance = (cumDistance*2);
            int myCumFreq = (frequency*2);
            System.out.println("Distance was " + myDistance);
            while ( myCumFreq < myDistance ) {
                myCumFreq += (frequency*2);
            }
            System.out.println("Actual distance is " + myCumFreq);
            return myCumFreq;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += theInterface.getScenario().getDistance(theTerminus2Box.getItemAt(i-1).toString(), theTerminus2Box.getItemAt(i).toString());
        }
        //Return distance * 2.
        int myDistance = (cumDistance*2);
        int myCumFreq = (frequency*2);
        System.out.println("Distance was " + myDistance);
        while ( myCumFreq < myDistance ) {
            myCumFreq += (frequency*2);
        }
        System.out.println("Actual distance is " + myCumFreq);
        return myCumFreq;
    }

    private int getMaxRouteDuration ( ) {
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 first of all - this is guaranteed.
        cumDistance += theInterface.getScenario().getDistance(theTerminus1Box.getSelectedItem().toString(), theTerminus2Box.getItemAt(0).toString());
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = theTerminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            return cumDistance*2;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += theInterface.getScenario().getDistance(theTerminus2Box.getItemAt(i-1).toString(), theTerminus2Box.getItemAt(i).toString());
        }
        //Return distance * 2.
        return cumDistance*2;
    }
    
    private int getMinVehicles ( ) {
        return (int) Math.ceil((double) getCurrentRouteDuration(Integer.parseInt(theEveryMinuteSpinner.getValue().toString())) / Double.parseDouble(theEveryMinuteSpinner.getValue().toString()) );
    }
    
    public JPanel makeTimetablePanel ( String route, int min, int dateIndex ) {
        
        //Initialise variables.
        theSelectedRouteStr = route;
        theCurrentMin = min;
        
        //Create screen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout( new BoxLayout(routeScreenPanel, BoxLayout.PAGE_AXIS));
        routeScreenPanel.setBackground(Color.WHITE);
        
        //This will be easiest done by copying the route object here to maximise efficiency.
        theSelectedRoute = theInterface.getRoute(route);
            
        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
            
        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        //Here, we have the "Route Selection Screen" label.
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Timetable for Route " + theSelectedRoute.toString(), SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topPanel.add(topLabel, BorderLayout.NORTH);
        //Show valid information.
        JPanel validityPanel = new JPanel(new BorderLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromDateLabel = new JLabel("Valid From: " + theSelectedRoute.getCurrentTimetable(theInterface.getCurrentSimTime()).getValidFromDateInfo());
        validFromDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validFromDateLabel, BorderLayout.NORTH);
        JLabel validToDateLabel = new JLabel("Valid To: " + theSelectedRoute.getCurrentTimetable(theInterface.getCurrentSimTime()).getValidToDateInfo());
        validToDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validToDateLabel, BorderLayout.SOUTH);
        topPanel.add(validityPanel, BorderLayout.SOUTH);
        //Add top panel to topLabel panel and topLabel panel to screenPanel.
        topLabelPanel.add(topPanel, BorderLayout.NORTH);
        routeScreenPanel.add(topLabelPanel);
            
        //Create day of the week label and field.
        JPanel datesPanel = new JPanel();
        datesPanel.setBackground(Color.WHITE);
        JLabel datesLabel = new JLabel("Dates:");
        datesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        System.out.println("Test: " + theSelectedRoute.toString());
        System.out.println(Arrays.toString(theSelectedRoute.getPossibleSchedulesDates(theInterface.getCurrentSimTime())));
        theDatesComboBox = new JComboBox ( theSelectedRoute.getPossibleSchedulesDates(theInterface.getCurrentSimTime()) );
        theDatesComboBox.setSelectedIndex(dateIndex);
        theDatesComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                theControlScreen.redrawManagement(makeTimetablePanel(theSelectedRoute.getRouteNumber(), 0, theDatesComboBox.getSelectedIndex()));
            }
        });
        datesPanel.add(datesLabel); datesPanel.add(theDatesComboBox);
        routeScreenPanel.add(datesPanel);
            
        //Now make the first portion of the screen - this will list the stops in ascending order.
        JPanel outgoingPanel = new JPanel(new BorderLayout());
        outgoingPanel.setBackground(Color.WHITE);
        JLabel outgoingLabel = new JLabel(theSelectedRoute.getStop(Route.OUTWARDSTOPS, 0) + " - " + theSelectedRoute.getStop(Route.OUTWARDSTOPS, theSelectedRoute.getNumStops(Route.OUTWARDSTOPS)-1));
        outgoingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        outgoingPanel.add(outgoingLabel, BorderLayout.NORTH);
            
        //Process data...
        String[] outgoingColumnNames = new String[] { "Stop Name", "", "", "", "", "", "", "", "", "", "" };
        Object[][] outgoingData = new Object[theSelectedRoute.getNumStops(Route.OUTWARDSTOPS)][11];
        Calendar cal = theSelectedRoute.translateDate(theDatesComboBox.getSelectedItem().toString());
        List<Service> services = theSelectedRoute.generateServiceTimetables(cal, theInterface.getScenario(), Route.OUTWARDSTOPS);
        //LinkedList<Service> services = theSelectedRoute.getAllOutgoingServices(theDatesComboBox.getSelectedItem().toString());
        for ( int i = 0; i < theSelectedRoute.getNumStops(Route.OUTWARDSTOPS); i++) {
            outgoingData[i][0] = theSelectedRoute.getStop(Route.OUTWARDSTOPS, i).getStopName();
            for ( int j = 0; j < 10; j++ ) {
                int pos = (min+j);
                System.out.println("This is #" + pos + " of the loop...");
                if ( services.size() <= (min+j) ) {
                    System.out.println("No more services!");
                    outgoingData[i][j+1] = "";
                }
                else if ( services.get(min+j).getStop(theSelectedRoute.getStop(Route.OUTWARDSTOPS, i).getStopName()) == null ) {
                    System.out.println("Blank data!");
                    outgoingData[i][j+1] = "";
                }
                else {
                    outgoingData[i][j+1] = services.get(min+j).getStop(theSelectedRoute.getStop(Route.OUTWARDSTOPS, i).getStopName()).getDisplayStopTime();
                    System.out.println("Adding [" + i + "][" + j + "] At " + services.get(min+j).getStop(theSelectedRoute.getStop(Route.OUTWARDSTOPS, i).getStopName()).getStopName() + " at " + services.get(min+j).getStop(theSelectedRoute.getStop(Route.OUTWARDSTOPS, i).getStopName()).getDisplayStopTime());
                }
            }
        }
        //Display it!
        JTable outgoingTable = new JTable(outgoingData, outgoingColumnNames);
        JScrollPane outgoingScrollPane = new JScrollPane(outgoingTable);
        outgoingTable.setFillsViewportHeight(true);
        outgoingPanel.add(outgoingScrollPane, BorderLayout.CENTER);

        routeScreenPanel.add(outgoingPanel);
            
        //Now make the second portion of the screen - this will list the stops in descending order.
        JPanel ingoingPanel = new JPanel(new BorderLayout());
        ingoingPanel.setBackground(Color.WHITE);
        JLabel ingoingLabel = new JLabel(theSelectedRoute.getStop(Route.RETURNSTOPS, 0) + " - " + theSelectedRoute.getStop(Route.RETURNSTOPS, theSelectedRoute.getNumStops(Route.RETURNSTOPS)-1));
        ingoingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ingoingPanel.add(ingoingLabel, BorderLayout.NORTH);
        routeScreenPanel.add(ingoingPanel);
            
        //Process data...
        String[] ingoingColumnNames = new String[] { "Stop Name", "", "", "", "", "", "", "", "", "", "" };
        Object[][] ingoingData = new Object[theSelectedRoute.getNumStops(Route.RETURNSTOPS)][11];
        List<Service> returnServices = theSelectedRoute.generateServiceTimetables(cal, theInterface.getScenario(), Route.RETURNSTOPS);
        //LinkedList<Service> returnServices = theSelectedRoute.getAllReturnServices(theDatesComboBox.getSelectedItem().toString());
        for ( int i = 0; i < theSelectedRoute.getNumStops(Route.RETURNSTOPS); i++) {
            ingoingData[i][0] = theSelectedRoute.getStop(Route.RETURNSTOPS, i);
            for ( int j = 0; j < 10; j++ ) {
                if ( returnServices.size() <= (min+j) ) {
                    ingoingData[i][j+1] = "";
                }
                else if ( returnServices.get(min+j).getStop(theSelectedRoute.getStop(Route.RETURNSTOPS, i).getStopName()) == null ) {
                    ingoingData[i][j+1] = "";
                }
                else {
                    ingoingData[i][j+1] = returnServices.get(min+j).getStop(theSelectedRoute.getStop(Route.RETURNSTOPS, i).getStopName()).getDisplayStopTime();
                }
            }
        }
        //Display it!
        JTable ingoingTable = new JTable(ingoingData, ingoingColumnNames);
        JScrollPane ingoingScrollPane = new JScrollPane(ingoingTable);
        ingoingTable.setFillsViewportHeight(true);
        ingoingPanel.add(ingoingScrollPane, BorderLayout.CENTER);
            
        //Create two buttons for previous and next.
        JPanel otherServicesButtonPanel = new JPanel();
        otherServicesButtonPanel.setBackground(Color.WHITE);
        JButton previousButton = new JButton("< Previous Services");
        if ( min == 0 ) {
            previousButton.setEnabled(false);
        }
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(makeTimetablePanel(theSelectedRouteStr, theCurrentMin-10, theDatesComboBox.getSelectedIndex()));
            }
        });
        otherServicesButtonPanel.add(previousButton);
        JButton nextButton = new JButton("Next Services >");
        if ( (min+10) > services.size() && (min+10) > returnServices.size() ) {
            nextButton.setEnabled(false);
        }
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(makeTimetablePanel(theSelectedRouteStr, theCurrentMin+10, theDatesComboBox.getSelectedIndex()));
            }
        });
        otherServicesButtonPanel.add(nextButton);
        JButton amendRouteButton = new JButton("Amend Route");
        amendRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Show the actual screen!
                theControlScreen.redrawManagement(makeAddRoutePanel(theSelectedRoute));
                //int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete route " + ((Route) theRoutesModel.get(theRoutesList.getSelectedIndex())).getRouteNumber() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                //if ( confirm == JOptionPane.YES_OPTION ) {
                //    theInterface.deleteRoute(((Route) theRoutesModel.get(theRoutesList.getSelectedIndex())));
                //}
            }
        });
        otherServicesButtonPanel.add(amendRouteButton);
        JButton managementScreenButton = new JButton("Back to Management Screen");
        managementScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(getDisplayPanel());
            }
        });
        otherServicesButtonPanel.add(managementScreenButton);
        routeScreenPanel.add(otherServicesButtonPanel);
            
        overallScreenPanel.add(routeScreenPanel, BorderLayout.CENTER);
            
        //Third part of route panel is list of routes.
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout( new BoxLayout(modelPanel, BoxLayout.PAGE_AXIS));
        modelPanel.setBackground(Color.WHITE);
        JLabel modelLabel = new JLabel("Routes:");
        modelLabel.setFont(new Font("Arial", Font.BOLD, 16));
        modelPanel.add(modelLabel);
            
        theRoutesModel = new DefaultListModel();
        theInterface.sortRoutes();
        for ( int i = 0; i < theInterface.getNumberRoutes(); i++ ) {
            theRoutesModel.addElement(theInterface.getRoute(i).getRouteNumber());
        }
        theRoutesList = new JList(theRoutesModel);
        theRoutesList.setFixedCellWidth(40);
        theRoutesList.setVisibleRowCount(15);
        theRoutesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theRoutesList.setFont(new Font("Arial", Font.PLAIN, 15));
        if ( theRoutesModel.getSize() > 0 ) { theRoutesList.setSelectedValue(route, true); }
        theRoutesList.addListSelectionListener ( new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                theControlScreen.redrawManagement(makeTimetablePanel(theRoutesList.getSelectedValue().toString(), 0, 0));
            }
        });
        JScrollPane routesPane = new JScrollPane(theRoutesList);
        modelPanel.add(routesPane);
            
        overallScreenPanel.add(modelPanel, BorderLayout.EAST);
            
        return overallScreenPanel;
    }

    public JPanel makeEmployDriverPanel ( int num ) {
        //Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel textLabelPanel = new JPanel(new GridBagLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Employ Driver");
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        //topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel);
        driverScreenPanel.add(textLabelPanel);

        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(7,2,2,2));
        gridPanel.setBackground(Color.WHITE);  
        //Driver name.
        JPanel driverNamePanel = new JPanel();
        driverNamePanel.setBackground(Color.WHITE);
        JLabel driverNameLabel = new JLabel("Name:");
        driverNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        driverNamePanel.add(driverNameLabel);
        theDriverNameField = new JTextField("");
        theDriverNameField.setColumns(30);
        theDriverNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        driverNamePanel.add(theDriverNameField);

        gridPanel.add(driverNamePanel);

        //Contracted hours.
        JPanel contractedHoursPanel = new JPanel();
        contractedHoursPanel.setBackground(Color.WHITE);
        JLabel contractedHoursLabel = new JLabel("Contracted Hours:");
        contractedHoursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contractedHoursPanel.add(contractedHoursLabel);
        theContractedHoursSpinner = new JSpinner(new SpinnerNumberModel(35,10,40,5));
        theContractedHoursSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        contractedHoursPanel.add(theContractedHoursSpinner);

        gridPanel.add(contractedHoursPanel);

        //Create label and field for start date and add it to the start panel.
        JPanel startLabelPanel = new JPanel();
        startLabelPanel.setBackground(Color.WHITE);
        JLabel startLabel = new JLabel("Start Date:", SwingConstants.CENTER);
        startLabel.setFont(new Font("Arial", Font.BOLD, 16));
        startLabelPanel.add(startLabel);
        theStartDate = (Calendar) theInterface.getSimulator().getCurrentSimTime().clone();
        theStartDate.add(Calendar.HOUR, 72);
        JLabel startField = new JLabel("" + theInterface.getSimulator().formatDateString(theStartDate));
        startField.setFont(new Font("Arial", Font.ITALIC, 14));
        startLabelPanel.add(startField);
        gridPanel.add(startLabelPanel);
        driverScreenPanel.add(gridPanel);

        //Create return to create game screen button and add it to screen panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton employDriverButton = new JButton("Employ Driver");
        employDriverButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.employDriver(theDriverNameField.getText(), (Integer) theContractedHoursSpinner.getValue(), theStartDate);
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        buttonPanel.add(employDriverButton);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        buttonPanel.add(managementScreenButton);
        driverScreenPanel.add(buttonPanel);

        return driverScreenPanel;
    }

    public JPanel makeViewDriversPanel ( String text ) {
        //Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        JLabel driverComingSoonLabel = new JLabel("Coming Soon...");

        driverScreenPanel.add(driverComingSoonLabel);

        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        driverScreenPanel.add(managementScreenButton);

        return driverScreenPanel;
    }
    
    public JPanel makePurchaseVehiclePanel ( int typePos ) {
        
        //Initialise type position variable.
        theTypePosition = typePos;
        
        //Create screen panel to add things to.
        JPanel vehicleScreenPanel = new JPanel();
        vehicleScreenPanel.setLayout ( new BoxLayout ( vehicleScreenPanel, BoxLayout.PAGE_AXIS ) );
        vehicleScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel textLabelPanel = new JPanel(new GridBagLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Vehicle Showroom");
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        //topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel);
        vehicleScreenPanel.add(textLabelPanel);
        
        //Create vehicle object so that we can pull information from it.
        theVehicle = theInterface.createVehicleObject(theTypePosition);
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        //Previous vehicle type button.
        JPanel previousButtonPanel = new JPanel(new GridBagLayout());
        previousButtonPanel.setBackground(Color.WHITE);
        JButton previousVehicleTypeButton = new JButton("< Previous Vehicle Type");
        if ( theTypePosition == 0 ) { previousVehicleTypeButton.setEnabled(false); }
        previousVehicleTypeButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.makePurchaseVehiclePanel(theTypePosition-1));
            }
        });
        previousButtonPanel.add(previousVehicleTypeButton);
        picturePanel.add(previousButtonPanel, BorderLayout.WEST);
        //Bus Display Picture.
        JPanel busPicture = new JPanel(new GridBagLayout());
        busPicture.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(theVehicle.getImageFileName(),0,0);
        busDisplay.setSize(220,180);
        busDisplay.setBackground(Color.WHITE);
        busPicture.add(busDisplay);
        picturePanel.add(busPicture, BorderLayout.CENTER);
        //Next vehicle type button.
        JPanel nextButtonPanel = new JPanel(new GridBagLayout());
        nextButtonPanel.setBackground(Color.WHITE);
        JButton nextVehicleTypeButton = new JButton("Next Vehicle Type >");
        if ( theTypePosition == (theInterface.getNumVehicleTypes()-1) ) { nextVehicleTypeButton.setEnabled(false); }
        nextVehicleTypeButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.makePurchaseVehiclePanel(theTypePosition+1));
            }
        });
        nextButtonPanel.add(nextVehicleTypeButton);
        picturePanel.add(nextButtonPanel, BorderLayout.EAST);
        vehicleScreenPanel.add(picturePanel);
            
        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(7,2,2,2));
        gridPanel.setBackground(Color.WHITE);      
        //Create label and field for vehicle type and add it to the type panel.
        JPanel typeLabelPanel = new JPanel();
        typeLabelPanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:", SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        typeLabelPanel.add(typeLabel);
        gridPanel.add(typeLabel);
        JLabel typeField = new JLabel(theVehicle.getModel());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for seating capacity and add it to the seating panel.
        JPanel seatingLabelPanel = new JPanel();
        seatingLabelPanel.setBackground(Color.WHITE);
        JLabel seatingLabel = new JLabel("Seating Capacity:", SwingConstants.CENTER);
        seatingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        seatingLabelPanel.add(seatingLabel);
        gridPanel.add(seatingLabel);
        JLabel seatingField = new JLabel("" + theVehicle.getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel("" + theVehicle.getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for delivery date and add it to the delivery panel.
        JPanel deliveryLabelPanel = new JPanel();
        deliveryLabelPanel.setBackground(Color.WHITE);
        JLabel deliveryLabel = new JLabel("Delivery Date:", SwingConstants.CENTER);
        deliveryLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        deliveryLabelPanel.add(deliveryLabel);
        gridPanel.add(deliveryLabel);
        theDeliveryDate = (Calendar) theInterface.getSimulator().getCurrentSimTime().clone();
        theDeliveryDate.add(Calendar.HOUR, 72);
        JLabel deliveryField = new JLabel("" + theInterface.getSimulator().formatDateString(theDeliveryDate));
        deliveryField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(deliveryField);
        //Create label and field for purchase price and add it to the price panel.
        JPanel priceLabelPanel = new JPanel();
        priceLabelPanel.setBackground(Color.WHITE);
        JLabel priceLabel = new JLabel("Purchase Price:", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        priceLabelPanel.add(priceLabel);
        gridPanel.add(priceLabel);
        theFormat = new DecimalFormat("0.00");
        JLabel priceField = new JLabel("€" + theFormat.format(theVehicle.getPurchasePrice()));
        priceField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(priceField);
        //Create label and field for quantity and add it to the quantity panel.
        JPanel quantityLabelPanel = new JPanel(new BorderLayout());
        quantityLabelPanel.setBackground(Color.WHITE);
        JLabel quantityLabel = new JLabel("Quantity:", SwingConstants.CENTER);
        quantityLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        quantityLabelPanel.add(quantityLabel);
        gridPanel.add(quantityLabel);
        theQuantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,40,1));
        theQuantitySpinner.setFont(new Font("Arial", Font.PLAIN, 12));
        theQuantitySpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                double totalPrice = Double.parseDouble(theQuantitySpinner.getValue().toString()) * theVehicle.getPurchasePrice();
                if ( totalPrice > theInterface.getBalance() ) {
                    theTotalPriceField.setText("€" + theFormat.format(totalPrice) + " (Insufficient funds available)");
                    theTotalPriceField.setForeground(Color.RED);
                    thePurchaseVehicleButton.setEnabled(false);
                }
                else {
                    theTotalPriceField.setText("€" + theFormat.format(totalPrice));
                    theTotalPriceField.setForeground(Color.BLACK);
                    thePurchaseVehicleButton.setEnabled(true);
                }
            }
        });
        theQuantitySpinner.setMaximumSize(new Dimension(10,15));
        gridPanel.add(theQuantitySpinner);
        //Create label and field for total price and add it to the total price panel.
        JPanel totalPriceLabelPanel = new JPanel();
        totalPriceLabelPanel.setBackground(Color.WHITE);
        JLabel totalPriceLabel = new JLabel("Total Price:", SwingConstants.CENTER);
        totalPriceLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        totalPriceLabelPanel.add(totalPriceLabel);
        gridPanel.add(totalPriceLabel);
        double totalPrice = Double.parseDouble(theQuantitySpinner.getValue().toString()) * theVehicle.getPurchasePrice();
        theTotalPriceField = new JLabel("€" + theFormat.format(totalPrice));
        theTotalPriceField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(theTotalPriceField);
        
        //Add the grid panel to the screen panel.
        vehicleScreenPanel.add(gridPanel);
        
        //Create bottom button panel.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
                
        //Create purchase vehicle button and add it to screen panel.
        thePurchaseVehicleButton = new JButton("Purchase Vehicle");
        thePurchaseVehicleButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int quantity = Integer.parseInt(theQuantitySpinner.getValue().toString());
                for ( int i = 0; i < quantity; i++ ) {
                    theInterface.purchaseVehicle(theVehicle.getModel(), theDeliveryDate);
                }
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        bottomButtonPanel.add(thePurchaseVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        bottomButtonPanel.add(managementScreenButton);
        
        //Add bottom button panel to the screen panel.
        vehicleScreenPanel.add(bottomButtonPanel);
        
        return vehicleScreenPanel;
    }
    
    public JPanel makeVehicleDepotPanel ( String vehicleId ) {
        
        //Initialise type position variable.
        theVehicleId = vehicleId;
        
        //Create screen panel to add things to.
        JPanel vehicleScreenPanel = new JPanel();
        vehicleScreenPanel.setLayout ( new BoxLayout ( vehicleScreenPanel, BoxLayout.PAGE_AXIS ) );
        vehicleScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel textLabelPanel = new JPanel(new BorderLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Vehicle Depot", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel, BorderLayout.CENTER);
        vehicleScreenPanel.add(textLabelPanel);
        
        //Now create a border layout so that we can have a choice of vehicles on the right hand side.
        JPanel vehicleBorderPanel = new JPanel(new BorderLayout());
        vehicleBorderPanel.setBackground(Color.WHITE);
        
        //Create centre panel and add all those to appear in the centre panel to it!
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout ( new BoxLayout ( centrePanel, BoxLayout.PAGE_AXIS ) );
        centrePanel.setBackground(Color.WHITE);
        
        //Get vehicle data now so that we can used to compile first!
        theVehiclesModel = new DefaultListModel();
        theInterface.sortVehicles();
        for ( int i = 0; i < theInterface.getNumberVehicles(); i++ ) {
            if ( theInterface.getVehicle(i).hasBeenDelivered(theInterface.getCurrentSimTime()) ) {
                theVehiclesModel.addElement(theInterface.getVehicle(i).getRegistrationNumber());
            }
        }
        
        //Create vehicle object so that we can pull information from it.
        theVehicle = theInterface.getVehicle(theVehiclesModel.get(0).toString());
        if ( !theVehicleId.equalsIgnoreCase("") ) {
            theVehicle = theInterface.getVehicle(theVehicleId);
        }
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new GridBagLayout());
        picturePanel.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(theVehicle.getImageFileName(),0,0);
        busDisplay.setSize(220,200);
        busDisplay.setBackground(Color.WHITE);
        picturePanel.add(busDisplay);
        centrePanel.add(picturePanel);
            
        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(7,2,5,5));
        gridPanel.setBackground(Color.WHITE);
        //Create label and field for vehicle id and add it to the id panel.
        JPanel idLabelPanel = new JPanel();
        idLabelPanel.setBackground(Color.WHITE);
        JLabel idLabel = new JLabel("ID:", SwingConstants.CENTER);
        idLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        idLabelPanel.add(idLabel);
        gridPanel.add(idLabel);
        JLabel idField = new JLabel(theVehicle.getRegistrationNumber());
        idField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(idField);
        //Create label and field for vehicle type and add it to the type panel.
        JPanel typeLabelPanel = new JPanel();
        typeLabelPanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:", SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        typeLabelPanel.add(typeLabel);
        gridPanel.add(typeLabel);
        JLabel typeField = new JLabel(theVehicle.getModel());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for age and add it to the age panel.
        JPanel ageLabelPanel = new JPanel();
        ageLabelPanel.setBackground(Color.WHITE);
        JLabel ageLabel = new JLabel("Age:", SwingConstants.CENTER);
        ageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        ageLabelPanel.add(ageLabel);
        gridPanel.add(ageLabel);
        JLabel ageField = new JLabel(theVehicle.getAge(theInterface.getCurrentSimTime()) + " months");
        ageField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(ageField);
        //Create label and field for seating capacity and add it to the seating panel.
        JPanel seatingLabelPanel = new JPanel();
        seatingLabelPanel.setBackground(Color.WHITE);
        JLabel seatingLabel = new JLabel("Seating Capacity:", SwingConstants.CENTER);
        seatingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        seatingLabelPanel.add(seatingLabel);
        gridPanel.add(seatingLabel);
        JLabel seatingField = new JLabel("" + theVehicle.getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel("" + theVehicle.getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for assigned schedule and add it to the schedule panel.
        JPanel assignedLabelPanel = new JPanel();
        assignedLabelPanel.setBackground(Color.WHITE);
        JLabel assignedLabel = new JLabel("Assigned Schedule:", SwingConstants.CENTER);
        assignedLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        assignedLabelPanel.add(assignedLabel);
        gridPanel.add(assignedLabel);
        JLabel assignedField = new JLabel(theVehicle.getAssignedScheduleId());
        assignedField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(assignedField);
        //Create label and field for value and add it to the value panel.
        JPanel valueLabelPanel = new JPanel();
        valueLabelPanel.setBackground(Color.WHITE);
        JLabel valueLabel = new JLabel("Value:", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        valueLabelPanel.add(valueLabel);
        gridPanel.add(valueLabel);
        theFormat = new DecimalFormat("0.00");
        JLabel valueField = new JLabel("€" + theFormat.format(theVehicle.getValue(theInterface.getCurrentSimTime())));
        valueField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(valueField);
        
        //Add the grid panel to the centre panel.
        centrePanel.add(gridPanel);
        
        //Create bottom button panel.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
                
        //Create sell vehicle button and add it to screen panel.
        JButton sellVehicleButton = new JButton("Sell Vehicle");
        sellVehicleButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.sellVehicle(theVehicle);
                theControlScreen.redrawManagement(makeVehicleDepotPanel(""));
            }
        });
        bottomButtonPanel.add(sellVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        bottomButtonPanel.add(managementScreenButton);
        
        //Add bottom button panel to the screen panel.
        centrePanel.add(bottomButtonPanel);
        
        //Add centre panel to border panel.
        vehicleBorderPanel.add(centrePanel, BorderLayout.CENTER);
        
        //Now create the east panel to display the vehicle list.
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBackground(Color.WHITE);
        //Third part of route panel is list of routes.
        JPanel modelPanel = new JPanel();
        modelPanel.setBackground(Color.WHITE);
        theVehiclesList = new JList(theVehiclesModel);
        theVehiclesList.setFixedCellWidth(100);
        theVehiclesList.setVisibleRowCount(25);
        theVehiclesList.setSelectedValue(theVehicle.getRegistrationNumber(), true);
        theVehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theVehiclesList.setFont(new Font("Arial", Font.PLAIN, 15));
        theVehiclesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                String selectedValue = theVehiclesList.getSelectedValue().toString();
                theControlScreen.redrawManagement(ManagePanel.this.makeVehicleDepotPanel(selectedValue)); 
            }
        });
        JScrollPane vehiclesPane = new JScrollPane(theVehiclesList);
        modelPanel.add(vehiclesPane);
        eastPanel.add(modelPanel, BorderLayout.CENTER);
        
        //Add east panel to border panel.
        vehicleBorderPanel.add(eastPanel, BorderLayout.EAST);
        
        //Add vehicleBorderPanel to vehicleScreenPanel.
        vehicleScreenPanel.add(vehicleBorderPanel);
        
        return vehicleScreenPanel;
        
    }
    
    public JPanel makeScenarioPanel ( ) {
        //Create screen panel to add things to.
        JPanel scenarioScreenPanel = new JPanel();
        scenarioScreenPanel.setLayout ( new BoxLayout ( scenarioScreenPanel, BoxLayout.PAGE_AXIS ) );
        scenarioScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Scenario Detail Screen", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        scenarioScreenPanel.add(topLabelPanel, BorderLayout.NORTH);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,10)));

        //Create info panel.
        /*JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);*/

        //Create panel for scenario name field.
        JPanel scenarioNamePanel = new JPanel(new GridLayout(1,2,5,5));
        scenarioNamePanel.setBackground(Color.WHITE);
        //Create label and field for scenario name.
        JLabel scenarioNameLabel = new JLabel("Scenario Name: ", SwingConstants.CENTER);
        scenarioNameLabel.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel scenarioActualNameLabel = new JLabel(theInterface.getScenario().getScenarioName(), SwingConstants.CENTER);
        scenarioActualNameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        scenarioNamePanel.add(scenarioNameLabel);
        scenarioNamePanel.add(scenarioActualNameLabel);

        //Add scenarioNamePanel to info panel.
        scenarioScreenPanel.add(scenarioNamePanel);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,15)));

        //Create panel for target field.
        JPanel targetPanel = new JPanel(new GridLayout(1,2,5,5));
        targetPanel.setBackground(Color.WHITE);
        //Create label and area for senario description.
        JLabel targetLabel = new JLabel("Scenario Targets: ", SwingConstants.CENTER);
        targetLabel.setFont(new Font("Arial", Font.BOLD, 17));
        targetPanel.add(targetLabel);
        JPanel scenarioTargetPanel = new JPanel(new BorderLayout());
        scenarioTargetPanel.setBackground(Color.WHITE);
        JTextArea scenarioTargetArea = new JTextArea(theInterface.getScenario().getTargets());
        scenarioTargetArea.setWrapStyleWord(true);
        scenarioTargetArea.setLineWrap(true);
        scenarioTargetArea.setFont(new Font("Arial", Font.ITALIC, 14));
        scenarioTargetPanel.add(scenarioTargetArea);
        targetPanel.add(scenarioTargetPanel);
        scenarioScreenPanel.add(targetPanel);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,15)));

        //Create panel for player name field.
        JPanel playerNamePanel = new JPanel(new GridLayout(1,2,5,5));
        playerNamePanel.setBackground(Color.WHITE);
        //Create label and field for player name.
        JLabel playerNameLabel = new JLabel("Player Name: ", SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel playerActualNameLabel = new JLabel(theInterface.getScenario().getPlayerName(), SwingConstants.CENTER);
        playerActualNameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        playerNamePanel.add(playerNameLabel);
        playerNamePanel.add(playerActualNameLabel);

        //Add scenarioNamePanel to info panel.
        scenarioScreenPanel.add(playerNamePanel);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,10)));

        //Options button panel with save details and return to management screen buttons.
        JPanel optionsButtonPanel = new JPanel();
        optionsButtonPanel.setBackground(Color.WHITE);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        optionsButtonPanel.add(managementScreenButton);
        
        //Add options button panel to screen panel.
        scenarioScreenPanel.add(optionsButtonPanel, BorderLayout.SOUTH);
        
        return scenarioScreenPanel;
    }
    
    public JPanel makeLocationMapPanel ( ) {
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(theInterface.getScenario().getLocationMapFileName(),0,0);
        busDisplay.setSize(750,380);
        busDisplay.setBackground(Color.WHITE);
        picturePanel.add(busDisplay, BorderLayout.CENTER);
        
        //Options button panel with save details and return to management screen buttons.
        JPanel optionsButtonPanel = new JPanel();
        optionsButtonPanel.setBackground(Color.WHITE);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        optionsButtonPanel.add(managementScreenButton);
        picturePanel.add(optionsButtonPanel, BorderLayout.SOUTH);
        
        return picturePanel;
    }

    public JPanel makeAllocationPanel ( ) {
        //Create allocation screen panel to add things to.
        JPanel allocationScreenPanel = new JPanel();
        allocationScreenPanel.setLayout ( new BoxLayout ( allocationScreenPanel, BoxLayout.PAGE_AXIS ) );
        allocationScreenPanel.setBackground(Color.WHITE);

        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        //Here, we have the "Vehicle Allocation Screen" and scenario name box.
        JPanel topRightPanel = new JPanel(new GridLayout(2,1,5,5));
        topRightPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Vehicle Allocation Screen", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topRightPanel.add(topLabel);
        JPanel dayPanel = new JPanel();
        dayPanel.setBackground(Color.WHITE);
        JLabel dayLabel = new JLabel(theSimulator.getCurrentDisplaySimDay(), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20));
        dayPanel.add(dayLabel);
        topRightPanel.add(dayPanel);
        //Add topRight panel to topLabel panel and topLabel panel to screenPanel.
        topLabelPanel.add(topRightPanel, BorderLayout.CENTER);
        allocationScreenPanel.add(topLabelPanel);

        //Create panel for route information.
        JPanel routeInfoPanel = new JPanel(new FlowLayout());
        routeInfoPanel.setBackground(Color.WHITE);

        //Create panel for route label.
        JPanel routeLabelPanel = new JPanel();
        routeLabelPanel.setBackground(Color.WHITE);

        //Add the route label panel to the screen panel.
        allocationScreenPanel.add(routeLabelPanel);

        //Create list of routes and vehicles created so far with scrollpane.
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(Color.WHITE);
        //First part of label panel is route heading.
        JPanel routesLabelPanel = new JPanel(new GridBagLayout());
        routesLabelPanel.setBackground(Color.WHITE);
        JLabel routesLabel = new JLabel("Route Detail(s):");
        routesLabel.setFont(new Font("Arial", Font.BOLD, 25));
        routesLabelPanel.add(routesLabel);
        labelPanel.add(routesLabelPanel, BorderLayout.WEST);
        //Second part of label panel is route heading.
        JLabel vehiclesLabel = new JLabel("Vehicle(s):", SwingConstants.CENTER);
        vehiclesLabel.setFont(new Font("Arial", Font.BOLD, 25));
        labelPanel.add(vehiclesLabel, BorderLayout.EAST);

        //Add label panel to screen panel.
        allocationScreenPanel.add(labelPanel);

        //Create lists of routes and vehicles with scrollpane.
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        //Third part of route panel is list of routes.
        JPanel routeModelPanel = new JPanel();
        routeModelPanel.setBackground(Color.WHITE);
        theRoutesModel = new DefaultListModel();
        theRoutesList = new JList(theRoutesModel);
        theRoutesList.setFixedCellWidth(270);
        theRoutesList.setFont(new Font("Arial", Font.PLAIN, 15));
        for ( int i = 0; i < theInterface.getNumberRoutes(); i++ ) {
        	System.out.println("Test Allocations: " + theInterface.getRoute(i).getNumRouteSchedules());
            for ( int j = 0; j < theInterface.getRoute(i).getNumRouteSchedules(); j++ ) {
                theRoutesModel.addElement(theInterface.getRoute(i).getRouteSchedule(j).toString());
            }
        }
        theRoutesList.setVisibleRowCount(4);
        theRoutesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane routesPane = new JScrollPane(theRoutesList);
        routeModelPanel.add(routesPane);
        listPanel.add(routeModelPanel, BorderLayout.WEST);
        //Third part of vehicle panel is vehicle list.
        JPanel modelPanel = new JPanel();
        modelPanel.setBackground(Color.WHITE);
        theVehiclesModel = new DefaultListModel();
        for ( int i = 0; i < theInterface.getNumberVehicles(); i++ ) {
            if ( !theInterface.getVehicle(i).hasAssignedSchedule() ) {
                theVehiclesModel.addElement(theInterface.getVehicle(i).getRegistrationNumber() + "(" + theInterface.getVehicle(i).getModel() + ")");
            }
        }
        theVehiclesList = new JList(theVehiclesModel);
        theVehiclesList.setFixedCellWidth(320);
        theVehiclesList.setVisibleRowCount(4);
        theVehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theVehiclesList.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane vehiclesPane = new JScrollPane(theVehiclesList);
        modelPanel.add(vehiclesPane);
        listPanel.add(modelPanel, BorderLayout.EAST);

        //Add list panel to screen panel.
        allocationScreenPanel.add(listPanel);

        //Allocate button panel with allocate and deallocate buttons.
        JPanel allocateButtonPanel = new JPanel();
        allocateButtonPanel.setBackground(Color.WHITE);
        JButton allocateButton = new JButton("Allocate");
        allocateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( theRoutesList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select a route before you can assign a vehicle to it!", "ERROR: No Route Selected", JOptionPane.ERROR_MESSAGE);
                }
                else if ( theVehiclesList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select a vehicle before you can assign a route to it!", "ERROR: No Vehicle Selected", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    theAllocationsModel.addElement(theRoutesList.getSelectedValue() + " & " + theVehiclesList.getSelectedValue().toString().split(" ")[0]);
                    theRoutesModel.removeElement(theRoutesList.getSelectedValue());
                    theVehiclesModel.removeElement(theVehiclesList.getSelectedValue());
                }
            }
        });
        allocateButtonPanel.add(allocateButton);
        JButton deAllocateButton = new JButton("Deallocate");
        deAllocateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( theAllocationsList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select an allocation before you can remove it!", "ERROR: No Allocation Selected", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String text = theAllocationsList.getSelectedValue().toString();
                    theAllocationsModel.removeElement(theAllocationsList.getSelectedValue());
                    String[] textParts = text.split("&");
                    theRoutesModel.addElement(textParts[0].trim());
                    theVehiclesModel.addElement(theInterface.getVehicle(textParts[1].trim()).getRegistrationNumber() + " (" + theInterface.getVehicle(textParts[1].trim()).getModel() + ")");
                    //Remove this from the interface as well.
                    /*String routeNumber = textParts[0].split("/")[0]; int routeDetailPos = -1;
                    for ( int k = 0; k < theInterface.getRoute(routeNumber).getNumRouteSchedules(); k++ ) {
                        if ( theInterface.getRoute(routeNumber).getRouteSchedule(k).toString().equalsIgnoreCase(textParts[0].trim()) ) {
                            routeDetailPos = k;
                        }
                    }*/
                    //Find vehicle object position.
                    int vehiclePos = -1;
                    for ( int j = 0; j < theInterface.getNumberVehicles(); j++ ) {
                        if ( theInterface.getVehicle(j).toString().equalsIgnoreCase(textParts[1].trim())) {
                            vehiclePos = j;
                        }
                    }
                    theInterface.getVehicle(vehiclePos).setAssignedSchedule(null);
                }
            }
        });
        allocateButtonPanel.add(deAllocateButton);

        //Add allocate button panel to screen panel.
        allocationScreenPanel.add(allocateButtonPanel);

        //Allocation label panel is "Allocation(s)".
        JPanel allocationsPanel = new JPanel();
        allocationsPanel.setBackground(Color.WHITE);
        theAllocationsLabel = new JLabel("Allocation(s):", SwingConstants.CENTER);
        theAllocationsLabel.setFont(new Font("Arial", Font.BOLD, 25));
        allocationsPanel.add(theAllocationsLabel, BorderLayout.EAST);

        //Add allocation label panel to screen panel.
        allocationScreenPanel.add(allocationsPanel);

        //Finally the allocation list.
        JPanel allocationListPanel = new JPanel();
        allocationListPanel.setBackground(Color.WHITE);
        theAllocationsModel = new DefaultListModel();
        ArrayList<String> allocations;
        String currentDate = theSimulator.getCurrentSimTime().get(Calendar.YEAR) + "-" + theSimulator.getCurrentSimTime().get(Calendar.MONTH) + "-" + theSimulator.getCurrentSimTime().get(Calendar.DATE);
        allocations = theInterface.getTodayAllocations(currentDate);
        for ( int i = 0; i < allocations.size(); i++ ) {
            theAllocationsModel.addElement(allocations.get(i).toString());
            //For each allocation, remove route and vehicle from list.
            String[] allocateSplit = allocations.get(i).toString().split("&");
            theRoutesModel.removeElement(allocateSplit[0].trim());
            theVehiclesModel.removeElement(allocateSplit[1].substring(1,allocateSplit[1].length()));
        }
        /*LinkedList<Vehicle> vehicles = theInterface.getVehicles();
        Collections.sort(vehicles, new SortedVehicles());
        for ( int i = 0; i < vehicles.size(); i++ ) {
            theVehiclesModel.addElement(vehicles.get(i).toString());
        }*/
        theAllocationsList = new JList(theAllocationsModel);
        theAllocationsList.setFixedCellWidth(250);
        theAllocationsList.setVisibleRowCount(4);
        theAllocationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theAllocationsList.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane allocationsPane = new JScrollPane(theAllocationsList);
        allocationListPanel.add(allocationsPane);

        //Add allocation list panel to screen panel.
        allocationScreenPanel.add(allocationListPanel);

        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);

        //Create save allocations button and add it to screen panel.
        JButton saveAllocationsButton = new JButton("Save Allocations");
        saveAllocationsButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int result = JOptionPane.YES_OPTION;
                if ( !theRoutesModel.isEmpty() ) {
                    result = JOptionPane.showConfirmDialog(null, "There are some route schedules which do not have vehicles. If you do not assign vehicles to these routes, your passenger satisfaction will decrease! Do you want to continue?", "WARNING: Some routes are unallocated!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                }
                if ( result == JOptionPane.YES_OPTION ) {
                    //Save vehicle positions - then set other ones to null!
                    ArrayList<Integer> vehiclePoses = new ArrayList<Integer>();
                    //Do requested allocations.
                    for ( int i = 0; i < theAllocationsModel.size(); i++ ) {
                        //Separate route and vehicle data.
                        String[] allocationSplit = theAllocationsModel.get(i).toString().split("&");
                        //Store route detail object.
                        String routeNumber = allocationSplit[0].split("/")[0]; int routeDetailPos = -1;
                        for ( int k = 0; k < theInterface.getRoute(routeNumber).getNumRouteSchedules(); k++ ) {
                            if ( theInterface.getRoute(routeNumber).getRouteSchedule(k).toString().equalsIgnoreCase(allocationSplit[0].trim()) ) {
                                routeDetailPos = k;
                            }
                        }
                        //Find vehicle object position.
                        int vehiclePos = -1;
                        for ( int j = 0; j < theInterface.getNumberVehicles(); j++ ) {
                            if ( theInterface.getVehicle(j).getRegistrationNumber().equalsIgnoreCase(allocationSplit[1].trim())) {
                                vehiclePos = j;
                                vehiclePoses.add(vehiclePos);
                            }
                        }
                        //Now assign route detail to vehicle and vice versa.
                        System.out.println("Assigning " + theInterface.getRoute(routeNumber).getRouteSchedule(routeDetailPos).toString() + " to vehicle " + theInterface.getVehicle(vehiclePos).toString());
                        theInterface.getVehicle(vehiclePos).setAssignedSchedule(theInterface.getRoute(routeNumber).getRouteSchedule(routeDetailPos));
                        theInterface.getRoute(routeNumber).addAllocation(theInterface.getRoute(routeNumber).getRouteSchedule(routeDetailPos).toString(), theInterface.getVehicle(vehiclePos));
                    }
                    for ( int i = 0; i < theInterface.getNumberVehicles(); i++ ) {
                        if ( !vehiclePoses.contains(i) ) {
                            theInterface.getVehicle(i).setAssignedSchedule(null);
                        }
                    }
                    //Now return to previous screen.
                    theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
                }
            }
        });
        bottomButtonPanel.add(saveAllocationsButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Now return to previous screen.
                theControlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        bottomButtonPanel.add(previousScreenButton);

        //Add bottom button panel to the screen panel.
        allocationScreenPanel.add(bottomButtonPanel);

        //Return allocationScreenPanel.
        return allocationScreenPanel;
    }
    
}
