package de.davelee.trams.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.controllers.GameController;
import de.davelee.trams.controllers.JourneyPatternController;
import de.davelee.trams.controllers.TimetableController;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.util.DateFormats;

public class TimetablePanel {
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private TimetableController timetableController;
	
	@Autowired
	private JourneyPatternController journeyPatternController;

    @Autowired
	private JourneyPatternPanel journeyPatternPanel;
	
	private JButton createJourneyPatternButton;
	private JButton deleteJourneyPatternButton;
	
	public JPanel createPanel ( final TimetableModel timetableModel, final RouteModel routeModel, final ControlScreen controlScreen, final RoutePanel routePanel, final DisplayPanel displayPanel ) {
		final GameModel gameModel = gameController.getGameModel();
        
        //Create timetableScreen panel to add things to.
        JPanel timetableScreenPanel = new JPanel();
        timetableScreenPanel.setLayout ( new BoxLayout ( timetableScreenPanel, BoxLayout.PAGE_AXIS ) );
        timetableScreenPanel.setBackground(Color.WHITE);
        
        //Create label at top of screen in a topLabelPanel added to screenPanel.
        JPanel topLabelPanel = new JPanel(new BorderLayout());
        topLabelPanel.setBackground(Color.WHITE);
        JLabel topLabel = new JLabel("Create New Timetable", SwingConstants.CENTER);
        if ( timetableModel != null ) {
            topLabel.setText("Modify Timetable");
        }
        topLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topLabel.setVerticalAlignment(JLabel.CENTER);
        topLabelPanel.add(topLabel, BorderLayout.CENTER);
        timetableScreenPanel.add(topLabelPanel);
        
        //Create timetable name panel.
        JPanel timetableNamePanel = new JPanel(new GridBagLayout());
        timetableNamePanel.setBackground(Color.WHITE);
        JLabel timetableNameLabel = new JLabel("Name: ");
        timetableNameLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        timetableNamePanel.add(timetableNameLabel);
        final JTextField timetableNameField = new JTextField(20);
        if ( timetableModel != null ) { 
        	timetableNameField.setText(timetableModel.getName()); 
        }
        timetableNameField.addKeyListener(new KeyListener()  {
            public void keyReleased(KeyEvent e) {
                if ( !timetableNameField.getText().equalsIgnoreCase("") ) {
                    createJourneyPatternButton.setEnabled(true);
                }
                else {
                    createJourneyPatternButton.setEnabled(false);
                }
            }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) { }
        });
        timetableNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        timetableNamePanel.add(timetableNameField);
        timetableScreenPanel.add(timetableNamePanel);
                
        //Create panel for validity first of all.
        JPanel validityPanel = new JPanel(new GridBagLayout());
        validityPanel.setBackground(Color.WHITE);
        JLabel validFromLabel = new JLabel("Valid From: ", SwingConstants.CENTER);
        validFromLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validFromLabel);
        //Get the calendar object with current time.
        Calendar currTime = (Calendar) gameModel.getCurrentTime().clone();
        currTime.add(Calendar.HOUR, 0); //TODO: Change this to 48!!!!
        //Valid From Day.
        final int fromStartDay = currTime.get(Calendar.DAY_OF_MONTH);
        final DefaultComboBoxModel validFromDayModel = new DefaultComboBoxModel();
        for ( int i = currTime.get(Calendar.DAY_OF_MONTH); i <= currTime.getActualMaximum(Calendar.MONTH); i++ ) {
            validFromDayModel.addElement(i);
        }
        JComboBox validFromDayBox = new JComboBox(validFromDayModel);
        if ( timetableModel != null ) {
            validFromDayBox.setSelectedItem(timetableModel.getValidFromDate().get(Calendar.DAY_OF_MONTH));
        }
        validFromDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validFromDayBox);
        //Valid From Month.
        final JComboBox validFromMonthBox = new JComboBox();
        for ( int i = 0; i < 4; i++ ) {
            validFromMonthBox.addItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(currTime));
            currTime.add(Calendar.MONTH, 1);
        }
        if ( timetableModel != null ) {
            validFromMonthBox.setSelectedItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(timetableModel.getValidFromDate()));
        }
        validFromMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validFromMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int month = validFromMonthBox.getSelectedIndex();
                if ( validFromMonthBox.getSelectedIndex() == 0 ) {
                    validFromDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = fromStartDay; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validFromDayModel.addElement(i);
                    }
                }
                else {
                    validFromDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = 1; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validFromDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(validFromMonthBox);
        //Valid to!!!
        JLabel validToLabel = new JLabel("Valid To: ", SwingConstants.CENTER);
        validToLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        validityPanel.add(validToLabel);
        //Get the calendar object with current time.
        Calendar myCurrTime = (Calendar) gameModel.getCurrentTime().clone();
        myCurrTime.add(Calendar.HOUR, 72);
        //Valid To Day.
        final int toStartDay = myCurrTime.get(Calendar.DAY_OF_MONTH);
        final DefaultComboBoxModel validToDayModel = new DefaultComboBoxModel();
        for ( int i = myCurrTime.get(Calendar.DAY_OF_MONTH); i <= myCurrTime.getActualMaximum(Calendar.MONTH); i++ ) {
            validToDayModel.addElement(i);
        }
        JComboBox validToDayBox = new JComboBox(validToDayModel);
        if ( timetableModel != null ) {
            validToDayBox.setSelectedItem(timetableModel.getValidToDate().get(Calendar.DAY_OF_MONTH));
        }
        validToDayBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validityPanel.add(validToDayBox);
        //Valid To Month.
        final JComboBox validToMonthBox = new JComboBox();
        for ( int i = 0; i < 25; i++ ) {
            validToMonthBox.addItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(myCurrTime));
            myCurrTime.add(Calendar.MONTH, 1);
        }
        if ( timetableModel != null ) {
            validToMonthBox.setSelectedItem(DateFormats.MONTH_YEAR_FORMAT.getFormat().format(timetableModel.getValidToDate()));
        }
        validToMonthBox.setFont(new Font("Arial", Font.PLAIN, 14));
        validToMonthBox.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                int month = validToMonthBox.getSelectedIndex();
                if ( validToMonthBox.getSelectedIndex() == 0 ) {
                    validToDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = toStartDay; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validToDayModel.addElement(i);
                    }
                }
                else {
                    validToDayModel.removeAllElements();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, month);
                    for ( int i = 1; i <= cal.getActualMaximum(Calendar.MONTH); i++ ) {
                        validToDayModel.addElement(i);
                    }
                }
            } 
        });
        validityPanel.add(validToMonthBox);
       
        //Add validityPanel to the screen panel.
        timetableScreenPanel.add(validityPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create label in middle of screen in a middleLabelPanel added to screenPanel.
        JPanel middleLabelPanel = new JPanel(new BorderLayout());
        middleLabelPanel.setBackground(Color.WHITE);
        JLabel middleLabel = new JLabel("Service Pattern(s)", SwingConstants.CENTER);
        middleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        middleLabel.setVerticalAlignment(JLabel.CENTER);
        middleLabelPanel.add(middleLabel, BorderLayout.CENTER);
        timetableScreenPanel.add(middleLabelPanel);
        
        //Create the journeyPattern list panel and three buttons.
        JPanel journeyPatternListPanel = new JPanel(new BorderLayout());
        journeyPatternListPanel.setBackground(Color.WHITE);
        //Here is the actual service pattern list.
        JPanel centreJourneyPatternListPanel = new JPanel(new GridBagLayout());
        centreJourneyPatternListPanel.setBackground(Color.WHITE);
        DefaultListModel journeyPatternModel = new DefaultListModel();
        //Now get all the journey pattern which we have at the moment.
        TimetableModel journeyTimetableModel = timetableController.getRouteTimetable(routeModel, timetableNameField.getText());
        JourneyPatternModel[] journeyPatternModels = journeyPatternController.getJourneyPatternModels(journeyTimetableModel, routeModel.getRouteNumber());
        for ( int i = 0; i < journeyPatternModels.length; i++ ) {
            journeyPatternModel.addElement(journeyPatternModels[i].getName());
        }
        JList journeyPatternList = new JList(journeyPatternModel);
        if ( journeyPatternModel.getSize() > 0 ) { journeyPatternList.setSelectedIndex(0); } 
        journeyPatternList.setVisibleRowCount(3);
        journeyPatternList.setFixedCellWidth(450);
        journeyPatternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timetablePane = new JScrollPane(journeyPatternList);
        centreJourneyPatternListPanel.add(timetablePane);
        journeyPatternListPanel.add(centreJourneyPatternListPanel, BorderLayout.CENTER);
        //Now the three create, modify and delete button.
        JPanel journeyPatternButtonPanel = new JPanel();
        journeyPatternButtonPanel.setBackground(Color.WHITE);
        createJourneyPatternButton = new JButton("Create");
        if (timetableNameField.getText().equalsIgnoreCase("")) { createJourneyPatternButton.setEnabled(false); }
        createJourneyPatternButton.addActionListener(new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
            	TimetableModel selectedTimetableModel = timetableController.getRouteTimetable(routeModel, timetableNameField.getText());
                //Create relevant calendar object.
                if ( selectedTimetableModel == null) {
                    int vfYear = Integer.parseInt(validFromMonthBox.getSelectedItem().toString().split(" ")[1]);
                    int vfMonth = validFromMonthBox.getSelectedIndex();
                    int vfDay = Integer.parseInt(validFromDayModel.getSelectedItem().toString());
                    GregorianCalendar validFrom = new GregorianCalendar(vfYear, vfMonth, vfDay);
                    int vtYear = Integer.parseInt(validToMonthBox.getSelectedItem().toString().split(" ")[1]);
                    int vtMonth = validToMonthBox.getSelectedIndex();
                    int vtDay = Integer.parseInt(validToDayModel.getSelectedItem().toString());
                    GregorianCalendar validTo = new GregorianCalendar(vtYear, vtMonth, vtDay);
                    //Save this timetable with valid dates first.
                    timetableController.createTimetable(timetableNameField.getText(), validFrom, validTo, routeModel);
                    //logger.debug("Adding timetable with name " + theTimetableNameField.getText() + " to route " + theSelectedRoute.getRouteNumber());
                }
                //Show the actual screen!
                controlScreen.redrawManagement(journeyPatternPanel.createPanel(routeModel.getStopNames(), selectedTimetableModel, null, routeModel, controlScreen, displayPanel), gameModel);
            }
        });
        journeyPatternButtonPanel.add(createJourneyPatternButton);
        JButton modifyJourneyPatternButton = new JButton("Modify");
        //TODO: reimplement modify!
        /*modifyJourneyPatternButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                long journeyPatternId = routeService.getRouteById(selectedRouteId).
                		getTimetable(timetableNameField.getText()).getJourneyPattern(journeyPatternList.
                				getSelectedValue().toString()).getId();
                //Process the stops.
                ArrayList<String> stops = new ArrayList<String>();
                for ( int i = 0; i < stopBoxes.length; i++ ) {
                    if ( !stopBoxes[i].getSelectedItem().toString().equalsIgnoreCase("-") ) {
                        stops.add(stopBoxes[i].getSelectedItem().toString());
                    }
                }
                controlScreen.redrawManagement(ManagePanel.this.makeJourneyPatternPanel(stops, timetableNameField.getText(), journeyPatternId));
            }
        });*/
        if ( journeyPatternModel.getSize() == 0 ) { modifyJourneyPatternButton.setEnabled(false); }
        journeyPatternButtonPanel.add(modifyJourneyPatternButton);
        //TODO: reimplement delete.
        /*deleteJourneyPatternButton = new JButton("Delete");
        deleteJourneyPatternButton.addActionListener( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                routeService.getRouteById(selectedRouteId).getTimetable(timetableNameField.getText()).
                	deleteJourneyPattern(journeyPatternList.getSelectedValue().toString());
                journeyPatternModel.removeElement(journeyPatternList.getSelectedValue());
                if ( journeyPatternModel.getSize() == 0 ) {
                    deleteJourneyPatternButton.setEnabled(false);
                    modifyJourneyPatternButton.setEnabled(false);
                }
                else {
                    journeyPatternList.setSelectedIndex(0);
                }
            }
        });*/
        if ( journeyPatternModel.getSize() == 0 ) { deleteJourneyPatternButton.setEnabled(false); }
        journeyPatternButtonPanel.add(deleteJourneyPatternButton);
        journeyPatternListPanel.add(journeyPatternButtonPanel, BorderLayout.SOUTH);
        //Add the journeyPatternList panel to the screen panel.
        timetableScreenPanel.add(journeyPatternListPanel);
        timetableScreenPanel.add(Box.createRigidArea(new Dimension(0,10))); //Spacer.
        
        //Create bottom button panel for next two buttons.
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setBackground(Color.WHITE);
        
        //Create new route button and add it to screen panel.
        JButton createTimetableButton = new JButton("Create Timetable");
        if(journeyPatternModel.getSize()==0) { createTimetableButton.setEnabled(false); }
        createTimetableButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Timetable is already saved so we just go back to the original screen.
                controlScreen.redrawManagement(routePanel.createPanel(routeModel, controlScreen, displayPanel), gameModel);
            }
        });
        bottomButtonPanel.add(createTimetableButton);
        JButton previousScreenButton = new JButton("Return to Previous Screen");
        previousScreenButton.addActionListener ( new ActionListener() {
            public void actionPerformed ( ActionEvent e ) {
                //Cancel addition.
                controlScreen.redrawManagement(routePanel.createPanel(routeModel, controlScreen, displayPanel), gameModel);
            }
        });
        bottomButtonPanel.add(previousScreenButton);
        
        //Add bottom button panel to the screen panel.
        timetableScreenPanel.add(bottomButtonPanel);
        
        //Return timetableScreenPanel.
        return timetableScreenPanel;
	}

}