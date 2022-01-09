package de.davelee.trams.util;

import de.davelee.trams.gui.ImageDisplay;

import javax.swing.*;
import java.awt.*;

public class GuiUtils {

    public static JPanel createWelcomePanel ( ) {
        JLabel welcomeLabel = new JLabel("Welcome to ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 40));
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        ImageDisplay logoDisplay = new ImageDisplay("trams-logo-small.png", 0, 0);
        logoDisplay.setSize(157,62);
        logoDisplay.setBackground(Color.WHITE);
        logoPanel.add(logoDisplay);
        return GuiUtils.createPanelWithTwoComponent(welcomeLabel, logoPanel);
    }

    /**
     * This is a private method to create a <code>JPanel</code> with two component.
     * @param component a <code>JComponent</code> object to add to the panel.
     * @param component2 a <code>JComponent</code> object to add to the panel.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    public static JPanel createPanelWithTwoComponent (final JComponent component, final JComponent component2) {
        JPanel twoComponentPanel = new JPanel();
        twoComponentPanel.setBackground(Color.WHITE);
        twoComponentPanel.add(component);
        twoComponentPanel.add(component2);
        return twoComponentPanel;
    }

    /**
     * This is a private helper method to create a <code>JPanel</code> with box layout.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    public static JPanel createBoxPanel ( ) {
        JPanel boxPanel = new JPanel();
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setLayout ( new BoxLayout ( boxPanel, BoxLayout.PAGE_AXIS ) );
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return boxPanel;
    }

}
