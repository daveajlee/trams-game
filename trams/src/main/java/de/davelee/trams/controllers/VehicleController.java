package de.davelee.trams.controllers;

import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.SortedVehicleModels;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.VehicleService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;

	public void assignVehicleToTour ( final String registrationNumber, final String allocatedTour, final String company ) {
		VehicleModel vehicleModel = vehicleService.getVehicleByRegistrationNumber(registrationNumber, company);
		vehicleService.assignVehicleToTour(vehicleModel, allocatedTour, company);
	}

	public VehicleModel[] getAllCreatedVehicles ( final String company ) {
		VehicleModel[] vehicleModels = vehicleService.getVehicleModels(company);
		Arrays.sort(vehicleModels, new SortedVehicleModels());
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
		VehicleModel[] vehicleModels = getAllCreatedVehicles(company);
		if ( vehicleModels.length == 0 ) { return false; }
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( hasVehicleBeenDelivered(vehicleModels[i].getDeliveryDate(), currentDate) ) { return true; }
		}
		return false;
	}

	/**
	 * Get a vehicle based on its registration number.
	 * @param registrationNumber a <code>String</code> with the registration number.
	 * @return a <code>VehicleModel</code> object.
	 */
	public VehicleModel getVehicleByRegistrationNumber ( final String registrationNumber, final String company ) {
		return vehicleService.getVehicleByRegistrationNumber(registrationNumber, company);
	}

	public int getAge (final LocalDate deliveryDate, final LocalDate currentDate ) {
		return vehicleService.getAge(deliveryDate, currentDate);
	}

	public double getValue ( final VehicleModel vehicleModel, final LocalDate currentDate ) {
		return vehicleService.getValue(vehicleModel.getPurchasePrice(), vehicleModel.getDepreciationFactor(),
			vehicleModel.getDeliveryDate(), currentDate);
	}

	/**
	 * Sell a vehicle.
	 * @param vehicleModel a <code>VehicleModel</code> object representing the vehicle to sell.
	 */
	public double sellVehicle ( final VehicleModel vehicleModel, final LocalDate currentDate ) {
		vehicleService.removeVehicle(vehicleModel);
		return vehicleService.getValue(vehicleModel.getPurchasePrice(), vehicleModel.getDepreciationFactor(), vehicleModel.getDeliveryDate(), currentDate);
	}

	/**
	 * Purchase a new vehicle.
	 * @param type a <code>String</code> with the vehicle type.
	 * @param deliveryDate a <code>LocalDate</code> with the delivery date.
	 * @return a <code>double</code> with the purchase price of the vehicle.
	 */
	public double purchaseVehicle ( final String type, final LocalDate deliveryDate, final String company, final int year ) {
		VehicleModel vehicle = vehicleService.createVehicleObject(type, vehicleService.generateRandomReg(
		year, company), deliveryDate, company);
		vehicleService.saveVehicle(vehicle);
		return vehicle.getPurchasePrice();
	}

	public VehicleModel getVehicleByAllocatedTour ( final String allocatedTour, final String company ) {
		return vehicleService.getVehicleByAllocatedTour(allocatedTour, company);
	}

	public int createSuppliedVehicles(final ScenarioModel scenarioModel, final LocalDate currentDate, final String company) {
		Iterator<String> vehicleModels = scenarioModel.getSuppliedVehicles().keySet().iterator();
		int numCreatedVehicles = 0;
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < scenarioModel.getSuppliedVehicles().get(vehicleModel); i++ )  {
				int fleetNumber = 100 + i;
				vehicleService.saveVehicle(
						VehicleModel.builder()
								.company(company)
								.deliveryDate(currentDate)
								.registrationNumber(vehicleService.generateRandomReg(
										currentDate.getYear(), company))
								.fleetNumber("" + fleetNumber)
								.model(vehicleModel)
								.livery("Green with Red text")
								.seatingCapacity(50)
								.standingCapacity(95)
								.build());
				numCreatedVehicles++;
			}
		}
		return numCreatedVehicles;
	}

	public VehicleModel[] getVehicleModels ( final String company ) {
		return vehicleService.getVehicleModels(company);
	}

	public VehicleModel[] getVehicleModelsForRoute ( final String company, final String routeNumber ) {
		//TODO: implement call to REST service to return the vehicles.
		return new VehicleModel[0];
	}

	public String getCurrentStopName (final VehicleModel vehicleModel, final LocalDateTime currentDateTime, final DifficultyLevel difficultyLevel ) {
		//TODO: implement method completely.
		return "";
	}

	public String getDestination ( final VehicleModel vehicleModel, final LocalDateTime currentDateTime, final DifficultyLevel difficultyLevel ) {
		//TODO: implement method completely.
		return "";
	}

	public void shortenSchedule ( final VehicleModel vehicleModel, final String newDestination, final LocalDateTime currentDateTime ) {
		//TODO: implement shorten schedule correctly.
	}

	public void outOfService ( final VehicleModel vehicleModel, final String restartStop, final LocalDateTime currentDateTime,
							   final DifficultyLevel difficultyLevel ) {
		final String currentStopName = getCurrentStopName(vehicleModel, currentDateTime, difficultyLevel);
	}

	public List<String> getAllocations ( final String company ) {
		return vehicleService.getAllocations(company);
	}

	public VehicleModel getVehicleByModel ( final String model, final String company ) {
		return vehicleService.createVehicleObject(model, vehicleService.generateRandomReg(LocalDate.now().getYear(), company), LocalDate.now(), company);
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
	public void loadVehicles ( final VehicleModel[] vehicleModels, final String company ) {
		vehicleService.deleteAllVehicles(company);
		for ( VehicleModel vehicleModel : vehicleModels ) {
			vehicleService.saveVehicle(vehicleModel);
		}
	}

}
