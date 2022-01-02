package de.davelee.trams.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.davelee.trams.api.response.CompanyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.beans.TramsFile;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class FileController {
	
	@Autowired
	private DriverController driverController;
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private MessageController messageController;
	
	@Autowired
	private RouteController routeController;
	
	@Autowired
	private VehicleController vehicleController;

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	/**
     * Load file.
	 * @param selectedFile a <code>File</code> object with the path to load the file from.
     * @return a <code>boolean</code> which is true iff the file was loaded successfully.
     */
    public CompanyResponse loadFile ( final File selectedFile ) {
		//Define json importer.
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile.getAbsolutePath())));
			StringBuilder out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);   // add everything to StringBuilder
			}
			TramsFile myFile = mapper.readValue(out.toString(), TramsFile.class);
			reloadDatabaseWithFile(myFile);
			gameController.pauseSimulation();
			return myFile.getGameModel()[0];
		} catch ( Exception exception ) {
			logger.error("exception whilst loading file", exception);
			return null;
		}
    }
    
    public void reloadDatabaseWithFile ( TramsFile myFile ) {
    	//Load game.
    	for ( CompanyResponse companyResponse : myFile.getGameModel() ) {
    		gameController.loadGameModel(companyResponse);
		}
		//Load drivers.
		if ( myFile.getDriverModels() != null ) {
			driverController.loadDrivers(myFile.getDriverModels(), myFile.getGameModel()[0].getName());
		}
    	//Load messages.
		if ( myFile.getMessageModels() != null ) {
			messageController.loadMessages(myFile.getMessageModels(), myFile.getGameModel()[0].getName());
		}
    	//Load routes.
		if ( myFile.getRouteModels() != null ) {
			routeController.loadRoutes(myFile.getRouteModels(), myFile.getGameModel()[0].getName());
		}
    	//Load vehicles.
		if ( myFile.getVehicleModels() != null ) {
			vehicleController.loadVehicles(myFile.getVehicleModels(), myFile.getGameModel()[0].getName(),
					LocalDate.parse(myFile.getGameModel()[0].getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		}
    }
    
    /**
     * Save file.
	 * @param selectedFile a <code>File</code> object with the path to save the file to.
     * @return a <code>boolean</code> which is true iff the file was saved successfully.
     */
    public boolean saveFile ( final File selectedFile ) {
		//Output json.
		try {
			if ( selectedFile.exists() ) {
				selectedFile.delete();
			}
			if ( selectedFile.createNewFile() ) {
				final ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new JavaTimeModule());
				mapper.writeValue(selectedFile, prepareTramsFile());
				return true;
			}
			return false;
		} catch ( IOException ioException) {
			logger.error("Failure converting to json: " + ioException);
			return false;
		}
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
