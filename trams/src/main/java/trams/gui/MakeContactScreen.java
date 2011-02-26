package trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import trams.data.*;
import trams.main.*;

/**
 * This class represents the contact screen between vehicles and the control centre in the TraMS program.
 * @author Dave
 */
public class MakeContactScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private ImageDisplay theBusDisplay;
    private JTextArea theCommunicationArea;
    private JLabel theStopLabel;
    private JComboBox theStopBox;
    private JButton theShortenRouteButton;
    private JButton theGoOutOfServiceButton;
    private JButton theCloseButton;
    //Stored route detail.
    private RouteSchedule theRouteDetail;
    
    private static final int DIMENSION_SPACER = 10;
    private static final int SMALL_FONT_SIZE = 12;
    
    /**
     * Create a new make contact screen.
     * @param ui a <code>UserInterface</code> object.
     * @param rd a <code>RouteSchedule</code> object.
     */
    public MakeContactScreen ( UserInterface ui, RouteSchedule rd ) {
        
        //Initialise user interface variable.
        theInterface = ui;
        
        //Initialise route detail variables.
        theRouteDetail = rd;
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(MakeContactScreen.class.getResource("/trams/images/TraMSlogo.png"));
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Contact With " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + " On Route " + rd.toString());
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        theInterface.setFrame ( this );
        
        //Call dispose method if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                theInterface.resumeSimulation();
                dispose();
            }
        });
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        c.setBackground(Color.WHITE);
        c.add(Box.createRigidArea(new Dimension(0,DIMENSION_SPACER))); //Spacer.
        
        //Create screen panel to add things to.
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout ( new BorderLayout () );
        screenPanel.setBackground(Color.WHITE);
        
        //Create panel for west - picture of bus.
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(Color.WHITE);
        theBusDisplay = new ImageDisplay(theInterface.getAllocatedVehicle(theRouteDetail.toString()).getImageFileName(),0,0);
        theBusDisplay.setSize(210,190);
        theBusDisplay.setBackground(Color.WHITE);
        westPanel.add(theBusDisplay, BorderLayout.CENTER);
        //Button panel.
        JPanel alterButtonPanel = new JPanel(new GridLayout(3,1,5,5));
        alterButtonPanel.setBackground(Color.WHITE);
        //Whilst not strictly a button, it is very important to have stop choice here.
        JPanel stopPanel = new JPanel();
        stopPanel.setBackground(Color.WHITE);
        theStopLabel = new JLabel("Stop");
        theStopLabel.setFont(new Font("Arial", Font.BOLD, SMALL_FONT_SIZE));
        stopPanel.add(theStopLabel);
        theStopBox = new JComboBox(getListOfStops());
        stopPanel.add(theStopBox);
        alterButtonPanel.add(stopPanel);
        theShortenRouteButton = new JButton("Terminate At Stop");
        theShortenRouteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Acknowledge control message!
                theCommunicationArea.setText(theCommunicationArea.getText() 
                        + "\n\n Control: Vehicle " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + ", please terminate at " + theStopBox.getSelectedItem().toString() + " and proceed in service in the reverse direction. Over!"
                        + "\n\n Vehicle " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + ": Message acknowledeged. Thanks. Over!");
                //Ask vehicle to shorten current route to the specified destination.
                theInterface.getAllocatedVehicle(theRouteDetail.toString()).shortenSchedule(theStopBox.getSelectedItem().toString(), theInterface.getSimulator().getCurrentSimTime());
            }
        });
        alterButtonPanel.add(theShortenRouteButton);
        theGoOutOfServiceButton = new JButton("Out of Service Until Stop");
        theGoOutOfServiceButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theCommunicationArea.setText(theCommunicationArea.getText() 
                        + "\n\n Control: Vehicle " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + ", please go out of service until " + theStopBox.getSelectedItem().toString() + ". Over!" 
                        + "\n\n Vehicle " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + ": Message acknowledeged. Thanks. Over!");
                //Request vehicle to go out of service.
                theInterface.getAllocatedVehicle(theRouteDetail.toString()).outOfService(theRouteDetail.getCurrentStop(theInterface.getSimulator().getCurrentSimTime(), theInterface.getSimulator())[0], theStopBox.getSelectedItem().toString(), theInterface.getSimulator().getCurrentSimTime());
            }
        });
        alterButtonPanel.add(theGoOutOfServiceButton);
        westPanel.add(alterButtonPanel, BorderLayout.SOUTH);
        screenPanel.add(westPanel, BorderLayout.WEST);
        
        //Create panel for east - description of current status.
        JPanel eastPanel = new JPanel(new GridLayout(1,1,5,5));
        eastPanel.setBackground(Color.WHITE);
        //Communication Area.
        theCommunicationArea = new JTextArea(3,5);
        theCommunicationArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theCommunicationArea.setText("Control: Vehicle " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + ", please state your current position. Over!");
        theCommunicationArea.setText(theCommunicationArea.getText() + "\n\n Vehicle " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber() + ": At " + theRouteDetail.getCurrentStop(theInterface.getSimulator().getCurrentSimTime(), theInterface.getSimulator())[0] + " heading towards " + getCurrentDestination() + " with delay of " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getDelay() + " mins. Over!");
        theCommunicationArea.setFont(new Font("Arial", Font.ITALIC, SMALL_FONT_SIZE));
        theCommunicationArea.setEditable(false);
        theCommunicationArea.setLineWrap(true);
        theCommunicationArea.setWrapStyleWord(true);
        JScrollPane theCommunicationPane = new JScrollPane(theCommunicationArea);
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
        theCloseButton = new JButton("End Contact");
        theCloseButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theInterface.resumeSimulation();
                dispose();
            }
        });
        southPanel.add(theCloseButton);
        
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
        return theRouteDetail.getCurrentService(theInterface.getSimulator().getCurrentSimTime()).getEndDestination();
    }
    
    /**
     * Get the list of stops of the vehicle.
     * @return a <code>String</code> array of stops.
     */
    public String[] getListOfStops() {
        //Create the String array.
        String[] stops = new String[theRouteDetail.getCurrentService(theInterface.getSimulator().getCurrentSimTime()).getNumStops()];
        for ( int i = 0; i < stops.length; i++ ) {
            stops[i] = theRouteDetail.getCurrentService(theInterface.getSimulator().getCurrentSimTime()).getStop(i).getStopName();
        }
        return stops;
    }
    
}
