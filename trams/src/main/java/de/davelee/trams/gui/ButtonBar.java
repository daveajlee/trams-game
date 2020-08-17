package de.davelee.trams.gui;

import javax.swing.*;
import java.awt.event.*;

import de.davelee.trams.controllers.ControllerHandler;

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
    protected JMenuItem newGameItem;
    protected JMenuItem loadGameItem;
    protected JMenuItem saveGameItem;
    protected JMenuItem exitGameItem;
    protected JMenuItem optionsItem;
    protected JMenuItem helpItem;
    protected JMenuItem updateItem;
    protected JMenuItem aboutItem;

    private ControllerHandler controllerHandler;
    
    /**
     * Create a new button bar.
     * @param controllerHandler a <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
    public ButtonBar ( final ControllerHandler controllerHandler ) {

        this.controllerHandler = controllerHandler;
        
        menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                new NewGameScreen(controllerHandler);
                dispose();
            }
        });
        fileMenu.add(newGameItem);
        
        loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                controllerHandler.getGameController().pauseSimulation();
                FileDialog fileDialog = new FileDialog();
                fileDialog.createLoadFileDialog(ButtonBar.this, controllerHandler, new ControlScreen(controllerHandler));
            }
        });
        fileMenu.add(loadGameItem);
        
        fileMenu.addSeparator();
        
        saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                controllerHandler.getGameController().pauseSimulation();
                FileDialog fileDialog = new FileDialog();
                fileDialog.createSaveFileDialog(ButtonBar.this, controllerHandler);
            }
        });
        fileMenu.add(saveGameItem);
        
        fileMenu.addSeparator();
        
        exitGameItem = new JMenuItem("Exit Game");
        exitGameItem.addActionListener ( new ActionListener () {
            public void actionPerformed ( ActionEvent e ) {
                ExitDialog exitDialog = new ExitDialog();
                exitDialog.createExitDialog(ButtonBar.this);
            }
        });
        fileMenu.add(exitGameItem);
        
        JMenu toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);
        
        optionsItem = new JMenuItem("Options");
        optionsItem.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controllerHandler.getGameController().pauseSimulation();
                new OptionsScreen(controllerHandler);
            }
        });
        toolsMenu.add(optionsItem);
        
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        helpItem = new JMenuItem("Contents");
        helpMenu.add(helpItem);
        
        helpMenu.addSeparator();
        
        aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        
    }

    public ControllerHandler getControllerHandler() {
        return controllerHandler;
    }

}
