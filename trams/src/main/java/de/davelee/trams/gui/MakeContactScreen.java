package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.davelee.trams.main.UserInterface;
import de.davelee.trams.services.JourneyService;
import de.davelee.trams.services.RouteScheduleService;


/**
 * This class represents the contact screen between vehicles and the control centre in the TraMS program.
 * @author Dave
 */
public class MakeContactScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface userInterface;
    private ImageDisplay busDisplay;
    private JTextArea communicationArea;
    private JLabel stopLabel;
    private JComboBox stopBox;
    private JButton shortenRouteButton;
    private JButton goOutOfServiceButton;
    private JButton closeButton;
    
    private long routeScheduleId;
    
    private RouteScheduleService routeScheduleService;
    private JourneyService journeyService;

    /**
     * Create a new make contact screen.
     * @param ui a <code>UserInterface</code> object.
     * @param rd a <code>RouteSchedule</code> object.
     */
    public MakeContactScreen ( UserInterface ui, final long routeScheduleId ) {
        
        //Initialise user interface variable.
        userInterface = ui;
        
        //Initialise route detail variables.
        this.routeScheduleId = routeScheduleId;
        
        routeScheduleService = new RouteScheduleService();
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(MakeContactScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Contact With " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + " On Route " + routeScheduleService.getRouteScheduleById(routeScheduleId).getRouteNumber());
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        userInterface.setFrame ( this );
        
        //Call dispose method if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                userInterface.resumeSimulation();
                dispose();
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        c.setBackground(Color.WHITE);
        c.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create screen panel to add things to.
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout ( new BorderLayout () );
        screenPanel.setBackground(Color.WHITE);
        
        //Create panel for west - picture of bus.
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(Color.WHITE);
        busDisplay = new ImageDisplay(userInterface.getAllocatedVehicleImage(routeScheduleId),0,0);
        busDisplay.setSize(210,190);
        busDisplay.setBackground(Color.WHITE);
        westPanel.add(busDisplay, BorderLayout.CENTER);
        //Button panel.
        JPanel alterButtonPanel = new JPanel(new GridLayout(3,1,5,5));
        alterButtonPanel.setBackground(Color.WHITE);
        //Whilst not strictly a button, it is very important to have stop choice here.
        JPanel stopPanel = new JPanel();
        stopPanel.setBackground(Color.WHITE);
        stopLabel = new JLabel("Stop");
        stopLabel.setFont(new Font("Arial", Font.BOLD, 12));
        stopPanel.add(stopLabel);
        stopBox = new JComboBox(getListOfStops());
        stopPanel.add(stopBox);
        alterButtonPanel.add(stopPanel);
        shortenRouteButton = new JButton("Terminate At Stop");
        shortenRouteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Acknowledge control message!
                communicationArea.setText(communicationArea.getText() + 
                        "\n\n Control: Vehicle " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + ", please terminate at " + stopBox.getSelectedItem().toString() + " and proceed in service in the reverse direction. Over!" +
                        "\n\n Vehicle " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + ": Message acknowledeged. Thanks. Over!");
                //Ask vehicle to shorten current route to the specified destination.
                userInterface.shortenSchedule(routeScheduleId, stopBox.getSelectedItem().toString(), userInterface.getCurrentSimTime());
            }
        });
        alterButtonPanel.add(shortenRouteButton);
        goOutOfServiceButton = new JButton("Out of Service Until Stop");
        goOutOfServiceButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                communicationArea.setText(communicationArea.getText() + 
                        "\n\n Control: Vehicle " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + ", please go out of service until " + stopBox.getSelectedItem().toString() + ". Over!" +
                        "\n\n Vehicle " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + ": Message acknowledeged. Thanks. Over!");
                //Request vehicle to go out of service.
                userInterface.outOfService(routeScheduleId, userInterface.getCurrentStopName(routeScheduleId, userInterface.getCurrentSimTime(), userInterface.getDifficultyLevel()), stopBox.getSelectedItem().toString(), userInterface.getCurrentSimTime());
            }
        });
        alterButtonPanel.add(goOutOfServiceButton);
        westPanel.add(alterButtonPanel, BorderLayout.SOUTH);
        screenPanel.add(westPanel, BorderLayout.WEST);
        
        //Create panel for east - description of current status.
        JPanel eastPanel = new JPanel(new GridLayout(1,1,5,5));
        eastPanel.setBackground(Color.WHITE);
        //Communication Area.
        communicationArea = new JTextArea(3,5);
        communicationArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        communicationArea.setText("Control: Vehicle " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + ", please state your current position. Over!");
        communicationArea.setText(communicationArea.getText() + "\n\n Vehicle " + userInterface.getAllocatedRegistrationNumber(routeScheduleId) + ": At " + userInterface.getCurrentStopName(routeScheduleId, userInterface.getCurrentSimTime(), userInterface.getDifficultyLevel()) + " heading towards " + getCurrentDestination() + " with delay of " + routeScheduleService.getDelay(routeScheduleId) + " mins. Over!");
        communicationArea.setFont(new Font("Arial", Font.ITALIC, 12));
        communicationArea.setEditable(false);
        communicationArea.setLineWrap(true);
        communicationArea.setWrapStyleWord(true);
        JScrollPane theCommunicationPane = new JScrollPane(communicationArea);
        theCommunicationPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        theCommunicationPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //theCommunicationPane.add(theCommunicationArea);
        eastPanel.add(theCommunicationPane);
        //Add east panel to screen panel.
        screenPanel.add(eastPanel, BorderLayout.CENTER);
        
        //Create south panel - two buttons - make contact and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Close button.
        closeButton = new JButton("End Contact");
        closeButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userInterface.resumeSimulation();
                dispose();
            }
        });
        southPanel.add(closeButton);
        
        //Add south panel to screen panel.
        screenPanel.add(southPanel, BorderLayout.SOUTH);
        
        //Add panel to container.
        c.add(screenPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(400,300);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(400,350) );
        
    }
    
    /**
     * Get the current destination of the vehicle.
     * @return a <code>String</code> with the current destination.
     */
    public String getCurrentDestination ( ) {
        return userInterface.getLastStopName(routeScheduleId, userInterface.getCurrentSimTime(), userInterface.getDifficultyLevel());
    }
    
    /**
     * Get the list of stops of the vehicle.
     * @return a <code>String</code> array of stops.
     */
    public String[] getListOfStops() {
        //Create the String array.
        String[] stops = new String[journeyService.getCurrentJourney(routeScheduleService.getRouteScheduleById(routeScheduleId).getJourneyList(), userInterface.getCurrentSimTime()).getJourneyStops().size()];
        for ( int i = 0; i < stops.length; i++ ) {
            stops[i] = journeyService.getCurrentJourney(routeScheduleService.getRouteScheduleById(routeScheduleId).getJourneyList(), userInterface.getCurrentSimTime()).getJourneyStops().get(i).getStopName();
        }
        return stops;
    }
    
}
