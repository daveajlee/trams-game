package de.davelee.trams.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Display the loading screen in the TraMS program.
 * @author Dave Lee
 */
public class LoadingScreen extends JFrame implements Runnable {
    
	private static final long serialVersionUID = 1L;
	
	private JLabel loadingLabel;
    private JLabel pleaseWaitLabel;
    
    /**
     * Create a new loading screen.
     */
    public LoadingScreen ( ) {
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        this.setUndecorated(true);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(WelcomeScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        c.setBackground(Color.WHITE);
        c.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create one JPanel with border layout.
        JPanel layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.setBackground(Color.WHITE);
        
        //Construct loading label.
        loadingLabel = new JLabel("Loading ", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 40));
        layoutPanel.add(loadingLabel, BorderLayout.NORTH);
        
        //Construct please wait label.
        pleaseWaitLabel = new JLabel("Please Wait...", SwingConstants.CENTER);
        pleaseWaitLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 35));
        layoutPanel.add(pleaseWaitLabel, BorderLayout.SOUTH);
        
        //Add layout panel to container.
        c.add(layoutPanel, BorderLayout.CENTER);
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = new Dimension(350,150);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setSize ( new Dimension(350,150) );
        
    }
    
    /** 
     * Run the loading screen.
     */
    public void run ( ) {
        this.setVisible (true);
    }
}
