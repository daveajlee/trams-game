package de.davelee.trams.gui;

//Import java awt packages.
import java.awt.*;
import java.awt.event.*;
//Import java io and util packages.
import java.io.*;
//Import java swing packages.
import javax.swing.*;
import javax.swing.event.*;
//Import trams main package.
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.controllers.GameController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HelpScreen.java is the screen to display the help screen for TraMS.
 * @author Dave Lee
 * @version 1.0
 */
public class HelpScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JLabel searchLabel;
    private JTextField searchField;
    private JLabel topicsLabel;
    private JList topicsList;
    private DefaultListModel topicsModel;
    private JEditorPane displayPane;

    /**
     * Default constructor for HelpScreen which creates the help screen interface and displays it to the user.
     */
    public HelpScreen ( final ControllerHandler controllerHandler ) {
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(HelpScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Close this window if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                dispose();
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
        searchLabel = new JLabel("Search for Help...");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabelPanel.add(searchLabel);
        leftPanel.add(searchLabelPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        //Add search field.
        searchField = new JTextField();
        searchField.setColumns(40);
        searchField.addKeyListener( new KeyAdapter() {
            public void keyReleased ( KeyEvent e ) {
                updateList(searchField.getText());
            }
        });
        leftPanel.add(searchField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20))); //Spacer.
        
        //Add search label.
        JPanel topicLabelPanel = new JPanel();
        topicsLabel = new JLabel("Choose a Help Topic...");
        topicsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topicLabelPanel.add(topicsLabel);
        leftPanel.add(topicLabelPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        //Add topics list.
        JPanel topicListPanel = new JPanel(new BorderLayout());
        topicsModel = new DefaultListModel();
        topicsModel.addElement("Welcome"); topicsModel.addElement("Getting Started");
        topicsModel.addElement("Create New Game"); topicsModel.addElement("Add/Edit/Delete Route");
        topicsModel.addElement("Purchase/Sell Vehicle");
        topicsModel.addElement("Load Game"); topicsModel.addElement("Allocate Vehicles to Routes");
        topicsModel.addElement("Control Screen"); topicsModel.addElement("Make Contact with Vehicle");
        topicsModel.addElement("Vehicle Info Screen"); topicsModel.addElement("Web Site");
        /*theTopicsModel.addElement("Load Output"); theTopicsModel.addElement("Save Output");*/
        topicsList = new JList(topicsModel);
        topicsList.setVisibleRowCount(10);
        topicsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Default.
        topicsList.setSelectedIndex(0);
        //Action Listener for when a particular help topic is selected.
        topicsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged ( ListSelectionEvent lse ) {
                //Get selected item.
                String selectedItem;
                try {
                    selectedItem = topicsList.getSelectedValue().toString();
                }
                catch ( NullPointerException npe ) {
                    selectedItem = topicsList.getModel().getElementAt(0).toString();
                    topicsList.setSelectedValue(selectedItem, true);
                }
                //If loading content fails, then stack trace and dispose.
                try {
                    //If statements to display correct content.
                    if ( selectedItem.equalsIgnoreCase("Welcome") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/intro.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Getting Started") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/gettingstarted.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Load Game") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/loadgame.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Web Site") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/website.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Create New Game") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/newgame.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Add/Edit/Delete Route") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/newroute.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Purchase/Sell Vehicle") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/newvehicle.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Allocate Vehicles to Routes") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/allocationscreen.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Control Screen") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/controlscreen.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Vehicle Info Screen") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/vehicleinfoscreen.html"));
                    }
                    else if ( selectedItem.equalsIgnoreCase("Make Contact With Vehicle") ) {
                        displayPane.setPage(HelpScreen.class.getResource("/makecontact.html"));
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
        JScrollPane topicsPane = new JScrollPane(topicsList);
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
            displayPane = new JEditorPane(HelpScreen.class.getResource("/intro.html")); 
            displayPane.setMaximumSize(new Dimension(650,500));
        }
        catch (IOException e) {
            e.printStackTrace();
            dispose();
        }
        JScrollPane displayScroll = new JScrollPane(displayPane);
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
            tempModel = topicsModel;
        }
        //Otherwise, add those which have this prefix.
        else {
            for ( int i = 0; i < topicsModel.size(); i++ ) {
                if ( includeString(text, topicsModel.get(i).toString()) ) {
                    tempModel.addElement(topicsModel.get(i).toString());
                }
            }
        }
        //Set the list to the temp model.
        topicsList.setModel(tempModel);
        topicsList.setSelectedIndex(0);
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
