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

/**
 * This class controls the loading and saving of game files in TraMS.
 * @author Dave Lee
 */
@Controller
public class FileController {

	private DriverController driverController;

	private CompanyController companyController;

	private MessageController messageController;

	private RouteController routeController;

	private SimulationController simulationController;

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
			//Load game.
			for ( CompanyResponse companyResponse : myFile.getCompanyResponses() ) {
				companyController.loadCompany(companyResponse);
			}
			//Load drivers.
			if ( myFile.getUserResponses() != null ) {
				driverController.loadDrivers(myFile.getUserResponses(), myFile.getCompanyResponses()[0].getName());
			}
			//Load messages.
			if ( myFile.getMessageResponses() != null ) {
				messageController.loadMessages(myFile.getMessageResponses(), myFile.getCompanyResponses()[0].getName());
			}
			//Load routes.
			if ( myFile.getRouteResponses() != null ) {
				routeController.loadRoutes(myFile.getRouteResponses(), myFile.getCompanyResponses()[0].getName());
			}
			//Load vehicles.
			if ( myFile.getVehicleResponses() != null ) {
				vehicleController.loadVehicles(myFile.getVehicleResponses(), myFile.getCompanyResponses()[0].getName(),
						LocalDate.parse(myFile.getCompanyResponses()[0].getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			}
			simulationController.pauseSimulation();
			return myFile.getCompanyResponses()[0];
		} catch ( Exception exception ) {
			logger.error("exception whilst loading file", exception);
			return null;
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
			boolean resultOfDelete = true;
			if ( selectedFile.exists() ) {
				resultOfDelete = selectedFile.delete();
			}
			if ( resultOfDelete && selectedFile.createNewFile() ) {
				final ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new JavaTimeModule());
				//TODO: rebuild the trams file with all classes in a suitable structure.
				mapper.writeValue(selectedFile, TramsFile.builder()
						.build());
				return true;
			}
			return false;
		} catch ( IOException ioException) {
			logger.error("Failure converting to json: " + ioException);
			return false;
		}
    }

	/**
	 * Set the driver controller object via Spring.
	 * @param driverController a <code>DriverController</code> object.
	 */
	@Autowired
	public void setDriverController(final DriverController driverController) {
		this.driverController = driverController;
	}

	/**
	 * Set the company controller object via Spring.
	 * @param companyController a <code>CompanyController</code> object.
	 */
	@Autowired
	public void setCompanyController(final CompanyController companyController) {
		this.companyController = companyController;
	}

	/**
	 * Set the message controller object via Spring.
	 * @param messageController a <code>MessageController</code> object.
	 */
	@Autowired
	public void setMessageController(final MessageController messageController) {
		this.messageController = messageController;
	}

	/**
	 * Set the route controller object via Spring.
	 * @param routeController a <code>RouteController</code> object.
	 */
	@Autowired
	public void setRouteController(final RouteController routeController) {
		this.routeController = routeController;
	}

	/**
	 * Set the simulation object via Spring.
	 * @param simulationController a <code>SimulationController</code> object.
	 */
	@Autowired
	public void setSimulationController(final SimulationController simulationController) {
		this.simulationController = simulationController;
	}

	/**
	 * Set the vehicle controller object via Spring.
	 * @param vehicleController a <code>VehicleController</code> object.
	 */
	@Autowired
	public void setVehicleController(final VehicleController vehicleController) {
		this.vehicleController = vehicleController;
	}
}
