package trams.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import trams.main.UserInterface;


/**
 * Class to display options within the TraMS program.
 * @author Dave Lee.
 */
public class OptionsScreen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInterface theInterface;
    private JLabel theOptionsLabel;
    private JTabbedPane theOptionsTabbedPane;
    
    private JRadioButton[] theDifficultButtons;
    
    private JButton theOKButton;
    private JButton theCloseButton;
    
    //Difficulty Variables.
    private int numDifficultyLevels = 4;
    
    private static final int DIMENSION_SPACER = 10;
    private static final int DESCRIPTION_FONT_SIZE = 11;
    
    /**
     * Create a new options screen.
     * @param ui a <code>UserInterface</code> object with the current user interface.
     */
    public OptionsScreen ( UserInterface ui ) {
        
        //Initialise user interface variable.
        theInterface = ui;
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(OptionsScreen.class.getResource("/trams/images/TraMSlogo.png"));
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Options Screen");
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
        
        //Create panel for north - options screen label.
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        JPanel optionsLabelPanel = new JPanel();
        theOptionsLabel = new JLabel("Options Screen");
        theOptionsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        optionsLabelPanel.add(theOptionsLabel);
        northPanel.add(optionsLabelPanel, BorderLayout.NORTH);
        
        //Create panel for centre - options in grid layout.
        theOptionsTabbedPane = new JTabbedPane();
        //Create panel to display the difficulty options.
        JPanel difficultyPanel = new JPanel(new GridLayout(numDifficultyLevels+1,2,5,5));
        difficultyPanel.setBackground(Color.WHITE);
        JLabel difficultyLevelLabel = new JLabel("Difficulty Level:");
        difficultyLevelLabel.setFont(new Font("Arial", Font.BOLD, 15));
        difficultyPanel.add(difficultyLevelLabel);
        JLabel difficultyDescLabel = new JLabel("Description:");
        difficultyDescLabel.setFont(new Font("Arial", Font.BOLD, 15));
        difficultyPanel.add(difficultyDescLabel);
        //That's the headings done - now the levels.
        String[] difficultyLevels = new String[] { "Easy", "Intermediate", "Medium", "Hard" };
        String[] difficultyDescs = new String[] { "Minimal delays requiring little intervention.",
                                                  "Minimal delays requiring intervention.",
                                                  "Frequent delays requiring occasional intervention.",
                                                  "Regular substantial delays." };
        //Now put them into the interface.
        theDifficultButtons = new JRadioButton[numDifficultyLevels];
        JTextArea[] difficultDescrips = new JTextArea[numDifficultyLevels];
        ButtonGroup difficultGroup = new ButtonGroup();
        for ( int i = 0; i < numDifficultyLevels; i++ ) {
            theDifficultButtons[i] = new JRadioButton(difficultyLevels[i]);
            theDifficultButtons[i].setBackground(Color.WHITE);
            difficultyPanel.add(theDifficultButtons[i]);
            difficultGroup.add(theDifficultButtons[i]);
            difficultDescrips[i] = new JTextArea(difficultyDescs[i]);
            difficultDescrips[i].setLineWrap(true);
            difficultDescrips[i].setWrapStyleWord(true);
            difficultDescrips[i].setEditable(false);
            difficultDescrips[i].setFont(new Font("Arial", Font.ITALIC, DESCRIPTION_FONT_SIZE));
            difficultyPanel.add(difficultDescrips[i]);
        }
        switch ( theInterface.getDifficultyLevel() ) {
        	case EASY:
        		theDifficultButtons[0].setSelected(true);
        		break;
        	case INTERMEDIATE:
        		theDifficultButtons[1].setSelected(true);
        		break;
        	case MEDIUM:
        		theDifficultButtons[2].setSelected(true);
        		break;
        	case HARD:
        		theDifficultButtons[3].setSelected(true);
        		break;
        }
        //Now add the difficulty panel to the tabbed panel.
        theOptionsTabbedPane.addTab("Difficulty", difficultyPanel);
        //Add theOptionsTabbedPane to screen panel.
        screenPanel.add(theOptionsTabbedPane, BorderLayout.CENTER);
        
        //Create south panel - two buttons - OK and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Make Contact button.
        theOKButton = new JButton("OK");
        theOKButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Process options - set difficulty level!
                if ( theDifficultButtons[0].isSelected() ) {
                    theInterface.setDifficultyLevel("Easy");
                } else if ( theDifficultButtons[1].isSelected() ) {
                    theInterface.setDifficultyLevel("Intermediate");
                } else if ( theDifficultButtons[2].isSelected() ) {
                    theInterface.setDifficultyLevel("Medium");
                } else if ( theDifficultButtons[3].isSelected() ) {
                    theInterface.setDifficultyLevel("Hard");
                }
                if ( !theInterface.getManagementScreen() && !theInterface.getMessageScreen() ) {
                    theInterface.resumeSimulation();
                }
                dispose();
            }
        });
        southPanel.add(theOKButton);
        //Close button.
        theCloseButton = new JButton("Close Window");
        theCloseButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( !theInterface.getManagementScreen() && !theInterface.getMessageScreen() ) {
                    theInterface.resumeSimulation();
                }
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
        Dimension displayDim = new Dimension(550,300);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(400,300) );
        
    }
    
}
