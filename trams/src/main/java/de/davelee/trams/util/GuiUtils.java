package de.davelee.trams.util;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.EditingScreen;
import de.davelee.trams.gui.ImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class provides various static methods related to GUI Swing Processes to reduce code duplication.
 * @author Dave Lee
 */
public class GuiUtils {

    /**
     * This method creates a welcome panel including welcome text and trams logo.
     * @return a <code>JPanel</code> object to display to the user.
     */
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
     * This method creates a <code>JPanel</code> with two component.
     * @param component a <code>JComponent</code> object to add to the panel.
     * @param component2 a <code>JComponent</code> object to add to the panel.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    public static JPanel createPanelWithTwoComponent (final JComponent component, final JComponent component2) {
        JPanel twoComponentPanel = new JPanel();
        twoComponentPanel.setBackground(Color.WHITE);
        twoComponentPanel.add(component);
        if ( component2 != null ) { twoComponentPanel.add(component2); }
        return twoComponentPanel;
    }

    /**
     * This method creates a <code>JPanel</code> with box layout.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    public static JPanel createBoxPanel ( ) {
        JPanel boxPanel = new JPanel();
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setLayout ( new BoxLayout ( boxPanel, BoxLayout.PAGE_AXIS ) );
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return boxPanel;
    }

    /**
     * This method creates a <code>JPanel</code> with a header text.
     * @param text a <code>String</code> with the header text.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    public static JPanel createHeadingPanel ( final String text ) {
        JPanel textLabelPanel = new JPanel(new BorderLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel(text, SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel, BorderLayout.CENTER);
        return textLabelPanel;
    }

    /**
     * This method creates a <code>JPanel</code> containing a list of components.
     * @param listModel a <code>DefaultListModel</code> object with the values to be included in the list.
     * @param initialValue a <code>String</code> with the initial value to be included on the list.
     * @param controlScreen a <code>ControlScreen</code> with the control screen that is being displayed to the user.
     * @param companyResponse a <code>CompanyResponse</code> with the company information.
     * @param editingScreen a <code>EditingScreen</code> with the screen that the list should appear on.
     * @return a <code>JPanel</code> object which can be added to another panel.
     */
    public static JPanel createListPanel (final DefaultListModel<String> listModel, final String initialValue,
                                          final ControlScreen controlScreen, final CompanyResponse companyResponse,
                                          final EditingScreen editingScreen) {
        //Now create the east panel to display the vehicle list.
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        //Third part of route panel is list of routes.
        final JList<String> jList = new JList<>(listModel);
        jList.setFixedCellWidth(100);
        jList.setVisibleRowCount(25);
        jList.setSelectedValue(initialValue, true);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setFont(new Font("Arial", Font.PLAIN, 15));
        jList.addListSelectionListener(e -> {
            String selectedValue = jList.getSelectedValue();
            controlScreen.redrawManagement(editingScreen.createPanel(selectedValue, controlScreen), companyResponse);
        });
        JScrollPane vehiclesPane = new JScrollPane(jList);
        listPanel.add(GuiUtils.createPanelWithTwoComponent(vehiclesPane, null), BorderLayout.CENTER);
        return listPanel;
    }

    /**
     * This method creates a button.
     * @param buttonText a <code>String</code> containing the text to display on the button.
     * @param buttonActionListener a <code>ActionListener</code> containing the action to perform when the button is clicked on.
     * @param enableCondition a <code>boolean</code> which is true iff the button should be shown as enabled.
     * @return a <code>JButton</code> object which can be added to a panel.
     */
    public static JButton createButton (final String buttonText, final ActionListener buttonActionListener, final boolean enableCondition) {
        final JButton button = new JButton(buttonText);
        button.addActionListener(buttonActionListener);
        button.setEnabled(enableCondition);
        return button;
    }

}
