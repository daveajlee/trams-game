package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.RouteScheduleController;
import de.davelee.trams.controllers.VehicleController;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.VehicleModel;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Class to represent the screen which displays vehicle info in the TraMS program.
 * @author Dave Lee
 */
public class BusInfoScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Autowired
    private GameController gameController;

    @Autowired
    private RouteScheduleController routeScheduleController;

    @Autowired
    private VehicleController vehicleController;
    
    /**
     * Create a new bus information screen.
     * @param routeScheduleModel a <code>RouteScheduleModel</code> object with the current route schedule being run by the vehicle.
     */
    public BusInfoScreen ( final RouteScheduleModel routeScheduleModel ) {

        //Retrieve vehicle model.
        VehicleModel vehicleModel = vehicleController.getVehicleByRouteNumberAndRouteScheduleNumber(routeScheduleModel.getRouteNumber(), "" + routeScheduleModel.getScheduleNumber());

        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(BusInfoScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Vehicle Information Screen");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        
        //Call dispose method if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
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
        ImageDisplay busDisplay = new ImageDisplay(vehicleModel.getImagePath(),0,0);
        busDisplay.setSize(220,200);
        busDisplay.setBackground(Color.WHITE);
        westPanel.add(busDisplay);
        screenPanel.add(westPanel, BorderLayout.WEST);

        GameModel gameModel = gameController.getGameModel();

        //Add current status panel to screen panel.
        screenPanel.add(createCurrentStatusPanel(routeScheduleModel, vehicleModel, gameModel), BorderLayout.CENTER);
        
        //Create south panel - two buttons - make contact and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Make Contact button.
        JButton makeContactButton = new JButton("Make Contact");
        makeContactButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MakeContactScreen(routeScheduleModel);
                dispose();
            }
        });
        southPanel.add(makeContactButton);
        //Close button.
        JButton closeButton = new JButton("Close Window");
        closeButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

    public JPanel createCurrentStatusPanel ( final RouteScheduleModel routeScheduleModel, final VehicleModel vehicleModel, final GameModel gameModel ) {
        JPanel eastPanel = new JPanel(new GridLayout(6,1,5,5));
        eastPanel.setBackground(Color.WHITE);
        //Timetable id.
        JLabel timetableIDLabel = new JLabel("Timetable ID: " + routeScheduleModel.getScheduleNumber());
        timetableIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(timetableIDLabel);
        //Vehicle id.
        JLabel vehicleIDLabel = new JLabel("Vehicle ID: " + vehicleModel.getRegistrationNumber());
        vehicleIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(vehicleIDLabel);
        //Location.
        JLabel locationLabel = new JLabel("Location: " + routeScheduleController.getCurrentStopName(routeScheduleModel, gameModel.getCurrentDateTime(), gameModel.getDifficultyLevel()));
        locationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(locationLabel);
        //Destination.
        JLabel destinationLabel = new JLabel("Destination: " + routeScheduleController.getLastStopName(routeScheduleModel, gameModel.getCurrentDateTime(), gameModel.getDifficultyLevel()));
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(destinationLabel);
        //Delay.
        JLabel delayLabel = new JLabel("Delay: " + routeScheduleModel.getDelay() + " mins");
        delayLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(delayLabel);
        return eastPanel;
    }
    
}
