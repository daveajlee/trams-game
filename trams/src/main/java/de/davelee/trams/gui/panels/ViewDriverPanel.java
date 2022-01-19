package de.davelee.trams.gui.panels;

import java.awt.*;

import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.EditingScreen;
import de.davelee.trams.util.GuiUtils;

/**
 * This class represents a panel to show a particular driver for a company.
 * @author Dave Lee
 */
public record ViewDriverPanel ( ControllerHandler controllerHandler ) implements EditingScreen {

    /**
     * Create a new <code>ViewDriverPanel</code> panel and display it to the user.
     * @param driverName a <code>String</code> object containing the name of the driver to show.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final String driverName, final ControlScreen controlScreen ) {
		final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
    	
        //Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        //Create label at top of screen in a topLabelPanel added to screenPanel.
        driverScreenPanel.add(GuiUtils.createHeadingPanel("View Drivers"));

        //Now create a border layout so that we can have a choice of drivers on the right-hand side.
        JPanel driverBorderPanel = new JPanel(new BorderLayout());
        driverBorderPanel.setBackground(Color.WHITE);

        //Create centre panel and add all those to appear in the centre panel to it!
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout ( new BoxLayout ( centrePanel, BoxLayout.PAGE_AXIS ) );
        centrePanel.setBackground(Color.WHITE);

        //Get driver data now!
        DefaultListModel<String> driversModel = new DefaultListModel<>();
        UserResponse[] userResponses = controllerHandler.getDriverController().getAllDrivers(companyResponse.getName());
        for (UserResponse userResponse : userResponses) {
            driversModel.addElement(userResponse.getUsername());
        }

        //Create driver object so that we can pull information from it.
        final UserResponse userResponse;
        if ( !driverName.equalsIgnoreCase("") ) {
            userResponse = controllerHandler.getDriverController().getDriverByName(driverName, companyResponse.getName());
        } else {
            userResponse = controllerHandler.getDriverController().getDriverByName(driversModel.get(0), companyResponse.getName());
        }

        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(3,2,5,5));
        gridPanel.setBackground(Color.WHITE);
        //Create label and field for name and add it to the name panel.
        JPanel nameLabelPanel = new JPanel();
        nameLabelPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Name:", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        nameLabelPanel.add(nameLabel);
        gridPanel.add(nameLabel);
        JLabel nameField = new JLabel(userResponse.getFirstName() + " " + userResponse.getSurname());
        nameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(nameField);
        //Create label and field for contracted hours and add it to the hours panel.
        JPanel hoursLabelPanel = new JPanel();
        hoursLabelPanel.setBackground(Color.WHITE);
        JLabel hoursLabel = new JLabel("Contracted Hours:", SwingConstants.CENTER);
        hoursLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        hoursLabelPanel.add(hoursLabel);
        gridPanel.add(hoursLabel);
        JLabel hoursField = new JLabel("" + userResponse.getContractedHoursPerWeek());
        hoursField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(hoursField);
        //Create label and field for start date and add it to the start date panel.
        JPanel startDateLabelPanel = new JPanel();
        startDateLabelPanel.setBackground(Color.WHITE);
        JLabel startDateLabel = new JLabel("Start Date:", SwingConstants.CENTER);
        startDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        startDateLabelPanel.add(startDateLabel);
        gridPanel.add(startDateLabel);
        JLabel startDateField = new JLabel(userResponse.getStartDate());
        startDateField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(startDateField);

        //Add the grid panel to the centre panel.
        centrePanel.add(gridPanel);

        //Create bottom button panel.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);

        //Create sack driver button and add it to screen panel.
        bottomButtonPanel.add(GuiUtils.createButton("Sack Driver", e -> {
            controllerHandler.getDriverController().sackDriver(userResponse.getCompany(), userResponse.getUsername());
            controlScreen.redrawManagement(createPanel("", controlScreen), companyResponse);
        }, true));

        //Create return to create game screen button and add it to screen panel.
        bottomButtonPanel.add(GuiUtils.createButton("Return to Management Screen", e -> controlScreen.redrawManagement(new ManagementPanel(controllerHandler).createPanel(controlScreen), companyResponse), true
        ));

        //Add bottom button panel to the screen panel.
        centrePanel.add(bottomButtonPanel);

        //Add centre panel to border panel.
        driverBorderPanel.add(centrePanel, BorderLayout.CENTER);

        //Add east panel to border panel.
        driverBorderPanel.add(GuiUtils.createListPanel(driversModel, userResponse.getUsername(), controlScreen, companyResponse, ViewDriverPanel.this), BorderLayout.EAST);

        //Add driverBorderPanel to driverScreenPanel.
        driverScreenPanel.add(driverBorderPanel);

        return driverScreenPanel;
	}

}
