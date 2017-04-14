package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.davelee.trams.controllers.ControllerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.JourneyController;
import de.davelee.trams.controllers.JourneyPatternController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;

public class JourneyPatternPanel {

    private ControllerHandler controllerHandler;

    public JourneyPatternPanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
	
	private JCheckBox[] daysBox;
	private JButton createJourneyPatternButton;
	private JComboBox terminus1Box;
	private JComboBox terminus2Box;
	private SpinnerNumberModel everyMinuteModel;
	private JSpinner everyMinuteSpinner;
	
	private static final Logger logger = LoggerFactory.getLogger(JourneyPatternPanel.class);
	
	public JPanel createPanel ( final List<String> stopNames, final TimetableModel timetableModel, final JourneyPatternModel journeyPatternModel,
			final RouteModel routeModel, final ControlScreen controlScreen, final DisplayPanel displayPanel) {
        
        //Create journeyPatternScreen panel to add things to.
        JPanel journeyPatternScreenPanel = new JPanel();
        journeyPatternScreenPanel.setLayout ( new BoxLayout ( journeyPatternScreenPanel, BoxLayout.PAGE_AXIS ) );
        journeyPatternScreenPanel.setBackground(Color.WHITE);
        
        //Create label in middle of screen in a middleLabelPanel added to screenPanel.
        JPanel middleLabelPanel = new JPanel(new BorderLayout());
        middleLabelPanel.setBackground(Color.WHITE);
        JLabel middleLabel = new JLabel("Create Service Pattern", SwingConstants.CENTER);
        if ( journeyPatternModel != null ) { middleLabel.setText("Modify Service Pattern"); }
        middleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        middleLabel.setVerticalAlignment(JLabel.CENTER);
        middleLabelPanel.add(middleLabel, BorderLayout.CENTER);
        journeyPatternScreenPanel.add(middleLabelPanel);
        
        //Create journey pattern name panel.
        JPanel journeyPatternNamePanel = new JPanel(new GridBagLayout());
        journeyPatternNamePanel.setBackground(Color.WHITE);
        JLabel journeyPatternNameLabel = new JLabel("Name: ");
        journeyPatternNameLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        journeyPatternNamePanel.add(journeyPatternNameLabel);
        final JTextField journeyPatternNameField = new JTextField(20);
        if ( journeyPatternModel != null ) { journeyPatternNameField.setText(journeyPatternModel.getName()); }
        journeyPatternNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        journeyPatternNameField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                if ( !journeyPatternNameField.getText().equalsIgnoreCase("") ) {
                    for ( int i = 0; i < daysBox.length; i++ ) {
                        if ( daysBox[i].isSelected() ) {
                            createJourneyPatternButton.setEnabled(true);
                            return;
                        }
                    }
                    createJourneyPatternButton.setEnabled(false);
                }
                else {
                    createJourneyPatternButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        journeyPatternNamePanel.add(journeyPatternNameField);
        journeyPatternScreenPanel.add(journeyPatternNamePanel);
        
        //Create day of week panel with 7 tick boxes.
        JPanel dayOfWeekPanel = new JPanel(new GridBagLayout());
        dayOfWeekPanel.setBackground(Color.WHITE);
        daysBox = new JCheckBox[7];
        String[] dayStr = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        for ( int i = 0; i < daysBox.length; i++ ) {
            daysBox[i] = new JCheckBox(dayStr[i]);
            if ( journeyPatternModel != null && journeyPatternModel.getDaysOfOperation().contains((i+1))) {
                daysBox[i].setSelected(true);
            }
            daysBox[i].setFont(new Font("Arial", Font.PLAIN, 14));
            daysBox[i].addActionListener(new ActionListener() {
                public void actionPerformed ( ActionEvent e ) {
                    if ( !journeyPatternNameField.getText().equalsIgnoreCase("") ) {
                        for ( int i = 0; i < daysBox.length; i++ ) {
                            if ( daysBox[i].isSelected() ) {
                                createJourneyPatternButton.setEnabled(true);
                                return;
                            }
                        }
                        createJourneyPatternButton.setEnabled(false);
                    }
                    else {
                        createJourneyPatternButton.setEnabled(false);
                    }
                }
            });
            int addPos = (i+1);
            if (journeyPatternModel != null ) { 
            	if ( journeyPatternModel.getDaysOfOperation().contains("" + addPos) ) { 
            		daysBox[i].setSelected(true); 
            	} 
            }
            dayOfWeekPanel.add(daysBox[i]);
        }
        journeyPatternScreenPanel.add(dayOfWeekPanel);
        
        //Create panel with between stops.
        JPanel betweenStopsPanel = new JPanel(new GridBagLayout());
        betweenStopsPanel.setBackground(Color.WHITE);
        //Between label.
        JLabel betweenLabel = new JLabel("Between:");
        betweenLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(betweenLabel);
        //Terminus 1 Combo box.
        terminus1Box = new JComboBox();
        for ( int i = 0; i < stopNames.size()-1; i++ ) {
            terminus1Box.addItem(stopNames.get(i));
        }
        terminus1Box.setSelectedIndex(0);
        if ( journeyPatternModel != null ) { terminus1Box.setSelectedItem(journeyPatternModel.getReturnTerminus()); }
        terminus1Box.setFont(new Font("Arial", Font.PLAIN, 14));
        terminus1Box.addItemListener(new ItemListener() {
            public void itemStateChanged ( ItemEvent e ) {
                //Update terminus 2 box!!!
                terminus2Box.removeAllItems();
                for ( int i = (stopNames.indexOf(terminus1Box.getSelectedItem().toString()))+1; i < stopNames.size(); i++ ) {
                    terminus2Box.addItem(stopNames.get(i));
                }
                //Update spinner!
                everyMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())));
            }
        });
        betweenStopsPanel.add(terminus1Box);
        //And label.
        JLabel andLabel = new JLabel("  and  ");
        andLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(andLabel);
        //Terminus 2 Combo box.
        terminus2Box = new JComboBox();
        for ( int i = 1; i < stopNames.size(); i++ ) {
            terminus2Box.addItem(stopNames.get(i));
        }
        terminus2Box.setSelectedIndex(terminus2Box.getItemCount() - 1);
        if ( journeyPatternModel != null) { terminus2Box.setSelectedItem(journeyPatternModel.getOutgoingTerminus()); }
        terminus2Box.setFont(new Font("Arial", Font.PLAIN, 14));
        terminus2Box.addItemListener( new ItemListener () {
            public void itemStateChanged ( ItemEvent e ) {
                //Update spinner!
                if ( terminus2Box.getItemCount() != 0 ) {
                    everyMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())));
                }
            }
        });
        betweenStopsPanel.add(terminus2Box);
        //Add betweenStopsPanel.
        journeyPatternScreenPanel.add(betweenStopsPanel);
        
        //Create panel with between times and every x frequency - this is bascially full of spinners.
        JPanel timesPanel = new JPanel(new GridBagLayout());
        timesPanel.setBackground(Color.WHITE);
        //From + times.
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(fromLabel);
        final JSpinner fromHourSpinner = new JSpinner(new SpinnerNumberModel(6,0,23,1));
        if ( journeyPatternModel != null ) { fromHourSpinner.setValue(journeyPatternModel.getStartTime().get(Calendar.HOUR_OF_DAY)); }
        fromHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(fromHourSpinner);
        final JSpinner fromMinuteSpinner = new JSpinner(new SpinnerNumberModel(0,0,59,1));
        if ( journeyPatternModel != null ) { fromMinuteSpinner.setValue(journeyPatternModel.getStartTime().get(Calendar.MINUTE)); }
        fromMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(fromMinuteSpinner);
        //To + times.
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(toLabel);
        final JSpinner toHourSpinner = new JSpinner(new SpinnerNumberModel(18,0,23,1));
        if ( journeyPatternModel != null ) { toHourSpinner.setValue(journeyPatternModel.getEndTime().get(Calendar.HOUR_OF_DAY)); }
        toHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(toHourSpinner);
        final JSpinner toMinuteSpinner = new JSpinner(new SpinnerNumberModel(30,0,59,1));
        if ( journeyPatternModel != null ) { toMinuteSpinner.setValue(journeyPatternModel.getEndTime().get(Calendar.MINUTE)); }
        toMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(toMinuteSpinner);
        //Every.
        JLabel everyLabel = new JLabel("Every: ");
        everyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(everyLabel);
        int min = 10;
        if ( journeyPatternModel != null ) {
            min = journeyPatternModel.getFrequency();
        }
        if ( min > getCurrentRouteDuration(1) ) { min = getCurrentRouteDuration(1); }
        everyMinuteModel = new SpinnerNumberModel(min,1,getMaxRouteDuration(),1);
        everyMinuteSpinner = new JSpinner(everyMinuteModel);
        //Initialise minVehicles label here but then actually place it later.
        final JLabel minVehicleLabel = new JLabel("NOTE: " + getMinVehicles() + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!" );
        everyMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        everyMinuteSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged ( ChangeEvent e ) {
                minVehicleLabel.setText("NOTE: " + getMinVehicles() + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!");
            }
        });
        timesPanel.add(everyMinuteSpinner);
        JLabel minutesLabel = new JLabel("minutes");
        minutesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(minutesLabel);
        
        journeyPatternScreenPanel.add(timesPanel);
        
        //Create panel to state vehicles required to maintain frequency.
        JPanel minVehiclePanel = new JPanel(new GridBagLayout());
        minVehiclePanel.setBackground(Color.WHITE);
        
        minVehicleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        minVehiclePanel.add(minVehicleLabel);
        journeyPatternScreenPanel.add(minVehiclePanel);
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        final GameModel gameModel = controllerHandler.getGameController().getGameModel();
        
        //Create new journey pattern button and add it to screen panel.
        createJourneyPatternButton = new JButton("Create Journey Pattern");
        if ( journeyPatternModel != null ) { createJourneyPatternButton.setText("Modify Journey Pattern"); }
        else { createJourneyPatternButton.setEnabled(false); }
        createJourneyPatternButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Create a linked list of days selected.
                List<Integer> operatingDays = new ArrayList<Integer>();
                for ( int i = 0; i < daysBox.length; i++ ) {
                    if ( daysBox[i].isSelected() ) {
                        operatingDays.add(i+1);
                    }
                }
                //Create time from.
                GregorianCalendar timeFrom = new GregorianCalendar(2009,7,3,Integer.parseInt(fromHourSpinner.getValue().toString()), Integer.parseInt(fromMinuteSpinner.getValue().toString()));
                //Create time to.
                GregorianCalendar timeTo = new GregorianCalendar(2009,7,3,Integer.parseInt(toHourSpinner.getValue().toString()), Integer.parseInt(toMinuteSpinner.getValue().toString()));
                //Create + add journey pattern.
                if ( journeyPatternModel != null ) {
                    //If editing, delete old journey pattern model before readding it.
                    controllerHandler.getJourneyPatternController().deleteJourneyPattern(journeyPatternModel.getName(), journeyPatternModel.getTimetableName(), journeyPatternModel.getRouteNumber());
                }
                logger.debug("I am calling add method with timetable name " + timetableModel.getName() + "!");
                controllerHandler.getJourneyPatternController().createJourneyPattern(journeyPatternNameField.getText(), operatingDays,
            			terminus1Box.getSelectedItem().toString(), terminus2Box.getSelectedItem().toString(), timeFrom,
            			timeTo, Integer.parseInt(everyMinuteSpinner.getValue().toString()),
            			getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())),
            			timetableModel, routeModel.getRouteNumber());
                //Now return to the timetable screen.
                TimetablePanel myTimetablePanel = new TimetablePanel(controllerHandler);
                RoutePanel myRoutePanel = new RoutePanel(controllerHandler);
                controlScreen.redrawManagement(myTimetablePanel.createPanel(timetableModel, routeModel, controlScreen, myRoutePanel, displayPanel), gameModel);
            }
        });
        bottomButtonPanel.add(createJourneyPatternButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Return to the timetable screen.
                TimetablePanel myTimetablePanel = new TimetablePanel(controllerHandler);
                RoutePanel myRoutePanel = new RoutePanel(controllerHandler);
                controlScreen.redrawManagement(myTimetablePanel.createPanel(timetableModel, routeModel, controlScreen, myRoutePanel, displayPanel), gameModel);
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Add bottom button panel to the screen panel.
        journeyPatternScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return journeyPatternScreenPanel;
	}
	
private int getCurrentRouteDuration ( int frequency ) {
        
    	final GameModel gameModel = controllerHandler.getGameController().getGameModel();
    	//So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 first of all - this is guaranteed.
        cumDistance += controllerHandler.getJourneyController().getDistance(gameModel.getScenarioName(), terminus1Box.getSelectedItem().toString(), terminus2Box.getItemAt(0).toString());
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = terminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            int myDistance = (cumDistance*2);
            int myCumFreq = (frequency*2);
            logger.debug("Distance was " + myDistance);
            while ( myCumFreq < myDistance ) {
                myCumFreq += (frequency*2);
            }
            logger.debug("Actual distance is " + myCumFreq);
            return myCumFreq;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += controllerHandler.getJourneyController().getDistance(gameModel.getScenarioName(), terminus2Box.getItemAt(i-1).toString(), terminus2Box.getItemAt(i).toString());
        }
        //Return distance * 2.
        int myDistance = (cumDistance*2);
        int myCumFreq = (frequency*2);
        logger.debug("Distance was " + myDistance);
        while ( myCumFreq < myDistance ) {
            myCumFreq += (frequency*2);
        }
        logger.debug("Actual distance is " + myCumFreq);
        return myCumFreq;
    }

	private int getMinVehicles ( ) {
		return (int) Math.ceil((double) getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())) / Double.parseDouble(everyMinuteSpinner.getValue().toString()) );
	}

	private int getMaxRouteDuration ( ) {
    	final GameModel gameModel = controllerHandler.getGameController().getGameModel();
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 first of all - this is guaranteed.
        cumDistance += controllerHandler.getJourneyController().getDistance(gameModel.getScenarioName(), terminus1Box.getSelectedItem().toString(), terminus2Box.getItemAt(0).toString());
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = terminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            return cumDistance*2;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += controllerHandler.getJourneyController().getDistance(gameModel.getScenarioName(), terminus2Box.getItemAt(i-1).toString(), terminus2Box.getItemAt(i).toString());
        }
        //Return distance * 2.
        return cumDistance*2;
    }
    
	
}
