package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.RouteController;
import de.davelee.trams.controllers.RouteScheduleController;
import de.davelee.trams.controllers.ScenarioController;
import de.davelee.trams.controllers.TimetableController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.model.TimetableModel;

public class RoutePanel {
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private ScenarioController scenarioController;
	
	@Autowired
	private TimetableController timetableController;
	
	@Autowired
	private RouteController routeController;
	
	@Autowired
	private RouteScheduleController routeScheduleController;

    @Autowired
	private TimetablePanel myTimetablePanel;
	
	private JComboBox[] stopBoxes;
	private DefaultListModel timetableModel;
	private JButton createRouteButton;
	private JButton createTimetableButton;
	private JTextField routeNumberField;
	
	public JPanel createPanel ( final RouteModel routeModel, final ControlScreen controlScreen, final DisplayPanel displayPanel ) {
        
        //Create routeScreen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout ( new BoxLayout ( routeScreenPanel, BoxLayout.PAGE_AXIS ) );
        routeScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Route", SwingConstants.CENTER);
        if ( routeModel != null ) { topLabel.setText("Amend Route"); }
        topLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        routeScreenPanel.add(topLabelPanel);
                
        //Create panel for route number first of all.
        JPanel routeNumberPanel = new JPanel(new GridBagLayout());
        routeNumberPanel.setBackground(Color.WHITE);
        JLabel routeNumberLabel = new JLabel("Route Number: ", SwingConstants.CENTER);
        routeNumberLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        routeNumberPanel.add(routeNumberLabel);
        routeNumberField = new JTextField(10);
        routeNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        if ( routeModel != null ) { routeNumberField.setText(routeModel.getRouteNumber()); }
        routeNumberField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                enableCreateButtons();
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        routeNumberPanel.add(routeNumberField);
        
        //Add the routeNumber panel to the screen panel.
        routeScreenPanel.add(routeNumberPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Now create stops - a 2 x 3 grid layout.
        JPanel stopGridPanel = new JPanel(new GridLayout(2,3,5,5));
        stopGridPanel.setBackground(Color.WHITE);
        //Create the boxes and labels as appropriate.
        //Create the stops.
        JPanel[] stopPanels = new JPanel[5];
        stopBoxes = new JComboBox[5];
        for ( int i = 0; i < stopPanels.length; i++ ) {
            stopPanels[i] = new JPanel(new GridBagLayout());
            stopPanels[i].setBackground(Color.WHITE);
            JLabel stopLabel = new JLabel("Stop " + (i+1) + ":");
            stopLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            stopPanels[i].add(stopLabel);
            ScenarioModel scenario = scenarioController.getScenario("Landuff Transport Company");
            stopBoxes[i] = new JComboBox(scenario.getStopNames());
            stopBoxes[i].setFont(new Font("Arial", Font.PLAIN, 14));
            stopBoxes[i].setSelectedIndex(stopBoxes[i].getItemCount()-1);
            if ( routeModel != null ) { 
                if ( routeModel.getStopNames().size() > i ) {
                    int findIndexPos = findIndex(routeModel.getStopNames().get(i), i);
                    if ( findIndexPos != -1 ) { stopBoxes[i].setSelectedIndex(findIndexPos); }
                }
            }
            if ( i < 2 ) {
                stopBoxes[i].addActionListener( new ActionListener() {
                    public void actionPerformed ( ActionEvent e ) {
                        enableCreateButtons();
                    }
                });
            }
            stopPanels[i].add(stopBoxes[i]);
            stopGridPanel.add(stopPanels[i]);
        }
        //There's no stop 6 so it is just a filler.
        JPanel tempPanel = new JPanel();
        tempPanel.setBackground(Color.WHITE);
        stopGridPanel.add(tempPanel);
        
        //Add the stopGrid panel to the screen panel.
        routeScreenPanel.add(stopGridPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create the timetable list panel and three buttons.
        JPanel timetableListPanel = new JPanel(new BorderLayout());
        timetableListPanel.setBackground(Color.WHITE);
        JPanel routeDetailsLabelPanel = new JPanel();
        routeDetailsLabelPanel.setBackground(Color.WHITE);
        JLabel routeTimetableLabel = new JLabel("Route Timetable:");
        routeTimetableLabel.setFont(new Font("Arial", Font.ITALIC, 17));
        routeDetailsLabelPanel.add(routeTimetableLabel);
        timetableListPanel.add(routeDetailsLabelPanel, BorderLayout.NORTH);
        //Here is the actual timetable list.
        JPanel centreTimetableListPanel = new JPanel(new GridBagLayout());
        centreTimetableListPanel.setBackground(Color.WHITE);
        timetableModel = new DefaultListModel();
        //Now get all the timetables which we have at the moment.
        try {
            TimetableModel[] timetables = timetableController.getRouteTimetables(routeModel);
            for ( TimetableModel timetable : timetables) {
                timetableModel.addElement(timetable.getName());
            }
        } 
        catch (NullPointerException npe) { }
        final JList timetableList = new JList(timetableModel);
        if ( timetableModel.getSize() != 0 ) {
            timetableList.setSelectedIndex(0);
        }
        timetableList.setVisibleRowCount(3);
        timetableList.setFixedCellWidth(450);
        timetableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timetablePane = new JScrollPane(timetableList);
        centreTimetableListPanel.add(timetablePane);
        timetableListPanel.add(centreTimetableListPanel, BorderLayout.CENTER);
        //Now the three create, modify and delete button.
        JPanel timetableButtonPanel = new JPanel();
        timetableButtonPanel.setBackground(Color.WHITE);
        createTimetableButton = new JButton("Create");
        createTimetableButton.setEnabled(false);
        createTimetableButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //First of all, set the selected route.
                if ( routeModel == null && timetableModel.getSize() == 0 ) {
                    List<String> selectedOutwardStops = new ArrayList<String>();
                    List<String> selectedReturnStops = new ArrayList<String>();
                    for ( int i = 0; i < stopBoxes.length; i++ ) {
                        if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        	selectedOutwardStops.add(stopBoxes[i].getSelectedItem().toString());
                        }
                    }
                    for ( int i = (stopBoxes.length-1); i >=0; i-- ) {
                        if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        	selectedReturnStops.add(stopBoxes[i].getSelectedItem().toString());
                        }
                    }
                }
                //Show the actual screen!
                controlScreen.redrawManagement(myTimetablePanel.createPanel(null, routeModel, controlScreen, RoutePanel.this, displayPanel), gameController.getGameModel());
            }
        });
        timetableButtonPanel.add(createTimetableButton);
        final JButton modifyTimetableButton = new JButton("Modify");
        if ( timetableModel.getSize() == 0 ) { modifyTimetableButton.setEnabled(false); }
        modifyTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed (ActionEvent e ) {
                controlScreen.redrawManagement(myTimetablePanel.createPanel(timetableController.getRouteTimetable(routeModel, timetableList.getSelectedValue().toString()), routeModel, controlScreen, RoutePanel.this, displayPanel), gameController.getGameModel());
            }
        });
        timetableButtonPanel.add(modifyTimetableButton);
        final JButton deleteTimetableButton = new JButton("Delete");
        if ( timetableModel.getSize() == 0 ) { deleteTimetableButton.setEnabled(false); }
        deleteTimetableButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
            	timetableController.deleteTimetable(routeModel, timetableList.getSelectedValue().toString());
                timetableModel.removeElement(timetableList.getSelectedValue());
                if ( timetableModel.getSize() == 0 ) {
                    deleteTimetableButton.setEnabled(false);
                    modifyTimetableButton.setEnabled(false);
                }
                else {
                    timetableList.setSelectedIndex(0);
                }
            }
        });
        timetableButtonPanel.add(deleteTimetableButton);
        timetableListPanel.add(timetableButtonPanel, BorderLayout.SOUTH);
        
        //Add the timetableList panel to the screen panel.
        routeScreenPanel.add(timetableListPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        createRouteButton = new JButton("Create Route");
        createRouteButton.setEnabled(false);
        createRouteButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
               final GameModel gameModel = gameController.getGameModel();
               routeScheduleController.generateRouteSchedules(routeModel, gameModel.getCurrentTime(), gameModel.getScenarioName()); 
               List<String> selectedOutwardStops = new ArrayList<String>();
               for ( int i = 0; i < stopBoxes.length; i++ ) {
                   if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                   	selectedOutwardStops.add(stopBoxes[i].getSelectedItem().toString());
                   }
               }
               routeController.addNewRoute(routeNumberField.getText(), selectedOutwardStops);
               //Now return to previous screen.
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), gameModel);
            }
        });
        bottomButtonPanel.add(createRouteButton);
        
        //Create new route button and add it to screen panel.
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), gameController.getGameModel());
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Check enabling of buttons.
        enableCreateButtons();
        
        //Add bottom button panel to the screen panel.
        routeScreenPanel.add(bottomButtonPanel);
        
        //Return routeScreenPanel.
        return routeScreenPanel;
	}
	
	private void enableCreateButtons ( ) {
        //To enable create timetable button we need the selected item in stop1Box and stop2Box to not be -.
        if ( !stopBoxes[0].getSelectedItem().toString().equalsIgnoreCase("-") && !stopBoxes[1].getSelectedItem().toString().equalsIgnoreCase("-") && !routeNumberField.getText().equalsIgnoreCase("") ) {
            createTimetableButton.setEnabled(true);
            //In addition, the timetable model must not be 0 to create a route.
            if ( timetableModel.getSize() > 0 ) {
                createRouteButton.setEnabled(true);
            }
        }
    }
	
	private int findIndex ( String text, int pos ) {
        for ( int i = 0; i < stopBoxes[pos].getItemCount(); i++ ) {
            if ( stopBoxes[pos].getItemAt(i).toString().equalsIgnoreCase(text) ) {
                return i;
            }
        }
        return -1;
    }

}
