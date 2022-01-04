package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;

public class VehicleDepotPanel {
	
	private ControllerHandler controllerHandler;

	public VehicleDepotPanel (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final String registrationNumber, final ControlScreen controlScreen, final ManagementPanel displayPanel ) {
        
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
        
        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
        
        //Get vehicle data now so that we can used to compile first!
        DefaultListModel vehiclesModel = new DefaultListModel();
        VehicleResponse[] vehicleModels = controllerHandler.getVehicleController().getAllCreatedVehicles(companyResponse.getName());
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( controllerHandler.getVehicleController().hasVehicleBeenDelivered(vehicleModels[i].getDeliveryDate(), companyResponse.getTime()) ) {
                vehiclesModel.addElement(vehicleModels[i].getAdditionalTypeInformationMap().get("Registration Number"));
            }
        }
        
        //Create vehicle object so that we can pull information from it.
        final VehicleResponse vehicleModel;
        if ( !registrationNumber.equalsIgnoreCase("") ) {
            vehicleModel = controllerHandler.getVehicleController().getVehicleByRegistrationNumber(registrationNumber, companyResponse.getName());
        } else {
        	vehicleModel = controllerHandler.getVehicleController().getVehicleByRegistrationNumber(vehiclesModel.get(0).toString(), companyResponse.getName());
        }
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new GridBagLayout());
        picturePanel.setBackground(Color.WHITE);
        //TODO: Add a mapping of images to model names.
        /*ImageDisplay busDisplay = new ImageDisplay(vehicleModel.getImagePath(),0,0);
        busDisplay.setSize(220,200);
        busDisplay.setBackground(Color.WHITE);
        picturePanel.add(busDisplay);*/
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
        JLabel idField = new JLabel(vehicleModel.getAdditionalTypeInformationMap().get("Registration Number"));
        idField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(idField);
        //Create label and field for vehicle type and add it to the type panel.
        JPanel typeLabelPanel = new JPanel();
        typeLabelPanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:", SwingConstants.CENTER);
        typeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        typeLabelPanel.add(typeLabel);
        gridPanel.add(typeLabel);
        JLabel typeField = new JLabel(vehicleModel.getModelName());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for age and add it to the age panel.
        JPanel ageLabelPanel = new JPanel();
        ageLabelPanel.setBackground(Color.WHITE);
        JLabel ageLabel = new JLabel("Age:", SwingConstants.CENTER);
        ageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        ageLabelPanel.add(ageLabel);
        gridPanel.add(ageLabel);
        JLabel ageField = new JLabel(controllerHandler.getVehicleController().getAge(vehicleModel.getDeliveryDate(),
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
        JLabel seatingField = new JLabel("" + vehicleModel.getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel("" + vehicleModel.getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for assigned schedule and add it to the schedule panel.
        JPanel assignedLabelPanel = new JPanel();
        assignedLabelPanel.setBackground(Color.WHITE);
        JLabel assignedLabel = new JLabel("Assigned Schedule:", SwingConstants.CENTER);
        assignedLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        assignedLabelPanel.add(assignedLabel);
        gridPanel.add(assignedLabel);
        JLabel assignedField = new JLabel(displayAssignedRoute(vehicleModel));
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
        JLabel valueField = new JLabel("£" + format.format(controllerHandler.getVehicleController().getValue(vehicleModel, companyResponse.getTime())));
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
                double sellingPrice = controllerHandler.getVehicleController().sellVehicle(vehicleModel, companyResponse.getTime());
                controllerHandler.getCompanyController().withdrawOrCreditBalance(sellingPrice, controlScreen.getPlayerName());
                controlScreen.redrawManagement(createPanel("", controlScreen, displayPanel), companyResponse);
            }
        });
        bottomButtonPanel.add(sellVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(new ManagementPanel(controllerHandler).createPanel(controlScreen), companyResponse);
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
        final JList vehiclesList = new JList(vehiclesModel);
        vehiclesList.setFixedCellWidth(100);
        vehiclesList.setVisibleRowCount(25);
        vehiclesList.setSelectedValue(vehicleModel.getAdditionalTypeInformationMap().get("Registration Number"), true);
        vehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehiclesList.setFont(new Font("Arial", Font.PLAIN, 15));
        vehiclesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                String selectedValue = vehiclesList.getSelectedValue().toString();
                controlScreen.redrawManagement(createPanel(selectedValue, controlScreen, displayPanel), companyResponse);
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

	private String displayAssignedRoute ( final VehicleResponse vehicleModel ) {
	    if ( vehicleModel.getAllocatedTour() != null ) {
            return "" + vehicleModel.getAllocatedTour();
        } else {
	        return "Not Assigned";
        }
    }

}
