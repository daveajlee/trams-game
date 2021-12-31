package de.davelee.trams.controllers;

import de.davelee.trams.api.request.PurchaseVehicleRequest;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.SortedVehicleResponses;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.VehicleService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;

	public void assignVehicleToTour ( final String registrationNumber, final String allocatedTour, final String company ) {
		VehicleResponse vehicleModel = vehicleService.getVehicleByRegistrationNumber(registrationNumber, company);
		vehicleService.assignVehicleToTour(vehicleModel.getFleetNumber(), allocatedTour, company);
	}

	public VehicleResponse[] getAllCreatedVehicles (final String company ) {
		VehicleResponse[] vehicleModels = vehicleService.getVehicleModels(company);
		Arrays.sort(vehicleModels, new SortedVehicleResponses());
		return vehicleModels;
	}

	public boolean hasVehicleBeenDelivered (final LocalDate deliveryDate, final LocalDate currentDate ) {
		return vehicleService.hasBeenDelivered(deliveryDate, currentDate);
	}

	/**
	 * This method checks if any vehicles have been delivered to the company yet!
	 * @return a <code>boolean</code> which is true iff some vehicles have been delivered!
	 */
	public boolean hasSomeVehiclesBeenDelivered ( final String company, final LocalDate currentDate) {
		VehicleResponse[] vehicleModels = getAllCreatedVehicles(company);
		if ( vehicleModels.length == 0 ) { return false; }
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( hasVehicleBeenDelivered(LocalDate.parse(vehicleModels[i].getDeliveryDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")), currentDate) ) { return true; }
		}
		return false;
	}

	/**
	 * Get a vehicle based on its registration number.
	 * @param registrationNumber a <code>String</code> with the registration number.
	 * @return a <code>VehicleModel</code> object.
	 */
	public VehicleResponse getVehicleByRegistrationNumber ( final String registrationNumber, final String company ) {
		return vehicleService.getVehicleByRegistrationNumber(registrationNumber, company);
	}

	public int getAge (final LocalDate deliveryDate, final LocalDate currentDate ) {
		return vehicleService.getAge(deliveryDate, currentDate);
	}

	public double getValue ( final VehicleResponse vehicleModel, final LocalDate currentDate ) {
		//TODO: Calculate depreciation factor in the server.
		//return vehicleModel.getPurchasePrice() - ((vehicleModel.getDepreciationFactor() * getAge(vehicleModel.getDeliveryDate(), currentDate)) * vehicleModel.getPurchasePrice());
		return 0.0;
	}

	/**
	 * Sell a vehicle.
	 * @param vehicleResponse a <code>VehicleResponse</code> object representing the vehicle to sell.
	 */
	public double sellVehicle ( final VehicleResponse vehicleResponse, final LocalDate currentDate ) {
		vehicleService.removeVehicle(vehicleResponse.getCompany(), vehicleResponse.getFleetNumber());
		return getValue(vehicleResponse, currentDate);
	}

	/**
	 * Purchase a new vehicle.
	 * @param type a <code>String</code> with the vehicle type.
	 * @param company a <code>String</code> with the name of the company.
	 * @return a <code>double</code> with the purchase price of the vehicle.
	 */
	public double purchaseVehicle ( final String type, final String company, final int year, final Optional<Integer> fleetNumber) {
		Random random = new Random();
		int fleetNum = fleetNumber.isPresent() ? fleetNumber.get() : random.nextInt();
		return vehicleService.saveVehicle(PurchaseVehicleRequest.builder()
				.company(company)
				.fleetNumber("" + fleetNum)
				.livery("Green with Red text")
				.vehicleType("BUS")
				.modelName(type)
				.additionalTypeInformationMap(Map.of("Registration Number", vehicleService.generateRandomReg(
						year, company)))
				.seatingCapacity(50)
				.standingCapacity(95)
				.build());
	}

	public VehicleResponse getVehicleByAllocatedTour ( final String allocatedTour, final String company ) {
		return vehicleService.getVehicleByAllocatedTour(allocatedTour, company);
	}

	public int createSuppliedVehicles(final ScenarioModel scenarioModel, final LocalDate currentDate, final String company) {
		Iterator<String> vehicleModels = scenarioModel.getSuppliedVehicles().keySet().iterator();
		int numCreatedVehicles = 0;
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < scenarioModel.getSuppliedVehicles().get(vehicleModel); i++ )  {
				int fleetNumber = 100 + i;
				purchaseVehicle(vehicleModel, company, currentDate.getYear(), Optional.of(fleetNumber));
				numCreatedVehicles++;
			}
		}
		return numCreatedVehicles;
	}

	public VehicleResponse[] getVehicleModels ( final String company ) {
		return vehicleService.getVehicleModels(company);
	}

	public VehicleResponse[] getVehicleModelsForRoute ( final String company, final String routeNumber ) {
		//TODO: implement call to REST service to return the vehicles.
		return new VehicleResponse[0];
	}

	public String getCurrentStopName (final VehicleResponse vehicleModel, final LocalDateTime currentDateTime, final DifficultyLevel difficultyLevel ) {
		//TODO: implement method completely.
		return "";
	}

	public String getDestination ( final VehicleResponse vehicleModel, final LocalDateTime currentDateTime, final DifficultyLevel difficultyLevel ) {
		//TODO: implement method completely.
		return "";
	}

	public void shortenSchedule ( final VehicleResponse vehicleModel, final String newDestination, final LocalDateTime currentDateTime ) {
		//TODO: implement shorten schedule correctly.
	}

	public void outOfService ( final VehicleResponse vehicleModel, final String restartStop, final LocalDateTime currentDateTime,
							   final DifficultyLevel difficultyLevel ) {
		final String currentStopName = getCurrentStopName(vehicleModel, currentDateTime, difficultyLevel);
	}

	public List<String> getAllocations ( final String company ) {
		return vehicleService.getAllocations(company);
	}

	public String getFirstVehicleModel ( final String company ) {
		return vehicleService.getFirstVehicleModel(company);
	}

	public String getLastVehicleModel ( final String company ) {
		return vehicleService.getLastVehicleModel(company);
	}

	public String getPreviousVehicleModel ( final String model, final String company ) {
		return vehicleService.getPreviousVehicleModel(model, company);
	}

	public String getNextVehicleModel ( final String model, final String company) {
		return vehicleService.getNextVehicleModel(model, company);
	}

	/**
	 * Load Vehicles.
	 * @param vehicleModels an array of <code>VehicleModel</code> objects with vehicles to store and delete all other vehicles.
	 */
	public void loadVehicles ( final VehicleResponse[] vehicleModels, final String company, final LocalDate currentDate ) {
		vehicleService.deleteAllVehicles(company);
		for ( VehicleResponse vehicleModel : vehicleModels ) {
			purchaseVehicle(vehicleModel.getModelName(), vehicleModel.getCompany(), currentDate.getYear(), Optional.of(Integer.parseInt(vehicleModel.getFleetNumber())));
		}
	}

}
