package de.davelee.trams.gui;

import javax.swing.*;
import java.io.Serial;

import de.davelee.trams.controllers.ControllerHandler;

/**
 * ButtonBar class represents the menu system in the TraMS program.
 * @author Dave Lee.
 */
public class ButtonBar extends JFrame {
    
    /**
	 * Serial Version id for serialisation.
	 */
    @Serial
	private static final long serialVersionUID = 1L;
    /**
     * Menu Bar containing all Menu Items to show to the user.
     */
	protected final JMenuBar menuBar;
    /**
     * New game item in Menu Bar.
     */
    protected final JMenuItem newGameItem;
    /**
     * Load game item in Menu Bar.
     */
    protected final JMenuItem loadGameItem;
    /**
     * Save game item in Menu Bar.
     */
    protected final JMenuItem saveGameItem;
    /**
     * Exit Game item in Menu Bar.
     */
    protected final JMenuItem exitGameItem;
    /**
     * Options item in Menu Bar.
     */
    protected final JMenuItem optionsItem;
    /**
     * Help item in Menu Bar.
     */
    protected final JMenuItem helpItem;
    /**
     * About item in Menu Bar.
     */
    protected final JMenuItem aboutItem;

    /**
     * A <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
    private final ControllerHandler controllerHandler;

    /**
     * A <code>String</code> with the name of the company currently being played.
     */
    private final String company;

    /**
     * A <code>String</code> with the name of the player currently playing the game.
     */
    private final String playerName;
    
    /**
     * Create a new button bar.
     * @param controllerHandler a <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     * @param company a <code>String</code> with the company currently being played.
     * @param playerName a <code>String</code> with the name of the player currently playing the game.
     */
    public ButtonBar ( final ControllerHandler controllerHandler, final String company, final String playerName ) {

        this.controllerHandler = controllerHandler;
        this.company = company;
        this.playerName = playerName;

        menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            new NewGameScreen(controllerHandler);
            dispose();
        });
        fileMenu.add(newGameItem);
        
        loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener (e -> {
            controllerHandler.getSimulationController().pauseSimulation();
            FileDialog fileDialog = new FileDialog();
            fileDialog.createLoadFileDialog(ButtonBar.this, controllerHandler);
        });
        fileMenu.add(loadGameItem);
        
        fileMenu.addSeparator();
        
        saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener (e -> {
            controllerHandler.getSimulationController().pauseSimulation();
            FileDialog fileDialog = new FileDialog();
            fileDialog.createSaveFileDialog(ButtonBar.this, controllerHandler);
        });
        fileMenu.add(saveGameItem);
        
        fileMenu.addSeparator();
        
        exitGameItem = new JMenuItem("Exit Game");
        exitGameItem.addActionListener (e -> {
            ExitDialog exitDialog = new ExitDialog();
            exitDialog.createExitDialog(ButtonBar.this);
        });
        fileMenu.add(exitGameItem);
        
        JMenu toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);
        
        optionsItem = new JMenuItem("Options");
        optionsItem.addActionListener (e -> {
            controllerHandler.getSimulationController().pauseSimulation();
            new OptionsScreen(controllerHandler, company, playerName);
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

    /**
     * Return the currently used controllers from Spring.
     * @return a <code>ControllerHandler</code> obtaining the currently used controllers from Spring.
     */
    public ControllerHandler getControllerHandler() {
        return controllerHandler;
    }

    /**
     * Return the name of the company currently being played.
     * @return a <code>String</code> with the company currently being played.
     */
    public String getCompany() { return company; }

    /**
     * Return the name of the player currently playing the game.
     * @return a <code>String</code> with the name of the player currently playing the game.
     */
    public String getPlayerName() { return playerName; }

}
