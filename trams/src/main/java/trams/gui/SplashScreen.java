package trams.gui;

//Import the Java GUI packages.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import trams.main.UserInterface;
import trams.main.*;

/**
 * Splash screen for the TraMS program.
 * @author Dave Lee.
 */
public class SplashScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageDisplay theImageDisplay;
    private ImageDisplay theLogoDisplay;
    private ImageDisplay theBusDisplay;
    private UserInterface theInterface;
    private JLabel theTitleLabel;
    private JLabel theLoadingLabel;
    private JLabel theCopyrightLabel;
    
    /**
     * Create a new splash screen.
     * @param isAboutScreen a <code>boolean</code> which is true iff this is the about screen rather than splash screen at beginning.
     * @param ui a <code>UserInterface</code> object with the current user interface.
     */
    public SplashScreen ( boolean isAboutScreen, UserInterface ui ) {
        
        //Initialise GUI with resizable, title and decorate methods.
        this.setTitle ("TraMS - Transport Management Simulator");
        this.setResizable (true);
        this.setUndecorated(true);
        
        //Initialise user interface object.
        theInterface = ui;
        
        //Set image icon.
        //Image img = Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/TraMSlogo.png"));
        Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/trams/images/TraMSlogo.png");
        setIconImage(img);
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Construct centre panel with box layout to display all components.
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout ( new BoxLayout ( centrePanel, BoxLayout.PAGE_AXIS ) );
        centrePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1), BorderFactory.createEmptyBorder(5,5,5,5)));
        centrePanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
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
        if ( isAboutScreen ) { theBusDisplay = new ImageDisplay("abouttransparent.png",60,0); }
        else { theBusDisplay = new ImageDisplay("loadingtransparent.png",120,0); }
        theBusDisplay.setSize(450,340);
        theBusDisplay.setBackground(Color.WHITE);
        busPanel.add(theBusDisplay);
        centrePanel.add(busPanel);
        
        //Construct loading panel to add to the centre panel.
        JPanel loadingPanel = new JPanel();
        loadingPanel.setBackground(Color.WHITE);
        if ( isAboutScreen ) { theLoadingLabel = new JLabel("Version: " + theInterface.getVersion()); }
        else { theLoadingLabel = new JLabel("Loading... Please Wait!"); }
        theLoadingLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        loadingPanel.add(theLoadingLabel);
        centrePanel.add(loadingPanel);
        
        //Construct copyright panel to add to the centre panel.
        JPanel copyrightPanel = new JPanel();
        copyrightPanel.setBackground(Color.WHITE);
        theCopyrightLabel = new JLabel("http://trams.sourceforge.net");
        theCopyrightLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        copyrightPanel.add(theCopyrightLabel);
        centrePanel.add(copyrightPanel);
        
        c.add(centrePanel, BorderLayout.CENTER);
        
        //Mouse listeners if this is the about screen.
        if ( isAboutScreen ) {
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
            theImageDisplay.addMouseListener ( new MouseListener () {
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
        }
        
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
    
    public void moveImage ( int width, int height ) {
        theBusDisplay.moveImage(width, height);
    }
    
}
