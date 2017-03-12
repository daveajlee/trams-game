package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.davelee.trams.main.UserInterface;


/**
 * Class to represent the screen which displays vehicle info in the TraMS program.
 * @author Dave Lee
 */
public class BusInfoScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//GUI Parameters.
    private ImageDisplay busDisplay;
    private JLabel timetableIDLabel;
    private JLabel vehicleIDLabel;
    private JLabel locationLabel;
    private JLabel destinationLabel;
    private JLabel delayLabel;
    private JButton makeContactButton;
    private JButton closeButton;
    
    private UserInterface userInterface;
    
    
    /**
     * Create a new bus information screen.
     * @param ui a <code>UserInterface</code> representing the current user interface.
     * @param rd a <code>RouteSchedule</code> object with the current route schedule being run by the vehicle.
     */
    public BusInfoScreen ( UserInterface ui, final long routeScheduleId ) {
        
        //Initialise user interface variable.
        userInterface = ui;
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(BusInfoScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Vehicle Information Screen");
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
        busDisplay.setSize(220,200);
        busDisplay.setBackground(Color.WHITE);
        westPanel.add(busDisplay);
        screenPanel.add(westPanel, BorderLayout.WEST);
        
        //Create panel for east - description of current status.
        JPanel eastPanel = new JPanel(new GridLayout(6,1,5,5));
        eastPanel.setBackground(Color.WHITE);
        //Timetable id.
        timetableIDLabel = new JLabel("Timetable ID: " + routeScheduleId);
        timetableIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(timetableIDLabel);
        //Vehicle id.
        vehicleIDLabel = new JLabel("Vehicle ID: " + userInterface.getAllocatedRegistrationNumber(routeScheduleId));
        vehicleIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(vehicleIDLabel);
        //Location.
        locationLabel = new JLabel("Location: " + userInterface.getCurrentStopName(routeScheduleId, userInterface.getCurrentSimTime(), userInterface.getDifficultyLevel()));
        locationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(locationLabel);
        //Destination.
        destinationLabel = new JLabel("Destination: " + userInterface.getLastStopName(routeScheduleId, userInterface.getCurrentSimTime(), userInterface.getDifficultyLevel()));
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(destinationLabel);
        //Delay.
        delayLabel = new JLabel("Delay: " + userInterface.getDelay(routeScheduleId) + " mins");
        delayLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(delayLabel);
        //Add east panel to screen panel.
        screenPanel.add(eastPanel, BorderLayout.CENTER);
        
        //Create south panel - two buttons - make contact and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Make Contact button.
        makeContactButton = new JButton("Make Contact");
        makeContactButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MakeContactScreen(userInterface, routeScheduleId);
                dispose();
            }
        });
        southPanel.add(makeContactButton);
        //Close button.
        closeButton = new JButton("Close Window");
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
        this.setSize ( new Dimension(400,300) );
        
    }
    
}
