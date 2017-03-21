package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.ScenarioController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.ScenarioModel;

public class LocationMapPanel {
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private ScenarioController scenarioController;
	
	public JPanel createPanel ( final ControlScreen controlScreen ) {
		final GameModel gameModel = gameController.getGameModel();
    	
    	ScenarioModel scenarioModel = scenarioController.getScenario(gameModel.getScenarioName());
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(scenarioModel.getLocationMapFileName(),0,0);
        busDisplay.setSize(750,380);
        busDisplay.setBackground(Color.WHITE);
        picturePanel.add(busDisplay, BorderLayout.CENTER);
        
        //Options button panel with save details and return to management screen buttons.
        JPanel optionsButtonPanel = new JPanel();
        optionsButtonPanel.setBackground(Color.WHITE);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controlScreen.redrawManagement(new DisplayPanel().createPanel(controlScreen), gameModel); 
            }
        });
        optionsButtonPanel.add(managementScreenButton);
        picturePanel.add(optionsButtonPanel, BorderLayout.SOUTH);
        
        return picturePanel;
	}

}
