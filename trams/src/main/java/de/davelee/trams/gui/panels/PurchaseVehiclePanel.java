package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.VehicleController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.DateFormats;

public class PurchaseVehiclePanel {

	@Autowired
	private VehicleController vehicleController;
	
	@Autowired
	private GameController gameController;
	
	private JLabel totalPriceField;
	private JButton purchaseVehicleButton;
	
	public JPanel createPanel ( final String vehicleType, final ControlScreen controlScreen) {
        
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
        final VehicleModel vehicleModel = vehicleController.getVehicleByModel(vehicleType);
        
        final GameModel gameModel = gameController.getGameModel();
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        //Previous vehicle type button.
        JPanel previousButtonPanel = new JPanel(new GridBagLayout());
        previousButtonPanel.setBackground(Color.WHITE);
        JButton previousVehicleTypeButton = new JButton("< Previous Vehicle Type");
        if ( vehicleType.contentEquals(vehicleController.getFirstVehicleModel()) ) { previousVehicleTypeButton.setEnabled(false); }
        previousVehicleTypeButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(createPanel(vehicleController.getPreviousVehicleModel(vehicleType), controlScreen), gameModel);
            }
        });
        previousButtonPanel.add(previousVehicleTypeButton);
        picturePanel.add(previousButtonPanel, BorderLayout.WEST);
        //Bus Display Picture.
        JPanel busPicture = new JPanel(new GridBagLayout());
        busPicture.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(vehicleModel.getImagePath(),0,0);
        busDisplay.setSize(220,180);
        busDisplay.setBackground(Color.WHITE);
        busPicture.add(busDisplay);
        picturePanel.add(busPicture, BorderLayout.CENTER);
        //Next vehicle type button.
        JPanel nextButtonPanel = new JPanel(new GridBagLayout());
        nextButtonPanel.setBackground(Color.WHITE);
        JButton nextVehicleTypeButton = new JButton("Next Vehicle Type >");
        if ( vehicleType.contentEquals(vehicleController.getLastVehicleModel()))  { nextVehicleTypeButton.setEnabled(false); }
        nextVehicleTypeButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(createPanel(vehicleController.getNextVehicleModel(vehicleType), controlScreen), gameModel);
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
        JLabel typeField = new JLabel(vehicleModel.getModel());
        typeField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(typeField);
        //Create label and field for seating capacity and add it to the seating panel.
        JPanel seatingLabelPanel = new JPanel();
        seatingLabelPanel.setBackground(Color.WHITE);
        JLabel seatingLabel = new JLabel("Seating Capacity:", SwingConstants.CENTER);
        seatingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        seatingLabelPanel.add(seatingLabel);
        gridPanel.add(seatingLabel);
        JLabel seatingField = new JLabel(vehicleModel.getSeatingCapacity());
        seatingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(seatingField);
        //Create label and field for standing capacity and add it to the standing panel.
        JPanel standingLabelPanel = new JPanel();
        standingLabelPanel.setBackground(Color.WHITE);
        JLabel standingLabel = new JLabel("Standing Capacity:", SwingConstants.CENTER);
        standingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        standingLabelPanel.add(standingLabel);
        gridPanel.add(standingLabel);
        JLabel standingField = new JLabel(vehicleModel.getStandingCapacity());
        standingField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(standingField);
        //Create label and field for delivery date and add it to the delivery panel.
        JPanel deliveryLabelPanel = new JPanel();
        deliveryLabelPanel.setBackground(Color.WHITE);
        JLabel deliveryLabel = new JLabel("Delivery Date:", SwingConstants.CENTER);
        deliveryLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        deliveryLabelPanel.add(deliveryLabel);
        gridPanel.add(deliveryLabel);
        final Calendar deliveryDate = (Calendar) gameModel.getCurrentTime().clone();
        deliveryDate.add(Calendar.HOUR, 72);
        JLabel deliveryField = new JLabel("" + gameController.formatDateString(deliveryDate, DateFormats.FULL_FORMAT));
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
        JLabel priceField = new JLabel("£" + format.format(vehicleModel.getPurchasePrice()));
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
        quantitySpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                double totalPrice = Double.parseDouble(quantitySpinner.getValue().toString()) * vehicleModel.getPurchasePrice();
                if ( totalPrice > gameModel.getBalance() ) {
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
        double totalPrice = Double.parseDouble(quantitySpinner.getValue().toString()) * vehicleModel.getPurchasePrice();
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
                    vehicleController.purchaseVehicle(vehicleModel.getModel(), deliveryDate);
                }
                controlScreen.redrawManagement(new DisplayPanel().createPanel(controlScreen), gameModel); 
            }
        });
        bottomButtonPanel.add(purchaseVehicleButton);
        
        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(new DisplayPanel().createPanel(controlScreen), gameModel); 
            }
        });
        bottomButtonPanel.add(managementScreenButton);
        
        //Add bottom button panel to the screen panel.
        vehicleScreenPanel.add(bottomButtonPanel);
        
        return vehicleScreenPanel;
	}
	
}
