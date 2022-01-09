package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.ImageDisplay;

/**
 * This class represents a panel to display the location map to the user.
 * @author Dave Lee
 */
public record LocationMapPanel(ControllerHandler controllerHandler) {

    /**
     * Create a new <code>LocationMapPanel</code> panel and display it to the user.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param managementPanel a <code>ManagementPanel</code> object which is the management panel that has been displayed to the user (for back button functionality).
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final ControlScreen controlScreen, final ManagementPanel managementPanel  ) {
		final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());
    	
    	Scenario scenario = controllerHandler.getScenarioController().getScenario(companyResponse.getScenarioName());
        
        //Create picture panel.
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setBackground(Color.WHITE);
        ImageDisplay busDisplay = new ImageDisplay(scenario.getLocationMapFileName(),0,0);
        busDisplay.setSize(750,380);
        busDisplay.setBackground(Color.WHITE);
        picturePanel.add(busDisplay, BorderLayout.CENTER);
        
        //Options button panel with save details and return to management screen buttons.
        JPanel optionsButtonPanel = createPanel();
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener(e -> controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse));
        optionsButtonPanel.add(managementScreenButton);
        picturePanel.add(optionsButtonPanel, BorderLayout.SOUTH);
        
        return picturePanel;
	}

    private JPanel createPanel() {
        JPanel optionsButtonPanel = new JPanel();
        optionsButtonPanel.setBackground(Color.WHITE);
        return optionsButtonPanel;
    }

}
