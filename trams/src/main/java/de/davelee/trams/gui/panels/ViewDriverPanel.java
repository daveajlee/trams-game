package de.davelee.trams.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;

public class ViewDriverPanel {
	
	private ControllerHandler controllerHandler;

	public ViewDriverPanel ( final ControllerHandler controllerHandler ) {
	    this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final ControlScreen controlScreen ) {
		final GameModel gameModel = controllerHandler.getGameController().getGameModel();
    	
        //Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        JLabel driverComingSoonLabel = new JLabel("Coming Soon...");

        driverScreenPanel.add(driverComingSoonLabel);

        //Create return to create game screen button and add it to screen panel.
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controlScreen.redrawManagement(new DisplayPanel(controllerHandler).createPanel(controlScreen), gameModel);
            }
        });
        driverScreenPanel.add(managementScreenButton);

        return driverScreenPanel;
	}

}
