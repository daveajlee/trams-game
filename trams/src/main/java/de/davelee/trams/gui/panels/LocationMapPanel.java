package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;

public class LocationMapPanel {
	
	private ControllerHandler controllerHandler;

	public LocationMapPanel (final ControllerHandler controllerHandler ) {
	    this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final ControlScreen controlScreen, final DisplayPanel displayPanel  ) {
		final CompanyResponse companyResponse = controllerHandler.getGameController().getGameModel(controlScreen.getCompany(), controlScreen.getPlayerName());
    	
    	Scenario scenarioModel = controllerHandler.getScenarioController().getScenario(companyResponse.getScenarioName());
        
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
                controlScreen.redrawManagement(displayPanel.createPanel(controlScreen), companyResponse);
            }
        });
        optionsButtonPanel.add(managementScreenButton);
        picturePanel.add(optionsButtonPanel, BorderLayout.SOUTH);
        
        return picturePanel;
	}

}
