package trams.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import trams.data.*;
import trams.main.*;

/**
 * Class to represent the screen which displays vehicle info in the TraMS program.
 * @author Dave Lee
 */
public class BusInfoScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private ImageDisplay theBusDisplay;
    private JLabel theTimetableIDLabel;
    private JLabel theVehicleIDLabel;
    private JLabel theLocationLabel;
    private JLabel theDestinationLabel;
    private JLabel theDelayLabel;
    private JButton theMakeContactButton;
    private JButton theCloseButton;
    //Stored route detail.
    private RouteSchedule theRouteDetail;
    
    /**
     * Create a new bus information screen.
     * @param ui a <code>UserInterface</code> representing the current user interface.
     * @param rd a <code>RouteSchedule</code> object with the current route schedule being run by the vehicle.
     */
    public BusInfoScreen ( UserInterface ui, RouteSchedule rd ) {
        
        //Initialise user interface variable.
        theInterface = ui;
        
        //Initialise vehicle and route detail variables.
        theRouteDetail = rd;
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/trams/images/TraMSlogo.png");
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Vehicle Information Screen");
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
        c.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create screen panel to add things to.
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout ( new BorderLayout () );
        screenPanel.setBackground(Color.WHITE);
        
        //Create panel for west - picture of bus.
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(Color.WHITE);
        theBusDisplay = new ImageDisplay(theInterface.getAllocatedVehicle(theRouteDetail.toString()).getImageFileName(),0,0);
        theBusDisplay.setSize(220,200);
        theBusDisplay.setBackground(Color.WHITE);
        westPanel.add(theBusDisplay);
        screenPanel.add(westPanel, BorderLayout.WEST);
        
        //Create panel for east - description of current status.
        JPanel eastPanel = new JPanel(new GridLayout(6,1,5,5));
        eastPanel.setBackground(Color.WHITE);
        //Timetable id.
        theTimetableIDLabel = new JLabel("Timetable ID: " + theRouteDetail.toString());
        theTimetableIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(theTimetableIDLabel);
        //Vehicle id.
        theVehicleIDLabel = new JLabel("Vehicle ID: " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getRegistrationNumber());
        theVehicleIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(theVehicleIDLabel);
        //Location.
        theLocationLabel = new JLabel("Location: " + theRouteDetail.getCurrentService(theInterface.getSimulator().getCurrentSimTime()).getCurrentStop(theInterface.getSimulator().getCurrentSimTime())[0]);
        theLocationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(theLocationLabel);
        //Destination.
        theDestinationLabel = new JLabel("Destination: " + theRouteDetail.getCurrentService(theInterface.getSimulator().getCurrentSimTime()).getEndDestination());
        theDestinationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(theDestinationLabel);
        //Delay.
        theDelayLabel = new JLabel("Delay: " + theInterface.getAllocatedVehicle(theRouteDetail.toString()).getDelay() + " mins");
        theDelayLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(theDelayLabel);
        //Add east panel to screen panel.
        screenPanel.add(eastPanel, BorderLayout.CENTER);
        
        //Create south panel - two buttons - make contact and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Make Contact button.
        theMakeContactButton = new JButton("Make Contact");
        theMakeContactButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MakeContactScreen(theInterface, theRouteDetail);
                dispose();
            }
        });
        southPanel.add(theMakeContactButton);
        //Close button.
        theCloseButton = new JButton("Close Window");
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
        this.setSize ( new Dimension(400,300) );
        
    }
    
}
