package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.davelee.trams.gui.util.ScrollableTable;
import de.davelee.trams.model.TimetableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.JourneyController;
import de.davelee.trams.controllers.JourneyPatternController;
import de.davelee.trams.controllers.RouteController;
import de.davelee.trams.controllers.TimetableController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.TramsConstants;

public class ViewTimetablePanel {
	
	@Autowired
	private RouteController routeController;
	
	@Autowired
	private TimetableController timetableController;
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private JourneyPatternController journeyPatternController;
	
	@Autowired
	private JourneyController journeyController;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewTimetablePanel.class);
	
	public JPanel createPanel ( final String route, final int min, final int dateIndex, final ControlScreen controlScreen, final RoutePanel routePanel, final DisplayPanel displayPanel ) {
        
        //Create screen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout( new BoxLayout(routeScreenPanel, BoxLayout.PAGE_AXIS));
        routeScreenPanel.setBackground(Color.WHITE);
     
        final RouteModel routeModel = routeController.getRoute(route);
        final GameModel gameModel = gameController.getGameModel();
            
        //Create an overall screen panel.
        JPanel overallScreenPanel = new JPanel(new BorderLayout());
        overallScreenPanel.setBackground(Color.WHITE);
            
        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        //Here, we have the "Route Selection Screen" label.
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        //Selection options.
        JPanel selectionPanel = new JPanel();
        selectionPanel.setBackground(Color.WHITE);
        //Choose route.
        JLabel routeSelectionLabel = new JLabel("Route:");
        routeSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(routeSelectionLabel);
        final DefaultComboBoxModel routeSelectionModel = new DefaultComboBoxModel();
        RouteModel[] routeModels = routeController.getRouteModels();
        for ( int i = 0; i < routeModels.length; i++ ) {
            routeSelectionModel.addElement(routeModels[i].getRouteNumber());
        }
        final JComboBox routeSelectionBox = new JComboBox(routeSelectionModel);
        routeSelectionBox.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(createPanel(routeSelectionBox.getSelectedItem().toString(), 0, 0, controlScreen, routePanel, displayPanel), gameModel);
            }
        });
        routeSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        selectionPanel.add(routeSelectionBox);
        //Choose stop.
        JLabel stopSelectionLabel = new JLabel("Stop:");
        stopSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(stopSelectionLabel);
        final DefaultComboBoxModel stopSelectionModel = new DefaultComboBoxModel();
        List<String> routeStopNames = routeModel.getStopNames();
        for ( int i = 0; i < routeStopNames.size(); i++ ) {
            stopSelectionModel.addElement(routeStopNames.get(i));
        }
        final JComboBox stopSelectionBox = new JComboBox(stopSelectionModel);
        stopSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        selectionPanel.add(stopSelectionBox);
        //Choose direction.
        JLabel directionSelectionLabel = new JLabel("Direction:");
        directionSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(directionSelectionLabel);
        final DefaultComboBoxModel directionSelectionModel = new DefaultComboBoxModel();
        directionSelectionModel.addElement(routeStopNames.get(routeStopNames.size()-1));
        directionSelectionModel.addElement(routeStopNames.get(0));
        final JComboBox directionSelectionBox = new JComboBox(directionSelectionModel);
        directionSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        selectionPanel.add(directionSelectionBox);
        //Choose timetable.
        JLabel timetableSelectionLabel = new JLabel("Timetable:");
        timetableSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(timetableSelectionLabel);
        final DefaultComboBoxModel timetableSelectionModel = new DefaultComboBoxModel();
        TimetableModel[] timetableModels = timetableController.getRouteTimetables(routeModel);
        for ( int i = 0; i < timetableModels.length; i++ ) {
            timetableSelectionModel.addElement(timetableModels[i].getName());
        }
        final JComboBox timetableSelectionBox = new JComboBox(timetableSelectionModel);
        timetableSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        selectionPanel.add(timetableSelectionBox);
        //Add to top panel.
        topPanel.add(selectionPanel, BorderLayout.NORTH);
        //Show valid information.
        JPanel validityPanel = new JPanel(new BorderLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromDateLabel = new JLabel("Valid From: " + timetableController.getDateInfo(timetableController.getCurrentTimetable(routeModel, gameModel.getCurrentTime()).getValidFromDate()));
        validFromDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validFromDateLabel, BorderLayout.NORTH);
        JLabel validToDateLabel = new JLabel("Valid To: " + timetableController.getDateInfo(timetableController.getCurrentTimetable(routeModel, gameModel.getCurrentTime()).getValidToDate()));
        validToDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validToDateLabel, BorderLayout.SOUTH);
        topPanel.add(validityPanel, BorderLayout.SOUTH);
        //Add top panel to topLabel panel and topLabel panel to screenPanel.
        topLabelPanel.add(topPanel, BorderLayout.NORTH);
        routeScreenPanel.add(topLabelPanel);
            
        //Process data...
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        String[] columnNames = new String[] { "", "Monday - Friday", "Saturday", "Sunday" };
        String[][] data = new String[24][4];
        //TODO: Preprocessing necessary?
        /*Calendar cal = Calendar.getInstance();
        try {
        	cal.setTime(DateFormats.FULL_FORMAT.getFormat().parse(datesComboBox.getSelectedItem().toString()));
        } catch ( ParseException parseEx ) {
        	//TODO: exception handling.
        }
        List<JourneyModel> journeyModels = journeyController.generateJourneyTimetables(routeModel, cal, gameModel.getScenarioName(), TramsConstants.OUTWARD_DIRECTION);*/
        //LinkedList<Service> services = theSelectedRoute.getAllOutgoingServices(theDatesComboBox.getSelectedItem().toString());
        for ( int i = 0; i < 24; i++) {
            data[i][0] = "" + i;
            for ( int j = 1; j < 4; j++ ) {
                data[i][j] = "";
                //TODO: get timetable data.
                /*int pos = (min+j);
                logger.debug("This is #" + pos + " of the loop...");
                if ( journeyModels.size() <= (min+j) ) {
                    logger.debug("No more services!");
                    outgoingData[i][j+1] = "";
                }
                else if ( journeyController.getStopTime(journeyModels.get(min+j), routeStops.get(i)) == null ) {
                    logger.debug("Blank data!");
                    outgoingData[i][j+1] = "";
                }
                else {
                    outgoingData[i][j+1] = journeyController.getDisplayStopTime(journeyModels.get(min+j), routeStops.get(i));
                }*/
            }
        }
        //Display it!
        ScrollableTable scrollableTable = new ScrollableTable(data, columnNames);
        scrollableTable.setFont(new Font("Arial", Font.PLAIN, 10));
        tablePanel.add(scrollableTable, BorderLayout.CENTER);

        routeScreenPanel.add(tablePanel);
            
        //Create two buttons for previous and next.
        JPanel otherServicesButtonPanel = new JPanel();
        otherServicesButtonPanel.setBackground(Color.WHITE);
        JButton amendRouteButton = new JButton("Amend Route");
        amendRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Show the actual screen!
                controlScreen.redrawManagement(routePanel.createPanel(routeModel, controlScreen, displayPanel), gameModel);
                //int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete route " + ((Route) theRoutesModel.get(theRoutesList.getSelectedIndex())).getRouteNumber() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                //if ( confirm == JOptionPane.YES_OPTION ) {
                //    theInterface.deleteRoute(((Route) theRoutesModel.get(theRoutesList.getSelectedIndex())));
                //}
            }
        });
        otherServicesButtonPanel.add(amendRouteButton);
        JButton managementScreenButton = new JButton("Back to Management Screen");
        managementScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), gameModel);
            }
        });
        otherServicesButtonPanel.add(managementScreenButton);
        routeScreenPanel.add(otherServicesButtonPanel);
            
        overallScreenPanel.add(routeScreenPanel, BorderLayout.CENTER);
            
        return overallScreenPanel;
	}

}
