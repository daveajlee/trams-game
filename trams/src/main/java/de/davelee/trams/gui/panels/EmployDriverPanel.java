package de.davelee.trams.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.util.DateFormats;

public class EmployDriverPanel {

    private ControllerHandler controllerHandler;
	
	public EmployDriverPanel (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final ControlScreen controlScreen, final DisplayPanel displayPanel ) {
		//Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel textLabelPanel = new JPanel(new GridBagLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Employ Driver");
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        //topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel);
        driverScreenPanel.add(textLabelPanel);

        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(7,2,2,2));
        gridPanel.setBackground(Color.WHITE);  
        //Driver name.
        JPanel driverNamePanel = new JPanel();
        driverNamePanel.setBackground(Color.WHITE);
        JLabel driverNameLabel = new JLabel("Name:");
        driverNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        driverNamePanel.add(driverNameLabel);
        final JTextField driverNameField = new JTextField("");
        driverNameField.setColumns(30);
        driverNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        driverNamePanel.add(driverNameField);

        gridPanel.add(driverNamePanel);

        //Contracted hours.
        JPanel contractedHoursPanel = new JPanel();
        contractedHoursPanel.setBackground(Color.WHITE);
        JLabel contractedHoursLabel = new JLabel("Contracted Hours:");
        contractedHoursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contractedHoursPanel.add(contractedHoursLabel);
        final JSpinner contractedHoursSpinner = new JSpinner(new SpinnerNumberModel(35,10,40,5));
        contractedHoursSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        contractedHoursPanel.add(contractedHoursSpinner);

        gridPanel.add(contractedHoursPanel);
        
        final GameModel gameModel = controllerHandler.getGameController().getGameModel();

        //Create label and field for start date and add it to the start panel.
        JPanel startLabelPanel = new JPanel();
        startLabelPanel.setBackground(Color.WHITE);
        JLabel startLabel = new JLabel("Start Date:", SwingConstants.CENTER);
        startLabel.setFont(new Font("Arial", Font.BOLD, 16));
        startLabelPanel.add(startLabel);
        final Calendar startDate = (Calendar) gameModel.getCurrentTime().clone();
        startDate.add(Calendar.HOUR, 72);
        JLabel startField = new JLabel("" + controllerHandler.getGameController().formatDateString(startDate, DateFormats.FULL_FORMAT));
        startField.setFont(new Font("Arial", Font.ITALIC, 14));
        startLabelPanel.add(startField);
        gridPanel.add(startLabelPanel);
        driverScreenPanel.add(gridPanel);

        //Create return to create game screen button and add it to screen panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton employDriverButton = new JButton("Employ Driver");
        employDriverButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
            	DriverModel driverModel = new DriverModel();
            	driverModel.setName(driverNameField.getText());
            	driverModel.setContractedHours((Integer) contractedHoursSpinner.getValue());
            	driverModel.setStartDate(startDate);
                controllerHandler.getDriverController().employDriver(driverModel);
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), controllerHandler.getGameController().getGameModel());
            }
        });
        buttonPanel.add(employDriverButton);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                    controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), gameModel);
            }
        });
        buttonPanel.add(managementScreenButton);
        driverScreenPanel.add(buttonPanel);

        return driverScreenPanel;
	}

}
