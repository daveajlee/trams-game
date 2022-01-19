package de.davelee.trams.gui.panels;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.StopTimeResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.util.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davelee.trams.gui.ControlScreen;

/**
 * This class represents a panel to show a particular timetable for a particular route for a particular company.
 * @author Dave Lee
 */
public class ViewTimetablePanel {

    private final ControllerHandler controllerHandler;
    private final JTable myTable = new JTable();
	private JComboBox<Direction> directionSelectionBox;
	
	private static final Logger logger = LoggerFactory.getLogger(ViewTimetablePanel.class);

    /**
     * Create a new <code>ViewTimetablePanel</code> with access to all Controllers to get or send data where needed.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
    public ViewTimetablePanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Create a new <code>ViewTimetablePanel</code> panel and display it to the user.
     * @param route a <code>String</code> object containing the route to show timetables for.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param managementPanel a <code>ManagementPanel</code> object which is the management panel that has been displayed to the user (for back button functionality).
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final String route, final ControlScreen controlScreen, final ManagementPanel managementPanel ) {
        
        //Create screen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout( new BoxLayout(routeScreenPanel, BoxLayout.PAGE_AXIS));
        routeScreenPanel.setBackground(Color.WHITE);
     
        final RouteResponse initialRouteResponse = controllerHandler.getRouteController().getRoute(route, controlScreen.getCompany());
        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
            
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
        final DefaultComboBoxModel<String> routeSelectionModel = new DefaultComboBoxModel<>();
        RouteResponse[] routeResponses = controllerHandler.getRouteController().getRoutes(companyResponse.getName()).getRouteResponses();
        for (RouteResponse model : routeResponses) {
            routeSelectionModel.addElement(model.getRouteNumber());
        }
        final JComboBox<String> routeSelectionBox = new JComboBox<>(routeSelectionModel);
        routeSelectionBox.addActionListener (e -> {
            if ( routeSelectionBox.getSelectedItem() != null ) {
                controlScreen.redrawManagement(createPanel(routeSelectionBox.getSelectedItem().toString(), controlScreen, managementPanel), companyResponse);
            }
        });
        routeSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        selectionPanel.add(routeSelectionBox);
        //Choose stop.
        JLabel stopSelectionLabel = new JLabel("Stop:");
        stopSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(stopSelectionLabel);
        final DefaultComboBoxModel<String> stopSelectionModel = new DefaultComboBoxModel<>();
        //TODO: Add a list of stop names served by this route.
        stopSelectionModel.addElement("");
        final JComboBox<String> stopSelectionBox = new JComboBox<>(stopSelectionModel);
        stopSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        stopSelectionBox.addActionListener (e -> {
            if ( stopSelectionBox.getSelectedItem() != null ) {
                logger.debug("You chose stop " + stopSelectionBox.getSelectedItem().toString());
                myTable.setModel(createTableModel(initialRouteResponse.getRouteNumber(), stopSelectionBox.getSelectedItem().toString(), (Direction) directionSelectionBox.getSelectedItem(), companyResponse.getTime(), companyResponse.getName()));
                autoResizeColWidth(myTable, (DefaultTableModel) myTable.getModel());
            }
            });
        selectionPanel.add(stopSelectionBox);
        //Choose direction.
        JLabel directionSelectionLabel = new JLabel("Direction:");
        directionSelectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectionPanel.add(directionSelectionLabel);
        final DefaultComboBoxModel<Direction> directionSelectionModel = new DefaultComboBoxModel<>();
        directionSelectionModel.addElement(Direction.OUTGOING);
        directionSelectionModel.addElement(Direction.RETURN);
        directionSelectionBox = new JComboBox<>(directionSelectionModel);
        directionSelectionBox.setFont(new Font("Arial", Font.PLAIN, 15));
        directionSelectionBox.addActionListener(e -> {
            if ( directionSelectionBox.getSelectedItem() != null && stopSelectionBox.getSelectedItem() != null ) {
                logger.debug("You chose direction " + directionSelectionBox.getSelectedIndex());
                myTable.setModel(createTableModel(initialRouteResponse.getRouteNumber(), stopSelectionBox.getSelectedItem().toString(), (Direction) directionSelectionBox.getSelectedItem(), companyResponse.getTime(), companyResponse.getName()));
                autoResizeColWidth(myTable, (DefaultTableModel) myTable.getModel());
            }
            });
        selectionPanel.add(directionSelectionBox);
        //Add to top panel.
        topPanel.add(selectionPanel, BorderLayout.NORTH);
        //Show valid information.
        JPanel validityPanel = new JPanel(new BorderLayout());
        validityPanel.setBackground(Color.WHITE);
        StopTimeResponse[] stopTimeModels = controllerHandler.getStopTimeController().getStopTimes(Optional.empty(), initialRouteResponse.getRouteNumber(), companyResponse.getTime(), companyResponse.getName(), Optional.empty());
        JLabel validFromDateLabel = new JLabel("Valid From: " + stopTimeModels[0].getValidFromDate());
        validFromDateLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        validityPanel.add(validFromDateLabel, BorderLayout.NORTH);
        JLabel validToDateLabel = new JLabel("Valid To: " + stopTimeModels[0].getValidToDate());
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
        if ( stopSelectionBox.getSelectedItem() != null ) {
            myTable.setModel(createTableModel(initialRouteResponse.getRouteNumber(), stopSelectionBox.getSelectedItem().toString(), Direction.OUTGOING, companyResponse.getTime(), companyResponse.getName()));
        }
        myTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane tableScrollPane = new JScrollPane(autoResizeColWidth(myTable, (DefaultTableModel) myTable.getModel()));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        routeScreenPanel.add(tablePanel);
            
        //Create two buttons for previous and next.
        JPanel otherServicesButtonPanel = new JPanel();
        otherServicesButtonPanel.setBackground(Color.WHITE);
        JButton amendRouteButton = new JButton("Amend Route");
        amendRouteButton.addActionListener(e -> {
            //Show the actual screen!
            RoutePanel routePanel = new RoutePanel(controllerHandler);
            controlScreen.redrawManagement(routePanel.createPanel(initialRouteResponse, controlScreen, managementPanel), companyResponse);
        });
        otherServicesButtonPanel.add(amendRouteButton);
        JButton managementScreenButton = new JButton("Back to Management Screen");
        managementScreenButton.addActionListener(e -> controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse));
        otherServicesButtonPanel.add(managementScreenButton);
        routeScreenPanel.add(otherServicesButtonPanel);
            
        overallScreenPanel.add(routeScreenPanel, BorderLayout.CENTER);
            
        return overallScreenPanel;
	}


	private DefaultTableModel createTableModel ( final String routeNumber, final String stopName, final Direction direction,
                                                 final String date, final String company) {
        String[] columnNames = new String[] { "", "Monday - Friday", "Saturday", "Sunday" };
        String[][] data = new String[24][4];
        //TODO: Add multiple route schedules.
        StopTimeResponse[] stopTimeResponses = controllerHandler.getStopTimeController().getStopTimes(Optional.of(direction), routeNumber, date, company, Optional.empty() );
        for ( StopTimeResponse stopTimeResponse : stopTimeResponses ) {
            try {
                String[] timeSplit = stopTimeResponse.getDepartureTime().split(":");
                int displayPos = 1;
                data[Integer.parseInt(timeSplit[0])][displayPos] = stopTimeResponse.getDepartureTime();
                if ( data[Integer.parseInt(timeSplit[0])][displayPos] == null ) {
                    data[Integer.parseInt(timeSplit[0])][displayPos] = "" + timeSplit[1];
                } else {
                    data[Integer.parseInt(timeSplit[0])][displayPos] = data[Integer.parseInt(timeSplit[0])][displayPos] + " " + timeSplit[1];
                }
            } catch (NoSuchElementException ex) {
                logger.debug("No stop time found for " + stopTimeResponse + " and stop name " + stopName);
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
 	 * @param table a <code>JTable</code> object to adjust the width of.
 	 * @param model a <code>DefaultTableModel</code> object containing the content of the table.
 	 * @return a <code>JTable</code> with the adjusted width.
 	 */
     private JTable autoResizeColWidth(final JTable table, final DefaultTableModel model) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setModel(model);
        int margin = 5;

        for (int i = 0; i < table.getColumnCount(); i++) {
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn(i);
            int width;

            // Get width of column header
            TableCellRenderer renderer = col.getHeaderRenderer();

            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            width = comp.getPreferredSize().width;

            // Get maximum width of column data
            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, i);
                comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, i), false, false,
                        r, i);
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
