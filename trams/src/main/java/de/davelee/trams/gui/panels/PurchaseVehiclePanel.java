package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;

/**
 * This class represents a panel to purchase a vehicle.
 * @author Dave Lee
 */
public class PurchaseVehiclePanel {

	private final ControllerHandler controllerHandler;
	private JLabel totalPriceField;
	private JButton purchaseVehicleButton;

    /**
     * Create a new <code>PurchaseVehiclePanel</code> with access to all Controllers to get or send data where needed.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
    public PurchaseVehiclePanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Create a new <code>PurchaseVehiclePanel</code> panel and display it to the user.
     * @param vehicleType a <code>String</code> with the model name which should be purchased.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param managementPanel a <code>ManagementPanel</code> object which is the management panel that has been displayed to the user (for back button functionality).
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final String vehicleType, final ControlScreen controlScreen, final ManagementPanel managementPanel) {
        
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
        final VehicleResponse vehicleResponse = controllerHandler.getVehicleController().getAllCreatedVehicles(controlScreen.getCompany()).getVehicleResponses()[0];

        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        //Previous vehicle type button.
        JPanel previousButtonPanel = new JPanel(new GridBagLayout());
        previousButtonPanel.setBackground(Color.WHITE);
        JButton previousVehicleTypeButton = new JButton("< Previous Vehicle Type");
        if ( vehicleType.contentEquals(controllerHandler.getVehicleController().getVehicles(controlScreen.getCompany()).getVehicleResponses()[0].getModelName()) ) { previousVehicleTypeButton.setEnabled(false); }
        previousVehicleTypeButton.addActionListener(e -> controlScreen.redrawManagement(createPanel(controllerHandler.getVehicleController().getPreviousVehicleModel(controlScreen.getCompany(), vehicleType), controlScreen, managementPanel), companyResponse));
        previousButtonPanel.add(previousVehicleTypeButton);
        picturePanel.add(previousButtonPanel, BorderLayout.WEST);
        //Bus Display Picture.
        JPanel busPicture = new JPanel(new GridBagLayout());
        busPicture.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(vehicleResponse.getVehicleType() != null ? vehicleResponse.getVehicleType() + ".jpg" : "Bus.jpg",0,0);
        busDisplay.setSize(220,180);
        busDisplay.setBackground(Color.WHITE);
        busPicture.add(busDisplay);
        picturePanel.add(busPicture, BorderLayout.CENTER);
        //Next vehicle type button.
        JPanel nextButtonPanel = new JPanel(new GridBagLayout());
        nextButtonPanel.setBackground(Color.WHITE);
        JButton nextVehicleTypeButton = new JButton("Next Vehicle Type >");
        if ( vehicleType.contentEquals(controllerHandler.getVehicleController().getVehicles(controlScreen.getCompany()).getVehicleResponses()[0].getModelName()))  { nextVehicleTypeButton.setEnabled(false); }
        nextVehicleTypeButton.addActionListener(e -> controlScreen.redrawManagement(createPanel(controllerHandler.getVehicleController().getNextVehicleModel(controlScreen.getCompany(), vehicleType), controlScreen, managementPanel), companyResponse));
        nextButtonPanel.add(nextVehicleTypeButton);
        picturePanel.add(nextButtonPanel, BorderLayout.EAST);
        vehicleScreenPanel.add(picturePanel);
            
        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(7,2,2,2));
        gridPanel.setBackground(Color.WHITE);      
        //Create label and field for vehicle type and add it to the type panel.
        gridPanel.add(createLabel("Type:"));
        JLabel typeField = new JLabel(vehicleResponse.getModelName());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for seating capacity and add it to the seating panel.
        gridPanel.add(createLabel("Seating Capacity"));
        JLabel seatingField = new JLabel("" + vehicleResponse.getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        gridPanel.add(createLabel("Standing Capacity:"));
        JLabel standingField = new JLabel("" + vehicleResponse.getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for delivery date and add it to the delivery panel.
        gridPanel.add(createLabel("Delivery Date:"));
        LocalDate deliveryDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        deliveryDate = deliveryDate.plusDays(3);
        JLabel deliveryField = new JLabel("" + companyResponse.getTime().substring(0, 10));
        deliveryField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(deliveryField);
        //Create label and field for purchase price and add it to the price panel.
        JPanel priceLabelPanel = new JPanel();
        priceLabelPanel.setBackground(Color.WHITE);
        JLabel priceLabel = new JLabel("Purchase Price:", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        priceLabelPanel.add(priceLabel);
        gridPanel.add(priceLabel);
        final DecimalFormat format = new DecimalFormat("0.00");
        JLabel priceField = new JLabel("£");
        priceField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(priceField);
        //Create label and field for quantity and add it to the quantity panel.
        JPanel quantityLabelPanel = new JPanel(new BorderLayout());
        quantityLabelPanel.setBackground(Color.WHITE);
        JLabel quantityLabel = new JLabel("Quantity:", SwingConstants.CENTER);
        quantityLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        quantityLabelPanel.add(quantityLabel);
        gridPanel.add(quantityLabel);
        final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,40,1));
        quantitySpinner.setFont(new Font("Arial", Font.PLAIN, 12));
        quantitySpinner.addChangeListener(e -> {
            //TODO: make it possible to retrieve the purchase price of a vehicle
            //double totalPrice = Double.parseDouble(quantitySpinner.getValue().toString()) * vehicleResponse.getPurchasePrice();
            double totalPrice = 0.0;
            if ( totalPrice > companyResponse.getBalance() ) {
                totalPriceField.setText("£" + format.format(totalPrice) + " (Insufficient funds available)");
                totalPriceField.setForeground(Color.RED);
                purchaseVehicleButton.setEnabled(false);
            }
            else {
                totalPriceField.setText("£" + format.format(totalPrice));
                totalPriceField.setForeground(Color.BLACK);
                purchaseVehicleButton.setEnabled(true);
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
        //TODO: make it possible to calculate total price after retrieving purchase price.
        //double totalPrice = Double.parseDouble(quantitySpinner.getValue().toString()) * vehicleResponse.getPurchasePrice();
        double totalPrice = 0.0;
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
        purchaseVehicleButton.addActionListener (e -> {
            int quantity = Integer.parseInt(quantitySpinner.getValue().toString());
            for ( int i = 0; i < quantity; i++ ) {
                double purchasePrice = controllerHandler.getVehicleController().purchaseVehicle(
                        vehicleResponse.getModelName(), companyResponse.getName(), LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getYear(), Optional.empty());
                controllerHandler.getCompanyController().withdrawOrCreditBalance(purchasePrice, controlScreen.getPlayerName());
            }
            controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse);
        });
        bottomButtonPanel.add(purchaseVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener (e -> controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse));
        bottomButtonPanel.add(managementScreenButton);
        
        //Add bottom button panel to the screen panel.
        vehicleScreenPanel.add(bottomButtonPanel);
        
        return vehicleScreenPanel;
	}

    /**
     * This is a private helper method to generate a <code>JLabel</code> object with the desired text.
     * @param text a <code>String</code> with the text to display to the user.
     * @return a <code>JLabel</code> object which can be added to a panel.
     */
    private JLabel createLabel ( final String text ) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        return label;
    }
	
}
