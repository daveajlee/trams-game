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
	private StopController journeyController;
	
	@Autowired
	private MessageController messageController;
	
	@Autowired
	private RouteController routeController;
	
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
    	//Load messages.
		if ( myFile.getMessageModels() != null ) {
			messageController.loadMessages(myFile.getMessageModels(), myFile.getGameModel()[0].getCompany());
		}
    	//Load routes.
		if ( myFile.getRouteModels() != null ) {
			routeController.loadRoutes(myFile.getRouteModels(), myFile.getGameModel()[0].getCompany());
		}
    	//Load vehicles.
		if ( myFile.getVehicleModels() != null ) {
			vehicleController.loadVehicles(myFile.getVehicleModels(), myFile.getGameModel()[0].getCompany(), myFile.getGameModel()[0].getCurrentDateTime().toLocalDate());
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
		//TODO: rebuild the trams file with all classes in a suitable structure.
    	return TramsFile.builder()
				/*.driverModels(driverController.getAllDrivers(gameController.getGameModel().getCompany()))
				.gameModel(new GameModel[] { gameController.getGameModel() })
				.messageModels(messageController.getAllMessages(gameController.getGameModel().getCompany()))
				.routeModels(routeController.getRouteModels(gameController.getGameModel().getCompany()))
				.stops(journeyController.getAllStops(gameController.getGameModel().getCompany()))
				.vehicleModels(vehicleController.getVehicleModels(gameController.getGameModel().getCompany()))*/
				.build();
    }

}
