package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.DriverController;
import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.RouteController;
import de.davelee.trams.controllers.RouteScheduleController;
import de.davelee.trams.controllers.TipController;
import de.davelee.trams.controllers.VehicleController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;

public class DisplayPanel {
	
	@Autowired
	private DriverController driverController;
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private RouteController routeController;
	
	@Autowired 
	private RouteScheduleController routeScheduleController;
	
	@Autowired
	private VehicleController vehicleController;
	
	@Autowired
	private TipController tipController;

	@Autowired
	private ScenarioPanel myScenarioPanel;

	@Autowired
	private LocationMapPanel myLocationMapPanel;

    @Autowired
	private RoutePanel myRoutePanel;

    @Autowired
	private ViewTimetablePanel myViewTimetablePanel;

    @Autowired
	private PurchaseVehiclePanel myPurchaseVehiclePanel;

    @Autowired
	private VehicleDepotPanel vehicleDepotPanel;

    @Autowired
	private EmployDriverPanel employDriverPanel;

    @Autowired
	private AllocationPanel myAllocationPanel;

	private static final Logger logger = LoggerFactory.getLogger(DisplayPanel.class);
	
	public JPanel createPanel ( final ControlScreen controlScreen ) {
    	
        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
        //Create a label for an information picture and an information area.
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.WHITE);
        ImageDisplay infoDisplay = null;
        if ( routeController.getNumberRoutes() == 0 || (vehicleController.getAllCreatedVehicles() != null && vehicleController.getAllCreatedVehicles().length == 0) || (vehicleController.getAllocations() != null && vehicleController.getAllocations().size() == 0 )) {
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
        if ( routeController.getNumberRoutes() == 0 ) {
            informationArea.setText("WARNING: No routes have been devised yet. Click 'Create Route' to define a route.");
        }
        else if ( vehicleController.getAllCreatedVehicles().length == 0 ) {
            informationArea.setText("WARNING: You can't run routes without vehicles. Click 'Purchase Vehicle' to buy a vehicle");
        }
        else if ( vehicleController.getAllocations().size() == 0 ) {
            informationArea.setText("WARNING: To successfully run journeys, you must assign vehicles to route schedules. Click 'Allocations' to match vehicles to route schedules");
        }
        else {
            logger.debug("The allocations size was " + vehicleController.getAllocations().size() + " which is " + vehicleController.getAllocations().toString());
            informationArea.setText(tipController.getRandomTipMessage());
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
        final JLabel scenarioLabel = new JLabel("Scenario:", SwingConstants.CENTER);
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
        final JButton viewScenarioButton = new JButton("View Information");
        viewScenarioButton.addActionListener( new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(myScenarioPanel.createPanel(controlScreen, DisplayPanel.this), gameController.getGameModel());
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
                    controlScreen.redrawManagement(myLocationMapPanel.createPanel(controlScreen, DisplayPanel.this), gameController.getGameModel());
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
        JLabel routesLabel = new JLabel("Routes:", SwingConstants.CENTER);
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
        final JButton addRouteButton = new JButton("Create Route");
        addRouteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(myRoutePanel.createPanel(null, controlScreen, DisplayPanel.this), gameController.getGameModel());
            }
        });
        createRouteButtonPanel.add(addRouteButton);
        routeButtonPanel.add(createRouteButtonPanel);
        routeButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel timetableButtonPanel = new JPanel(new GridBagLayout());
        timetableButtonPanel.setBackground(Color.WHITE);
        final JButton routeTimetableButton = new JButton("View Route Info");
        if ( routeController.getNumberRoutes() == 0 ) { routeTimetableButton.setEnabled(false); }
        routeTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(myViewTimetablePanel.createPanel(routeController.getRouteModels()[0].getRouteNumber(), 0, 0, controlScreen, myRoutePanel, DisplayPanel.this), gameController.getGameModel());
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
        final JLabel vehiclesLabel = new JLabel("Vehicles:", SwingConstants.CENTER);
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
        final JButton purchaseVehicleScreenButton = new JButton("Purchase");
        purchaseVehicleScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(myPurchaseVehiclePanel.createPanel(vehicleController.getFirstVehicleModel(), controlScreen, DisplayPanel.this), gameController.getGameModel());
            }
        });
        purchaseVehicleButtonPanel.add(purchaseVehicleScreenButton);
        vehicleButtonPanel.add(purchaseVehicleButtonPanel);
        vehicleButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDepotButtonPanel = new JPanel(new GridBagLayout());
        viewDepotButtonPanel.setBackground(Color.WHITE);
        final JButton viewDepotButton = new JButton("View Depot");
        if ( !vehicleController.hasSomeVehiclesBeenDelivered() ) { viewDepotButton.setEnabled(false); }
        viewDepotButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(vehicleDepotPanel.createPanel("", controlScreen, DisplayPanel.this), gameController.getGameModel());
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
        JLabel driversLabel = new JLabel("Drivers:", SwingConstants.CENTER);
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
        final JButton employDriversButton = new JButton("Employ");
        employDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(employDriverPanel.createPanel(controlScreen, DisplayPanel.this), gameController.getGameModel());
            }
        });
        employDriverButtonPanel.add(employDriversButton);
        driverButtonPanel.add(employDriverButtonPanel);
        driverButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDriversButtonPanel = new JPanel(new GridBagLayout());
        viewDriversButtonPanel.setBackground(Color.WHITE);
        final JButton viewDriversButton = new JButton("View Drivers");
        if ( !driverController.hasSomeDriversBeenEmployed() ) { viewDriversButton.setEnabled(false); }
        viewDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(new ViewDriverPanel().createPanel(controlScreen), gameController.getGameModel());
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
        final JLabel allocationsLabel = new JLabel("Allocations:", SwingConstants.CENTER);
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
        final JButton changeAllocationButton = new JButton("Change");
        changeAllocationButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                    controlScreen.redrawManagement(myAllocationPanel.createPanel(controlScreen, DisplayPanel.this), gameController.getGameModel());
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

}
