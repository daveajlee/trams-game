package de.davelee.trams.gui;

import javax.swing.*;
import javax.swing.event.*;

import de.davelee.trams.data.JourneyPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.text.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import de.davelee.trams.main.UserInterface;
import de.davelee.trams.services.JourneyPatternService;
import de.davelee.trams.services.JourneyService;
import de.davelee.trams.services.RouteService;
import de.davelee.trams.services.ScenarioService;
import de.davelee.trams.services.StopService;
import de.davelee.trams.services.TimetableService;
import de.davelee.trams.services.VehicleService;
import de.davelee.trams.util.DateFormats;

/**
 * This class displays the management options for the control screen in TraMS.
 * @author Dave Lee
 */
public class ManagePanel {
	
	private static final Logger logger = LoggerFactory.getLogger(ManagePanel.class);

    private JLabel scenarioLabel;
    private JButton viewScenarioButton;
    private JLabel routesLabel;
    private JButton addRouteButton;
    private JButton routeTimetableButton;
    private JLabel vehiclesLabel;
    private JButton purchaseVehicleScreenButton;
    private JButton viewDepotButton;
    private JLabel driversLabel;
    private JButton employDriversButton;
    private JButton viewDriversButton;
    private JLabel allocationsLabel;
    private JButton changeAllocationButton;
    
    private UserInterface userInterface;
    private ControlScreen controlScreen;
    
    /** THESE VARIABLES ARE NEEDED FOR ACTION LISTENERS ETC. **/
    private JTextField routeNumberField;
    private JComboBox[] stopBoxes;
    private DefaultListModel timetableModel;
    private JList timetableList;
    private JButton createTimetableButton;
    private JButton modifyTimetableButton;
    private JButton deleteTimetableButton;
    private JButton createRouteButton;
    private long selectedRouteId;
    private String[] selectedOutwardStops;
    private String[] selectedReturnStops;
    private String selectedTimetableName;

    private JTextField driverNameField;
    private JSpinner contractedHoursSpinner;
    private Calendar startDate;

    private JTextField timetableNameField;
    private DefaultComboBoxModel validFromDayModel;
    private int fromStartDay;
    private JComboBox validFromMonthBox;
    private DefaultComboBoxModel validToDayModel;
    private int toStartDay;
    private JComboBox validToMonthBox;
    private JComboBox terminus1Box;
    private ArrayList<String> stopNames;
    private JComboBox terminus2Box;
    private JSpinner fromHourSpinner;
    private JSpinner fromMinuteSpinner;
    private JSpinner toHourSpinner;
    private JSpinner toMinuteSpinner;
    private JSpinner everyMinuteSpinner;
    private SpinnerNumberModel everyMinuteModel;
    private JLabel minVehicleLabel;
    
    private DefaultListModel routesModel;
    private JList routesList;

    private DefaultListModel allocationsModel;
    private JList allocationsList;
    
    private DefaultListModel journeyPatternModel;
    private JList journeyPatternList;
    private long selectedJourneyPatternId;
    private JButton createJourneyPatternButton;
    private JButton modifyJourneyPatternButton;
    private JButton deleteJourneyPatternButton;
    
    private JButton createJPButton;
    private JCheckBox[] daysBox;
    private JTextField journeyPatternNameField;
    
    private int typePosition;
    private String vehicleType;
    
    private DefaultListModel vehiclesModel;
    private JList vehiclesList;
    private long vehicleId;
    private Calendar deliveryDate;
    private JSpinner quantitySpinner;
    private JLabel totalPriceField;
    private DecimalFormat format;
    private JButton purchaseVehicleButton;
    
    private String selectedRouteStr;
    private int currentMin;
    private JComboBox datesComboBox;
    
    private VehicleService vehicleService;
    private StopService stopService;
    private JourneyService journeyService;
    private TimetableService timetableService;
    private RouteService routeService;
    private ScenarioService scenarioService;
    private JourneyPatternService journeyPatternService;
    
    public ManagePanel ( UserInterface ui, ControlScreen cs ) {
        userInterface = ui;
        controlScreen = cs;
        vehicleService = new VehicleService();
        stopService = new StopService();
        journeyService = new JourneyService();
        timetableService = new TimetableService();
        routeService = new RouteService();
        scenarioService = new ScenarioService();
        journeyPatternService = new JourneyPatternService();
    }
    
    public JPanel getDisplayPanel ( ) {
        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
        //Create a label for an information picture and an information area.
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.WHITE);
        ImageDisplay infoDisplay = null;
        if ( userInterface.getNumberRoutes() == 0 || userInterface.getNumberVehicles() == 0 || userInterface.getAllocations().size() == 0 ) {
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
        if ( userInterface.getNumberRoutes() == 0 ) {
            informationArea.setText("WARNING: No routes have been devised yet. Click 'Create Route' to define a route.");
        }
        else if ( userInterface.getNumberVehicles() == 0 ) {
            informationArea.setText("WARNING: You can't run routes without vehicles. Click 'Purchase Vehicle' to buy a vehicle");
        }
        else if ( userInterface.getAllocations().size() == 0 ) {
            informationArea.setText("WARNING: To successfully run journeys, you must assign vehicles to route schedules. Click 'Allocations' to match vehicles to route schedules");
        }
        else {
            logger.debug("The allocations size was " + userInterface.getAllocations().size() + " which is " + userInterface.getAllocations().toString());
            informationArea.setText(userInterface.getRandomTipMessage());
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
        scenarioLabel = new JLabel("Scenario:", SwingConstants.CENTER);
        scenarioLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scenarioLabelPanel.add(scenarioLabel);
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
        viewScenarioButton = new JButton("View Information");
        viewScenarioButton.addActionListener( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeScenarioPanel());
            }
        });
        viewScenarioButtonPanel.add(viewScenarioButton);
        scenarioButtonPanel.add(viewScenarioButtonPanel);
        scenarioButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel locationMapButtonPanel = new JPanel(new GridBagLayout());
        locationMapButtonPanel.setBackground(Color.WHITE);
        JButton locationMapButton = new JButton("Location Map");
        locationMapButton.addActionListener( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeLocationMapPanel());
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
        routesLabel = new JLabel("Routes:", SwingConstants.CENTER);
        routesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        routeLabelPanel.add(routesLabel);
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
        addRouteButton = new JButton("Create Route");
        addRouteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeAddRoutePanel(-1));
            }
        });
        createRouteButtonPanel.add(addRouteButton);
        routeButtonPanel.add(createRouteButtonPanel);
        routeButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel timetableButtonPanel = new JPanel(new GridBagLayout());
        timetableButtonPanel.setBackground(Color.WHITE);
        routeTimetableButton = new JButton("View Route Info");
        if ( userInterface.getNumberRoutes() == 0 ) { routeTimetableButton.setEnabled(false); }
        routeTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeTimetablePanel(routeService.getRouteById(userInterface.getRoute(0)).getRouteNumber(), 0, 0));
            }
        });
        timetableButtonPanel.add(routeTimetableButton);
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
        vehiclesLabel = new JLabel("Vehicles:", SwingConstants.CENTER);
        vehiclesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        vehicleLabelPanel.add(vehiclesLabel);
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
        purchaseVehicleScreenButton = new JButton("Purchase");
        purchaseVehicleScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makePurchaseVehiclePanel(0));
            }
        });
        purchaseVehicleButtonPanel.add(purchaseVehicleScreenButton);
        vehicleButtonPanel.add(purchaseVehicleButtonPanel);
        vehicleButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDepotButtonPanel = new JPanel(new GridBagLayout());
        viewDepotButtonPanel.setBackground(Color.WHITE);
        viewDepotButton = new JButton("View Depot");
        if ( !userInterface.hasSomeVehiclesBeenDelivered() ) { viewDepotButton.setEnabled(false); }
        viewDepotButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeVehicleDepotPanel(""));
            }
        });
        viewDepotButtonPanel.add(viewDepotButton);
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
        driversLabel = new JLabel("Drivers:", SwingConstants.CENTER);
        driversLabel.setFont(new Font("Arial", Font.BOLD, 18));
        driverLabelPanel.add(driversLabel);
        driverPanel.add(driverLabelPanel, BorderLayout.NORTH);
        //Create description panel.
        JPanel driverDescriptionPanel = new JPanel(new BorderLayout());
        driverDescriptionPanel.setBackground(Color.WHITE);
        JTextArea driverDescriptionArea = new JTextArea("Employ drivers, view current employees and sack drivers");
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
        employDriversButton = new JButton("Employ");
        employDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeEmployDriverPanel(0));
            }
        });
        employDriverButtonPanel.add(employDriversButton);
        driverButtonPanel.add(employDriverButtonPanel);
        driverButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDriversButtonPanel = new JPanel(new GridBagLayout());
        viewDriversButtonPanel.setBackground(Color.WHITE);
        viewDriversButton = new JButton("View Drivers");
        if ( !userInterface.hasSomeDriversBeenEmployed() ) { viewDriversButton.setEnabled(false); }
        viewDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeViewDriversPanel(""));
            }
        });
        viewDriversButtonPanel.add(viewDriversButton);
        driverButtonPanel.add(viewDriversButtonPanel);
        driverPanel.add(driverButtonPanel, BorderLayout.SOUTH);
        gridPanel.add(driverPanel);
        //Allocation panel.
        JPanel allocationPanel = new JPanel();
        allocationPanel.setBackground(Color.WHITE);
        allocationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        allocationPanel.setLayout ( new BoxLayout ( allocationPanel, BoxLayout.PAGE_AXIS ) );
        JPanel allocationLabelPanel = new JPanel();
        allocationLabelPanel.setBackground(Color.WHITE);
        allocationsLabel = new JLabel("Allocations:", SwingConstants.CENTER);
        allocationsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        allocationLabelPanel.add(allocationsLabel);
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
        changeAllocationButton = new JButton("Change");
        changeAllocationButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeAllocationPanel());
            }
        });
        allocationButtonPanel.add(changeAllocationButton);
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
    
    public JPanel makeAddRoutePanel( long amendRouteId ) {
        
        //Create routeScreen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout ( new BoxLayout ( routeScreenPanel, BoxLayout.PAGE_AXIS ) );
        routeScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Route", SwingConstants.CENTER);
        if ( amendRouteId != -1 ) { topLabel.setText("Amend Route"); }
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
        routeNumberField = new JTextField(10);
        routeNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        if ( amendRouteId != -1 ) { routeNumberField.setText(routeService.getRouteById(amendRouteId).getRouteNumber()); }
        routeNumberField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                enableCreateButtons();
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        routeNumberPanel.add(routeNumberField);
        
        //Add the routeNumber panel to the screen panel.
        routeScreenPanel.add(routeNumberPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Now create stops - a 2 x 3 grid layout.
        JPanel stopGridPanel = new JPanel(new GridLayout(2,3,5,5));
        stopGridPanel.setBackground(Color.WHITE);
        //Create the boxes and labels as appropriate.
        //Create the stops.
        JPanel[] stopPanels = new JPanel[5];
        stopBoxes = new JComboBox[5];
        for ( int i = 0; i < stopPanels.length; i++ ) {
            stopPanels[i] = new JPanel(new GridBagLayout());
            stopPanels[i].setBackground(Color.WHITE);
            JLabel stopLabel = new JLabel("Stop " + (i+1) + ":");
            stopLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            stopPanels[i].add(stopLabel);
            stopBoxes[i] = new JComboBox(scenarioService.getStopNames(userInterface.createScenarioObject(userInterface.getScenarioName())));
            stopBoxes[i].setFont(new Font("Arial", Font.PLAIN, 14));
            stopBoxes[i].setSelectedIndex(stopBoxes[i].getItemCount()-1);
            if ( amendRouteId != -1 ) { 
                if ( routeService.getRouteById(amendRouteId).getStops().size() > i ) {
                    int findIndexPos = findIndex(routeService.getRouteById(amendRouteId).getStops().get(i).getStopName(), i);
                    if ( findIndexPos != -1 ) { stopBoxes[i].setSelectedIndex(findIndexPos); }
                }
            }
            if ( i < 2 ) {
                stopBoxes[i].addActionListener( new ActionListener() {
                    public void actionPerformed ( ActionEvent e ) {
                        enableCreateButtons();
                    }
                });
            }
            stopPanels[i].add(stopBoxes[i]);
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
        timetableModel = new DefaultListModel();
        //Now get all the timetables which we have at the moment.
        try {
            Iterator<String> timetableKeys = routeService.getRouteById(selectedRouteId).getTimetableNames();
            while ( timetableKeys.hasNext() ) {
                String timetableName = timetableKeys.next();
                timetableModel.addElement(routeService.getRouteById(selectedRouteId).getTimetable(timetableName).getName());
            }
        } 
        catch (NullPointerException npe) { }
        timetableList = new JList(timetableModel);
        if ( timetableModel.getSize() != 0 ) {
            timetableList.setSelectedIndex(0);
        }
        timetableList.setVisibleRowCount(3);
        timetableList.setFixedCellWidth(450);
        timetableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timetablePane = new JScrollPane(timetableList);
        centreTimetableListPanel.add(timetablePane);
        timetableListPanel.add(centreTimetableListPanel, BorderLayout.CENTER);
        //Now the three create, modify and delete button.
        JPanel timetableButtonPanel = new JPanel();
        timetableButtonPanel.setBackground(Color.WHITE);
        createTimetableButton = new JButton("Create");
        createTimetableButton.setEnabled(false);
        createTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //First of all, set the selected route.
                if ( selectedRouteId == -1 && timetableModel.getSize() == 0 ) {
                    selectedOutwardStops = new String[stopBoxes.length];
                    selectedReturnStops = new String[stopBoxes.length];
                    for ( int i = 0; i < stopBoxes.length; i++ ) {
                        if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        	selectedOutwardStops[i] = stopBoxes[i].getSelectedItem().toString();
                        }
                    }
                    for ( int i = (stopBoxes.length-1); i >=0; i-- ) {
                        if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        	selectedReturnStops[i] = stopBoxes[i].getSelectedItem().toString();
                        }
                    }
                }
                //Show the actual screen!
                controlScreen.redrawManagement(makeCreateTimetablePanel(""));
            }
        });
        timetableButtonPanel.add(createTimetableButton);
        modifyTimetableButton = new JButton("Modify");
        if ( timetableModel.getSize() == 0 ) { modifyTimetableButton.setEnabled(false); }
        modifyTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e ) {
                controlScreen.redrawManagement(makeCreateTimetablePanel(timetableList.getSelectedValue().toString()));
            }
        });
        timetableButtonPanel.add(modifyTimetableButton);
        deleteTimetableButton = new JButton("Delete");
        if ( timetableModel.getSize() == 0 ) { deleteTimetableButton.setEnabled(false); }
        deleteTimetableButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                routeService.getRouteById(selectedRouteId).deleteTimetable(timetableList.getSelectedValue().toString());
                timetableModel.removeElement(timetableList.getSelectedValue());
                if ( timetableModel.getSize() == 0 ) {
                    deleteTimetableButton.setEnabled(false);
                    modifyTimetableButton.setEnabled(false);
                }
                else {
                    timetableList.setSelectedIndex(0);
                }
            }
        });
        timetableButtonPanel.add(deleteTimetableButton);
        timetableListPanel.add(timetableButtonPanel, BorderLayout.SOUTH);
        
        //Add the timetableList panel to the screen panel.
        routeScreenPanel.add(timetableListPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        createRouteButton = new JButton("Create Route");
        createRouteButton.setEnabled(false);
        createRouteButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
               userInterface.generateRouteSchedules(selectedRouteId);
               
               userInterface.addNewRoute(routeNumberField.getText(), selectedOutwardStops);
               //Now return to previous screen.
               controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        bottomButtonPanel.add(createRouteButton);
        
        //Create new route button and add it to screen panel.
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
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
        
        //Create timetableScreen panel to add things to.
        JPanel timetableScreenPanel = new JPanel();
        timetableScreenPanel.setLayout ( new BoxLayout ( timetableScreenPanel, BoxLayout.PAGE_AXIS ) );
        timetableScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Timetable", SwingConstants.CENTER);
        if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableName) != null ) {
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
        timetableNameField = new JTextField(20);
        if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableName) != null ) { 
        	timetableNameField.setText(routeService.getRouteById(selectedRouteId).getTimetable(timetableName).getName()); 
        }
        timetableNameField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                if ( !timetableNameField.getText().equalsIgnoreCase("") ) {
                    createJourneyPatternButton.setEnabled(true);
                }
                else {
                    createJourneyPatternButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        timetableNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        timetableNamePanel.add(timetableNameField);
        timetableScreenPanel.add(timetableNamePanel);
                
        //Create panel for validity first of all.
        JPanel validityPanel = new JPanel(new GridBagLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromLabel = new JLabel("Valid From: ", SwingConstants.CENTER);
        validFromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validFromLabel);
        //Get the calendar object with current time.
        Calendar currTime = (Calendar) userInterface.getCurrentSimTime().clone();
        currTime.add(Calendar.HOUR, 0); //TODO: Change this to 48!!!!
        //Valid From Day.
        fromStartDay = currTime.get(Calendar.DAY_OF_MONTH);
        validFromDayModel = new DefaultComboBoxModel();
        for ( int i = currTime.get(Calendar.DAY_OF_MONTH); i <= currTime.getActualMaximum(Calendar.MONTH); i++ ) {
            validFromDayModel.addElement(i);
        }
        JComboBox validFromDayBox = new JComboBox(validFromDayModel);
        if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableName) != null ) {
            validFromDayBox.setSelectedItem(routeService.getRouteById(selectedRouteId).getTimetable(timetableName).
            		getValidFromDate().get(Calendar.DAY_OF_MONTH));
        }
        validFromDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validFromDayBox);
        //Valid From Month.
        validFromMonthBox = new JComboBox();
        for ( int i = 0; i < 4; i++ ) {
            validFromMonthBox.addItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(currTime));
            currTime.add(Calendar.MONTH, 1);
        }
        if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableName) != null ) {
            validFromMonthBox.setSelectedItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(routeService.getRouteById(selectedRouteId).
            		getTimetable(timetableName).getValidFromDate()));
        }
        validFromMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validFromMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int month = validFromMonthBox.getSelectedIndex();
                if ( validFromMonthBox.getSelectedIndex() == 0 ) {
                    validFromDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = fromStartDay; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validFromDayModel.addElement(i);
                    }
                }
                else {
                    validFromDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = 1; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validFromDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(validFromMonthBox);
        //Valid to!!!
        JLabel validToLabel = new JLabel("Valid To: ", SwingConstants.CENTER);
        validToLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validToLabel);
        //Get the calendar object with current time.
        Calendar myCurrTime = (Calendar) userInterface.getCurrentSimTime().clone();
        myCurrTime.add(Calendar.HOUR, 72);
        //Valid To Day.
        toStartDay = myCurrTime.get(Calendar.DAY_OF_MONTH);
        validToDayModel = new DefaultComboBoxModel();
        for ( int i = myCurrTime.get(Calendar.DAY_OF_MONTH); i <= myCurrTime.getActualMaximum(Calendar.MONTH); i++ ) {
            validToDayModel.addElement(i);
        }
        JComboBox validToDayBox = new JComboBox(validToDayModel);
        if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableName) != null ) {
            validToDayBox.setSelectedItem(routeService.getRouteById(selectedRouteId).getTimetable(timetableName).
            		getValidToDate().get(Calendar.DAY_OF_MONTH));
        }
        validToDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validToDayBox);
        //Valid To Month.
        validToMonthBox = new JComboBox();
        for ( int i = 0; i < 25; i++ ) {
            validToMonthBox.addItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(myCurrTime));
            myCurrTime.add(Calendar.MONTH, 1);
        }
        if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableName) != null ) {
            validToMonthBox.setSelectedItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(routeService.getRouteById(selectedRouteId).
            		getTimetable(timetableName).getValidToDate()));
        }
        validToMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validToMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int month = validToMonthBox.getSelectedIndex();
                if ( validToMonthBox.getSelectedIndex() == 0 ) {
                    validToDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = toStartDay; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validToDayModel.addElement(i);
                    }
                }
                else {
                    validToDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = 1; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validToDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(validToMonthBox);
       
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
        
        //Create the journeyPattern list panel and three buttons.
        JPanel journeyPatternListPanel = new JPanel(new BorderLayout());
        journeyPatternListPanel.setBackground(Color.WHITE);
        //Here is the actual service pattern list.
        JPanel centreJourneyPatternListPanel = new JPanel(new GridBagLayout());
        centreJourneyPatternListPanel.setBackground(Color.WHITE);
        journeyPatternModel = new DefaultListModel();
        //Now get all the journey pattern which we have at the moment.
        try {
            List<JourneyPattern> journeyPatterns = journeyPatternService.getJourneyPatterns( routeService.getRouteById(selectedRouteId).
                    getTimetable(timetableNameField.getText()).getId());
            for ( JourneyPattern journeyPattern : journeyPatterns ) {
                journeyPatternModel.addElement(journeyPattern.getName());
            }
        } 
        catch (NullPointerException npe) { }
        journeyPatternList = new JList(journeyPatternModel);
        if ( journeyPatternModel.getSize() > 0 ) { journeyPatternList.setSelectedIndex(0); } 
        journeyPatternList.setVisibleRowCount(3);
        journeyPatternList.setFixedCellWidth(450);
        journeyPatternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timetablePane = new JScrollPane(journeyPatternList);
        centreJourneyPatternListPanel.add(timetablePane);
        journeyPatternListPanel.add(centreJourneyPatternListPanel, BorderLayout.CENTER);
        //Now the three create, modify and delete button.
        JPanel journeyPatternButtonPanel = new JPanel();
        journeyPatternButtonPanel.setBackground(Color.WHITE);
        createJourneyPatternButton = new JButton("Create");
        if (timetableNameField.getText().equalsIgnoreCase("")) { createJourneyPatternButton.setEnabled(false); }
        createJourneyPatternButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Create relevant calendar object.
                if ( routeService.getRouteById(selectedRouteId).getTimetable(timetableNameField.getText()) == null) {
                    int vfYear = Integer.parseInt(validFromMonthBox.getSelectedItem().toString().split(" ")[1]);
                    int vfMonth = validFromMonthBox.getSelectedIndex();
                    int vfDay = Integer.parseInt(validFromDayModel.getSelectedItem().toString());
                    GregorianCalendar validFrom = new GregorianCalendar(vfYear, vfMonth, vfDay);
                    int vtYear = Integer.parseInt(validToMonthBox.getSelectedItem().toString().split(" ")[1]);
                    int vtMonth = validToMonthBox.getSelectedIndex();
                    int vtDay = Integer.parseInt(validToDayModel.getSelectedItem().toString());
                    GregorianCalendar validTo = new GregorianCalendar(vtYear, vtMonth, vtDay);
                    //Save this timetable with valid dates first.
                    routeService.getRouteById(selectedRouteId).addTimetable(timetableNameField.getText(), validFrom, validTo);
                    //logger.debug("Adding timetable with name " + theTimetableNameField.getText() + " to route " + theSelectedRoute.getRouteNumber());
                }
                //Process the stops.
                ArrayList<String> stops = new ArrayList<String>();
                for ( int i = 0; i < stopBoxes.length; i++ ) {
                    if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        stops.add(stopBoxes[i].getSelectedItem().toString());
                    }
                }
                //Show the actual screen!
                controlScreen.redrawManagement(makeJourneyPatternPanel(stops, timetableNameField.getText(), -1));
            }
        });
        journeyPatternButtonPanel.add(createJourneyPatternButton);
        modifyJourneyPatternButton = new JButton("Modify");
        //TODO: reimplement modify!
        /*modifyJourneyPatternButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                long journeyPatternId = routeService.getRouteById(selectedRouteId).
                		getTimetable(timetableNameField.getText()).getJourneyPattern(journeyPatternList.
                				getSelectedValue().toString()).getId();
                //Process the stops.
                ArrayList<String> stops = new ArrayList<String>();
                for ( int i = 0; i < stopBoxes.length; i++ ) {
                    if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        stops.add(stopBoxes[i].getSelectedItem().toString());
                    }
                }
                controlScreen.redrawManagement(ManagePanel.this.makeJourneyPatternPanel(stops, timetableNameField.getText(), journeyPatternId));
            }
        });*/
        if ( journeyPatternModel.getSize() == 0 ) { modifyJourneyPatternButton.setEnabled(false); }
        journeyPatternButtonPanel.add(modifyJourneyPatternButton);
        deleteJourneyPatternButton = new JButton("Delete");
        //TODO: reimplement delete.
        /*deleteJourneyPatternButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                routeService.getRouteById(selectedRouteId).getTimetable(timetableNameField.getText()).
                	deleteJourneyPattern(journeyPatternList.getSelectedValue().toString());
                journeyPatternModel.removeElement(journeyPatternList.getSelectedValue());
                if ( journeyPatternModel.getSize() == 0 ) {
                    deleteJourneyPatternButton.setEnabled(false);
                    modifyJourneyPatternButton.setEnabled(false);
                }
                else {
                    journeyPatternList.setSelectedIndex(0);
                }
            }
        });*/
        if ( journeyPatternModel.getSize() == 0 ) { deleteJourneyPatternButton.setEnabled(false); }
        journeyPatternButtonPanel.add(deleteJourneyPatternButton);
        journeyPatternListPanel.add(journeyPatternButtonPanel, BorderLayout.SOUTH);
        //Add the journeyPatternList panel to the screen panel.
        timetableScreenPanel.add(journeyPatternListPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        JButton createTimetableButton = new JButton("Create Timetable");
        if(journeyPatternModel.getSize()==0) { createTimetableButton.setEnabled(false); }
        createTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Timetable is already saved so we just go back to the original screen.
                controlScreen.redrawManagement(ManagePanel.this.makeAddRoutePanel(selectedRouteId));
            }
        });
        bottomButtonPanel.add(createTimetableButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Cancel addition.
                controlScreen.redrawManagement(ManagePanel.this.makeAddRoutePanel(selectedRouteId));
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
    
    public JPanel makeJourneyPatternPanel ( ArrayList<String> myStopNames, String timetableName, long journeyPatternId ) {
        //Initialise selected timetable.
        selectedTimetableName = timetableName;
        //Initialise journey pattern.
        selectedJourneyPatternId = journeyPatternId;
        
        //Create journeyPatternScreen panel to add things to.
        JPanel journeyPatternScreenPanel = new JPanel();
        journeyPatternScreenPanel.setLayout ( new BoxLayout ( journeyPatternScreenPanel, BoxLayout.PAGE_AXIS ) );
        journeyPatternScreenPanel.setBackground(Color.WHITE);
        
        //Initialise stop names.
        stopNames = myStopNames;
        
        //Create label in middle of screen in a middleLabelPanel added to screenPanel.
        JPanel middleLabelPanel = new JPanel(new BorderLayout());
        middleLabelPanel.setBackground(Color.WHITE);
        JLabel middleLabel = new JLabel("Create Service Pattern", SwingConstants.CENTER);
        if ( selectedJourneyPatternId != -1 ) { middleLabel.setText("Modify Service Pattern"); }
        middleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        middleLabel.setVerticalAlignment(JLabel.CENTER);
        middleLabelPanel.add(middleLabel, BorderLayout.CENTER);
        journeyPatternScreenPanel.add(middleLabelPanel);
        
        //Create journey pattern name panel.
        JPanel journeyPatternNamePanel = new JPanel(new GridBagLayout());
        journeyPatternNamePanel.setBackground(Color.WHITE);
        JLabel journeyPatternNameLabel = new JLabel("Name: ");
        journeyPatternNameLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        journeyPatternNamePanel.add(journeyPatternNameLabel);
        journeyPatternNameField = new JTextField(20);
        if ( selectedJourneyPatternId != -1 ) { journeyPatternNameField.setText(journeyPatternService.
        		getJourneyPatternById(selectedJourneyPatternId).getName()); }
        journeyPatternNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        journeyPatternNameField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                if ( !journeyPatternNameField.getText().equalsIgnoreCase("") ) {
                    for ( int i = 0; i < daysBox.length; i++ ) {
                        if ( daysBox[i].isSelected() ) {
                            createJPButton.setEnabled(true);
                            return;
                        }
                    }
                    createJPButton.setEnabled(false);
                }
                else {
                    createJPButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        journeyPatternNamePanel.add(journeyPatternNameField);
        journeyPatternScreenPanel.add(journeyPatternNamePanel);
        
        //Create day of week panel with 7 tick boxes.
        JPanel dayOfWeekPanel = new JPanel(new GridBagLayout());
        dayOfWeekPanel.setBackground(Color.WHITE);
        daysBox = new JCheckBox[7];
        String[] dayStr = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        for ( int i = 0; i < daysBox.length; i++ ) {
            daysBox[i] = new JCheckBox(dayStr[i]);
            daysBox[i].setFont(new Font("Arial", Font.PLAIN, 14));
            daysBox[i].addActionListener(new ActionListener() {
                public void actionPerformed ( ActionEvent e ) {
                    if ( !journeyPatternNameField.getText().equalsIgnoreCase("") ) {
                        for ( int i = 0; i < daysBox.length; i++ ) {
                            if ( daysBox[i].isSelected() ) {
                                createJPButton.setEnabled(true);
                                return;
                            }
                        }
                        createJPButton.setEnabled(false);
                    }
                    else {
                        createJPButton.setEnabled(false);
                    }
                }
            });
            int addPos = (i+1);
            if ( selectedJourneyPatternId != -1 ) { if ( journeyPatternService.getJourneyPatternById(selectedJourneyPatternId)
            		.getDaysOfOperation().contains("" + addPos) ) { daysBox[i].setSelected(true); } }
            dayOfWeekPanel.add(daysBox[i]);
        }
        journeyPatternScreenPanel.add(dayOfWeekPanel);
        
        //Create panel with between stops.
        JPanel betweenStopsPanel = new JPanel(new GridBagLayout());
        betweenStopsPanel.setBackground(Color.WHITE);
        //Between label.
        JLabel betweenLabel = new JLabel("Between:");
        betweenLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(betweenLabel);
        //Terminus 1 Combo box.
        terminus1Box = new JComboBox();
        for ( int i = 0; i < stopNames.size()-1; i++ ) {
            terminus1Box.addItem(stopNames.get(i));
        }
        terminus1Box.setSelectedIndex(0);
        if ( selectedJourneyPatternId != -1 ) { terminus1Box.setSelectedItem(journeyPatternService
        		.getJourneyPatternById(selectedJourneyPatternId).getReturnTerminus()); }
        terminus1Box.setFont(new Font("Arial", Font.PLAIN, 14));
        terminus1Box.addItemListener(new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                //Update terminus 2 box!!!
                terminus2Box.removeAllItems();
                for ( int i = (stopNames.indexOf(terminus1Box.getSelectedItem().toString()))+1; i < stopNames.size(); i++ ) {
                    terminus2Box.addItem(stopNames.get(i));
                }
                //Update spinner!
                everyMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())));
            }
        });
        betweenStopsPanel.add(terminus1Box);
        //And label.
        JLabel andLabel = new JLabel("  and  ");
        andLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(andLabel);
        //Terminus 2 Combo box.
        terminus2Box = new JComboBox();
        for ( int i = 1; i < stopNames.size(); i++ ) {
            terminus2Box.addItem(stopNames.get(i));
        }
        terminus2Box.setSelectedIndex(terminus2Box.getItemCount() - 1);
        if ( selectedJourneyPatternId != -1) { terminus2Box.setSelectedItem(journeyPatternService
        		.getJourneyPatternById(selectedJourneyPatternId).getOutgoingTerminus()); }
        terminus2Box.setFont(new Font("Arial", Font.PLAIN, 14));
        terminus2Box.addItemListener( new ItemListener () {
            public void itemStateChanged ( ItemEvent e ) {
                //Update spinner!
                if ( terminus2Box.getItemCount() != 0 ) {
                    everyMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())));
                }
            }
        });
        betweenStopsPanel.add(terminus2Box);
        //Add betweenStopsPanel.
        journeyPatternScreenPanel.add(betweenStopsPanel);
        
        //Create panel with between times and every x frequency - this is bascially full of spinners.
        JPanel timesPanel = new JPanel(new GridBagLayout());
        timesPanel.setBackground(Color.WHITE);
        //From + times.
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(fromLabel);
        fromHourSpinner = new JSpinner(new SpinnerNumberModel(6,0,23,1));
        if ( selectedJourneyPatternId != -1 ) { fromHourSpinner.setValue(journeyPatternService
        		.getJourneyPatternById(selectedJourneyPatternId).getStartTime().get(Calendar.HOUR_OF_DAY)); }
        fromHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(fromHourSpinner);
        fromMinuteSpinner = new JSpinner(new SpinnerNumberModel(0,0,59,1));
        if ( selectedJourneyPatternId != -1 ) { fromMinuteSpinner.setValue(journeyPatternService
        		.getJourneyPatternById(selectedJourneyPatternId).getStartTime().get(Calendar.MINUTE)); }
        fromMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(fromMinuteSpinner);
        //To + times.
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(toLabel);
        toHourSpinner = new JSpinner(new SpinnerNumberModel(18,0,23,1));
        if ( selectedJourneyPatternId != -1 ) { toHourSpinner.setValue(journeyPatternService
        		.getJourneyPatternById(selectedJourneyPatternId).getEndTime().get(Calendar.HOUR_OF_DAY)); }
        toHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(toHourSpinner);
        toMinuteSpinner = new JSpinner(new SpinnerNumberModel(30,0,59,1));
        if ( selectedJourneyPatternId != -1 ) { toMinuteSpinner.setValue(journeyPatternService
        		.getJourneyPatternById(selectedJourneyPatternId).getEndTime().get(Calendar.MINUTE)); }
        toMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(toMinuteSpinner);
        //Every.
        JLabel everyLabel = new JLabel("Every: ");
        everyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(everyLabel);
        int min = 10;
        if ( min > getCurrentRouteDuration(1) ) { min = getCurrentRouteDuration(1); }
        everyMinuteModel = new SpinnerNumberModel(min,1,getMaxRouteDuration(),1);
        everyMinuteSpinner = new JSpinner(everyMinuteModel);
        //Initialise minVehicles label here but then actually place it later.
        minVehicleLabel = new JLabel("NOTE: " + getMinVehicles() + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!" );
        everyMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        everyMinuteSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                minVehicleLabel.setText("NOTE: " + getMinVehicles() + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!");
            }
        });
        timesPanel.add(everyMinuteSpinner);
        JLabel minutesLabel = new JLabel("minutes");
        minutesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(minutesLabel);
        
        journeyPatternScreenPanel.add(timesPanel);
        
        //Create panel to state vehicles required to maintain frequency.
        JPanel minVehiclePanel = new JPanel(new GridBagLayout());
        minVehiclePanel.setBackground(Color.WHITE);
        
        minVehicleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        minVehiclePanel.add(minVehicleLabel);
        journeyPatternScreenPanel.add(minVehiclePanel);
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new journey pattern button and add it to screen panel.
        createJPButton = new JButton("Create Journey Pattern");
        if ( journeyPatternId != -1 ) { createJPButton.setText("Modify Journey Pattern"); }
        else { createJPButton.setEnabled(false); }
        createJPButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Create a linked list of days selected.
            	//TODO: Improve - use Calendar instead of for loop!
                List<Integer> operatingDays = new ArrayList<Integer>();
                for ( int i = 0; i < daysBox.length; i++ ) {
                    if ( daysBox[i].isSelected() ) {
                        operatingDays.add(i+1);
                    }
                }
                //Create time from.
                GregorianCalendar timeFrom = new GregorianCalendar(2009,7,3,Integer.parseInt(fromHourSpinner.getValue().toString()), Integer.parseInt(fromMinuteSpinner.getValue().toString()));
                //Create time to.
                GregorianCalendar timeTo = new GregorianCalendar(2009,7,3,Integer.parseInt(toHourSpinner.getValue().toString()), Integer.parseInt(toMinuteSpinner.getValue().toString()));
                //Create + add journey pattern.
                if ( selectedJourneyPatternId != -1 ) {
                    //TODO: reimplement edit method.
                	//theSelectedRoute.getTimetable(theSelectedTimetableName).getJourneyPattern(theSelectedJourneyPattern.getName()).editJourneyPattern(theJourneyPatternNameField.getText(), operatingDays, theTerminus1Box.getSelectedItem().toString(), theTerminus2Box.getSelectedItem().toString(), timeFrom, timeTo, Integer.parseInt(theEveryMinuteSpinner.getValue().toString()), getCurrentRouteDuration(Integer.parseInt(theEveryMinuteSpinner.getValue().toString())));
                }
                else {
                    logger.debug("I am calling add method with timetable name " + selectedTimetableName + "!");
                    journeyPatternService.createJourneyPattern(journeyPatternNameField.getText(), operatingDays,
                            terminus1Box.getSelectedItem().toString(), terminus2Box.getSelectedItem().toString(), timeFrom,
                            timeTo, Integer.parseInt(everyMinuteSpinner.getValue().toString()),
                            getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())),
                            routeService.getRouteById(selectedRouteId).getTimetable(selectedTimetableName).getId());
                }
                //Now return to the timetable screen.
                controlScreen.redrawManagement(ManagePanel.this.makeCreateTimetablePanel(selectedTimetableName));
            }
        });
        bottomButtonPanel.add(createJPButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Return to the timetable screen.
                controlScreen.redrawManagement(ManagePanel.this.makeCreateTimetablePanel(selectedTimetableName));
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Add bottom button panel to the screen panel.
        journeyPatternScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return journeyPatternScreenPanel;
        
    }
    
    public int findIndex ( String text, int pos ) {
        for ( int i = 0; i < stopBoxes[pos].getItemCount(); i++ ) {
            if ( stopBoxes[pos].getItemAt(i).toString().equalsIgnoreCase(text) ) {
                return i;
            }
        }
        return -1;
    }
    
    public void enableCreateButtons ( ) {
        //To enable create timetable button we need the selected item in stop1Box and stop2Box to not be -.
        if ( !stopBoxes[0].getSelectedItem().toString().equalsIgnoreCase("-") && !stopBoxes[1].getSelectedItem().toString().equalsIgnoreCase("-") && !routeNumberField.getText().equalsIgnoreCase("") ) {
            createTimetableButton.setEnabled(true);
            //In addition, the timetable model must not be 0 to create a route.
            if ( timetableModel.getSize() > 0 ) {
                createRouteButton.setEnabled(true);
            }
        }
    }
    
    private int getCurrentRouteDuration ( int frequency ) {
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 first of all - this is guaranteed.
        cumDistance += routeService.getDistance(userInterface.getScenarioName(), terminus1Box.getSelectedItem().toString(), terminus2Box.getItemAt(0).toString());
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = terminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            int myDistance = (cumDistance*2);
            int myCumFreq = (frequency*2);
            logger.debug("Distance was " + myDistance);
            while ( myCumFreq < myDistance ) {
                myCumFreq += (frequency*2);
            }
            logger.debug("Actual distance is " + myCumFreq);
            return myCumFreq;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += routeService.getDistance(userInterface.getScenarioName(), terminus2Box.getItemAt(i-1).toString(), terminus2Box.getItemAt(i).toString());
        }
        //Return distance * 2.
        int myDistance = (cumDistance*2);
        int myCumFreq = (frequency*2);
        logger.debug("Distance was " + myDistance);
        while ( myCumFreq < myDistance ) {
            myCumFreq += (frequency*2);
        }
        logger.debug("Actual distance is " + myCumFreq);
        return myCumFreq;
    }

    private int getMaxRouteDuration ( ) {
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 first of all - this is guaranteed.
        cumDistance += routeService.getDistance(userInterface.getScenarioName(), terminus1Box.getSelectedItem().toString(), terminus2Box.getItemAt(0).toString());
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = terminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            return cumDistance*2;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += routeService.getDistance(userInterface.getScenarioName(), terminus2Box.getItemAt(i-1).toString(), terminus2Box.getItemAt(i).toString());
        }
        //Return distance * 2.
        return cumDistance*2;
    }
    
    private int getMinVehicles ( ) {
        return (int) Math.ceil((double) getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())) / Double.parseDouble(everyMinuteSpinner.getValue().toString()) );
    }
    
    public JPanel makeTimetablePanel ( String route, int min, int dateIndex ) {
        
        //Initialise variables.
        selectedRouteStr = route;
        currentMin = min;
        
        //Create screen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout( new BoxLayout(routeScreenPanel, BoxLayout.PAGE_AXIS));
        routeScreenPanel.setBackground(Color.WHITE);
     
        selectedRouteId = userInterface.getRoute(route);
            
        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
            
        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        //Here, we have the "Route Selection Screen" label.
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Timetable for Route " + routeService.getRouteById(selectedRouteId).getRouteNumber(), SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topPanel.add(topLabel, BorderLayout.NORTH);
        //Show valid information.
        JPanel validityPanel = new JPanel(new BorderLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromDateLabel = new JLabel("Valid From: " + timetableService.getDateInfo(routeService.getCurrentTimetable(selectedRouteId, userInterface.getCurrentSimTime()).getValidFromDate()));
        validFromDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validFromDateLabel, BorderLayout.NORTH);
        JLabel validToDateLabel = new JLabel("Valid To: " + timetableService.getDateInfo(routeService.getCurrentTimetable(selectedRouteId, userInterface.getCurrentSimTime()).getValidToDate()));
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
        datesComboBox = new JComboBox ( routeService.getPossibleSchedulesDates(selectedRouteId, userInterface.getCurrentSimTime()) );
        datesComboBox.setSelectedIndex(dateIndex);
        datesComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                controlScreen.redrawManagement(makeTimetablePanel(routeService.getRouteById(selectedRouteId).getRouteNumber(), 0, datesComboBox.getSelectedIndex()));
            }
        });
        datesPanel.add(datesLabel); datesPanel.add(datesComboBox);
        routeScreenPanel.add(datesPanel);
            
        //Now make the first portion of the screen - this will list the stops in ascending order.
        JPanel outgoingPanel = new JPanel(new BorderLayout());
        outgoingPanel.setBackground(Color.WHITE);
        JLabel outgoingLabel = new JLabel(routeService.getRouteById(selectedRouteId).getStops().get(0) + " - " + 
        		routeService.getRouteById(selectedRouteId).getStops().get(routeService.getRouteById(selectedRouteId).getStops().size()-1));
        outgoingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        outgoingPanel.add(outgoingLabel, BorderLayout.NORTH);
            
        //Process data...
        String[] outgoingColumnNames = new String[] { "Stop Name", "", "", "", "", "", "", "", "", "", "" };
        Object[][] outgoingData = new Object[routeService.getRouteById(selectedRouteId).getStops().size()][11];
        Calendar cal = Calendar.getInstance();
        try {
        	cal.setTime(DateFormats.FULL_FORMAT.getFormat().parse(datesComboBox.getSelectedItem().toString()));
        } catch ( ParseException parseEx ) {
        	//TODO: exception handling.
        }
        long[] journeyIds = userInterface.generateOutwardJourneyTimetables(selectedRouteId, cal);
        //LinkedList<Service> services = theSelectedRoute.getAllOutgoingServices(theDatesComboBox.getSelectedItem().toString());
        for ( int i = 0; i < routeService.getRouteById(selectedRouteId).getStops().size(); i++) {
            outgoingData[i][0] = routeService.getRouteById(selectedRouteId).getStops().get(i).getStopName();
            for ( int j = 0; j < 10; j++ ) {
                int pos = (min+j);
                logger.debug("This is #" + pos + " of the loop...");
                if ( journeyIds.length <= (min+j) ) {
                    logger.debug("No more services!");
                    outgoingData[i][j+1] = "";
                }
                else if ( journeyService.getStop(journeyIds[min+j], routeService.getRouteById(selectedRouteId).getStops().get(i).getStopName()) == null ) {
                    logger.debug("Blank data!");
                    outgoingData[i][j+1] = "";
                }
                else {
                    outgoingData[i][j+1] = stopService.getDisplayStopTime(journeyService.getStop(journeyIds[min+j], routeService.getRouteById(selectedRouteId).getStops().get(i).getStopName()).getStopTime());
                }
            }
        }
        //Display it!
        JTable outgoingTable = new JTable(outgoingData, outgoingColumnNames);
        JScrollPane outgoingScrollPane = new JScrollPane(outgoingTable);
        outgoingTable.setFillsViewportHeight(true);
        outgoingPanel.add(outgoingScrollPane, BorderLayout.CENTER);

        routeScreenPanel.add(outgoingPanel);
            
        //Create two buttons for previous and next.
        JPanel otherServicesButtonPanel = new JPanel();
        otherServicesButtonPanel.setBackground(Color.WHITE);
        JButton previousButton = new JButton("< Previous Services");
        if ( min == 0 ) {
            previousButton.setEnabled(false);
        }
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(makeTimetablePanel(selectedRouteStr, currentMin-10, datesComboBox.getSelectedIndex()));
            }
        });
        otherServicesButtonPanel.add(previousButton);
        JButton nextButton = new JButton("Next Services >");
        if ( (min+10) > journeyIds.length ) {
            nextButton.setEnabled(false);
        }
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(makeTimetablePanel(selectedRouteStr, currentMin+10, datesComboBox.getSelectedIndex()));
            }
        });
        otherServicesButtonPanel.add(nextButton);
        JButton amendRouteButton = new JButton("Amend Route");
        amendRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Show the actual screen!
                controlScreen.redrawManagement(makeAddRoutePanel(selectedRouteId));
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
                controlScreen.redrawManagement(getDisplayPanel());
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
            
        routesModel = new DefaultListModel();
        userInterface.sortRoutes();
        for ( int i = 0; i < userInterface.getNumberRoutes(); i++ ) {
            routesModel.addElement(routeService.getRouteById(userInterface.getRoute(i)).getRouteNumber());
        }
        routesList = new JList(routesModel);
        routesList.setFixedCellWidth(40);
        routesList.setVisibleRowCount(15);
        routesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        routesList.setFont(new Font("Arial", Font.PLAIN, 15));
        if ( routesModel.getSize() > 0 ) { routesList.setSelectedValue(route, true); }
        routesList.addListSelectionListener ( new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                controlScreen.redrawManagement(makeTimetablePanel(routesList.getSelectedValue().toString(), 0, 0));
            }
        });
        JScrollPane routesPane = new JScrollPane(routesList);
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
        driverNameField = new JTextField("");
        driverNameField.setColumns(30);
        driverNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        driverNamePanel.add(driverNameField);

        gridPanel.add(driverNamePanel);

        //Contracted hours.
        JPanel contractedHoursPanel = new JPanel();
        contractedHoursPanel.setBackground(Color.WHITE);
        JLabel contractedHoursLabel = new JLabel("Contracted Hours:");
        contractedHoursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contractedHoursPanel.add(contractedHoursLabel);
        contractedHoursSpinner = new JSpinner(new SpinnerNumberModel(35,10,40,5));
        contractedHoursSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        contractedHoursPanel.add(contractedHoursSpinner);

        gridPanel.add(contractedHoursPanel);

        //Create label and field for start date and add it to the start panel.
        JPanel startLabelPanel = new JPanel();
        startLabelPanel.setBackground(Color.WHITE);
        JLabel startLabel = new JLabel("Start Date:", SwingConstants.CENTER);
        startLabel.setFont(new Font("Arial", Font.BOLD, 16));
        startLabelPanel.add(startLabel);
        startDate = (Calendar) userInterface.getCurrentSimTime().clone();
        startDate.add(Calendar.HOUR, 72);
        JLabel startField = new JLabel("" + userInterface.formatDateString(startDate, DateFormats.FULL_FORMAT));
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
                userInterface.employDriver(driverNameField.getText(), (Integer) contractedHoursSpinner.getValue(), startDate);
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        buttonPanel.add(employDriverButton);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
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
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        driverScreenPanel.add(managementScreenButton);

        return driverScreenPanel;
    }
    
    public JPanel makePurchaseVehiclePanel ( int typePos ) {
        
        //Initialise type position variable.
        typePosition = typePos;
        
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
        vehicleId = userInterface.createVehicleObject(typePosition);
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        //Previous vehicle type button.
        JPanel previousButtonPanel = new JPanel(new GridBagLayout());
        previousButtonPanel.setBackground(Color.WHITE);
        JButton previousVehicleTypeButton = new JButton("< Previous Vehicle Type");
        if ( typePosition == 0 ) { previousVehicleTypeButton.setEnabled(false); }
        previousVehicleTypeButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(ManagePanel.this.makePurchaseVehiclePanel(typePosition-1));
            }
        });
        previousButtonPanel.add(previousVehicleTypeButton);
        picturePanel.add(previousButtonPanel, BorderLayout.WEST);
        //Bus Display Picture.
        JPanel busPicture = new JPanel(new GridBagLayout());
        busPicture.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(vehicleService.getVehicleById(vehicleId).getImagePath(),0,0);
        busDisplay.setSize(220,180);
        busDisplay.setBackground(Color.WHITE);
        busPicture.add(busDisplay);
        picturePanel.add(busPicture, BorderLayout.CENTER);
        //Next vehicle type button.
        JPanel nextButtonPanel = new JPanel(new GridBagLayout());
        nextButtonPanel.setBackground(Color.WHITE);
        JButton nextVehicleTypeButton = new JButton("Next Vehicle Type >");
        if ( typePosition == (userInterface.getNumVehicleTypes()-1) ) { nextVehicleTypeButton.setEnabled(false); }
        nextVehicleTypeButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(ManagePanel.this.makePurchaseVehiclePanel(typePosition+1));
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
        JLabel typeField = new JLabel(vehicleService.getVehicleById(vehicleId).getModel());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for seating capacity and add it to the seating panel.
        JPanel seatingLabelPanel = new JPanel();
        seatingLabelPanel.setBackground(Color.WHITE);
        JLabel seatingLabel = new JLabel("Seating Capacity:", SwingConstants.CENTER);
        seatingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        seatingLabelPanel.add(seatingLabel);
        gridPanel.add(seatingLabel);
        JLabel seatingField = new JLabel("" + vehicleService.getVehicleById(vehicleId).getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel("" + vehicleService.getVehicleById(vehicleId).getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for delivery date and add it to the delivery panel.
        JPanel deliveryLabelPanel = new JPanel();
        deliveryLabelPanel.setBackground(Color.WHITE);
        JLabel deliveryLabel = new JLabel("Delivery Date:", SwingConstants.CENTER);
        deliveryLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        deliveryLabelPanel.add(deliveryLabel);
        gridPanel.add(deliveryLabel);
        deliveryDate = (Calendar) userInterface.getCurrentSimTime().clone();
        deliveryDate.add(Calendar.HOUR, 72);
        JLabel deliveryField = new JLabel("" + userInterface.formatDateString(deliveryDate, DateFormats.FULL_FORMAT));
        deliveryField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(deliveryField);
        //Create label and field for purchase price and add it to the price panel.
        JPanel priceLabelPanel = new JPanel();
        priceLabelPanel.setBackground(Color.WHITE);
        JLabel priceLabel = new JLabel("Purchase Price:", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        priceLabelPanel.add(priceLabel);
        gridPanel.add(priceLabel);
        format = new DecimalFormat("0.00");
        JLabel priceField = new JLabel("£" + format.format(vehicleService.getVehicleById(vehicleId).getPurchasePrice()));
        priceField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(priceField);
        //Create label and field for quantity and add it to the quantity panel.
        JPanel quantityLabelPanel = new JPanel(new BorderLayout());
        quantityLabelPanel.setBackground(Color.WHITE);
        JLabel quantityLabel = new JLabel("Quantity:", SwingConstants.CENTER);
        quantityLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        quantityLabelPanel.add(quantityLabel);
        gridPanel.add(quantityLabel);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,40,1));
        quantitySpinner.setFont(new Font("Arial", Font.PLAIN, 12));
        quantitySpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                double totalPrice = Double.parseDouble(quantitySpinner.getValue().toString()) * vehicleService.getVehicleById(vehicleId).getPurchasePrice();
                if ( totalPrice > userInterface.getBalance() ) {
                    totalPriceField.setText("£" + format.format(totalPrice) + " (Insufficient funds available)");
                    totalPriceField.setForeground(Color.RED);
                    purchaseVehicleButton.setEnabled(false);
                }
                else {
                    totalPriceField.setText("£" + format.format(totalPrice));
                    totalPriceField.setForeground(Color.BLACK);
                    purchaseVehicleButton.setEnabled(true);
                }
            }
        });
        quantitySpinner.setMaximumSize(new Dimension(10,15));
        gridPanel.add(quantitySpinner);
        //Create label and field for total price and add it to the total price panel.
        JPanel totalPriceLabelPanel = new JPanel();
        totalPriceLabelPanel.setBackground(Color.WHITE);
        JLabel totalPriceLabel = new JLabel("Total Price:", SwingConstants.CENTER);
        totalPriceLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        totalPriceLabelPanel.add(totalPriceLabel);
        gridPanel.add(totalPriceLabel);
        double totalPrice = Double.parseDouble(quantitySpinner.getValue().toString()) * vehicleService.getVehicleById(vehicleId).getPurchasePrice();
        totalPriceField = new JLabel("£" + format.format(totalPrice));
        totalPriceField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(totalPriceField);
        
        //Add the grid panel to the screen panel.
        vehicleScreenPanel.add(gridPanel);
        
        //Create bottom button panel.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
                
        //Create purchase vehicle button and add it to screen panel.
        purchaseVehicleButton = new JButton("Purchase Vehicle");
        purchaseVehicleButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int quantity = Integer.parseInt(quantitySpinner.getValue().toString());
                for ( int i = 0; i < quantity; i++ ) {
                    userInterface.purchaseVehicle(vehicleService.getVehicleById(vehicleId).getModel(), deliveryDate);
                }
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        bottomButtonPanel.add(purchaseVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
            }
        });
        bottomButtonPanel.add(managementScreenButton);
        
        //Add bottom button panel to the screen panel.
        vehicleScreenPanel.add(bottomButtonPanel);
        
        return vehicleScreenPanel;
    }
    
    public JPanel makeVehicleDepotPanel ( final String vehicleType2 ) {
        
        //Initialise type position variable.
        vehicleType = vehicleType2;
        
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
        vehiclesModel = new DefaultListModel();
        userInterface.sortVehicles();
        for ( int i = 0; i < userInterface.getNumberVehicles(); i++ ) {
            if ( vehicleService.hasBeenDelivered(vehicleService.getVehicleById(userInterface.getVehicle(i)).getDeliveryDate(), userInterface.getCurrentSimTime()) ) {
                vehiclesModel.addElement(vehicleService.getVehicleById(userInterface.getVehicle(i)).getRegistrationNumber());
            }
        }
        
        //Create vehicle object so that we can pull information from it.
        vehicleId = userInterface.getVehicle(vehiclesModel.get(0).toString());
        if ( !vehicleType.equalsIgnoreCase("") ) {
            vehicleId = userInterface.getVehicle(vehicleType);
        }
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new GridBagLayout());
        picturePanel.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(vehicleService.getVehicleById(vehicleId).getImagePath(),0,0);
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
        JLabel idField = new JLabel(vehicleService.getVehicleById(vehicleId).getRegistrationNumber());
        idField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(idField);
        //Create label and field for vehicle type and add it to the type panel.
        JPanel typeLabelPanel = new JPanel();
        typeLabelPanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:", SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        typeLabelPanel.add(typeLabel);
        gridPanel.add(typeLabel);
        JLabel typeField = new JLabel(vehicleService.getVehicleById(vehicleId).getModel());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for age and add it to the age panel.
        JPanel ageLabelPanel = new JPanel();
        ageLabelPanel.setBackground(Color.WHITE);
        JLabel ageLabel = new JLabel("Age:", SwingConstants.CENTER);
        ageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        ageLabelPanel.add(ageLabel);
        gridPanel.add(ageLabel);
        JLabel ageField = new JLabel(vehicleService.getAge(vehicleService.getVehicleById(vehicleId).getDeliveryDate(),
        		userInterface.getCurrentSimTime()) + " months");
        ageField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(ageField);
        //Create label and field for seating capacity and add it to the seating panel.
        JPanel seatingLabelPanel = new JPanel();
        seatingLabelPanel.setBackground(Color.WHITE);
        JLabel seatingLabel = new JLabel("Seating Capacity:", SwingConstants.CENTER);
        seatingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        seatingLabelPanel.add(seatingLabel);
        gridPanel.add(seatingLabel);
        JLabel seatingField = new JLabel("" + vehicleService.getVehicleById(vehicleId).getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel("" + vehicleService.getVehicleById(vehicleId).getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for assigned schedule and add it to the schedule panel.
        JPanel assignedLabelPanel = new JPanel();
        assignedLabelPanel.setBackground(Color.WHITE);
        JLabel assignedLabel = new JLabel("Assigned Schedule:", SwingConstants.CENTER);
        assignedLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        assignedLabelPanel.add(assignedLabel);
        gridPanel.add(assignedLabel);
        JLabel assignedField = new JLabel("" + vehicleService.getVehicleById(vehicleId).getRouteScheduleId());
        assignedField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(assignedField);
        //Create label and field for value and add it to the value panel.
        JPanel valueLabelPanel = new JPanel();
        valueLabelPanel.setBackground(Color.WHITE);
        JLabel valueLabel = new JLabel("Value:", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        valueLabelPanel.add(valueLabel);
        gridPanel.add(valueLabel);
        format = new DecimalFormat("0.00");
        JLabel valueField = new JLabel("£" + format.format(vehicleService.getValue(
        		vehicleService.getVehicleById(vehicleId).getPurchasePrice(), vehicleService.getVehicleById(vehicleId).getDepreciationFactor(), 
        		vehicleService.getVehicleById(vehicleId).getDeliveryDate(), userInterface.getCurrentSimTime())));
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
                userInterface.sellVehicle(vehicleId);
                controlScreen.redrawManagement(makeVehicleDepotPanel(""));
            }
        });
        bottomButtonPanel.add(sellVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
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
        vehiclesList = new JList(vehiclesModel);
        vehiclesList.setFixedCellWidth(100);
        vehiclesList.setVisibleRowCount(25);
        vehiclesList.setSelectedValue(vehicleService.getVehicleById(vehicleId).getRegistrationNumber(), true);
        vehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehiclesList.setFont(new Font("Arial", Font.PLAIN, 15));
        vehiclesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                String selectedValue = vehiclesList.getSelectedValue().toString();
                controlScreen.redrawManagement(ManagePanel.this.makeVehicleDepotPanel(selectedValue)); 
            }
        });
        JScrollPane vehiclesPane = new JScrollPane(vehiclesList);
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
        JLabel scenarioActualNameLabel = new JLabel(userInterface.getScenarioName(), SwingConstants.CENTER);
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
        JTextArea scenarioTargetArea = new JTextArea(userInterface.getScenarioTargets());
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
        JLabel playerActualNameLabel = new JLabel(userInterface.getPlayerName(), SwingConstants.CENTER);
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
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
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
        ImageDisplay busDisplay = new ImageDisplay(userInterface.getScenarioLocationMap(),0,0);
        busDisplay.setSize(750,380);
        busDisplay.setBackground(Color.WHITE);
        picturePanel.add(busDisplay, BorderLayout.CENTER);
        
        //Options button panel with save details and return to management screen buttons.
        JPanel optionsButtonPanel = new JPanel();
        optionsButtonPanel.setBackground(Color.WHITE);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel()); 
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
        JLabel dayLabel = new JLabel(userInterface.formatDateString(userInterface.getCurrentSimTime(), DateFormats.FULL_FORMAT), SwingConstants.CENTER);
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
        routesModel = new DefaultListModel();
        routesList = new JList(routesModel);
        routesList.setFixedCellWidth(270);
        routesList.setFont(new Font("Arial", Font.PLAIN, 15));
        for ( int i = 0; i < userInterface.getNumberRoutes(); i++ ) {
            for ( int j = 0; j < routeService.getRouteById(userInterface.getRoute(i)).getRouteSchedules().size(); j++ ) {
                routesModel.addElement(routeService.getRouteById(userInterface.getRoute(i)).getRouteSchedules().get(j).toString());
            }
        }
        routesList.setVisibleRowCount(4);
        routesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane routesPane = new JScrollPane(routesList);
        routeModelPanel.add(routesPane);
        listPanel.add(routeModelPanel, BorderLayout.WEST);
        //Third part of vehicle panel is vehicle list.
        JPanel modelPanel = new JPanel();
        modelPanel.setBackground(Color.WHITE);
        vehiclesModel = new DefaultListModel();
        for ( int i = 0; i < userInterface.getNumberVehicles(); i++ ) {
            if ( vehicleService.getVehicleById(userInterface.getVehicle(i)).getRouteScheduleId() != 0 ) {
                vehiclesModel.addElement(vehicleService.getVehicleById(userInterface.getVehicle(i))
                		.getRegistrationNumber() + "(" + vehicleService.getVehicleById(userInterface.getVehicle(i)).getModel() + ")");
            }
        }
        vehiclesList = new JList(vehiclesModel);
        vehiclesList.setFixedCellWidth(320);
        vehiclesList.setVisibleRowCount(4);
        vehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehiclesList.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane vehiclesPane = new JScrollPane(vehiclesList);
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
                if ( routesList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select a route before you can assign a vehicle to it!", "ERROR: No Route Selected", JOptionPane.ERROR_MESSAGE);
                }
                else if ( vehiclesList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select a vehicle before you can assign a route to it!", "ERROR: No Vehicle Selected", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    allocationsModel.addElement(routesList.getSelectedValue() + " & " + vehiclesList.getSelectedValue().toString().split(" ")[0]);
                    routesModel.removeElement(routesList.getSelectedValue());
                    vehiclesModel.removeElement(vehiclesList.getSelectedValue());
                }
            }
        });
        allocateButtonPanel.add(allocateButton);
        JButton deAllocateButton = new JButton("Deallocate");
        deAllocateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( allocationsList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select an allocation before you can remove it!", "ERROR: No Allocation Selected", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String text = allocationsList.getSelectedValue().toString();
                    allocationsModel.removeElement(allocationsList.getSelectedValue());
                    String[] textParts = text.split("&");
                    routesModel.addElement(textParts[0].trim());
                    vehiclesModel.addElement(vehicleService.getVehicleById(userInterface.getVehicle(textParts[1].trim()))
                    		.getRegistrationNumber() + " (" + vehicleService.getVehicleById(userInterface.getVehicle(textParts[1].trim())).getModel() + ")");
                    //Remove this from the interface as well.
                    /*String routeNumber = textParts[0].split("/")[0]; int routeDetailPos = -1;
                    for ( int k = 0; k < theInterface.getRoute(routeNumber).getNumRouteSchedules(); k++ ) {
                        if ( theInterface.getRoute(routeNumber).getRouteSchedule(k).toString().equalsIgnoreCase(textParts[0].trim()) ) {
                            routeDetailPos = k;
                        }
                    }*/
                    //Find vehicle object position.
                    int vehiclePos = -1;
                    for ( int j = 0; j < userInterface.getNumberVehicles(); j++ ) {
                        if ( vehicleService.getVehicleById(userInterface.getVehicle(j)).toString().equalsIgnoreCase(textParts[1].trim())) {
                            vehiclePos = j;
                        }
                    }
                    vehicleService.getVehicleById(userInterface.getVehicle(vehiclePos)).setRouteScheduleId(0);
                }
            }
        });
        allocateButtonPanel.add(deAllocateButton);

        //Add allocate button panel to screen panel.
        allocationScreenPanel.add(allocateButtonPanel);

        //Allocation label panel is "Allocation(s)".
        JPanel allocationsPanel = new JPanel();
        allocationsPanel.setBackground(Color.WHITE);
        allocationsLabel = new JLabel("Allocation(s):", SwingConstants.CENTER);
        allocationsLabel.setFont(new Font("Arial", Font.BOLD, 25));
        allocationsPanel.add(allocationsLabel, BorderLayout.EAST);

        //Add allocation label panel to screen panel.
        allocationScreenPanel.add(allocationsPanel);

        //Finally the allocation list.
        JPanel allocationListPanel = new JPanel();
        allocationListPanel.setBackground(Color.WHITE);
        allocationsModel = new DefaultListModel();
        ArrayList<String> allocations;
        String currentDate = userInterface.getCurrentSimTime().get(Calendar.YEAR) + "-" + userInterface.getCurrentSimTime().get(Calendar.MONTH) + "-" + userInterface.getCurrentSimTime().get(Calendar.DATE);
        allocations = userInterface.getTodayAllocations(currentDate);
        for ( int i = 0; i < allocations.size(); i++ ) {
            allocationsModel.addElement(allocations.get(i).toString());
            //For each allocation, remove route and vehicle from list.
            String[] allocateSplit = allocations.get(i).toString().split("&");
            routesModel.removeElement(allocateSplit[0].trim());
            vehiclesModel.removeElement(allocateSplit[1].substring(1,allocateSplit[1].length()));
        }
        /*LinkedList<Vehicle> vehicles = theInterface.getVehicles();
        Collections.sort(vehicles, new SortedVehicles());
        for ( int i = 0; i < vehicles.size(); i++ ) {
            theVehiclesModel.addElement(vehicles.get(i).toString());
        }*/
        allocationsList = new JList(allocationsModel);
        allocationsList.setFixedCellWidth(250);
        allocationsList.setVisibleRowCount(4);
        allocationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allocationsList.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane allocationsPane = new JScrollPane(allocationsList);
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
                if ( !routesModel.isEmpty() ) {
                    result = JOptionPane.showConfirmDialog(null, "There are some route schedules which do not have vehicles. If you do not assign vehicles to these routes, your passenger satisfaction will decrease! Do you want to continue?", "WARNING: Some routes are unallocated!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                }
                if ( result == JOptionPane.YES_OPTION ) {
                    //Save vehicle positions - then set other ones to null!
                    ArrayList<Integer> vehiclePoses = new ArrayList<Integer>();
                    //Do requested allocations.
                    for ( int i = 0; i < allocationsModel.size(); i++ ) {
                        //Separate route and vehicle data.
                        String[] allocationSplit = allocationsModel.get(i).toString().split("&");
                        //Store route detail object.
                        String routeNumber = allocationSplit[0].split("/")[0]; int routeDetailPos = -1;
                        for ( int k = 0; k < routeService.getRouteById(userInterface.getRoute(routeNumber)).getRouteSchedules().size(); k++ ) {
                            if ( routeService.getRouteById(userInterface.getRoute(routeNumber)).getRouteSchedules().get(k).toString().equalsIgnoreCase(allocationSplit[0].trim()) ) {
                                routeDetailPos = k;
                            }
                        }
                        //Find vehicle object position.
                        int vehiclePos = -1;
                        for ( int j = 0; j < userInterface.getNumberVehicles(); j++ ) {
                            if ( vehicleService.getVehicleById(userInterface.getVehicle(j)).getRegistrationNumber().equalsIgnoreCase(allocationSplit[1].trim())) {
                                vehiclePos = j;
                                vehiclePoses.add(vehiclePos);
                            }
                        }
                        //Now assign route detail to vehicle and vice versa.
                        vehicleService.getVehicleById(userInterface.getVehicle(vehiclePos)).setRouteScheduleId(routeService.getRouteById(userInterface.getRoute(routeNumber)).getRouteSchedules().get(routeDetailPos).getId());
                        routeService.getRouteById(userInterface.getRoute(routeNumber)).addAllocation(routeService.getRouteById(userInterface.getRoute(routeNumber)).getRouteSchedules().get(routeDetailPos).toString(), vehicleService.getVehicleById(userInterface.getVehicle(vehiclePos)));
                    }
                    for ( int i = 0; i < userInterface.getNumberVehicles(); i++ ) {
                        if ( !vehiclePoses.contains(i) ) {
                            vehicleService.getVehicleById(userInterface.getVehicle(i)).setRouteScheduleId(0);
                        }
                    }
                    //Now return to previous screen.
                    controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
                }
            }
        });
        bottomButtonPanel.add(saveAllocationsButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Now return to previous screen.
                controlScreen.redrawManagement(ManagePanel.this.getDisplayPanel());
            }
        });
        bottomButtonPanel.add(previousScreenButton);

        //Add bottom button panel to the screen panel.
        allocationScreenPanel.add(bottomButtonPanel);

        //Return allocationScreenPanel.
        return allocationScreenPanel;
    }
    
}
