package de.davelee.trams.controllers;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.beans.TramsFile;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.services.FileService;

public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private DriverController driverController;
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private JourneyController journeyController;
	
	@Autowired
	private JourneyPatternController journeyPatternController;
	
	@Autowired
	private MessageController messageController;
	
	@Autowired
	private RouteController routeController;
	
	@Autowired
	private RouteScheduleController routeScheduleController;
	
	@Autowired
	private TimetableController timetableController;
	
	@Autowired
	private VehicleController vehicleController;
	
	/**
     * Load file. 
     * @return a <code>boolean</code> which is true iff the file was loaded successfully.
     */
    public boolean loadFile ( final JFrame currentFrame ) {
        //Create file dialog box.
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Load Game");
        //Only display files with tra extension.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TraMS Saved Games", "tms");
        fileDialog.setFileFilter(filter);
        //Display file dialog.
        int returnVal = fileDialog.showOpenDialog(currentFrame);
        //Check if user submitted file and print coming soon.
        boolean validFile = true;
        if ( returnVal == JFileChooser.APPROVE_OPTION) {
        	TramsFile myFile = fileService.loadFile(fileDialog.getSelectedFile());
            if ( myFile != null ) {
            	reloadDatabaseWithFile(myFile);
                JFrame oldFrame = currentFrame;
                ControlScreen cs = new ControlScreen("", 0, 4, false);
                //cs.drawVehicles(false);
                //WelcomeScreen ws = new WelcomeScreen(this);
                //cs.setVisible(true);
                currentFrame.setVisible(true);
                oldFrame.dispose();
                //Set control screen.
                cs.setVisible(true);
                //Finally, run simulation
                /*isEnd = false;
                theRunningThread = new Thread(this, "simThread");
                theRunningThread.start();*/
                gameController.pauseSimulation();
                //runSimulation(cs, theOperations.getSimulator());
                return true;
            }
            //theScreen.dispose();
            validFile = false;
        }
        if ( !validFile ) {
            JOptionPane.showMessageDialog(currentFrame,"The selected file is not compatible with this version of TraMS.\nYou may want to check the TraMS website for a convertor at http://trams.davelee.me.uk\nPlease either choose another file or create a new game.", "ERROR: Saved Game Could Not Be Loaded", JOptionPane.ERROR_MESSAGE);
        }
        //If we reach here then return false.
        return false;
    }
    
    public void reloadDatabaseWithFile ( TramsFile myFile ) {
    	//TODO: Load file into database!
    }
    
    /**
     * Save file.
     * @return a <code>boolean</code> which is true iff the file was saved successfully.
     */
    public boolean saveFile ( final JFrame currentFrame ) {
        //Create file dialog box.
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Save Game");
        //Only display files with tra extension.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TraMS Saved Games", "tms");
        fileDialog.setFileFilter(filter);
        //Display file dialog.
        int returnVal = fileDialog.showSaveDialog(currentFrame);
        //Check if user submitted file.
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            if ( fileService.saveFile(fileDialog.getSelectedFile(), prepareTramsFile()) ) {
                String fileName = fileDialog.getSelectedFile().getPath();
                if ( !fileName.endsWith(".tms") ) { fileName += ".tms"; }
                JOptionPane.showMessageDialog(currentFrame, "The current simulation has been successfully saved to " + fileName, "File Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            JOptionPane.showMessageDialog(currentFrame, "The file could not be saved. Please try again later.", "ERROR: File Could Not Be Saved", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public TramsFile prepareTramsFile ( ) {
    	return new TramsFile(driverController.getAllDrivers(), gameController.getAllGames(), journeyController.getAllJourneys(),
				journeyPatternController.getAllJourneyPatterns(), messageController.getAllMessages(), routeController.getRouteModels(),
				routeScheduleController.getAllRouteSchedules(), journeyController.getAllStops(), journeyController.getAllStopTimes(),
				timetableController.getAllTimetableModels(), vehicleController.getVehicleModels());
    }

}
