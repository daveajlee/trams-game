package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;

public class ScenarioPanel {

    private ControllerHandler controllerHandler;
	
	public ScenarioPanel (final ControllerHandler controllerHandler) {
        this.controllerHandler = controllerHandler;
    }
	
	public JPanel createPanel ( final ControlScreen controlScreen, final ManagementPanel displayPanel ) {
		//Create screen panel to add things to.
        JPanel scenarioScreenPanel = new JPanel();
        scenarioScreenPanel.setLayout ( new BoxLayout ( scenarioScreenPanel, BoxLayout.PAGE_AXIS ) );
        scenarioScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in topLabelPanel and add it to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Scenario Detail Screen", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        scenarioScreenPanel.add(topLabelPanel, BorderLayout.NORTH);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,10)));

        //Create info panel.
        /*JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);*/
        
        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());

        //Create panel for scenario name field.
        JPanel scenarioNamePanel = new JPanel(new GridLayout(1,2,5,5));
        scenarioNamePanel.setBackground(Color.WHITE);
        //Create label and field for scenario name.
        JLabel scenarioNameLabel = new JLabel("Scenario Name: ", SwingConstants.CENTER);
        scenarioNameLabel.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel scenarioActualNameLabel = new JLabel(companyResponse.getScenarioName(), SwingConstants.CENTER);
        scenarioActualNameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        scenarioNamePanel.add(scenarioNameLabel);
        scenarioNamePanel.add(scenarioActualNameLabel);

        //Add scenarioNamePanel to info panel.
        scenarioScreenPanel.add(scenarioNamePanel);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,15)));
        
        Scenario scenario = controllerHandler.getScenarioController().getScenario(companyResponse.getScenarioName());

        //Create panel for target field.
        JPanel targetPanel = new JPanel(new GridLayout(1,2,5,5));
        targetPanel.setBackground(Color.WHITE);
        //Create label and area for senario description.
        JLabel targetLabel = new JLabel("Scenario Targets: ", SwingConstants.CENTER);
        targetLabel.setFont(new Font("Arial", Font.BOLD, 17));
        targetPanel.add(targetLabel);
        JPanel scenarioTargetPanel = new JPanel(new BorderLayout());
        scenarioTargetPanel.setBackground(Color.WHITE);
        JTextArea scenarioTargetArea = new JTextArea(scenario.getTargets());
        scenarioTargetArea.setWrapStyleWord(true);
        scenarioTargetArea.setLineWrap(true);
        scenarioTargetArea.setFont(new Font("Arial", Font.ITALIC, 14));
        scenarioTargetPanel.add(scenarioTargetArea);
        targetPanel.add(scenarioTargetPanel);
        scenarioScreenPanel.add(targetPanel);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,15)));

        //Create panel for player name field.
        JPanel playerNamePanel = new JPanel(new GridLayout(1,2,5,5));
        playerNamePanel.setBackground(Color.WHITE);
        //Create label and field for player name.
        JLabel playerNameLabel = new JLabel("Player Name: ", SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel playerActualNameLabel = new JLabel(companyResponse.getPlayerName(), SwingConstants.CENTER);
        playerActualNameLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        playerNamePanel.add(playerNameLabel);
        playerNamePanel.add(playerActualNameLabel);

        //Add scenarioNamePanel to info panel.
        scenarioScreenPanel.add(playerNamePanel);
        scenarioScreenPanel.add(Box.createRigidArea(new Dimension(0,10)));

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
        
        //Add options button panel to screen panel.
        scenarioScreenPanel.add(optionsButtonPanel, BorderLayout.SOUTH);
        
        return scenarioScreenPanel;
	}

}
