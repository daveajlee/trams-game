package de.davelee.trams.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.ControlScreen;

/**
 * This class represents a panel to employ drivers.
 * @author Dave Lee
 */
public class EmployDriverPanel {

    private ControllerHandler controllerHandler;

    /**
     * Create a new <code>EmployDriverPanel</code> with access to all Controllers to get or send data where needed.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
	public EmployDriverPanel (final ControllerHandler controllerHandler ) {
        this.controllerHandler = controllerHandler;
    }

    /**
     * Create a new <code>EmployDriverPanel</code> panel and display it to the user.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @param managementPanel a <code>ManagementPanel</code> object which is the management panel that has been displayed to the user (for back button functionality).
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
	public JPanel createPanel ( final ControlScreen controlScreen, final ManagementPanel managementPanel ) {
		//Create screen panel to add things to.
        JPanel driverScreenPanel = new JPanel();
        driverScreenPanel.setLayout ( new BoxLayout ( driverScreenPanel, BoxLayout.PAGE_AXIS ) );
        driverScreenPanel.setBackground(Color.WHITE);

        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel textLabelPanel = new JPanel(new GridBagLayout());
        textLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Employ Driver");
        topLabel.setFont(new Font("Arial", Font.BOLD, 25));
        //topLabel.setVerticalAlignment(JLabel.CENTER);
        textLabelPanel.add(topLabel);
        driverScreenPanel.add(textLabelPanel);

        //Create panel for information fields.
        JPanel gridPanel = new JPanel(new GridLayout(7,2,2,2));
        gridPanel.setBackground(Color.WHITE);  
        //Driver name.
        JPanel driverNamePanel = new JPanel();
        driverNamePanel.setBackground(Color.WHITE);
        JLabel driverNameLabel = new JLabel("Name:");
        driverNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        driverNamePanel.add(driverNameLabel);
        final JTextField driverNameField = new JTextField("");
        driverNameField.setColumns(30);
        driverNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        driverNamePanel.add(driverNameField);

        gridPanel.add(driverNamePanel);

        //Contracted hours.
        JPanel contractedHoursPanel = new JPanel();
        contractedHoursPanel.setBackground(Color.WHITE);
        JLabel contractedHoursLabel = new JLabel("Contracted Hours:");
        contractedHoursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contractedHoursPanel.add(contractedHoursLabel);
        final JSpinner contractedHoursSpinner = new JSpinner(new SpinnerNumberModel(35,10,40,5));
        contractedHoursSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        contractedHoursPanel.add(contractedHoursSpinner);

        gridPanel.add(contractedHoursPanel);
        
        final CompanyResponse companyResponse = controllerHandler.getCompanyController().getCompany(controlScreen.getCompany(), controlScreen.getPlayerName());

        //Create label and field for start date and add it to the start panel.
        JPanel startLabelPanel = new JPanel();
        startLabelPanel.setBackground(Color.WHITE);
        JLabel startLabel = new JLabel("Start Date:", SwingConstants.CENTER);
        startLabel.setFont(new Font("Arial", Font.BOLD, 16));
        startLabelPanel.add(startLabel);
        final LocalDate startDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        startDate.plusDays(3);
        JLabel startField = new JLabel("" + DateFormat.getDateInstance(DateFormat.FULL, Locale.UK).format(startDate));
        startField.setFont(new Font("Arial", Font.ITALIC, 14));
        startLabelPanel.add(startField);
        gridPanel.add(startLabelPanel);
        driverScreenPanel.add(gridPanel);

        //Create return to create game screen button and add it to screen panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton employDriverButton = new JButton("Employ Driver");
        employDriverButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                controllerHandler.getDriverController().employDriver(driverNameField.getText(), companyResponse.getName(), startField.getText());
                //TODO: Employing drivers should cost money.
                controllerHandler.getCompanyController().withdrawOrCreditBalance(0, companyResponse.getPlayerName());
                controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse);
            }
        });
        buttonPanel.add(employDriverButton);
        JButton managementScreenButton = new JButton("Return to Management Screen");
        managementScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                    controlScreen.redrawManagement(managementPanel.createPanel(controlScreen), companyResponse);
            }
        });
        buttonPanel.add(managementScreenButton);
        driverScreenPanel.add(buttonPanel);

        return driverScreenPanel;
	}

}
