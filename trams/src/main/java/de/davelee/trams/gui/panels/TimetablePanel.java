package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.davelee.trams.controllers.ControllerHandler;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.RouteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimetablePanel {

    private ControllerHandler controllerHandler;

    private JCheckBox[] daysBox;
    private SpinnerNumberModel everyMinuteModel;
    private JSpinner everyMinuteSpinner;
    private JComboBox terminus1Box;
    private JComboBox terminus2Box;

    private Logger logger = LoggerFactory.getLogger(TimetablePanel.class);

    public TimetablePanel (final ControllerHandler controllerHandler) {
        this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final RouteModel routeModel, final ControlScreen controlScreen, final RoutePanel routePanel, final DisplayPanel displayPanel ) {
		final GameModel gameModel = controllerHandler.getGameController().getGameModel();
        
        //Create timetableScreen panel to add things to.
        JPanel timetableScreenPanel = new JPanel();
        timetableScreenPanel.setLayout ( new BoxLayout ( timetableScreenPanel, BoxLayout.PAGE_AXIS ) );
        timetableScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Timetable", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        timetableScreenPanel.add(topLabelPanel);

        //Create day of week panel with 7 tick boxes.
        JPanel dayOfWeekPanel = new JPanel(new GridBagLayout());
        dayOfWeekPanel.setBackground(Color.WHITE);
        daysBox = new JCheckBox[7];
        String[] dayStr = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        for ( int i = 0; i < daysBox.length; i++ ) {
            daysBox[i] = new JCheckBox(dayStr[i]);
            daysBox[i].setFont(new Font("Arial", Font.PLAIN, 14));
            dayOfWeekPanel.add(daysBox[i]);
        }
        timetableScreenPanel.add(dayOfWeekPanel);

        //Create panel with between times and every x frequency - this is bascially full of spinners.
        JPanel timesPanel = new JPanel(new GridBagLayout());
        timesPanel.setBackground(Color.WHITE);
        //From + times.
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(fromLabel);
        final JSpinner fromHourSpinner = new JSpinner(new SpinnerNumberModel(6,0,23,1));
        fromHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(fromHourSpinner);
        final JSpinner fromMinuteSpinner = new JSpinner(new SpinnerNumberModel(0,0,59,1));
        fromMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(fromMinuteSpinner);
        //To + times.
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(toLabel);
        final JSpinner toHourSpinner = new JSpinner(new SpinnerNumberModel(18,0,23,1));
        toHourSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(toHourSpinner);
        final JSpinner toMinuteSpinner = new JSpinner(new SpinnerNumberModel(30,0,59,1));
        toMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(toMinuteSpinner);
        //Every.
        JLabel everyLabel = new JLabel("Every: ");
        everyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timesPanel.add(everyLabel);
        int min = 10;
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

        timetableScreenPanel.add(timesPanel);

        //Create panel to state vehicles required to maintain frequency.
        JPanel minVehiclePanel = new JPanel(new GridBagLayout());
        minVehiclePanel.setBackground(Color.WHITE);

        minVehicleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        minVehiclePanel.add(minVehicleLabel);
        timetableScreenPanel.add(minVehiclePanel);

        //Create panel with between stops.
        JPanel betweenStopsPanel = new JPanel(new GridBagLayout());
        betweenStopsPanel.setBackground(Color.WHITE);
        //Between label.
        JLabel betweenLabel = new JLabel("Between:");
        betweenLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(betweenLabel);
        //Save the stop names to a variable.
        List<String> stopNames = routeModel.getStopNames();
        //Terminus 1 Combo box.
        terminus1Box = new JComboBox();
        for ( int i = 0; i < stopNames.size()-1; i++ ) {
            terminus1Box.addItem(stopNames.get(i));
        }
        terminus1Box.setSelectedIndex(0);
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
        timetableScreenPanel.add(betweenStopsPanel);
                
        //Create panel for validity first of all.
        JPanel validityPanel = new JPanel(new GridBagLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromLabel = new JLabel("Valid From: ", SwingConstants.CENTER);
        validFromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validFromLabel);
        //Valid From Day.
        final int fromStartDay = gameModel.getCurrentDateTime().getDayOfMonth();
        final DefaultComboBoxModel validFromDayModel = new DefaultComboBoxModel();
        YearMonth yearMonth = YearMonth.of(gameModel.getCurrentDateTime().getYear(), gameModel.getCurrentDateTime().getMonthValue());
        for ( int i = gameModel.getCurrentDateTime().getDayOfMonth(); i <= yearMonth.lengthOfMonth(); i++ ) {
            validFromDayModel.addElement(i);
        }
        JComboBox validFromDayBox = new JComboBox(validFromDayModel);
        validFromDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validFromDayBox);
        //Valid From Month.
        final JComboBox validFromMonthBox = new JComboBox();
        LocalDate monthNames = gameModel.getCurrentDateTime().toLocalDate();
        for ( int i = 0; i < 4; i++ ) {
            validFromMonthBox.addItem(monthNames.getMonth() + " " + monthNames.getYear());
            monthNames.plusMonths(1);
        }
        validFromMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validFromMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int month = validFromMonthBox.getSelectedIndex();
                if ( validFromMonthBox.getSelectedIndex() == 0 ) {
                    validFromDayModel.removeAllElements();
                    YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
                    for ( int i = fromStartDay; i <= yearMonth.lengthOfMonth(); i++ ) {
                        validFromDayModel.addElement(i);
                    }
                }
                else {
                    validFromDayModel.removeAllElements();
                    YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
                    for ( int i = 1; i <= yearMonth.lengthOfMonth(); i++ ) {
                        validFromDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(validFromMonthBox);
        //Valid to!!!
        JLabel validToLabel = new JLabel("Valid To: ", SwingConstants.CENTER);
        validToLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validToLabel);
        //Get the local date object with current date.
        LocalDate defaultValidToDate = gameModel.getCurrentDateTime().toLocalDate();
        defaultValidToDate.plusDays(3);
        //Valid To Day.
        final int toStartDay = defaultValidToDate.getDayOfMonth();
        final DefaultComboBoxModel validToDayModel = new DefaultComboBoxModel();
        YearMonth yearMonthValidTo = YearMonth.of(defaultValidToDate.getYear(), defaultValidToDate.getMonthValue());
        for ( int i = toStartDay; i <= yearMonthValidTo.lengthOfMonth(); i++ ) {
            validToDayModel.addElement(i);
        }
        JComboBox validToDayBox = new JComboBox(validToDayModel);
        validToDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validToDayBox);
        //Valid To Month.
        final JComboBox validToMonthBox = new JComboBox();
        for ( int i = 0; i < 25; i++ ) {
            validToMonthBox.addItem(defaultValidToDate.getMonth() + " " + defaultValidToDate.getYear());
            defaultValidToDate.plusMonths(1);
        }
        validToMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validToMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int month = validToMonthBox.getSelectedIndex();
                if ( validToMonthBox.getSelectedIndex() == 0 ) {
                    validToDayModel.removeAllElements();
                    YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
                    for ( int i = toStartDay; i <= yearMonth.lengthOfMonth(); i++ ) {
                        validToDayModel.addElement(i);
                    }
                }
                else {
                    validToDayModel.removeAllElements();
                    YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
                    for ( int i = 1; i <= yearMonth.lengthOfMonth(); i++ ) {
                        validToDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(validToMonthBox);
       
        //Add validityPanel to the screen panel.
        timetableScreenPanel.add(validityPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        JButton generateTimetableButton = new JButton("Generate Timetable");
        generateTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                String[] validFromMonthYear = validFromMonthBox.getSelectedItem().toString().split(" ");
                LocalDate validFromDate = LocalDate.of(Integer.parseInt(validFromMonthYear[1]), Month.valueOf(validFromMonthYear[0]).getValue(), validFromDayBox.getSelectedIndex());
                String[] validToMonthYear = validToMonthBox.getSelectedItem().toString().split(" ");
                LocalDate validToDate = LocalDate.of(Integer.parseInt(validToMonthYear[1]), Month.valueOf(validToMonthYear[0]).getValue(), validToDayBox.getSelectedIndex());
                //Create a linked list of days selected.
                List<DayOfWeek> operatingDays = new ArrayList<DayOfWeek>();
                if ( daysBox[0].isSelected() ) { operatingDays.add(DayOfWeek.SUNDAY); }
                if ( daysBox[1].isSelected() ) { operatingDays.add(DayOfWeek.MONDAY); }
                if ( daysBox[2].isSelected() ) { operatingDays.add(DayOfWeek.TUESDAY); }
                if ( daysBox[3].isSelected() ) { operatingDays.add(DayOfWeek.WEDNESDAY); }
                if ( daysBox[4].isSelected() ) { operatingDays.add(DayOfWeek.THURSDAY); }
                if ( daysBox[5].isSelected() ) { operatingDays.add(DayOfWeek.FRIDAY); }
                if ( daysBox[6].isSelected() ) { operatingDays.add(DayOfWeek.SATURDAY); }
                //Create time from.
                LocalTime timeFrom = LocalTime.of(Integer.parseInt(fromHourSpinner.getValue().toString()), Integer.parseInt(fromMinuteSpinner.getValue().toString()));
                //Create time to.
                LocalTime timeTo = LocalTime.of(Integer.parseInt(toHourSpinner.getValue().toString()), Integer.parseInt(toMinuteSpinner.getValue().toString()));
                //Generate timetable as a series of stop times.
                controllerHandler.getStopTimeController().generateStopTimes(gameModel.getCompany(), stoppingTimes,
                        stopNames, routeModel.getRouteNumber(), distances, timeFrom, timeTo, (Integer) everyMinuteSpinner.getValue(), validFromDate,
                        validToDate, operatingDays);
                //Return to management screen.
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), gameModel);
            }
        });
        bottomButtonPanel.add(generateTimetableButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Cancel addition.
                controlScreen.redrawManagement(routePanel.createPanel(routeModel, controlScreen, displayPanel), gameModel);
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Add bottom button panel to the screen panel.
        timetableScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return timetableScreenPanel;
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

    private int getMinVehicles ( ) {
        return (int) Math.ceil((double) getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString())) / Double.parseDouble(everyMinuteSpinner.getValue().toString()) );
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

}
