package de.davelee.trams.gui.panels;

import java.awt.*;

import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.controllers.ControllerHandler;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;
import de.davelee.trams.util.GuiUtils;

/**
 * This class represents the panel that is shown on the control screen to manage the game e.g. add/remove drivers, vehicles etc.
 * @author Dave Lee
 */
public record ManagementPanel(ControllerHandler controllerHandler) {

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
        ImageDisplay infoDisplay;
        String company = companyResponse.getName();
        if ( numberRoutes == 0 || (controllerHandler.getVehicleController().getAllCreatedVehicles(company) != null && controllerHandler.getVehicleController().getAllCreatedVehicles(company).getCount() == 0) || (controllerHandler.getVehicleController().getAllocations(company) != null && controllerHandler.getVehicleController().getAllocations(company).size() == 0 )) {
            infoDisplay = new ImageDisplay("cross-picture.png",0,0);
        }
        else {
            infoDisplay = new ImageDisplay("info-picture.png",0,0);
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
        JPanel scenarioPanel = createBoxPanel();
        JPanel scenarioLabelPanel = new JPanel();
        scenarioLabelPanel.setBackground(Color.WHITE);
        final JLabel scenarioLabel = new JLabel("Scenario:", SwingConstants.CENTER);
        scenarioLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scenarioLabelPanel.add(scenarioLabel);
        scenarioPanel.add(scenarioLabelPanel, BorderLayout.NORTH);
        //Create description panel.
        JPanel scenarioDescriptionPanel = new JPanel(new BorderLayout());
        scenarioDescriptionPanel.setBackground(Color.WHITE);
        scenarioDescriptionPanel.add(createTextArea("View scenario information, game targets and the location map.", 0));
        scenarioPanel.add(scenarioDescriptionPanel, BorderLayout.CENTER);
        //Scenario buttons.
        JPanel scenarioButtonPanel = new JPanel();
        scenarioButtonPanel.setLayout ( new BoxLayout ( scenarioButtonPanel, BoxLayout.PAGE_AXIS ) );
        scenarioButtonPanel.setBackground(Color.WHITE);
        JPanel viewScenarioButtonPanel = new JPanel(new GridBagLayout());
        viewScenarioButtonPanel.setBackground(Color.WHITE);
        final JButton viewScenarioButton = new JButton("View Information");
        viewScenarioButton.addActionListener(e -> {
            //Show the actual screen!
            ScenarioPanel myScenarioPanel = new ScenarioPanel(controllerHandler);
            controlScreen.redrawManagement(myScenarioPanel.createPanel(controlScreen, ManagementPanel.this), companyResponse);
        });
        viewScenarioButtonPanel.add(viewScenarioButton);
        scenarioButtonPanel.add(viewScenarioButtonPanel);
        scenarioButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel locationMapButtonPanel = new JPanel(new GridBagLayout());
        locationMapButtonPanel.setBackground(Color.WHITE);
        locationMapButtonPanel.add(GuiUtils.createButton("Location Map", e -> {
            //Show the actual screen!
            LocationMapPanel myLocationMapPanel = new LocationMapPanel(controllerHandler);
            controlScreen.redrawManagement(myLocationMapPanel.createPanel(controlScreen, ManagementPanel.this), companyResponse);
        }, true));
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
        addRouteButton.addActionListener(e -> {
            //Show the actual screen!
            RoutePanel myRoutePanel = new RoutePanel(controllerHandler);
            controlScreen.redrawManagement(myRoutePanel.createPanel(null, controlScreen, ManagementPanel.this), companyResponse);
        });
        createRouteButtonPanel.add(addRouteButton);
        routeButtonPanel.add(createRouteButtonPanel);
        routeButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel timetableButtonPanel = new JPanel(new GridBagLayout());
        timetableButtonPanel.setBackground(Color.WHITE);
        timetableButtonPanel.add(GuiUtils.createButton("View Route Info", e -> {
            ViewTimetablePanel myViewTimetablePanel = new ViewTimetablePanel(controllerHandler);
            //Show the actual screen!
            controlScreen.redrawManagement(myViewTimetablePanel.createPanel(controllerHandler.getRouteController().getRoutes(company).getRouteResponses()[0].getRouteNumber(), controlScreen, ManagementPanel.this), companyResponse);
        }, numberRoutes > 0));
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
        vehiclePanel.add(createPanelWithOneComponent(createTextArea("Purchase vehicles, view current vehicles and sell old vehicles", 25),
                new BorderLayout()), BorderLayout.CENTER);
        //Vehicle buttons.
        JPanel vehicleButtonPanel = new JPanel();
        vehicleButtonPanel.setLayout ( new BoxLayout ( vehicleButtonPanel, BoxLayout.PAGE_AXIS ) );
        vehicleButtonPanel.setBackground(Color.WHITE);
        JPanel purchaseVehicleButtonPanel = new JPanel(new GridBagLayout());
        purchaseVehicleButtonPanel.setBackground(Color.WHITE);
        final JButton purchaseVehicleScreenButton = new JButton("Purchase");
        purchaseVehicleScreenButton.addActionListener(e -> {
            PurchaseVehiclePanel myPurchaseVehiclePanel = new PurchaseVehiclePanel(controllerHandler);
            //Show the actual screen!
            controlScreen.redrawManagement(myPurchaseVehiclePanel.createPanel(controllerHandler.getVehicleController().getVehicles(company).getVehicleResponses()[0].getModelName(), controlScreen, ManagementPanel.this), companyResponse);
        });
        purchaseVehicleButtonPanel.add(purchaseVehicleScreenButton);
        vehicleButtonPanel.add(purchaseVehicleButtonPanel);
        vehicleButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel viewDepotButtonPanel = new JPanel(new GridBagLayout());
        viewDepotButtonPanel.setBackground(Color.WHITE);
        viewDepotButtonPanel.add(GuiUtils.createButton("View Depot", e -> {
            VehicleDepotPanel vehicleDepotPanel = new VehicleDepotPanel(controllerHandler);
            //Show the actual screen!
            controlScreen.redrawManagement(vehicleDepotPanel.createPanel("", controlScreen), companyResponse);
        }, controllerHandler.getVehicleController().hasSomeVehiclesBeenDelivered(company, companyResponse.getTime())));
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
        driverPanel.add(createPanelWithOneComponent(createTextArea("Employ drivers, view current employees and sack drivers", 25),
                new BorderLayout()), BorderLayout.CENTER);
        //Driver buttons.
        JPanel driverButtonPanel = new JPanel();
        driverButtonPanel.setLayout ( new BoxLayout ( driverButtonPanel, BoxLayout.PAGE_AXIS ) );
        driverButtonPanel.setBackground(Color.WHITE);
        JPanel employDriverButtonPanel = new JPanel(new GridBagLayout());
        employDriverButtonPanel.setBackground(Color.WHITE);
        final JButton employDriversButton = new JButton("Employ");
        employDriversButton.addActionListener(e -> {
            EmployDriverPanel employDriverPanel = new EmployDriverPanel(controllerHandler);
            //Show the actual screen!
            controlScreen.redrawManagement(employDriverPanel.createPanel(controlScreen, ManagementPanel.this), companyResponse);
        });
        employDriverButtonPanel.add(employDriversButton);
        driverButtonPanel.add(employDriverButtonPanel);
        driverButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        driverButtonPanel.add(createPanelWithOneComponent(GuiUtils.createButton("View Drivers", e -> {
            //Show the actual screen!
            controlScreen.redrawManagement(new ViewDriverPanel(controllerHandler).createPanel("", controlScreen), companyResponse);
        }, controllerHandler.getDriverController().hasSomeDriversBeenEmployed(companyResponse)), new GridBagLayout()));
        driverPanel.add(driverButtonPanel, BorderLayout.SOUTH);
        gridPanel.add(driverPanel);

        //Allocation panel.
        JPanel allocationPanel = createBoxPanel();
        final JLabel allocationsLabel = new JLabel("Allocations:", SwingConstants.CENTER);
        allocationsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        allocationPanel.add(createPanelWithOneComponent(allocationsLabel, new BorderLayout()));
        //Create description panel.
        JPanel allocationDescriptionPanel = new JPanel(new BorderLayout());
        allocationDescriptionPanel.setBackground(Color.WHITE);
        allocationDescriptionPanel.add(createTextArea("Allocate or deallocate vehicles to routes.", 0), BorderLayout.CENTER);
        allocationPanel.add(allocationDescriptionPanel);
        //Allocation button.
        JPanel allocationButtonPanel = new JPanel();
        allocationButtonPanel.setBackground(Color.WHITE);
        allocationButtonPanel.add(GuiUtils.createButton("Change", e -> {
            AllocationPanel myAllocationPanel = new AllocationPanel(controllerHandler);
            //Show the actual screen!
            controlScreen.redrawManagement(myAllocationPanel.createPanel(controlScreen, ManagementPanel.this, companyResponse.getName(), companyResponse.getPlayerName()), companyResponse);
        }, true));
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

    /**
     * This is a private helper method to create a text area.
     * @param starterText a <code>String</code> containing the starter text to display in the text area.
     * @param columns a <code>int</code> containing the size of the text area. If 0 the normal size will be shown.
     * @return a <code>JTextArea</code> object which can be added to a panel.
     */
    private JTextArea createTextArea ( final String starterText, final Integer columns ) {
        JTextArea textArea = new JTextArea(starterText);
        textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.ITALIC, 14));
        if ( columns > 0 ) {
            textArea.setColumns(columns);
        }
        return textArea;
    }

    /**
     * This is a private method to create a <code>JPanel</code> with the desired layout and one component.
     * @param component a <code>JComponent</code> object to add to the panel.
     * @param layoutManager a <code>LayoutManager</code> object with the desired layout.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    private JPanel createPanelWithOneComponent (final JComponent component, final LayoutManager layoutManager) {
        JPanel oneComponentPanel = new JPanel(layoutManager);
        oneComponentPanel.setBackground(Color.WHITE);
        oneComponentPanel.add(component);
        return oneComponentPanel;
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

}
