package trams.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import trams.main.UserInterface;

/**
 * ButtonBar class represents the menu system in the TraMS program.
 * @author Dave Lee.
 */
public class ButtonBar extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JMenuBar theMenuBar;
    private JMenu theFileMenu;
    private JMenu theToolsMenu;
    private JMenu theHelpMenu;
    protected JMenuItem theNewGameItem;
    protected JMenuItem theLoadGameItem;
    protected JMenuItem theSaveGameItem;
    protected JMenuItem theExitGameItem;
    protected JMenuItem theOptionsItem;
    protected JMenuItem theHelpItem;
    protected JMenuItem theUpdateItem;
    protected JMenuItem theAboutItem;
    private UserInterface theInterface;
    
    /**
     * Create a new button bar.
     * @param ui a <code>UserInterface</code> object.
     */
    public ButtonBar ( UserInterface ui ) {
        
        theInterface = ui;
        
        theMenuBar = new JMenuBar();
        
        theFileMenu = new JMenu("File");
        theMenuBar.add(theFileMenu);
        
        theNewGameItem = new JMenuItem("New Game");
        theNewGameItem.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface = new UserInterface();
                new NewGameScreen(theInterface);
                dispose();
            }
        });
        theFileMenu.add(theNewGameItem);
        
        theLoadGameItem = new JMenuItem("Load Game");
        theLoadGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.pauseSimulation();
                theInterface.loadFile();
            }
        });
        theFileMenu.add(theLoadGameItem);
        
        theFileMenu.addSeparator();
        
        theSaveGameItem = new JMenuItem("Save Game");
        theSaveGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.pauseSimulation();
                theInterface.saveFile();
                if ( !theInterface.getMessageScreen() && !theInterface.getManagementScreen() ) {
                    theInterface.resumeSimulation();
                }
            }
        });
        theFileMenu.add(theSaveGameItem);
        
        theFileMenu.addSeparator();
        
        theExitGameItem = new JMenuItem("Exit Game");
        theExitGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.exit();
            }
        });
        theFileMenu.add(theExitGameItem);
        
        theToolsMenu = new JMenu("Tools");
        theMenuBar.add(theToolsMenu);
        
        theOptionsItem = new JMenuItem("Options");
        theOptionsItem.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                theInterface.pauseSimulation();
                new OptionsScreen(theInterface);
            }
        });
        theToolsMenu.add(theOptionsItem);
        
        theHelpMenu = new JMenu("Help");
        theMenuBar.add(theHelpMenu);
        
        theHelpItem = new JMenuItem("Contents");
        theHelpMenu.add(theHelpItem);
        
        theHelpMenu.addSeparator();
        
        theAboutItem = new JMenuItem("About");
        theHelpMenu.add(theAboutItem);
        
    }
    
}
