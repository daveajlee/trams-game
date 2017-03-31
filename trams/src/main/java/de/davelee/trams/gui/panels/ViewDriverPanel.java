package de.davelee.trams.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;

public class ViewDriverPanel {
	
	private ControllerHandler controllerHandler;

	public ViewDriverPanel ( final ControllerHandler controllerHandler ) {
	    this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final String driverName, final ControlScreen controlScreen, final DisplayPanel displayPanel ) {
		final GameModel gameModel = controllerHandler.getGameController().getGameModel();
    	
        //Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel textLabelPanel = new JPanel(new BorderLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("View Drivers", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel, BorderLayout.CENTER);
        driverScreenPanel.add(textLabelPanel);

        //Now create a border layout so that we can have a choice of drivers on the right hand side.
        JPanel driverBorderPanel = new JPanel(new BorderLayout());
        driverBorderPanel.setBackground(Color.WHITE);

        //Create centre panel and add all those to appear in the centre panel to it!
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout ( new BoxLayout ( centrePanel, BoxLayout.PAGE_AXIS ) );
        centrePanel.setBackground(Color.WHITE);

        //Get driver data now so that we can used to compile first!
        DefaultListModel driversModel = new DefaultListModel();
        DriverModel[] driverModels = controllerHandler.getDriverController().getAllDrivers();
        for ( int i = 0; i < driverModels.length; i++ ) {
            driversModel.addElement(driverModels[i].getName());
        }

        //Create driver object so that we can pull information from it.
        final DriverModel driverModel;
        if ( !driverName.equalsIgnoreCase("") ) {
            driverModel = controllerHandler.getDriverController().getDriverByName(driverName);
        } else {
            driverModel = controllerHandler.getDriverController().getDriverByName(driversModel.get(0).toString());
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
        JLabel nameField = new JLabel(driverModel.getName());
        nameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(nameField);
        //Create label and field for contracted hours and add it to the hours panel.
        JPanel hoursLabelPanel = new JPanel();
        hoursLabelPanel.setBackground(Color.WHITE);
        JLabel hoursLabel = new JLabel("Contracted Hours:", SwingConstants.CENTER);
        hoursLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        hoursLabelPanel.add(hoursLabel);
        gridPanel.add(hoursLabel);
        JLabel hoursField = new JLabel("" + driverModel.getContractedHours());
        hoursField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(hoursField);
        //Create label and field for start date and add it to the start date panel.
        JPanel startDateLabelPanel = new JPanel();
        startDateLabelPanel.setBackground(Color.WHITE);
        JLabel startDateLabel = new JLabel("Start Date:", SwingConstants.CENTER);
        startDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        startDateLabelPanel.add(startDateLabel);
        gridPanel.add(startDateLabel);
        JLabel startDateField = new JLabel(driverModel.getStartDate().get(Calendar.DAY_OF_MONTH) + "-" + (driverModel.getStartDate().get(Calendar.MONTH)+1) + "-" + driverModel.getStartDate().get(Calendar.YEAR));
        startDateField.setFont(new Font("Arial", Font.PLAIN, 12));
        gridPanel.add(startDateField);

        //Add the grid panel to the centre panel.
        centrePanel.add(gridPanel);

        //Create bottom button panel.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);

        //Create sack driver button and add it to screen panel.
        JButton sackDriverButton = new JButton("Sack Driver");
        sackDriverButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controllerHandler.getDriverController().sackDriver(driverModel);
                controlScreen.redrawManagement(createPanel("", controlScreen, displayPanel), controllerHandler.getGameController().getGameModel());
            }
        });
        bottomButtonPanel.add(sackDriverButton);

        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(new DisplayPanel(controllerHandler).createPanel(controlScreen), gameModel);
            }
        });
        bottomButtonPanel.add(managementScreenButton);

        //Add bottom button panel to the screen panel.
        centrePanel.add(bottomButtonPanel);

        //Add centre panel to border panel.
        driverBorderPanel.add(centrePanel, BorderLayout.CENTER);

        //Now create the east panel to display the driver list.
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBackground(Color.WHITE);
        //Third part of route panel is list of routes.
        JPanel modelPanel = new JPanel();
        modelPanel.setBackground(Color.WHITE);
        final JList driversList = new JList(driversModel);
        driversList.setFixedCellWidth(100);
        driversList.setVisibleRowCount(25);
        driversList.setSelectedValue(driverModel.getName(), true);
        driversList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        driversList.setFont(new Font("Arial", Font.PLAIN, 15));
        driversList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                String selectedValue = driversList.getSelectedValue().toString();
                controlScreen.redrawManagement(createPanel(selectedValue, controlScreen, displayPanel), gameModel);
            }
        });
        JScrollPane driversPane = new JScrollPane(driversList);
        modelPanel.add(driversPane);
        eastPanel.add(modelPanel, BorderLayout.CENTER);

        //Add east panel to border panel.
        driverBorderPanel.add(eastPanel, BorderLayout.EAST);

        //Add driverBorderPanel to driverScreenPanel.
        driverScreenPanel.add(driverBorderPanel);

        return driverScreenPanel;
	}

}
