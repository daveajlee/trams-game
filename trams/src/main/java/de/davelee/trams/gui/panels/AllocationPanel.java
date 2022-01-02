package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.controllers.ControllerHandler;

import de.davelee.trams.gui.ControlScreen;

/**
 * This class represents a panel to allow the user to allocate vehicles to tours.
 * @author Dave Lee
 */
public class AllocationPanel {
	
	private ControllerHandler controllerHandler;
	private DefaultListModel allocationsModel;
	private JList allocationsList;

    /**
     * Create a new allocation panel with access to all Controllers to get or send data where needed.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
    public AllocationPanel ( final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Create a new allocation panel and display it to the user.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param displayPanel a <code>DisplayPanel</code> object which should be displayed to the user.
     * @param company a <code>String</code> with the name of the company that the user is playing.
     * @param playerName a <code>String</code> with the name of the player.
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final ControlScreen controlScreen, final DisplayPanel displayPanel, final String company,
                                final String playerName) {
		//Create allocation screen panel to add things to.
        JPanel allocationScreenPanel = new JPanel();
        allocationScreenPanel.setLayout ( new BoxLayout ( allocationScreenPanel, BoxLayout.PAGE_AXIS ) );
        allocationScreenPanel.setBackground(Color.WHITE);
        
        final CompanyResponse companyResponse = controllerHandler.getGameController().getGameModel(company, playerName);

        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        //Here, we have the "Vehicle Allocation Screen" and scenario name box.
        JPanel topRightPanel = new JPanel(new GridLayout(2,1,5,5));
        topRightPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Vehicle Allocation Screen", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topRightPanel.add(topLabel);
        JPanel dayPanel = new JPanel();
        dayPanel.setBackground(Color.WHITE);
        JLabel dayLabel = new JLabel(companyResponse.getTime(), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20));
        dayPanel.add(dayLabel);
        topRightPanel.add(dayPanel);
        //Add topRight panel to topLabel panel and topLabel panel to screenPanel.
        topLabelPanel.add(topRightPanel, BorderLayout.CENTER);
        allocationScreenPanel.add(topLabelPanel);

        //Create panel for route information.
        JPanel routeInfoPanel = new JPanel(new FlowLayout());
        routeInfoPanel.setBackground(Color.WHITE);

        //Create panel for route label.
        JPanel routeLabelPanel = new JPanel();
        routeLabelPanel.setBackground(Color.WHITE);

        //Add the route label panel to the screen panel.
        allocationScreenPanel.add(routeLabelPanel);

        //Create list of routes and vehicles created so far with scrollpane.
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(Color.WHITE);
        //First part of label panel is route heading.
        JPanel routesLabelPanel = new JPanel(new GridBagLayout());
        routesLabelPanel.setBackground(Color.WHITE);
        JLabel routesLabel = new JLabel("Route Detail(s):");
        routesLabel.setFont(new Font("Arial", Font.BOLD, 25));
        routesLabelPanel.add(routesLabel);
        labelPanel.add(routesLabelPanel, BorderLayout.WEST);
        //Second part of label panel is route heading.
        JLabel vehiclesLabel = new JLabel("Vehicle(s):", SwingConstants.CENTER);
        vehiclesLabel.setFont(new Font("Arial", Font.BOLD, 25));
        labelPanel.add(vehiclesLabel, BorderLayout.EAST);

        //Add label panel to screen panel.
        allocationScreenPanel.add(labelPanel);

        //Create lists of routes and vehicles with scrollpane.
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        //Third part of route panel is list of routes.
        JPanel routeModelPanel = new JPanel();
        routeModelPanel.setBackground(Color.WHITE);
        final DefaultListModel routesModel = new DefaultListModel();
        final JList routesList = new JList(routesModel);
        routesList.setFixedCellWidth(270);
        routesList.setFont(new Font("Arial", Font.PLAIN, 15));
        RouteResponse[] routeModels = controllerHandler.getRouteController().getRoutes(companyResponse.getName());
        for ( int i = 0; i < routeModels.length; i++ ) {
            routesModel.addElement(routeModels[i].getRouteNumber());
        }
        routesList.setVisibleRowCount(4);
        routesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane routesPane = new JScrollPane(routesList);
        routeModelPanel.add(routesPane);
        listPanel.add(routeModelPanel, BorderLayout.WEST);
        //Third part of vehicle panel is vehicle list.
        JPanel modelPanel = new JPanel();
        modelPanel.setBackground(Color.WHITE);
        final DefaultListModel vehiclesModel = new DefaultListModel();
        VehicleResponse[] vehicleModels = controllerHandler.getVehicleController().getAllCreatedVehicles(companyResponse.getName());
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( vehicleModels[i].getAllocatedTour() != null ) {
                vehiclesModel.addElement(vehicleModels[i].getAdditionalTypeInformationMap().get("Registration Number")
                        + " (" + vehicleModels[i].getModelName() + ")");
            }
        }
        final JList vehiclesList = new JList(vehiclesModel);
        vehiclesList.setFixedCellWidth(320);
        vehiclesList.setVisibleRowCount(4);
        vehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehiclesList.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane vehiclesPane = new JScrollPane(vehiclesList);
        modelPanel.add(vehiclesPane);
        listPanel.add(modelPanel, BorderLayout.EAST);

        //Add list panel to screen panel.
        allocationScreenPanel.add(listPanel);

        //Allocate button panel with allocate and deallocate buttons.
        JPanel allocateButtonPanel = new JPanel();
        allocateButtonPanel.setBackground(Color.WHITE);
        JButton allocateButton = new JButton("Allocate");
        allocateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( routesList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select a route before you can assign a vehicle to it!", "ERROR: No Route Selected", JOptionPane.ERROR_MESSAGE);
                }
                else if ( vehiclesList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select a vehicle before you can assign a route to it!", "ERROR: No Vehicle Selected", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    allocationsModel.addElement(routesList.getSelectedValue() + " & " + vehiclesList.getSelectedValue().toString().split(" ")[0]);
                    routesModel.removeElement(routesList.getSelectedValue());
                    vehiclesModel.removeElement(vehiclesList.getSelectedValue());
                }
            }
        });
        allocateButtonPanel.add(allocateButton);
        JButton deAllocateButton = new JButton("Deallocate");
        deAllocateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( allocationsList.getSelectedValue() == null ) {
                    JOptionPane.showMessageDialog(null, "You must select an allocation before you can remove it!", "ERROR: No Allocation Selected", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String text = allocationsList.getSelectedValue().toString();
                    allocationsModel.removeElement(allocationsList.getSelectedValue());
                    String[] textParts = text.split("&");
                    routesModel.addElement(textParts[0].trim());
                    VehicleResponse vehicleModel = controllerHandler.getVehicleController().getVehicleByRegistrationNumber(textParts[1].trim(), companyResponse.getName());
                    vehiclesModel.addElement(vehicleModel.getAdditionalTypeInformationMap().get("Registration Number") +
                    		" (" + vehicleModel.getModelName() + ")");
                    //Remove this from the interface as well.
                    /*String routeNumber = textParts[0].split("/")[0]; int routeDetailPos = -1;
                    for ( int k = 0; k < theInterface.getRoute(routeNumber).getNumRouteSchedules(); k++ ) {
                        if ( theInterface.getRoute(routeNumber).getRouteSchedule(k).toString().equalsIgnoreCase(textParts[0].trim()) ) {
                            routeDetailPos = k;
                        }
                    }*/
                    //Find vehicle object position.
                    int vehiclePos = -1;
                    VehicleResponse[] vehicleModels = controllerHandler.getVehicleController().getAllCreatedVehicles(companyResponse.getName());
                    for ( int j = 0; j < vehicleModels.length; j++ ) {
                        if ( vehicleModels[j].getAdditionalTypeInformationMap().get("Registration Number").equalsIgnoreCase(textParts[1].trim())) {
                            vehiclePos = j;
                        }
                    }
                    //TODO: Set route and route schedule number.
                    controllerHandler.getVehicleController().assignVehicleToTour(vehicleModels[vehiclePos].getAdditionalTypeInformationMap().get("Registration Number"), "0/0", companyResponse.getName());
                }
            }
        });
        allocateButtonPanel.add(deAllocateButton);

        //Add allocate button panel to screen panel.
        allocationScreenPanel.add(allocateButtonPanel);

        //Allocation label panel is "Allocation(s)".
        JPanel allocationsPanel = new JPanel();
        allocationsPanel.setBackground(Color.WHITE);
        JLabel allocationsLabel = new JLabel("Allocation(s):", SwingConstants.CENTER);
        allocationsLabel.setFont(new Font("Arial", Font.BOLD, 25));
        allocationsPanel.add(allocationsLabel, BorderLayout.EAST);

        //Add allocation label panel to screen panel.
        allocationScreenPanel.add(allocationsPanel);

        //Finally the allocation list.
        JPanel allocationListPanel = new JPanel();
        allocationListPanel.setBackground(Color.WHITE);
        allocationsModel = new DefaultListModel();
        List<String> allocations;
        allocations = controllerHandler.getVehicleController().getAllocations(companyResponse.getName());
        for ( int i = 0; i < allocations.size(); i++ ) {
            allocationsModel.addElement(allocations.get(i).toString());
            //For each allocation, remove route and vehicle from list.
            String[] allocateSplit = allocations.get(i).toString().split("&");
            routesModel.removeElement(allocateSplit[0].trim());
            vehiclesModel.removeElement(allocateSplit[1].substring(1,allocateSplit[1].length()));
        }
        /*LinkedList<Vehicle> vehicles = theInterface.getVehicles();
        Collections.sort(vehicles, new SortedVehicles());
        for ( int i = 0; i < vehicles.size(); i++ ) {
            theVehiclesModel.addElement(vehicles.get(i).toString());
        }*/
        allocationsList = new JList(allocationsModel);
        allocationsList.setFixedCellWidth(250);
        allocationsList.setVisibleRowCount(4);
        allocationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allocationsList.setFont(new Font("Arial", Font.PLAIN, 15));
        JScrollPane allocationsPane = new JScrollPane(allocationsList);
        allocationListPanel.add(allocationsPane);

        //Add allocation list panel to screen panel.
        allocationScreenPanel.add(allocationListPanel);

        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);

        //Create save allocations button and add it to screen panel.
        JButton saveAllocationsButton = new JButton("Save Allocations");
        saveAllocationsButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int result = JOptionPane.YES_OPTION;
                if ( !routesModel.isEmpty() ) {
                    result = JOptionPane.showConfirmDialog(null, "There are some route schedules which do not have vehicles. If you do not assign vehicles to these routes, your passenger satisfaction will decrease! Do you want to continue?", "WARNING: Some routes are unallocated!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                }
                if ( result == JOptionPane.YES_OPTION ) {
                    //Save vehicle positions - then set other ones to null!
                    ArrayList<Integer> vehiclePoses = new ArrayList<Integer>();
                    //Do requested allocations.
                    for ( int i = 0; i < allocationsModel.size(); i++ ) {
                        //Separate route and vehicle data.
                        String[] allocationSplit = allocationsModel.get(i).toString().split("&");
                        //Store route detail object.
                        String routeNumber = allocationSplit[0].split("/")[0]; int routeDetailPos = -1;
                        VehicleResponse[] vehicleModels = controllerHandler.getVehicleController().getVehicleModelsForRoute(companyResponse.getName(), controllerHandler.getRouteController().getRoute(routeNumber, companyResponse.getName()).getRouteNumber());
                        for ( int k = 0; k < vehicleModels.length; k++ ) {
                            if ( vehicleModels[k].getAllocatedTour().contentEquals(allocationSplit[0]) ) {
                                routeDetailPos = k;
                            }
                        }
                        //Find vehicle object position.
                        int vehiclePos = -1;
                        for ( int j = 0; j < vehicleModels.length; j++ ) {
                            if ( vehicleModels[j].getAdditionalTypeInformationMap().get("Registration Number").equalsIgnoreCase(allocationSplit[1].trim())) {
                                vehiclePos = j;
                                vehiclePoses.add(vehiclePos);
                            }
                        }
                        //Now assign route detail to vehicle.
                        controllerHandler.getVehicleController().assignVehicleToTour(vehicleModels[vehiclePos].getAdditionalTypeInformationMap().get("Registration Number"), vehicleModels[routeDetailPos].getAllocatedTour(), companyResponse.getName());
                    }
                    //Now return to previous screen.
                    controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), companyResponse);
                }
            }
        });
        bottomButtonPanel.add(saveAllocationsButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Now return to previous screen.
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), companyResponse);
            }
        });
        bottomButtonPanel.add(previousScreenButton);

        //Add bottom button panel to the screen panel.
        allocationScreenPanel.add(bottomButtonPanel);

        //Return allocationScreenPanel.
        return allocationScreenPanel;
	}

}
