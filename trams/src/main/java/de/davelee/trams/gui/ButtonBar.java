package de.davelee.trams.gui;

import javax.swing.*;
import java.awt.event.*;

import de.davelee.trams.controllers.FileController;
import de.davelee.trams.controllers.GameController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ButtonBar class represents the menu system in the TraMS program.
 * @author Dave Lee.
 */
public class ButtonBar extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu toolsMenu;
    private JMenu helpMenu;
    protected JMenuItem newGameItem;
    protected JMenuItem loadGameItem;
    protected JMenuItem saveGameItem;
    protected JMenuItem exitGameItem;
    protected JMenuItem optionsItem;
    protected JMenuItem helpItem;
    protected JMenuItem updateItem;
    protected JMenuItem aboutItem;

    @Autowired
    private GameController gameController;

    @Autowired
    private FileController fileController;
    
    /**
     * Create a new button bar.
     */
    public ButtonBar ( ) {
        
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                new NewGameScreen();
                dispose();
            }
        });
        fileMenu.add(newGameItem);
        
        loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                gameController.pauseSimulation();
                fileController.loadFile(ButtonBar.this);
            }
        });
        fileMenu.add(loadGameItem);
        
        fileMenu.addSeparator();
        
        saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                gameController.pauseSimulation();
                fileController.saveFile(ButtonBar.this);
                gameController.resumeSimulation();
            }
        });
        fileMenu.add(saveGameItem);
        
        fileMenu.addSeparator();
        
        exitGameItem = new JMenuItem("Exit Game");
        exitGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                gameController.exit(ButtonBar.this);
            }
        });
        fileMenu.add(exitGameItem);
        
        toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);
        
        optionsItem = new JMenuItem("Options");
        optionsItem.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                gameController.pauseSimulation();
                new OptionsScreen();
            }
        });
        toolsMenu.add(optionsItem);
        
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        helpItem = new JMenuItem("Contents");
        helpMenu.add(helpItem);
        
        helpMenu.addSeparator();
        
        aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        
    }
    
}
