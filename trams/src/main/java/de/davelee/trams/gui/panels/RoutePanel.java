package de.davelee.trams.gui.panels;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.gui.ControlScreen;

import de.davelee.trams.controllers.ControllerHandler;

/**
 * This class represents a panel to create or edit routes.
 * @author Dave Lee
 */
public class RoutePanel {

    private final ControllerHandler controllerHandler;
    private DefaultListModel<String> availableStopModel;
	private DefaultListModel<String> routeStopModel;
	private JButton createRouteButton;
	private JTextField routeNumberField;

    /**
     * Create a new <code>RoutePanel</code> with access to all Controllers to get or send data where needed.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
    public RoutePanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Create a new <code>RoutePanel</code> panel and display it to the user.
     * @param routeResponse a <code>RouteResponse</code> object containing the data to edit which can be null if a new route should be added.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param managementPanel a <code>ManagementPanel</code> object which is the management panel that has been displayed to the user (for back button functionality).
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel (final RouteResponse routeResponse, final ControlScreen controlScreen, final ManagementPanel managementPanel ) {
        
        //Create routeScreen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout ( new BoxLayout ( routeScreenPanel, BoxLayout.PAGE_AXIS ) );
        routeScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JLabel topLabel = new JLabel("Create New Route", SwingConstants.CENTER);
        if ( routeResponse != null ) { topLabel.setText("Amend Route"); }
        topLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        routeScreenPanel.add(createPanelWithOneComponent(topLabel, new BorderLayout()));
                
        //Create panel for route number.
        JPanel routeNumberPanel = new JPanel(new GridBagLayout());
        routeNumberPanel.setBackground(Color.WHITE);
        JLabel routeNumberLabel = new JLabel("Route Number: ", SwingConstants.CENTER);
        routeNumberLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        routeNumberPanel.add(routeNumberLabel);
        routeNumberField = new JTextField(10);
        routeNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        if ( routeResponse != null ) { routeNumberField.setText(routeResponse.getRouteNumber()); }
        routeNumberField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                enableCreateButtons();
            }
            public void keyTyped(KeyEvent e) { /*Nothing happens when key is typed */ }
            public void keyPressed(KeyEvent e) { /*Nothing happens when key is pressed */ }
        });
        routeNumberPanel.add(routeNumberField);
        
        //Add the routeNumber panel to the screen panel.
        routeScreenPanel.add(routeNumberPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Now create stops - a 1 x 3 grid layout.
        JPanel stopGridPanel = new JPanel(new GridLayout(1,3,5,5));
        stopGridPanel.setBackground(Color.WHITE);

        //Create the route stop panel.
        routeStopModel = new DefaultListModel<>();
        if ( routeResponse != null ) {
            //TODO: Add each stop served by this route in a drop down.
            routeStopModel.addElement("");
        }
        CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
        String[] stopNames = convertToStopNames(controllerHandler.getScenarioController().getScenario(companyResponse.getScenarioName()).getStopDistances());
        availableStopModel = new DefaultListModel<>();
        for (String stopName : stopNames) {
            availableStopModel.addElement(stopName);
        }
        final JList<String> availableStopList = new JList<>(availableStopModel);
        final JList<String> routeStopList = new JList<>(routeStopModel);
        final JPanel routeStopListPanel = new JPanel(new BorderLayout());
        routeStopListPanel.setBackground(Color.WHITE);
        JLabel routeStopListLabel = new JLabel("Route Stops:");
        routeStopListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        routeStopListLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 14));
        routeStopListPanel.add(routeStopListLabel, BorderLayout.NORTH);
        final JScrollPane routeScrollPane = new JScrollPane(routeStopList);
        routeStopList.setVisibleRowCount(5);
        routeStopList.setFont(new Font("Arial", Font.PLAIN, 12));
        routeStopListPanel.add(routeScrollPane, BorderLayout.SOUTH);
        stopGridPanel.add(routeStopListPanel);
        //Create the buttons panel.
        final JPanel stopButtonPanel = new JPanel(new BorderLayout());
        stopButtonPanel.setBackground(Color.WHITE);
        final JButton routeAddStopButton = new JButton("<<");
        routeAddStopButton.addActionListener(e -> {
            String stopName = availableStopList.getSelectedValue();
            controllerHandler.getStopController().saveStop(stopName, controlScreen.getCompany());
            routeStopModel.addElement(stopName);
            availableStopModel.removeElement(stopName);
            enableCreateButtons();
        });
        routeAddStopButton.setHorizontalAlignment(SwingConstants.CENTER);
        stopButtonPanel.add(routeAddStopButton, BorderLayout.NORTH);
        final JButton routeRemoveStopButton = new JButton(">>");
        routeRemoveStopButton.addActionListener(e -> {
            String stopName = routeStopList.getSelectedValue();
            availableStopModel.addElement(stopName);
            routeStopModel.removeElement(stopName);
            enableCreateButtons();
        });
        routeRemoveStopButton.setHorizontalAlignment(SwingConstants.CENTER);
        stopButtonPanel.add(routeRemoveStopButton, BorderLayout.SOUTH);
        stopGridPanel.add(stopButtonPanel);
        final JPanel availableStopListPanel = new JPanel(new BorderLayout());
        availableStopListPanel.setBackground(Color.WHITE);
        JLabel availableStopListLabel = new JLabel("Available Stops:");
        availableStopListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        availableStopListLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 14));
        availableStopListPanel.add(createPanelWithOneComponent(availableStopListLabel, new BorderLayout()), BorderLayout.NORTH);
        final JScrollPane availableScrollPane = new JScrollPane(availableStopList);
        availableStopList.setVisibleRowCount(5);
        availableStopList.setFont(new Font("Arial", Font.PLAIN, 12));
        availableStopListPanel.add(availableScrollPane, BorderLayout.SOUTH);
        stopGridPanel.add(availableStopListPanel);
        
        //Add the stopGrid panel to the screen panel.
        routeScreenPanel.add(stopGridPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        createRouteButton = new JButton("Create Route");
        createRouteButton.setEnabled(false);
        createRouteButton.addActionListener (e -> {
           controllerHandler.getRouteController().addNewRoute( routeNumberField.getText(), companyResponse.getName());
           RouteResponse routeResponse1 = controllerHandler.getRouteController().getRoute(routeNumberField.getText(), companyResponse.getName());
           //Now move to timetable screen.
           TimetablePanel myTimetablePanel = new TimetablePanel(controllerHandler);
           controlScreen.redrawManagement(myTimetablePanel.createPanel(routeResponse1, controlScreen, managementPanel), companyResponse);
        });
        bottomButtonPanel.add(createRouteButton);
        
        //Create new route button and add it to screen panel.
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener (e -> controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse));
        bottomButtonPanel.add(previousScreenButton);
        
        //Check enabling of buttons.
        enableCreateButtons();
        
        //Add bottom button panel to the screen panel.
        routeScreenPanel.add(bottomButtonPanel);
        
        //Return routeScreenPanel.
        return routeScreenPanel;
	}

    /**
     * This is a private method to enable the create buttons based on the specified criteria.
     */
	private void enableCreateButtons ( ) {
        //To enable create timetable button we need the selected item in stop1Box and stop2Box to not be -.
        if ( routeStopModel.getSize() > 1 && !routeNumberField.getText().equalsIgnoreCase("") ) {
            createRouteButton.setEnabled(true);
        }
    }

    /**
     * This is a private method to convert the stop distances text in the format stop name : distance to
     * retrieve an array of only stop names.
     * @param stopDistances a <code>List</code> of <code>String</code> containing the stop distances in the format stop name : distance.
     * @return a <code>String</code> array with the stop names.
     */
    private String[] convertToStopNames ( final List<String> stopDistances ) {
        String[] stopNames = new String[stopDistances.size()];
        for ( int i = 0; i < stopDistances.size(); i++ ) {
            stopNames[i] = stopDistances.get(i).split(":")[0];
        }
        return stopNames;
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

}
