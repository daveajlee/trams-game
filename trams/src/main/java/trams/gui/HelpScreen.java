package trams.gui;

//Import java awt packages.
import java.awt.*;
import java.awt.event.*;
//Import java io and util packages.
import java.io.*;
//Import java swing packages.
import javax.swing.*;
import javax.swing.event.*;

import trams.main.UserInterface;

/**
 * HelpScreen.java is the screen to display the help screen for TraMS.
 * @author Dave Lee
 * @version 1.0
 */
public class HelpScreen extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel theSearchLabel;
    private JTextField theSearchField;
    private JLabel theTopicsLabel;
    private JList theTopicsList;
    private DefaultListModel theTopicsModel;
    private JEditorPane theDisplayPane;
    private UserInterface theInterface;
    
    /**
     * Default constructor for HelpScreen which creates the help screen interface and displays it to the user.
     * @param ui a <code>UserInterface</code> object.
     */
    public HelpScreen ( UserInterface ui ) {
        
        //Initialise user interface.
        theInterface = ui;
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(HelpScreen.class.getResource("/trams/images/TraMSlogo.png"));
        setIconImage(img);
        
        //Close this window if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                dispose();
                if ( theInterface != null ) {
                    theInterface.resumeSimulation();
                }
            }
        });
        
        //Initialise GUI with title and close attributes.
        this.setTitle ( "TraMS Help" );
        this.setResizable (false);
        this.setDefaultCloseOperation (DISPOSE_ON_CLOSE);
        
        //Get a container to add things to.
        Container c = this.getContentPane();
        
        //Create a panel to display components.
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout( new BoxLayout ( dialogPanel, BoxLayout.PAGE_AXIS ) );
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Spacer.
        
        //Create grid layout - 2 to 1.
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout( helpPanel, BoxLayout.LINE_AXIS ) );
        helpPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        //Create left hand panel.
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout( new BoxLayout ( leftPanel, BoxLayout.PAGE_AXIS ) );
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Spacer.
        
        //Add search label.
        JPanel searchLabelPanel = new JPanel();
        theSearchLabel = new JLabel("Search for Help...");
        theSearchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabelPanel.add(theSearchLabel);
        leftPanel.add(searchLabelPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        //Add search field.
        theSearchField = new JTextField();
        theSearchField.setColumns(40);
        theSearchField.addKeyListener( new KeyAdapter() {
            public void keyReleased ( KeyEvent e ) {
                updateList(theSearchField.getText());
            }
        });
        leftPanel.add(theSearchField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20))); //Spacer.
        
        //Add search label.
        JPanel topicLabelPanel = new JPanel();
        theTopicsLabel = new JLabel("Choose a Help Topic...");
        theTopicsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topicLabelPanel.add(theTopicsLabel);
        leftPanel.add(topicLabelPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        //Add topics list.
        JPanel topicListPanel = new JPanel(new BorderLayout());
        theTopicsModel = new DefaultListModel();
        theTopicsModel.addElement("Welcome"); theTopicsModel.addElement("Getting Started");
        theTopicsModel.addElement("Create New Game"); theTopicsModel.addElement("Add/Edit/Delete Route");
        theTopicsModel.addElement("Purchase/Sell Vehicle");
        theTopicsModel.addElement("Load Game"); theTopicsModel.addElement("Allocate Vehicles to Routes");
        theTopicsModel.addElement("Control Screen"); theTopicsModel.addElement("Make Contact with Vehicle");
        theTopicsModel.addElement("Vehicle Info Screen"); theTopicsModel.addElement("Web Site");
        /*theTopicsModel.addElement("Load Output"); theTopicsModel.addElement("Save Output");*/
        theTopicsList = new JList(theTopicsModel);
        theTopicsList.setVisibleRowCount(10);
        theTopicsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Default.
        theTopicsList.setSelectedIndex(0);
        //Action Listener for when a particular help topic is selected.
        theTopicsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent lse ) {
                //Get selected item.
                String selectedItem;
                try {
                    selectedItem = theTopicsList.getSelectedValue().toString();
                }
                catch ( NullPointerException npe ) {
                    selectedItem = theTopicsList.getModel().getElementAt(0).toString();
                    theTopicsList.setSelectedValue(selectedItem, true);
                }
                //If loading content fails, then stack trace and dispose.
                try {
                    //If statements to display correct content.
                    if ( selectedItem.equalsIgnoreCase("Welcome") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/intro.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Getting Started") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/gettingstarted.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Load Game") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/loadgame.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Web Site") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/website.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Create New Game") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/newgame.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Add/Edit/Delete Route") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/newroute.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Purchase/Sell Vehicle") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/newvehicle.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Allocate Vehicles to Routes") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/allocationscreen.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Control Screen") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/controlscreen.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Vehicle Info Screen") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/vehicleinfoscreen.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Make Contact With Vehicle") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/trams/help/makecontact.html"));
                    }
                    /*else if ( selectedItem.equalsIgnoreCase("Save Output") ) {
                        theDisplayPane.setPage(HelpScreen.class.getResource("/saveoutput.html"));
                    }*/
                }
                catch ( IOException e ) {
                    e.printStackTrace();
                    dispose();
                }
            }
        });
        JScrollPane topicsPane = new JScrollPane(theTopicsList);
        topicListPanel.add(topicsPane, BorderLayout.CENTER);
        leftPanel.add(topicListPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Spacer.
        leftPanel.setMaximumSize(new Dimension(450,400));
        
        //Add left panel to help panel.
        helpPanel.add(leftPanel);
        helpPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        //Create right pane.
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout( new BoxLayout ( rightPanel, BoxLayout.PAGE_AXIS ) );
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Spacer
        //Add editor pane.
        try {
            theDisplayPane = new JEditorPane(HelpScreen.class.getResource("/trams/help/intro.html")); 
            theDisplayPane.setMaximumSize(new Dimension(650,500));
        }
        catch (IOException e) {
            e.printStackTrace();
            dispose();
        }
        JScrollPane displayScroll = new JScrollPane(theDisplayPane);
        displayScroll.setMaximumSize(new Dimension(650,390));
        rightPanel.add(displayScroll);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Spacer.
        rightPanel.setMaximumSize(new Dimension(650,390));
        helpPanel.add(rightPanel);
        helpPanel.setMaximumSize(new Dimension(650,390));
        //Add help panel to dialog panel.
        dialogPanel.add(helpPanel);
        dialogPanel.setMaximumSize(new Dimension(450,390));
        
         //Add the panel to the container.
        c.add ( dialogPanel );
        
        //Display the dialog box to the user.
        //this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(700,450) );
        
        // Set the window's bounds, centering the window
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - this.getWidth()) / 2;
        int y = (screen.height - this.getHeight()) / 2;
        setBounds(x, y, this.getWidth(), this.getHeight());
        
    }
    
    /**
     * This method updates the topic lists according to the search text entered by the user.
     * @param text a <code>String</code> containing the text entered by the user in the search text box.
     */
    public void updateList ( String text ) {
        //Create temp model.
        DefaultListModel tempModel = new DefaultListModel();
        //If text is blank then set tempModel to fullModel.
        if ( text.equalsIgnoreCase("") ) {
            tempModel = theTopicsModel;
        }
        //Otherwise, add those which have this prefix.
        else {
            for ( int i = 0; i < theTopicsModel.size(); i++ ) {
                if ( includeString(text, theTopicsModel.get(i).toString()) ) {
                    tempModel.addElement(theTopicsModel.get(i).toString());
                }
            }
        }
        //Set the list to the temp model.
        theTopicsList.setModel(tempModel);
        theTopicsList.setSelectedIndex(0);
    }
    
    /**
     * This method determines whether a string should be included (strToCheck) against another String (strCheckAgainst).
     * Specifically e.g. statsres (in model) should be included if user's text was stats.
     * @param strToCheck a <code>String</code> containing the user's text.
     * @param strCheckAgainst a <code>String</code> containing the text in model.
     * @return a <code>boolean</code> which is true if and only if text in model should be included based on user's text.
     */
    public boolean includeString ( String strToCheck, String strCheckAgainst ) {
        for ( int i = 0; i < strToCheck.length(); i++ ) {
            if ( !strToCheck.substring(i, (i+1)).equalsIgnoreCase(strCheckAgainst.substring(i, (i+1))) ) {
                return false;
            }
        }
        return true;
    }
    
}
