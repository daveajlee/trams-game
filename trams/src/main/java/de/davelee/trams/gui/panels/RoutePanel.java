package de.davelee.trams.gui.panels;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);

        //Retrieve company response for further processing.
        CompanyResponse companyResponse = controlScreen.getControllerHandler().getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());

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
        if ( !routeNumberField.getText().equalsIgnoreCase("") ) {
            createRouteButton.setEnabled(true);
        }
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
