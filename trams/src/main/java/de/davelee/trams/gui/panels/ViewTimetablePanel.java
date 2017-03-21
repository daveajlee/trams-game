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

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	
	public JPanel createPanel ( final String route, final int min, final int dateIndex, final ControlScreen controlScreen ) {
        
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
        JLabel topLabel = new JLabel("Timetable for Route " + routeModel.getRouteNumber(), SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topPanel.add(topLabel, BorderLayout.NORTH);
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
            
        //Create day of the week label and field.
        JPanel datesPanel = new JPanel();
        datesPanel.setBackground(Color.WHITE);
        JLabel datesLabel = new JLabel("Dates:");
        datesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        final JComboBox datesComboBox = new JComboBox ( journeyPatternController.getPossibleSchedulesDates(routeModel, gameModel.getCurrentTime()) );
        datesComboBox.setSelectedIndex(dateIndex);
        datesComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                controlScreen.redrawManagement(createPanel(routeModel.getRouteNumber(), 0, datesComboBox.getSelectedIndex(), controlScreen), gameModel);
            }
        });
        datesPanel.add(datesLabel); datesPanel.add(datesComboBox);
        routeScreenPanel.add(datesPanel);
            
        List<String> routeStops = routeModel.getStopNames();
        //Now make the first portion of the screen - this will list the stops in ascending order.
        JPanel outgoingPanel = new JPanel(new BorderLayout());
        outgoingPanel.setBackground(Color.WHITE);
        JLabel outgoingLabel = new JLabel(routeStops.get(0) + " - " + routeStops.get(routeStops.size()-1));
        outgoingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        outgoingPanel.add(outgoingLabel, BorderLayout.NORTH);
            
        //Process data...
        String[] outgoingColumnNames = new String[] { "Stop Name", "", "", "", "", "", "", "", "", "", "" };
        Object[][] outgoingData = new Object[routeModel.getStopNames().size()][11];
        Calendar cal = Calendar.getInstance();
        try {
        	cal.setTime(DateFormats.FULL_FORMAT.getFormat().parse(datesComboBox.getSelectedItem().toString()));
        } catch ( ParseException parseEx ) {
        	//TODO: exception handling.
        }
        List<JourneyModel> journeyModels = journeyController.generateJourneyTimetables(routeModel, cal, gameModel.getScenarioName(), TramsConstants.OUTWARD_DIRECTION);
        //LinkedList<Service> services = theSelectedRoute.getAllOutgoingServices(theDatesComboBox.getSelectedItem().toString());
        for ( int i = 0; i < routeStops.size(); i++) {
            outgoingData[i][0] = routeStops.get(i);
            for ( int j = 0; j < 10; j++ ) {
                int pos = (min+j);
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
                }
            }
        }
        //Display it!
        JTable outgoingTable = new JTable(outgoingData, outgoingColumnNames);
        JScrollPane outgoingScrollPane = new JScrollPane(outgoingTable);
        outgoingTable.setFillsViewportHeight(true);
        outgoingPanel.add(outgoingScrollPane, BorderLayout.CENTER);

        routeScreenPanel.add(outgoingPanel);
            
        //Create two buttons for previous and next.
        JPanel otherServicesButtonPanel = new JPanel();
        otherServicesButtonPanel.setBackground(Color.WHITE);
        JButton previousButton = new JButton("< Previous Services");
        if ( min == 0 ) {
            previousButton.setEnabled(false);
        }
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(createPanel(route, min-10, datesComboBox.getSelectedIndex(), controlScreen), gameModel);
            }
        });
        otherServicesButtonPanel.add(previousButton);
        JButton nextButton = new JButton("Next Services >");
        if ( (min+10) > journeyModels.size() ) {
            nextButton.setEnabled(false);
        }
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(createPanel(route, min+10, datesComboBox.getSelectedIndex(), controlScreen), gameModel);
            }
        });
        otherServicesButtonPanel.add(nextButton);
        JButton amendRouteButton = new JButton("Amend Route");
        amendRouteButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Show the actual screen!
                controlScreen.redrawManagement(new RoutePanel().createPanel(routeModel, controlScreen), gameModel);
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
                controlScreen.redrawManagement(new DisplayPanel().createPanel(controlScreen), gameModel);
            }
        });
        otherServicesButtonPanel.add(managementScreenButton);
        routeScreenPanel.add(otherServicesButtonPanel);
            
        overallScreenPanel.add(routeScreenPanel, BorderLayout.CENTER);
            
        //Third part of route panel is list of routes.
        JPanel modelPanel = new JPanel();
        modelPanel.setLayout( new BoxLayout(modelPanel, BoxLayout.PAGE_AXIS));
        modelPanel.setBackground(Color.WHITE);
        JLabel modelLabel = new JLabel("Routes:");
        modelLabel.setFont(new Font("Arial", Font.BOLD, 16));
        modelPanel.add(modelLabel);
            
        DefaultListModel routesModel = new DefaultListModel();
        RouteModel[] routeModels = routeController.getRouteModels();
        for ( int i = 0; i < routeModels.length; i++ ) {
            routesModel.addElement(routeModels[i].getRouteNumber());
        }
        final JList routesList = new JList(routesModel);
        routesList.setFixedCellWidth(40);
        routesList.setVisibleRowCount(15);
        routesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        routesList.setFont(new Font("Arial", Font.PLAIN, 15));
        if ( routesModel.getSize() > 0 ) { routesList.setSelectedValue(route, true); }
        routesList.addListSelectionListener ( new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent e ) {
                controlScreen.redrawManagement(createPanel(routesList.getSelectedValue().toString(), 0, 0, controlScreen), gameModel);
            }
        });
        JScrollPane routesPane = new JScrollPane(routesList);
        modelPanel.add(routesPane);
            
        overallScreenPanel.add(modelPanel, BorderLayout.EAST);
            
        return overallScreenPanel;
	}

}
