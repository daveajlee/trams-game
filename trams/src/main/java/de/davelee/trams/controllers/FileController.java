package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.beans.TramsFile;
import de.davelee.trams.services.FileService;

import java.io.File;

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
    public boolean loadFile ( final File selectedFile ) {
        TramsFile myFile = fileService.loadFile(selectedFile);
        if ( myFile != null ) {
            reloadDatabaseWithFile(myFile);
            gameController.pauseSimulation();
            return true;
        }
        return false;
    }
    
    public void reloadDatabaseWithFile ( TramsFile myFile ) {
    	//TODO: Load file into database!
    }
    
    /**
     * Save file.
     * @return a <code>boolean</code> which is true iff the file was saved successfully.
     */
    public boolean saveFile ( final File selectedFile ) {
        return fileService.saveFile(selectedFile, prepareTramsFile());
    }
    
    public TramsFile prepareTramsFile ( ) {
    	return new TramsFile(driverController.getAllDrivers(), gameController.getAllGames(), journeyController.getAllJourneys(),
				journeyPatternController.getAllJourneyPatterns(), messageController.getAllMessages(), routeController.getRouteModels(),
				routeScheduleController.getAllRouteSchedules(), journeyController.getAllStops(), journeyController.getAllStopTimes(),
				timetableController.getAllTimetableModels(), vehicleController.getVehicleModels());
    }

}
