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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.gui.ControlScreen;

import de.davelee.trams.controllers.ControllerHandler;

public class RoutePanel {

    private ControllerHandler controllerHandler;
    private DefaultListModel availableStopModel;
	private DefaultListModel routeStopModel;
	private JButton createRouteButton;
	private JTextField routeNumberField;

    public RoutePanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel (final RouteResponse routeResponse, final ControlScreen controlScreen, final ManagementPanel displayPanel ) {
        
        //Create routeScreen panel to add things to.
        JPanel routeScreenPanel = new JPanel();
        routeScreenPanel.setLayout ( new BoxLayout ( routeScreenPanel, BoxLayout.PAGE_AXIS ) );
        routeScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Route", SwingConstants.CENTER);
        if ( routeResponse != null ) { topLabel.setText("Amend Route"); }
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
        if ( routeResponse != null ) { routeNumberField.setText(routeResponse.getRouteNumber()); }
        routeNumberField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                enableCreateButtons();
            }
            public void keyTyped(KeyEvent e) { /*Nothing happens when key is typed */ }
            public void keyPressed(KeyEvent e) { /*Nothing happens when key is pressed */ }
        });
        routeNumberPanel.add(routeNumberField);
        
        //Add the routeNumber panel to the screen panel.
        routeScreenPanel.add(routeNumberPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Now create stops - a 1 x 3 grid layout.
        JPanel stopGridPanel = new JPanel(new GridLayout(1,3,5,5));
        stopGridPanel.setBackground(Color.WHITE);

        //Create the route stop panel.
        routeStopModel = new DefaultListModel();
        if ( routeResponse != null ) {
            //TODO: Add each stop served by this route in a drop down.
            List<String> currentStopNames = /*routeResponse.getStopNames()*/ new ArrayList<>();
            for ( int i = 0; i < currentStopNames.size(); i++ ) {
                routeStopModel.addElement(currentStopNames.get(i));
            }
        }
        CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
        String[] stopNames = convertToStopNames(controllerHandler.getScenarioController().getScenario(companyResponse.getScenarioName()).getStopDistances());
        availableStopModel = new DefaultListModel();
        for ( int i = 0; i < stopNames.length; i++ ) {
            availableStopModel.addElement(stopNames[i]);
        }
        final JList availableStopList = new JList(availableStopModel);
        final JList routeStopList = new JList(routeStopModel);
        final JPanel routeStopListPanel = new JPanel(new BorderLayout());
        routeStopListPanel.setBackground(Color.WHITE);
        JLabel routeStopListLabel = new JLabel("Route Stops:");
        routeStopListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        routeStopListLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 14));
        routeStopListPanel.add(routeStopListLabel, BorderLayout.NORTH);
        final JScrollPane routeScrollPane = new JScrollPane(routeStopList);
        routeStopList.setVisibleRowCount(5);
        routeStopList.setFont(new Font("Arial", Font.PLAIN, 12));
        routeStopListPanel.add(routeScrollPane, BorderLayout.SOUTH);
        stopGridPanel.add(routeStopListPanel);
        //Create the buttons panel.
        final JPanel stopButtonPanel = new JPanel(new BorderLayout());
        stopButtonPanel.setBackground(Color.WHITE);
        final JButton routeAddStopButton = new JButton("<<");
        routeAddStopButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                String stopName = availableStopList.getSelectedValue().toString();
                routeStopModel.addElement(stopName);
                availableStopModel.removeElement(stopName);
                enableCreateButtons();
            }
        });
        routeAddStopButton.setHorizontalAlignment(SwingConstants.CENTER);
        stopButtonPanel.add(routeAddStopButton, BorderLayout.NORTH);
        final JButton routeRemoveStopButton = new JButton(">>");
        routeRemoveStopButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                String stopName = routeStopList.getSelectedValue().toString();
                availableStopModel.addElement(stopName);
                routeStopModel.removeElement(stopName);
                enableCreateButtons();
            }
        });
        routeRemoveStopButton.setHorizontalAlignment(SwingConstants.CENTER);
        stopButtonPanel.add(routeRemoveStopButton, BorderLayout.SOUTH);
        stopGridPanel.add(stopButtonPanel);
        final JPanel availableStopListPanel = new JPanel(new BorderLayout());
        availableStopListPanel.setBackground(Color.WHITE);
        JLabel availableStopListLabel = new JLabel("Available Stops:");
        availableStopListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        availableStopListLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 14));
        availableStopListPanel.add(availableStopListLabel, BorderLayout.NORTH);
        final JScrollPane availableScrollPane = new JScrollPane(availableStopList);
        availableStopList.setVisibleRowCount(5);
        availableStopList.setFont(new Font("Arial", Font.PLAIN, 12));
        availableStopListPanel.add(availableScrollPane, BorderLayout.SOUTH);
        stopGridPanel.add(availableStopListPanel);
        
        //Add the stopGrid panel to the screen panel.
        routeScreenPanel.add(stopGridPanel);
        routeScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        createRouteButton = new JButton("Create Route");
        createRouteButton.setEnabled(false);
        createRouteButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
               List<String> selectedOutwardStops = new ArrayList<String>();
               for ( int i = 0; i < routeStopModel.size(); i++ ) {
                    selectedOutwardStops.add(routeStopModel.getElementAt(i).toString());
               }
               controllerHandler.getRouteController().addNewRoute( routeNumberField.getText(), selectedOutwardStops, companyResponse.getName());
               RouteResponse routeResponse1 = controllerHandler.getRouteController().getRoute(routeNumberField.getText(), companyResponse.getName());
               //Now move to timetable screen.
               TimetablePanel myTimetablePanel = new TimetablePanel(controllerHandler);
               controlScreen.redrawManagement(myTimetablePanel.createPanel(routeResponse1, controlScreen, RoutePanel.this, displayPanel), companyResponse);
            }
        });
        bottomButtonPanel.add(createRouteButton);
        
        //Create new route button and add it to screen panel.
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), companyResponse);
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
        if ( routeStopModel.getSize() > 1 && !routeNumberField.getText().equalsIgnoreCase("") ) {
            createRouteButton.setEnabled(true);
        }
    }

    private String[] convertToStopNames ( final List<String> stopDistances ) {
        String[] stopNames = new String[stopDistances.size()];
        for ( int i = 0; i < stopDistances.size(); i++ ) {
            stopNames[i] = stopDistances.get(i).split(":")[0];
        }
        return stopNames;
    }

}
