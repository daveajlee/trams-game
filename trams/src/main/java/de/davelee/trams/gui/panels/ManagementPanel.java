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

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.controllers.ControllerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;

/**
 * This class represents the panel that is shown on the control screen to manage the game e.g. add/remove drivers, vehicles etc.
 * @author Dave Lee
 */
public class ManagementPanel {

    private ControllerHandler controllerHandler;

	private static final Logger logger = LoggerFactory.getLogger(ManagementPanel.class);

    /**
     * Create a new management panel.
     * @param controllerHandler a <code>ControllerHandler</code> containing all controllers available via Spring.
     */
    public ManagementPanel(final ControllerHandler controllerHandler) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Display the management panel to the user in the supplied control screen.
     * @param controlScreen a <code>ControlScreen</code> object representing the frame to display the panel in.
     * @return a <code>JPanel</code> object containing the panel to display to the user.
     */
	public JPanel createPanel ( final ControlScreen controlScreen ) {

        //Store company response.
        CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());

        //Store number of routes.
        long numberRoutes = controllerHandler.getRouteController().getRoutes(companyResponse.getName()).getCount();

        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
        //Create a label for an information picture and an information area.
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.WHITE);
        ImageDisplay infoDisplay = null;
        String company = companyResponse.getName();
        if ( numberRoutes == 0 || (controllerHandler.getVehicleController().getAllCreatedVehicles(company) != null && controllerHandler.getVehicleController().getAllCreatedVehicles(company).getCount() == 0) || (controllerHandler.getVehicleController().getAllocations(company) != null && controllerHandler.getVehicleController().getAllocations(company).size() == 0 )) {
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
        if ( numberRoutes == 0 ) {
            informationArea.setText("WARNING: No routes have been devised yet. Click 'Create Route' to define a route.");
        }
        else if ( controllerHandler.getVehicleController().getAllCreatedVehicles(company).getCount() == 0 ) {
            informationArea.setText("WARNING: You can't run routes without vehicles. Click 'Purchase Vehicle' to buy a vehicle");
        }
        else if ( controllerHandler.getVehicleController().getAllocations(company).size() == 0 ) {
            informationArea.setText("WARNING: To successfully run journeys, you must assign vehicles to route schedules. Click 'Allocations' to match vehicles to route schedules");
        }
        else {
            logger.debug("The allocations size was " + controllerHandler.getVehicleController().getAllocations(company).size() + " which is " + controllerHandler.getVehicleController().getAllocations(company).toString());
            informationArea.setText(controllerHandler.getTipController().getRandomTipMessage());
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
                ScenarioPanel myScenarioPanel = new ScenarioPanel(controllerHandler);
                controlScreen.redrawManagement(myScenarioPanel.createPanel(controlScreen, ManagementPanel.this), companyResponse);
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
                LocationMapPanel myLocationMapPanel = new LocationMapPanel(controllerHandler);
                controlScreen.redrawManagement(myLocationMapPanel.createPanel(controlScreen, ManagementPanel.this), companyResponse);
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
                RoutePanel myRoutePanel = new RoutePanel(controllerHandler);
                controlScreen.redrawManagement(myRoutePanel.createPanel(null, controlScreen, ManagementPanel.this), companyResponse);
            }
        });
        createRouteButtonPanel.add(addRouteButton);
        routeButtonPanel.add(createRouteButtonPanel);
        routeButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel timetableButtonPanel = new JPanel(new GridBagLayout());
        timetableButtonPanel.setBackground(Color.WHITE);
        final JButton routeTimetableButton = new JButton("View Route Info");
        if ( numberRoutes == 0 ) { routeTimetableButton.setEnabled(false); }
        routeTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RoutePanel myRoutePanel = new RoutePanel(controllerHandler);
                ViewTimetablePanel myViewTimetablePanel = new ViewTimetablePanel(controllerHandler);
                //Show the actual screen!
                controlScreen.redrawManagement(myViewTimetablePanel.createPanel(controllerHandler.getRouteController().getRoutes(company).getRouteResponses()[0].getRouteNumber(), controlScreen, ManagementPanel.this), companyResponse);
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
                PurchaseVehiclePanel myPurchaseVehiclePanel = new PurchaseVehiclePanel(controllerHandler);
                //Show the actual screen!
                controlScreen.redrawManagement(myPurchaseVehiclePanel.createPanel(controllerHandler.getVehicleController().getVehicles(company).getVehicleResponses()[0].getModelName(), controlScreen, ManagementPanel.this), companyResponse);
            }
        });
        purchaseVehicleButtonPanel.add(purchaseVehicleScreenButton);
        vehicleButtonPanel.add(purchaseVehicleButtonPanel);
        vehicleButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDepotButtonPanel = new JPanel(new GridBagLayout());
        viewDepotButtonPanel.setBackground(Color.WHITE);
        final JButton viewDepotButton = new JButton("View Depot");
        if ( !controllerHandler.getVehicleController().hasSomeVehiclesBeenDelivered(company, companyResponse.getTime()) ) { viewDepotButton.setEnabled(false); }
        viewDepotButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VehicleDepotPanel vehicleDepotPanel = new VehicleDepotPanel(controllerHandler);
                //Show the actual screen!
                controlScreen.redrawManagement(vehicleDepotPanel.createPanel("", controlScreen), companyResponse);
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
                EmployDriverPanel employDriverPanel = new EmployDriverPanel(controllerHandler);
                //Show the actual screen!
                controlScreen.redrawManagement(employDriverPanel.createPanel(controlScreen, ManagementPanel.this), companyResponse);
            }
        });
        employDriverButtonPanel.add(employDriversButton);
        driverButtonPanel.add(employDriverButtonPanel);
        driverButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDriversButtonPanel = new JPanel(new GridBagLayout());
        viewDriversButtonPanel.setBackground(Color.WHITE);
        final JButton viewDriversButton = new JButton("View Drivers");
        if ( !controllerHandler.getDriverController().hasSomeDriversBeenEmployed(companyResponse) ) { viewDriversButton.setEnabled(false); }
        viewDriversButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Show the actual screen!
                controlScreen.redrawManagement(new ViewDriverPanel(controllerHandler).createPanel("", controlScreen), companyResponse);
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
                AllocationPanel myAllocationPanel = new AllocationPanel(controllerHandler);
                //Show the actual screen!
                controlScreen.redrawManagement(myAllocationPanel.createPanel(controlScreen, ManagementPanel.this, companyResponse.getName(), companyResponse.getPlayerName()), companyResponse);
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
