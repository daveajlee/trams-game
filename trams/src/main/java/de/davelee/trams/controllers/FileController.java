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
		//Load drivers.
		if ( myFile.getDriverModels() != null ) {
			driverController.loadDrivers(myFile.getDriverModels(), myFile.getGameModel()[0].getCompany());
		}
    	//Load journeys.
		if ( myFile.getJourneyModels() != null ) {
			journeyController.loadJourneys(myFile.getJourneyModels());
		}
		//Load journey patterns.
		if ( myFile.getJourneyPatternModels() != null ) {
			journeyPatternController.loadJourneyPatterns(myFile.getJourneyPatternModels());
		}
    	//Load messages.
		if ( myFile.getMessageModels() != null ) {
			messageController.loadMessages(myFile.getMessageModels(), myFile.getGameModel()[0].getCompany());
		}
    	//Load routes.
		if ( myFile.getRouteModels() != null ) {
			routeController.loadRoutes(myFile.getRouteModels(), myFile.getGameModel()[0].getCompany());
		}
    	//Load route schedules.
		if ( myFile.getRouteScheduleModels() != null ) {
			routeScheduleController.loadRouteSchedules(myFile.getRouteScheduleModels());
		}
    	//Load timetables.
		if ( myFile.getTimetableModels() != null ) {
			timetableController.loadTimetables(myFile.getTimetableModels());
		}
    	//Load vehicles.
		if ( myFile.getVehicleModels() != null ) {
			vehicleController.loadVehicles(myFile.getVehicleModels(), myFile.getGameModel()[0].getCompany());
		}
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
    	return TramsFile.builder()
				.driverModels(driverController.getAllDrivers(gameController.getGameModel().getCompany()))
				.gameModel(new GameModel[] { gameController.getGameModel() })
				.journeyModels(journeyController.getAllJourneys())
				.journeyPatternModels(journeyPatternController.getAllJourneyPatterns())
				.messageModels(messageController.getAllMessages(gameController.getGameModel().getCompany()))
				.routeModels(routeController.getRouteModels(gameController.getGameModel().getCompany()))
				.routeScheduleModels(routeScheduleController.getAllRouteSchedules())
				.stops(journeyController.getAllStops(gameController.getGameModel().getCompany()))
				.timetableModels(timetableController.getAllTimetableModels())
				.vehicleModels(vehicleController.getVehicleModels(gameController.getGameModel().getCompany()))
				.build();
    }

}
