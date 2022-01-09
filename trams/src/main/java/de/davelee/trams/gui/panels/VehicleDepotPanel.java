package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.EditingScreen;
import de.davelee.trams.util.GuiUtils;

/**
 * This class represents a panel to show a particular vehicle in the depot for a company.
 * @author Dave Lee
 */
public record VehicleDepotPanel ( ControllerHandler controllerHandler ) implements EditingScreen  {

    /**
     * Create a new <code>VehicleDepotPanel</code> panel and display it to the user.
     * @param registrationNumber a <code>String</code> object containing the vehicle to show at the start.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final String registrationNumber, final ControlScreen controlScreen ) {
        
        //Create screen panel to add things to.
        JPanel vehicleScreenPanel = new JPanel();
        vehicleScreenPanel.setLayout ( new BoxLayout ( vehicleScreenPanel, BoxLayout.PAGE_AXIS ) );
        vehicleScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        vehicleScreenPanel.add(GuiUtils.createHeadingPanel("Vehicle Depot"));
        
        //Now create a border layout so that we can have a choice of vehicles on the right-hand side.
        JPanel vehicleBorderPanel = new JPanel(new BorderLayout());
        vehicleBorderPanel.setBackground(Color.WHITE);
        
        //Create centre panel and add all those to appear in the centre panel to it!
        JPanel centrePanel = GuiUtils.createBoxPanel();
        
        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
        
        //Get vehicle data now!
        DefaultListModel<String> vehiclesModel = new DefaultListModel<>();
        VehicleResponse[] vehicleResponses = controllerHandler.getVehicleController().getAllCreatedVehicles(companyResponse.getName()).getVehicleResponses();
        for (VehicleResponse vehicleResponse : vehicleResponses) {
            if (controllerHandler.getVehicleController().hasVehicleBeenDelivered(vehicleResponse.getDeliveryDate(), companyResponse.getTime())) {
                vehiclesModel.addElement(vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"));
            }
        }
        
        //Create vehicle object so that we can pull information from it.
        final VehicleResponse vehicleResponse;
        if ( !registrationNumber.equalsIgnoreCase("") ) {
            vehicleResponse = controllerHandler.getVehicleController().getVehicleByRegistrationNumber(registrationNumber, companyResponse.getName());
        } else {
        	vehicleResponse = controllerHandler.getVehicleController().getVehicleByRegistrationNumber(vehiclesModel.get(0), companyResponse.getName());
        }
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new GridBagLayout());
        picturePanel.setBackground(Color.WHITE);
        //TODO: Add a mapping of images to model names.
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
        JLabel idField = new JLabel(vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"));
        idField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(idField);
        //Create label and field for vehicle type and add it to the type panel.
        JPanel typeLabelPanel = new JPanel();
        typeLabelPanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:", SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        typeLabelPanel.add(typeLabel);
        gridPanel.add(typeLabel);
        JLabel typeField = new JLabel(vehicleResponse.getModelName());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for age and add it to the age panel.
        JPanel ageLabelPanel = new JPanel();
        ageLabelPanel.setBackground(Color.WHITE);
        JLabel ageLabel = new JLabel("Age:", SwingConstants.CENTER);
        ageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        ageLabelPanel.add(ageLabel);
        gridPanel.add(ageLabel);
        JLabel ageField = new JLabel(controllerHandler.getVehicleController().getAge(vehicleResponse.getDeliveryDate(),
        		companyResponse.getTime()) + " months");
        ageField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(ageField);
        //Create label and field for seating capacity and add it to the seating panel.
        JPanel seatingLabelPanel = new JPanel();
        seatingLabelPanel.setBackground(Color.WHITE);
        JLabel seatingLabel = new JLabel("Seating Capacity:", SwingConstants.CENTER);
        seatingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        seatingLabelPanel.add(seatingLabel);
        gridPanel.add(seatingLabel);
        JLabel seatingField = new JLabel("" + vehicleResponse.getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel("" + vehicleResponse.getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for assigned schedule and add it to the schedule panel.
        JPanel assignedLabelPanel = new JPanel();
        assignedLabelPanel.setBackground(Color.WHITE);
        JLabel assignedLabel = new JLabel("Assigned Schedule:", SwingConstants.CENTER);
        assignedLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        assignedLabelPanel.add(assignedLabel);
        gridPanel.add(assignedLabel);
        JLabel assignedField = new JLabel(displayAssignedRoute(vehicleResponse));
        assignedField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(assignedField);
        //Create label and field for value and add it to the value panel.
        JPanel valueLabelPanel = new JPanel();
        valueLabelPanel.setBackground(Color.WHITE);
        JLabel valueLabel = new JLabel("Value:", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        valueLabelPanel.add(valueLabel);
        gridPanel.add(valueLabel);
        DecimalFormat format = new DecimalFormat("0.00");
        JLabel valueField = new JLabel("£" + format.format(controllerHandler.getVehicleController().getValue(vehicleResponse, companyResponse.getTime())));
        valueField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(valueField);
        
        //Add the grid panel to the centre panel.
        centrePanel.add(gridPanel);
        
        //Create bottom button panel.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
                
        //Create sell vehicle button and add it to screen panel.
        bottomButtonPanel.add(GuiUtils.createButton("Sell Vehicle", e -> {
            double sellingPrice = controllerHandler.getVehicleController().sellVehicle(vehicleResponse, companyResponse.getTime());
            controllerHandler.getCompanyController().withdrawOrCreditBalance(sellingPrice, controlScreen.getPlayerName());
            controlScreen.redrawManagement(createPanel("", controlScreen), companyResponse);
        }, true));
        
        //Create return to create game screen button and add it to screen panel.
        bottomButtonPanel.add(GuiUtils.createButton("Return to Management Screen", e -> controlScreen.redrawManagement(new ManagementPanel(controllerHandler).createPanel(controlScreen), companyResponse), true
                ));
        
        //Add bottom button panel to the screen panel.
        centrePanel.add(bottomButtonPanel);
        
        //Add centre panel to border panel.
        vehicleBorderPanel.add(centrePanel, BorderLayout.CENTER);
        
        //Add east panel to border panel.
        vehicleBorderPanel.add(GuiUtils.createListPanel(vehiclesModel,
                vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"),
                controlScreen, companyResponse, VehicleDepotPanel.this), BorderLayout.EAST);
        
        //Add vehicleBorderPanel to vehicleScreenPanel.
        vehicleScreenPanel.add(vehicleBorderPanel);
        
        return vehicleScreenPanel;
	}

	private String displayAssignedRoute ( final VehicleResponse vehicleModel ) {
	    if ( vehicleModel.getAllocatedTour() != null ) {
            return "" + vehicleModel.getAllocatedTour();
        } else {
	        return "Not Assigned";
        }
    }

}
