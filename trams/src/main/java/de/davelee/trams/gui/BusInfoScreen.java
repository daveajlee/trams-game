package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.Serial;

import javax.swing.*;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.controllers.CompanyController;
import de.davelee.trams.controllers.VehicleController;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Class to represent the screen which displays vehicle info in the TraMS program.
 * @author Dave Lee
 */
public class BusInfoScreen extends JFrame {

    @Serial
	private static final long serialVersionUID = 1L;

    /**
     * A <code>CompanyController</code> object which allows access to company data.
     */
    @Autowired
    private CompanyController companyController;

    /**
     * A <code>VehicleController</code> object which allows access to vehicle data.
     */
    @Autowired
    private VehicleController vehicleController;
    
    /**
     * Create a new bus information screen.
     * @param allocatedTour a <code>String</code> with the allocated tour currently being run.
     * @param company a <code>String</code> with the company that the tour belongs to.
     * @param playerName a <code>String</code> with the name of the player currently playing the game.
     */
    public BusInfoScreen ( final String allocatedTour, final String company, final String playerName ) {

        //Retrieve vehicle model.
        VehicleResponse vehicleModel = vehicleController.getVehicleByAllocatedTour(allocatedTour, company);

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
        //TODO: Add a mapping from model name to images.
        /*ImageDisplay busDisplay = new ImageDisplay(vehicleModel.getImagePath(),0,0);
        busDisplay.setSize(220,200);
        busDisplay.setBackground(Color.WHITE);
        westPanel.add(busDisplay);*/
        screenPanel.add(westPanel, BorderLayout.WEST);

        CompanyResponse companyResponse = companyController.getCompany(company, playerName);

        //Add current status panel to screen panel.
        screenPanel.add(createCurrentStatusPanel(allocatedTour, vehicleModel, companyResponse), BorderLayout.CENTER);
        
        //Create south panel - two buttons - make contact and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Make Contact button.
        JButton makeContactButton = new JButton("Make Contact");
        makeContactButton.addActionListener(e -> {
            new MakeContactScreen(allocatedTour, company, playerName);
            dispose();
        });
        southPanel.add(makeContactButton);
        //Close button.
        JButton closeButton = new JButton("Close Window");
        closeButton.addActionListener(e -> dispose());
        southPanel.add(closeButton);
        
        //Add south panel to screen panel.
        screenPanel.add(southPanel, BorderLayout.SOUTH);
        
        //Add panel to container.
        c.add(screenPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(400,300);
        this.setLocation (  (screenDim.width/2)-(displayDim.width/2), (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(400,300) );
    }

    /**
     * Create a panel showing the current status to the user.
     * @param allocatedTour a <code>String</code> with the name of the tour being run.
     * @param vehicleResponse a <code>VehicleResponse</code> object with information about the vehicle who's status should be shown.
     * @param companyResponse a <code>CompanyResponse</code> object with information about the company who the vehicle belongs to.
     * @return a <code>JPanel</code> object representing the panel to be shown to the user.
     */
    public JPanel createCurrentStatusPanel ( final String allocatedTour, final VehicleResponse vehicleResponse, final CompanyResponse companyResponse ) {
        JPanel eastPanel = new JPanel(new GridLayout(6,1,5,5));
        eastPanel.setBackground(Color.WHITE);
        //Timetable id.
        JLabel timetableIDLabel = new JLabel("Timetable ID: " + allocatedTour);
        timetableIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(timetableIDLabel);
        //Vehicle id.
        JLabel vehicleIDLabel = new JLabel("Vehicle ID: " + vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"));
        vehicleIDLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(vehicleIDLabel);
        //Location.
        JLabel locationLabel = new JLabel("Location: " + vehicleController.getCurrentStopName(vehicleResponse, companyResponse.getTime(), companyResponse.getDifficultyLevel()));
        locationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(locationLabel);
        //Destination.
        JLabel destinationLabel = new JLabel("Destination: " + vehicleController.getDestination(vehicleResponse, companyResponse.getTime(), DifficultyLevel.valueOf(companyResponse.getDifficultyLevel())));
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(destinationLabel);
        //Delay.
        JLabel delayLabel = new JLabel("Delay: " + vehicleResponse.getDelayInMinutes() + " mins");
        delayLabel.setFont(new Font("Arial", Font.BOLD, 15));
        eastPanel.add(delayLabel);
        return eastPanel;
    }
    
}
