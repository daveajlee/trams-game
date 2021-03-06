package de.davelee.trams.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.model.TimetableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.RouteModel;

public class ViewTimetablePanel {

    private ControllerHandler controllerHandler;
    private JTable myTable = new JTable();
	private JComboBox directionSelectionBox;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewTimetablePanel.class);

    public ViewTimetablePanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final String route, final int min, final int dateIndex, final ControlScreen controlScreen, final RoutePanel routePanel, final DisplayPanel displayPanel ) {
        
        //Create screen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout( new BoxLayout(routeScreenPanel, BoxLayout.PAGE_AXIS));
        routeScreenPanel.setBackground(Color.WHITE);
     
        final RouteModel routeModel = controllerHandler.getRouteController().getRoute(route);
        final GameModel gameModel = controllerHandler.getGameController().getGameModel();
            
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
        RouteModel[] routeModels = controllerHandler.getRouteController().getRouteModels();
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
        stopSelectionBox.addActionListener ( new ActionListener()  {
        public void actionPerformed ( ActionEvent e ) {
            logger.debug("You chose stop " + stopSelectionBox.getSelectedItem().toString());
            myTable.setModel(createTableModel(routeModel.getRouteNumber(), stopSelectionBox.getSelectedItem().toString(), directionSelectionBox.getSelectedIndex()));
            autoResizeColWidth(myTable, (DefaultTableModel) myTable.getModel());
            }
        });
        selectionPanel.add(stopSelectionBox);
        //Choose direction.
        JLabel directionSelectionLabel = new JLabel("Direction:");
        directionSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(directionSelectionLabel);
        final DefaultComboBoxModel directionSelectionModel = new DefaultComboBoxModel();
        directionSelectionModel.addElement(routeStopNames.get(routeStopNames.size()-1));
        directionSelectionModel.addElement(routeStopNames.get(0));
        directionSelectionBox = new JComboBox(directionSelectionModel);
        directionSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        directionSelectionBox.addActionListener(new ActionListener() {
        public void actionPerformed ( ActionEvent e ) {
            logger.debug("You chose direction " + directionSelectionBox.getSelectedIndex());
            myTable.setModel(createTableModel(routeModel.getRouteNumber(), stopSelectionBox.getSelectedItem().toString(), directionSelectionBox.getSelectedIndex()));
            autoResizeColWidth(myTable, (DefaultTableModel) myTable.getModel());
            }
        });
        selectionPanel.add(directionSelectionBox);
        //Choose timetable.
        JLabel timetableSelectionLabel = new JLabel("Timetable:");
        timetableSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(timetableSelectionLabel);
        final DefaultComboBoxModel timetableSelectionModel = new DefaultComboBoxModel();
        TimetableModel[] timetableModels = controllerHandler.getTimetableController().getRouteTimetables(routeModel);
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
        JLabel validFromDateLabel = new JLabel("Valid From: " + controllerHandler.getTimetableController().getDateInfo(controllerHandler.getTimetableController().getCurrentTimetable(routeModel, gameModel.getCurrentDateTime().toLocalDate()).getValidFromDate()));
        validFromDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validFromDateLabel, BorderLayout.NORTH);
        JLabel validToDateLabel = new JLabel("Valid To: " + controllerHandler.getTimetableController().getDateInfo(controllerHandler.getTimetableController().getCurrentTimetable(routeModel, gameModel.getCurrentDateTime().toLocalDate()).getValidToDate()));
        validToDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validToDateLabel, BorderLayout.SOUTH);
        topPanel.add(validityPanel, BorderLayout.SOUTH);
        //Add top panel to topLabel panel and topLabel panel to screenPanel.
        topLabelPanel.add(topPanel, BorderLayout.NORTH);
        routeScreenPanel.add(topLabelPanel);
            
        //Process data...
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);

        //Display it!
        myTable.setModel(createTableModel(routeModel.getRouteNumber(), stopSelectionBox.getSelectedItem().toString(), 0));
        myTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane tableScrollPane = new JScrollPane(autoResizeColWidth(myTable, (DefaultTableModel) myTable.getModel()));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

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


	private DefaultTableModel createTableModel ( final String routeNumber, final String stopName, final int direction ) {
        String[] columnNames = new String[] { "", "Monday - Friday", "Saturday", "Sunday" };
        String[][] data = new String[24][4];
        //TODO: Preprocessing necessary?
        //TODO: Add multiple route schedules.
        JourneyModel[] journeyModels = controllerHandler.getJourneyController().getJourneysByRouteScheduleNumberAndRouteNumber(direction, routeNumber);
        for ( int i = 0; i < journeyModels.length; i++ ) {
            try {
                LocalTime myTime = controllerHandler.getJourneyController().getStopTime(journeyModels[i], stopName);
                String minuteStr = "";
                if ( myTime.getMinute() < 10 ) { minuteStr = "0" + myTime.getMinute(); } else { minuteStr = "" + myTime.getMinute(); }
                int displayPos = 1;
                /*if ( myDateTime.getDayOfWeek()==DayOfWeek.SATURDAY ) { displayPos = 2; }
                else if ( myDateTime.getDayOfWeek()==DayOfWeek.SUNDAY ) { displayPos = 3; }*/
                if ( data[myTime.getHour()][displayPos] == null ) {
                    data[myTime.getHour()][displayPos] = "" + minuteStr;
                } else {
                    data[myTime.getHour()][displayPos] = data[myTime.getHour()][displayPos] + " " + minuteStr;
                }
            } catch (NoSuchElementException ex) {
                logger.debug("No stop time found for " + journeyModels[i] + " and stop name " + stopName);
            }
        }
        //Null check.
        for ( int i = 0; i < 24; i++) {
            data[i][0] = "" + i;
            for ( int j = 1; j < 4; j++ ) {
                //Check for nulls and sort them.
                if ( data[i][j] == null ) {
                    data[i][j] = "";
                }
            }
        }
        return new DefaultTableModel(data, columnNames);
    }

    /**
 	 * http://ieatbinary.com/2008/08/13/auto-resize-jtable-column-width/
 	 * @param table
 	 * @param model
 	 * @return
 	 */
     private JTable autoResizeColWidth(JTable table, DefaultTableModel model) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setModel(model);
        int margin = 5;

        for (int i = 0; i < table.getColumnCount(); i++) {
            int vColIndex = i;
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn(vColIndex);
            int width = 0;

            // Get width of column header
            TableCellRenderer renderer = col.getHeaderRenderer();

            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            width = comp.getPreferredSize().width;

            // Get maximum width of column data
            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, vColIndex);
                comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false,
                        r, vColIndex);
                width = Math.max(width, comp.getPreferredSize().width);
            }

            // Add margin
            width += 2 * margin;

            // Set the width
            col.setPreferredWidth(width);
        }

        table.getTableHeader().setReorderingAllowed(false);
        return table;
     }

}
