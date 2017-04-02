package de.davelee.trams.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.controllers.GameController;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class to display options within the TraMS program.
 * @author Dave Lee.
 */
public class OptionsScreen extends JFrame {
    
	private static final long serialVersionUID = 1L;

    private JLabel optionsLabel;
    private JTabbedPane optionsTabbedPane;
    
    private JRadioButton[] difficultButtons;
    
    private JButton okButton;
    private JButton closeButton;

    private ControllerHandler controllerHandler;

    /**
     * Create a new options screen.
     * @param controllerHandler a <code>ControllerHandler</code> object containing the actual controllers from spring.
     */
    public OptionsScreen ( final ControllerHandler controllerHandler ) {

        this.controllerHandler = controllerHandler;
        
        //Set image icon.
        Image img = Toolkit.getDefaultToolkit().getImage(OptionsScreen.class.getResource("/TraMSlogo.png"));
        setIconImage(img);
        
        //Initialise GUI with title and close attributes.
        this.setTitle ("Options Screen");
        this.setResizable (false);
        this.setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
        this.setBackground(Color.WHITE);
        
        //Call dispose method if the user hits exit.
        this.addWindowListener ( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                controllerHandler.getGameController().resumeSimulation();
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
        
        //Create panel for north - options screen label.
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        JPanel optionsLabelPanel = new JPanel();
        optionsLabel = new JLabel("Options Screen");
        optionsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        optionsLabelPanel.add(optionsLabel);
        northPanel.add(optionsLabelPanel, BorderLayout.NORTH);

        final GameModel gameModel = controllerHandler.getGameController().getGameModel();
        
        //Create panel for centre - options in grid layout.
        optionsTabbedPane = new JTabbedPane();
        //Create panel to display the difficulty options.
        JPanel difficultyPanel = new JPanel(new GridLayout(DifficultyLevel.values().length+1,2,5,5));
        difficultyPanel.setBackground(Color.WHITE);
        JLabel difficultyLevelLabel = new JLabel("Difficulty Level:");
        difficultyLevelLabel.setFont(new Font("Arial", Font.BOLD, 15));
        difficultyPanel.add(difficultyLevelLabel);
        JLabel difficultyDescLabel = new JLabel("Description:");
        difficultyDescLabel.setFont(new Font("Arial", Font.BOLD, 15));
        difficultyPanel.add(difficultyDescLabel);
        //That's the headings done - now the levels.
        DifficultyLevel[] levels = DifficultyLevel.values();
        //Now put them into the interface.
        difficultButtons = new JRadioButton[levels.length];
        JTextArea[] difficultDescrips = new JTextArea[levels.length];
        ButtonGroup difficultGroup = new ButtonGroup();
        for ( int i = 0; i < levels.length; i++ ) {
            difficultButtons[i] = new JRadioButton(levels[i].getName());
            difficultButtons[i].setBackground(Color.WHITE);
            difficultyPanel.add(difficultButtons[i]);
            difficultGroup.add(difficultButtons[i]);
            difficultDescrips[i] = new JTextArea(levels[i].getDescription());
            difficultDescrips[i].setLineWrap(true);
            difficultDescrips[i].setWrapStyleWord(true);
            difficultDescrips[i].setEditable(false);
            difficultDescrips[i].setFont(new Font("Arial", Font.ITALIC, 11));
            difficultyPanel.add(difficultDescrips[i]);
        }
        if ( gameModel.getDifficultyLevel() == DifficultyLevel.EASY ) {
            difficultButtons[0].setSelected(true);
        }
        else if ( gameModel.getDifficultyLevel() == DifficultyLevel.INTERMEDIATE ) {
            difficultButtons[1].setSelected(true);
        }
        else if ( gameModel.getDifficultyLevel() == DifficultyLevel.MEDIUM ) {
            difficultButtons[2].setSelected(true);
        }
        else if ( gameModel.getDifficultyLevel() == DifficultyLevel.HARD ) {
            difficultButtons[3].setSelected(true);
        }
        //Now add the difficulty panel to the tabbed panel.
        optionsTabbedPane.addTab("Difficulty", difficultyPanel);
        //Add theOptionsTabbedPane to screen panel.
        screenPanel.add(optionsTabbedPane, BorderLayout.CENTER);
        
        //Create south panel - two buttons - OK and close.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        //Make Contact button.
        okButton = new JButton("OK");
        okButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Process options - set difficulty level!
                if ( difficultButtons[0].isSelected() ) {
                    gameModel.setDifficultyLevel(DifficultyLevel.EASY);
                }
                else if ( difficultButtons[1].isSelected() ) {
                    gameModel.setDifficultyLevel(DifficultyLevel.INTERMEDIATE);
                }
                else if ( difficultButtons[2].isSelected() ) {
                    gameModel.setDifficultyLevel(DifficultyLevel.MEDIUM);
                }
                else if ( difficultButtons[3].isSelected() ) {
                    gameModel.setDifficultyLevel(DifficultyLevel.HARD);
                }
                controllerHandler.getGameController().resumeSimulation();
                dispose();
            }
        });
        southPanel.add(okButton);
        //Close button.
        closeButton = new JButton("Close Window");
        closeButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controllerHandler.getGameController().resumeSimulation();
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
        Dimension displayDim = new Dimension(550,300);
        this.setLocation ( (int) (screenDim.width/2)-(displayDim.width/2), (int) (screenDim.height/2)-(displayDim.height/2));
        
        //Display the front screen to the user.
        this.pack ();
        this.setVisible (true);
        this.setSize ( new Dimension(400,300) );
        
    }
    
}
