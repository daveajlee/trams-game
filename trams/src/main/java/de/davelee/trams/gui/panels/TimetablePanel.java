package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.time.*;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.StopResponse;
import de.davelee.trams.controllers.ControllerHandler;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.util.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a panel to show a timetable for a particular route.
 * @author Dave Lee
 */
public class TimetablePanel {

    private final ControllerHandler controllerHandler;

    private JCheckBox[] daysBox;
    private SpinnerNumberModel everyMinuteModel;
    private JSpinner everyMinuteSpinner;
    private JComboBox<String> terminus1Box;
    private JComboBox<String> terminus2Box;

    private final Logger logger = LoggerFactory.getLogger(TimetablePanel.class);

    /**
     * Create a new <code>TimetablePanel</code> with access to all Controllers to get or send data where needed.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
    public TimetablePanel (final ControllerHandler controllerHandler) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Create a new <code>TimetablePanel</code> panel and display it to the user.
     * @param routeResponse a <code>RouteResponse</code> object containing the data for the specified route.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param managementPanel a <code>ManagementPanel</code> object which is the management panel that has been displayed to the user (for back button functionality).
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel (final RouteResponse routeResponse, final ControlScreen controlScreen, final ManagementPanel managementPanel ) {
        
        //Create timetableScreen panel to add things to.
        JPanel timetableScreenPanel = GuiUtils.createBoxPanel();
        
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

        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());

        //Create panel with between times and every x frequency - this is basically full of spinners.
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
        if ( min > getCurrentRouteDuration(1, companyResponse.getScenarioName()) ) { min = getCurrentRouteDuration(1, companyResponse.getScenarioName()); }
        everyMinuteModel = new SpinnerNumberModel(min,1,getMaxRouteDuration(companyResponse.getScenarioName()),1);
        everyMinuteSpinner = new JSpinner(everyMinuteModel);
        //Initialise minVehicles label here but then actually place it later.
        final JLabel minVehicleLabel = new JLabel("NOTE: " + getMinVehicles(companyResponse.getScenarioName()) + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!" );
        everyMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        everyMinuteSpinner.addChangeListener(e -> minVehicleLabel.setText("NOTE: " + getMinVehicles(companyResponse.getScenarioName()) + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!"));
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
        //TODO: Add a list of the names of stops served by this route.
        StopResponse[] stopResponses = controllerHandler.getStopController().getAllStops(controlScreen.getCompany());
        //Terminus 1 Combo box.
        terminus1Box = new JComboBox<>();
        for ( int i = 0; i < stopResponses.length-1; i++ ) {
            terminus1Box.addItem(stopResponses[i].getName());
        }
        terminus1Box.setSelectedIndex(0);
        terminus1Box.setFont(new Font("Arial", Font.PLAIN, 14));
        terminus1Box.addItemListener(e -> {
            //Update terminus 2 box!!!
            terminus2Box.removeAllItems();
            for ( StopResponse stopResponse : stopResponses ) {
                terminus2Box.addItem(stopResponse.getName());
            }
            //Update spinner!
            everyMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString()), companyResponse.getScenarioName()));
        });
        betweenStopsPanel.add(terminus1Box);
        //And label.
        JLabel andLabel = new JLabel("  and  ");
        andLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        betweenStopsPanel.add(andLabel);
        //Terminus 2 Combo box.
        terminus2Box = new JComboBox<>();
        for ( int i = 1; i < stopResponses.length; i++ ) {
            terminus2Box.addItem(stopResponses[i].getName());
        }
        terminus2Box.setSelectedIndex(terminus2Box.getItemCount() - 1);
        terminus2Box.setFont(new Font("Arial", Font.PLAIN, 14));
        terminus2Box.addItemListener(e -> {
            //Update spinner!
            if ( terminus2Box.getItemCount() != 0 ) {
                everyMinuteModel.setMaximum(getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString()), companyResponse.getScenarioName()));
            }
        });
        betweenStopsPanel.add(terminus2Box);
        //Add betweenStopsPanel.
        timetableScreenPanel.add(betweenStopsPanel);
                
        //Create panel for validity.
        JPanel validityPanel = new JPanel(new GridBagLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromLabel = new JLabel("Valid From: ", SwingConstants.CENTER);
        validFromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validFromLabel);
        //Valid From Day.
        final LocalDate currentDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        final int fromStartDay = currentDate.getDayOfMonth();
        final DefaultComboBoxModel<Integer> validFromDayModel = new DefaultComboBoxModel<>();
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonthValue());
        for ( int i = currentDate.getDayOfMonth(); i <= yearMonth.lengthOfMonth(); i++ ) {
            validFromDayModel.addElement(i);
        }
        JComboBox<Integer> validFromDayBox = new JComboBox<>(validFromDayModel);
        validFromDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validFromDayBox);
        //Valid From Month.
        final JComboBox<String> validFromMonthBox = new JComboBox<>();
        LocalDate monthNames = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        for ( int i = 0; i < 4; i++ ) {
            validFromMonthBox.addItem(monthNames.getMonth() + " " + monthNames.getYear());
            monthNames = monthNames.plusMonths(1);
        }
        validFromMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validFromMonthBox.addActionListener(e -> processMonth(validFromMonthBox, validFromDayModel, fromStartDay));
        validityPanel.add(validFromMonthBox);
        //Valid to!!!
        JLabel validToLabel = new JLabel("Valid To: ", SwingConstants.CENTER);
        validToLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validToLabel);
        //Get the local date object with current date.
        LocalDate defaultValidToDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        defaultValidToDate = defaultValidToDate.plusDays(3);
        //Valid To Day.
        final int toStartDay = defaultValidToDate.getDayOfMonth();
        final DefaultComboBoxModel<Integer> validToDayModel = new DefaultComboBoxModel<>();
        YearMonth yearMonthValidTo = YearMonth.of(defaultValidToDate.getYear(), defaultValidToDate.getMonthValue());
        for ( int i = toStartDay; i <= yearMonthValidTo.lengthOfMonth(); i++ ) {
            validToDayModel.addElement(i);
        }
        JComboBox<Integer> validToDayBox = new JComboBox<>(validToDayModel);
        validToDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validToDayBox);
        //Valid To Month.
        final JComboBox<String> validToMonthBox = new JComboBox<>();
        for ( int i = 0; i < 25; i++ ) {
            validToMonthBox.addItem(defaultValidToDate.getMonth() + " " + defaultValidToDate.getYear());
            defaultValidToDate = defaultValidToDate.plusMonths(1);
        }
        validToMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validToMonthBox.addActionListener(e -> processMonth(validToMonthBox, validToDayModel, toStartDay));
        validityPanel.add(validToMonthBox);
       
        //Add validityPanel to the screen panel.
        timetableScreenPanel.add(validityPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        JButton generateTimetableButton = new JButton("Generate Timetable");
        generateTimetableButton.addActionListener (e -> {
            if ( validFromMonthBox.getSelectedItem() != null && validToMonthBox.getSelectedItem() != null ) {
                String[] validFromMonthYear = validFromMonthBox.getSelectedItem().toString().split(" ");
                LocalDate validFromDate = LocalDate.of(Integer.parseInt(validFromMonthYear[1]), Month.valueOf(validFromMonthYear[0]).getValue(), validFromDayBox.getSelectedIndex());
                String[] validToMonthYear = validToMonthBox.getSelectedItem().toString().split(" ");
                LocalDate validToDate = LocalDate.of(Integer.parseInt(validToMonthYear[1]), Month.valueOf(validToMonthYear[0]).getValue(), validToDayBox.getSelectedIndex());
                //Create a linked list of days selected.
                String operatingDays = "";
                if (daysBox[0].isSelected()) {
                    operatingDays += DayOfWeek.SUNDAY.name();
                }
                if (daysBox[1].isSelected()) {
                    operatingDays += DayOfWeek.MONDAY.name();
                }
                if (daysBox[2].isSelected()) {
                    operatingDays += DayOfWeek.TUESDAY.name();
                }
                if (daysBox[3].isSelected()) {
                    operatingDays += DayOfWeek.WEDNESDAY.name();
                }
                if (daysBox[4].isSelected()) {
                    operatingDays += DayOfWeek.THURSDAY.name();
                }
                if (daysBox[5].isSelected()) {
                    operatingDays += DayOfWeek.FRIDAY.name();
                }
                if (daysBox[6].isSelected()) {
                    operatingDays += DayOfWeek.SATURDAY.name();
                }
                //Create time from.
                LocalTime timeFrom = LocalTime.of(Integer.parseInt(fromHourSpinner.getValue().toString()), Integer.parseInt(fromMinuteSpinner.getValue().toString()));
                //Create time to.
                LocalTime timeTo = LocalTime.of(Integer.parseInt(toHourSpinner.getValue().toString()), Integer.parseInt(toMinuteSpinner.getValue().toString()));
                //Generate timetable as a series of stop times.
                //TODO: implement the stoppingTimes and distances arrays in the GUI correctly instead of arrays of 0.
                int[] stoppingTimes = new int[stopResponses.length];
                int[] distances = new int[stopResponses.length - 1];
                controllerHandler.getStopTimeController().generateStopTimes(companyResponse.getName(), stoppingTimes,
                        stopResponses, routeResponse.getRouteNumber(), distances, timeFrom.format(DateTimeFormatter.ofPattern("HH:mm")),
                        timeTo.format(DateTimeFormatter.ofPattern("HH:mm")), (Integer) everyMinuteSpinner.getValue(),
                        validFromDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), validToDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        operatingDays);
                //Return to management screen.
                controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse);
            }
        });
        bottomButtonPanel.add(generateTimetableButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener (e -> {
            //Cancel addition.
            RoutePanel routePanel = new RoutePanel(controllerHandler);
            controlScreen.redrawManagement(routePanel.createPanel(routeResponse, controlScreen, managementPanel), companyResponse);
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Add bottom button panel to the screen panel.
        timetableScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return timetableScreenPanel;
	}

    private int getMaxRouteDuration ( final String scenarioName ) {
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 - this is guaranteed.
        if ( terminus1Box.getSelectedItem() != null ) {
            cumDistance += controllerHandler.getStopController().getDistance(scenarioName, terminus1Box.getSelectedItem().toString(), terminus2Box.getItemAt(0));
        }
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = terminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            return cumDistance*2;
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += controllerHandler.getStopController().getDistance(scenarioName, terminus2Box.getItemAt(i-1), terminus2Box.getItemAt(i));
        }
        //Return distance * 2.
        return cumDistance*2;
    }

    private int getMinVehicles ( final String scenarioName ) {
        return (int) Math.ceil((double) getCurrentRouteDuration(Integer.parseInt(everyMinuteSpinner.getValue().toString()), scenarioName) / Double.parseDouble(everyMinuteSpinner.getValue().toString()) );
    }

    private int getCurrentRouteDuration ( final int frequency, final String scenarioName ) {
        //So duration is the distance between selected one in terminus1 and then distance between all ones in terminus2 up to selected item.
        //Note cumulative total.
        int cumDistance = 0;
        //Add distance of terminus1 and first item of terminus2 - this is guaranteed.
        if ( terminus1Box.getSelectedItem() != null ) {
            cumDistance += controllerHandler.getStopController().getDistance(scenarioName, terminus1Box.getSelectedItem().toString(), terminus2Box.getItemAt(0));
        }
        //Now from 0 up until the selected index - add distances for terminus 2.
        int selectIndex = terminus2Box.getSelectedIndex();
        if ( selectIndex == 0 ) {
            return calculateDistance(cumDistance, frequency);
        }
        for ( int i = 1; i <= selectIndex; i++ ) {
            cumDistance += controllerHandler.getStopController().getDistance(scenarioName, terminus2Box.getItemAt(i-1), terminus2Box.getItemAt(i));
        }
        //Return distance * 2.
        return calculateDistance(cumDistance, frequency);
    }

    private void processMonth ( final JComboBox<String> validMonthBox, final DefaultComboBoxModel<Integer> validDayModel, final int startDay) {
        int month = validMonthBox.getSelectedIndex();
        if ( validMonthBox.getSelectedIndex() == 0 ) {
            validDayModel.removeAllElements();
            YearMonth yearMonth1 = YearMonth.of(LocalDate.now().getYear(), month);
            for (int i = startDay; i <= yearMonth1.lengthOfMonth(); i++ ) {
                validDayModel.addElement(i);
            }
        }
        else {
            validDayModel.removeAllElements();
            YearMonth yearMonth1 = YearMonth.of(LocalDate.now().getYear(), month);
            for (int i = 1; i <= yearMonth1.lengthOfMonth(); i++ ) {
                validDayModel.addElement(i);
            }
        }
    }

    private int calculateDistance ( int cumDistance, int frequency ) {
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
