package de.davelee.trams.controllers;

import de.davelee.trams.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.beans.TramsFile;
import de.davelee.trams.services.FileService;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
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
	 * @param selectedFile a <code>File</code> object with the path to load the file from.
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
    	//Load game.
    	for ( GameModel gameModel : myFile.getGameModel() ) {
    		gameController.loadGameModel(gameModel);
		}
		//TODO: load other parts of the file.
		myFile.getDriverModels();
    	myFile.getJourneyModels();
    	myFile.getJourneyPatternModels();
    	myFile.getMessageModels();
    	myFile.getRouteModels();
    	myFile.getRouteScheduleModels();
    	myFile.getStops();
    	myFile.getStopTimeModels();
    	myFile.getTimetableModels();
    	myFile.getVehicleModels();
    }
    
    /**
     * Save file.
	 * @param selectedFile a <code>File</code> object with the path to save the file to.
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
