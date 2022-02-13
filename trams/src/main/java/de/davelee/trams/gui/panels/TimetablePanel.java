package de.davelee.trams.gui.panels;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.StopResponse;
import de.davelee.trams.controllers.ControllerHandler;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.util.GuiUtils;

/**
 * This class represents a panel to show a timetable for a particular route.
 * @author Dave Lee
 */
public class TimetablePanel {

    private final ControllerHandler controllerHandler;

    private JCheckBox[] daysBox;
    private SpinnerNumberModel everyMinuteModel;
    private JSpinner everyMinuteSpinner;

    private DefaultListModel<String> availableStopsModel;
    private DefaultListModel<String> servedStopsModel;

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
            //Weekdays should be enabled.
            daysBox[i].setSelected(i > 0 && i < 6);
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
        //Save the stop names to a variable.
        StopResponse[] stopResponses = controllerHandler.getStopController().getAllStops(controlScreen.getCompany());
        Map<String, StopResponse> stopMap = new HashMap<>();
        for ( StopResponse stopResponse : stopResponses ) {
            stopMap.put(stopResponse.getName(), stopResponse);
        }
        servedStopsModel = new DefaultListModel<>();
        int min = getCurrentRouteDuration(stopMap);
        everyMinuteModel = new SpinnerNumberModel(min,0,120,1);
        everyMinuteSpinner = new JSpinner(everyMinuteModel);
        //Initialise minVehicles label here but then actually place it later.
        final JLabel minVehicleLabel = new JLabel("NOTE: " + getMinVehicles(stopMap) + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!" );
        everyMinuteSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        everyMinuteSpinner.addChangeListener(e -> minVehicleLabel.setText("NOTE: " + getMinVehicles(stopMap) + " vehicles are required to operate " + everyMinuteSpinner.getValue().toString() + " minute frequency!"));
        timesPanel.add(everyMinuteSpinner);
        JLabel minutesLabel = new JLabel("minutes");
        minutesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timesPanel.add(minutesLabel);

        timetableScreenPanel.add(timesPanel);

        //Create panel with between stops.
        JPanel stopsPanel = new JPanel(new GridBagLayout());
        stopsPanel.setBackground(Color.WHITE);
        //List of stops.
        availableStopsModel = new DefaultListModel<>();
        JList<String> servedStopsList = new JList<>(servedStopsModel);
        JList<String> availableStopsList = new JList<>(availableStopsModel);
        for ( StopResponse stopResponse : stopResponses ) {
            availableStopsModel.addElement(stopResponse.getName());
        }
        JScrollPane availableScrollPane = new JScrollPane();
        availableScrollPane.setViewportView(availableStopsList);
        availableStopsList.setLayoutOrientation(JList.VERTICAL);
        stopsPanel.add(availableScrollPane);
        //Add & Remove Buttons.
        JPanel addRemoveButtonPanel = new JPanel(new GridLayout(2,1,5,5));
        addRemoveButtonPanel.setBackground(Color.WHITE);
        //Add button
        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");
        JButton upButton = new JButton("Up");
        JButton downButton = new JButton("Down");
        JButton generateTimetableButton = new JButton("Generate Timetable");
        addButton.setFont(new Font("Arial", Font.ITALIC, 16));
        addButton.addActionListener(e -> {
            servedStopsModel.addElement(availableStopsList.getSelectedValue());
            availableStopsModel.removeElement(availableStopsList.getSelectedValue());
            removeButton.setEnabled(servedStopsModel.size() > 0);
            upButton.setEnabled(servedStopsModel.size() > 1);
            downButton.setEnabled(servedStopsModel.size() > 1);
            generateTimetableButton.setEnabled(servedStopsModel.size()>1);
            if ( servedStopsModel.size() > 1 ) {
                everyMinuteSpinner.setValue(Math. round(getCurrentRouteDuration(stopMap)/10.0) * 10);
            }
        });
        addRemoveButtonPanel.add(addButton);
        //Remove button
        removeButton.setFont(new Font("Arial", Font.ITALIC, 16));
        removeButton.setEnabled(false);
        removeButton.addActionListener(e -> {
            availableStopsModel.addElement(servedStopsList.getSelectedValue());
            servedStopsModel.removeElement(servedStopsList.getSelectedValue());
            removeButton.setEnabled(servedStopsModel.size() > 0);
            upButton.setEnabled(servedStopsModel.size() > 1);
            downButton.setEnabled(servedStopsModel.size() > 1);
            generateTimetableButton.setEnabled(servedStopsModel.size()>1);
        });
        addRemoveButtonPanel.add(removeButton);
        stopsPanel.add(addRemoveButtonPanel);
        //List of stops to be served.
        JScrollPane servedScrollPane = new JScrollPane();
        servedScrollPane.setViewportView(servedStopsList);
        servedStopsList.setLayoutOrientation(JList.VERTICAL);
        stopsPanel.add(servedScrollPane);

        //Up & Down Buttons.
        JPanel upDownButtonPanel = new JPanel(new GridLayout(2,1,5,5));
        upDownButtonPanel.setBackground(Color.WHITE);
        //Up button
        upButton.setFont(new Font("Arial", Font.ITALIC, 16));
        upButton.setEnabled(false);
        upButton.addActionListener(e -> {
            int posToMove = servedStopsList.getSelectedIndex();
            if ( posToMove != 0 ) {
                String stopToMoveUp = servedStopsList.getSelectedValue();
                String stopToMoveDown = servedStopsModel.getElementAt(posToMove - 1);
                servedStopsModel.removeElement(stopToMoveUp);
                servedStopsModel.removeElement(stopToMoveDown);
                servedStopsModel.insertElementAt(stopToMoveUp, posToMove - 1);
                servedStopsModel.insertElementAt(stopToMoveDown, posToMove);
            }
        });
        upDownButtonPanel.add(upButton);
        //Down button
        downButton.setFont(new Font("Arial", Font.ITALIC, 16));
        downButton.setEnabled(false);
        downButton.addActionListener(e -> {
            int posToMove = servedStopsList.getSelectedIndex();
            System.out.println("PosToMove is: " + posToMove);
            if ( posToMove != servedStopsModel.size()-1 ) {
                String stopToMoveUp = servedStopsModel.getElementAt(posToMove + 1);
                String stopToMoveDown = servedStopsList.getSelectedValue();
                servedStopsModel.removeElement(stopToMoveUp);
                servedStopsModel.removeElement(stopToMoveDown);
                servedStopsModel.insertElementAt(stopToMoveUp, posToMove);
                servedStopsModel.insertElementAt(stopToMoveDown, posToMove + 1);
            }
        });
        upDownButtonPanel.add(downButton);
        stopsPanel.add(upDownButtonPanel);
        //Add betweenStopsPanel.
        timetableScreenPanel.add(stopsPanel);

        //Create panel to state vehicles required to maintain frequency.
        JPanel minVehiclePanel = new JPanel(new GridBagLayout());
        minVehiclePanel.setBackground(Color.WHITE);
        minVehicleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        minVehiclePanel.add(minVehicleLabel);
        timetableScreenPanel.add(minVehiclePanel);
                
        //Create panel for validity.
        JPanel validityPanel = new JPanel(new GridBagLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromLabel = new JLabel("Valid From: ", SwingConstants.CENTER);
        validFromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validFromLabel);
        //Valid From Day.
        final LocalDate currentDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
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
        LocalDate monthNames = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
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
        LocalDate defaultValidToDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
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
        generateTimetableButton.setEnabled(servedStopsModel.size()>1);
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
                //These should be based on the servedStopModel and the waiting time + distance to next stop.
                int[] waitingTimes = new int[stopResponses.length];
                int[] distances = new int[stopResponses.length - 1];
                controllerHandler.getStopTimeController().generateStopTimes(companyResponse.getName(), waitingTimes,
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

    private int getMinVehicles ( final Map<String, StopResponse> stopMap ) {
        return (int) Math.ceil((double) getCurrentRouteDuration(stopMap) / Double.parseDouble(everyMinuteSpinner.getValue().toString()) );
    }

    private int getCurrentRouteDuration ( final Map<String, StopResponse> stopMap ) {
        int duration = 0;
        for ( int i = 0; i < servedStopsModel.size()-1; i++) {
            duration += stopMap.get(servedStopsModel.getElementAt(i)).getDistances().get(servedStopsModel.getElementAt(i+1));
        }
        return duration*2;
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

}
