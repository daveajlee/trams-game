package de.davelee.trams.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.davelee.trams.api.response.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

	private ExportController exportController;

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
			ExportCompanyResponse myFile = mapper.readValue(out.toString(), ExportCompanyResponse.class);
			//Load game.
			CompanyResponse companyResponse = CompanyResponse.builder()
					.name(myFile.getName())
					.playerName(myFile.getPlayerName())
					.satisfactionRate(myFile.getSatisfactionRate())
					.scenarioName(myFile.getScenarioName())
					.difficultyLevel(myFile.getDifficultyLevel())
					.time(myFile.getTime())
					.balance(myFile.getBalance())
					.build();
			companyController.loadCompany(companyResponse);
			//Load drivers.
			if ( !StringUtils.isBlank(myFile.getDrivers()) ) {
				driverController.loadDrivers(mapper.readValue(myFile.getDrivers(), UserResponse[].class), myFile.getName());
			}
			//Load messages.
			if ( !StringUtils.isBlank(myFile.getMessages()) ) {
				messageController.loadMessages(mapper.readValue(myFile.getMessages(), MessageResponse[].class), myFile.getName());
			}
			//Load routes.
			if ( !StringUtils.isBlank(myFile.getRoutes()) ) {
				routeController.loadRoutes(mapper.readValue(myFile.getRoutes(), RouteResponse[].class), myFile.getName());
			}
			//Load vehicles.
			if ( !StringUtils.isBlank(myFile.getVehicles()) ) {
				vehicleController.loadVehicles(mapper.readValue(myFile.getVehicles(), VehicleResponse[].class), myFile.getName(),
						LocalDate.parse(myFile.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
			}
			simulationController.pauseSimulation();
			return companyResponse;
		} catch ( Exception exception ) {
			logger.error("exception whilst loading file", exception);
			return null;
		}
    }
    
    /**
     * Save file - i.e. generate an export of all data for this company.
	 * @param selectedFile a <code>File</code> object with the path to save the file to.
	 * @param company a <code>String</code> containing the name of the company to export data for.
	 * @param playerName a <code>String</code> containing the player name of the company to export data for.
     * @return a <code>boolean</code> which is true iff the file was saved successfully.
     */
    public boolean saveFile ( final File selectedFile, final String company, final String playerName ) {
		//Output json.
		try {
			boolean resultOfDelete = true;
			if ( selectedFile.exists() ) {
				resultOfDelete = selectedFile.delete();
			}
			if ( resultOfDelete && selectedFile.createNewFile() ) {
				//Create object mapper to do exports to JSON.
				final ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new JavaTimeModule());
				//Get all drivers for the company.
				String drivers = mapper.writeValueAsString(driverController.getAllDrivers(company));
				//Get all messages for the company.
				String messages = mapper.writeValueAsString(messageController.getAllMessages(company));
				//Get all routes and vehicles for the company.
				ExportResponse exportResponse = exportController.getExport(company);
				String routes = mapper.writeValueAsString(exportResponse.getRouteResponses());
				String vehicles = mapper.writeValueAsString(exportResponse.getVehicleResponses());
				//Now send this information to trams-business and get content.
				ExportCompanyResponse exportCompanyResponse = companyController.exportCompany(company, playerName, drivers, messages, routes, vehicles);
				//Convert this content to JSON and save.
				mapper.writeValue(selectedFile, exportCompanyResponse);
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
	 * Set the export controller object via Spring.
	 * @param exportController a <code>ExportController</code> object.
	 */
	@Autowired
	public void setExportController(final ExportController exportController) {
		this.exportController = exportController;
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
