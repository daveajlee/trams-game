package trams.gui;

//Import the Java GUI packages.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import trams.main.UserInterface;

/**
 * Splash screen for the TraMS program.
 * @author Dave Lee.
 */
public class SplashScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private ImageDisplay theLogoDisplay;
    private ImageDisplay theBusDisplay;
    private UserInterface theInterface;
    private JLabel theTitleLabel;
    private JLabel theLoadingLabel;
    private JLabel theCopyrightLabel;
    
    private static final int DIMENSION_SPACER = 10;
    private static final int COPYRIGHT_FONT_SIZE = 10;
    private static final int LOADING_IMAGE_BORDER = 120;
    
    private boolean isFinished = false;
    private boolean isStarted = false;
    
    /**
     * Create a new splash screen.
     * @param isAboutScreen a <code>boolean</code> which is true iff this is the about screen rather than splash screen at beginning.
     * @param ui a <code>UserInterface</code> object with the current user interface.
     */
    public SplashScreen ( ) {
    	
    }
    
    public void init () {
        
        //Initialise GUI with resizable, title and decorate methods.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (true);
        this.setUndecorated(true);
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/trams/images/TraMSlogo.png"));
        setIconImage(img);
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Construct centre panel with box layout to display all components.
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout ( new BoxLayout ( centrePanel, BoxLayout.PAGE_AXIS ) );
        centrePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1), BorderFactory.createEmptyBorder(5,5,5,5)));
        centrePanel.add(Box.createRigidArea(new Dimension(0,DIMENSION_SPACER))); //Spacer.
        centrePanel.setBackground(Color.WHITE);
        
        //Construct logo panel to add to the centre panel.
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        theLogoDisplay = new ImageDisplay("TraMSlogo.png", 0, 0);
        theLogoDisplay.setSize(157,92);
        theLogoDisplay.setBackground(Color.WHITE);
        logoPanel.add(theLogoDisplay);
        centrePanel.add(logoPanel);
        
        //Construct title panel to add to the centre panel.
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        theTitleLabel = new JLabel("TraMS - Transport Management Simulator");
        theTitleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titlePanel.add(theTitleLabel);
        centrePanel.add(titlePanel);
        
        //Construct graphics panel to add to the centre panel.
        JPanel busPanel = new JPanel();
        busPanel.setBackground(Color.WHITE);
        //if ( isAboutScreen ) { theBusDisplay = new ImageDisplay("abouttransparent.png",60,0); 
    	//} else { theBusDisplay = new ImageDisplay("loadingtransparent.png",LOADING_IMAGE_BORDER,0); }
        theBusDisplay = new ImageDisplay("loadingtransparent.png",LOADING_IMAGE_BORDER,0);
        theBusDisplay.setSize(450,340);
        theBusDisplay.setBackground(Color.WHITE);
        busPanel.add(theBusDisplay);
        centrePanel.add(busPanel);
        
        //Construct loading panel to add to the centre panel.
        JPanel loadingPanel = new JPanel();
        loadingPanel.setBackground(Color.WHITE);
        //if ( isAboutScreen ) { theLoadingLabel = new JLabel("Version: " + theInterface.getVersion());
        //} else { theLoadingLabel = new JLabel("Loading... Please Wait!"); }
        theLoadingLabel = new JLabel("Loading... Please Wait!"); 
        theLoadingLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        loadingPanel.add(theLoadingLabel);
        centrePanel.add(loadingPanel);
        
        //Construct copyright panel to add to the centre panel.
        JPanel copyrightPanel = new JPanel();
        copyrightPanel.setBackground(Color.WHITE);
        theCopyrightLabel = new JLabel("http://trams.sourceforge.net");
        theCopyrightLabel.setFont(new Font("Arial", Font.PLAIN, COPYRIGHT_FONT_SIZE));
        copyrightPanel.add(theCopyrightLabel);
        centrePanel.add(copyrightPanel);
        
        c.add(centrePanel, BorderLayout.CENTER);
        
        //Mouse listeners if this is the about screen.
        //if ( isAboutScreen ) {
            this.getContentPane().addMouseListener ( new MouseListener () {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    if (theInterface != null) { theInterface.resumeSimulation(); }
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
            theBusDisplay.addMouseListener ( new MouseListener () {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    if (theInterface != null) { theInterface.resumeSimulation(); }
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
        //}
        
        //Position the screen at the center of the screen.
        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenDim = tools.getScreenSize();
        Dimension displayDim = getPreferredSize();
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( getPreferredSize() );
        
    }
    
    public void setInterface ( UserInterface ui ) {
    	theInterface = ui;
    }
    
    public void moveImage ( int width, int height ) {
        theBusDisplay.moveImage(width, height);
    }
    
    public int width() {
    	return theBusDisplay.getWidth();
    }
    
    public boolean notFinished ( ) {
    	return !isFinished;
    }
    
    public void setFinished ( ) {
    	isFinished = true;
    }
    
    public void setStarted ( ) {
    	isStarted = true;
    }
    
    public boolean getStarted ( ) {
    	return isStarted;
    }
    
}
