package de.davelee.trams.controllers;

import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.SortedVehicleModels;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.VehicleService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private GameController gameController;

	public void assignVehicleToRouteSchedule ( final String registrationNumber, final String routeNumber, final String scheduleNumber ) {
		VehicleModel vehicleModel = vehicleService.getVehicleByRegistrationNumber(registrationNumber);
		vehicleService.assignVehicleToRouteScheduleNumber(vehicleModel, routeNumber, scheduleNumber);
	}

	public VehicleModel[] getAllCreatedVehicles ( ) {
		VehicleModel[] vehicleModels = vehicleService.getVehicleModels();
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
	public boolean hasSomeVehiclesBeenDelivered ( ) {
		VehicleModel[] vehicleModels = getAllCreatedVehicles();
		GameModel gameModel = gameController.getGameModel();
		if ( vehicleModels.length == 0 ) { return false; }
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( hasVehicleBeenDelivered(vehicleModels[i].getDeliveryDate(), gameModel.getCurrentDateTime().toLocalDate()) ) { return true; }
		}
		return false;
	}

	/**
	 * Get a vehicle based on its registration number.
	 * @param registrationNumber a <code>String</code> with the registration number.
	 * @return a <code>VehicleModel</code> object.
	 */
	public VehicleModel getVehicleByRegistrationNumber ( final String registrationNumber ) {
		return vehicleService.getVehicleByRegistrationNumber(registrationNumber);
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
	public void sellVehicle ( final VehicleModel vehicleModel ) {
		GameModel gameModel = gameController.getGameModel();
		gameController.creditBalance(vehicleService.getValue(vehicleModel.getPurchasePrice(), vehicleModel.getDepreciationFactor(), vehicleModel.getDeliveryDate(), gameModel.getCurrentDateTime().toLocalDate()));
		vehicleService.removeVehicle(vehicleModel);
	}

	/**
	 * Purchase a new vehicle.
	 * @param type a <code>String</code> with the vehicle type.
	 * @param deliveryDate a <code>LocalDate</code> with the delivery date.
	 */
	public void purchaseVehicle ( final String type, final LocalDate deliveryDate ) {
		GameModel gameModel = gameController.getGameModel();
		VehicleModel vehicle = vehicleService.createVehicleObject(type, vehicleService.generateRandomReg(
		gameModel.getCurrentDateTime().getYear()), deliveryDate);
		gameController.withdrawBalance(vehicle.getPurchasePrice());
		vehicleService.saveVehicle(vehicle);
	}

	public VehicleModel getVehicleByRouteNumberAndRouteScheduleNumber ( final String routeNumber, final String scheduleNumber ) {
		return vehicleService.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, Long.parseLong(scheduleNumber));
	}

	public int createSuppliedVehicles(final ScenarioModel scenarioModel, LocalDate currentDate) {
		Iterator<String> vehicleModels = scenarioModel.getSuppliedVehicles().keySet().iterator();
		int numCreatedVehicles = 0;
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < scenarioModel.getSuppliedVehicles().get(vehicleModel); i++ )  {
				vehicleService.saveVehicle(vehicleService.createVehicleObject(vehicleModel, vehicleService.generateRandomReg(
						currentDate.getYear()), currentDate));
				numCreatedVehicles++;
			}
		}
		return numCreatedVehicles;
	}

	public VehicleModel[] getVehicleModels ( ) {
		return vehicleService.getVehicleModels();
	}

	public List<String> getAllocations ( ) {
		return vehicleService.getAllocations();
	}

    public int getNumberVehicleTypes ( ) {
		return vehicleService.getNumberVehicleTypes();
	}

	public VehicleModel getVehicleByModel ( final String model ) {
		return vehicleService.createVehicleObject(model, vehicleService.generateRandomReg(LocalDate.now().getYear()), LocalDate.now());
	}

	public String getFirstVehicleModel ( ) {
		return vehicleService.getFirstVehicleModel();
	}

	public String getLastVehicleModel ( ) {
		return vehicleService.getLastVehicleModel();
	}

	public String getPreviousVehicleModel ( final String model ) {
		return vehicleService.getPreviousVehicleModel(model);
	}

	public String getNextVehicleModel ( final String model) {
		return vehicleService.getNextVehicleModel(model);
	}

	/**
	 * Load Vehicles.
	 * @param vehicleModels an array of <code>VehicleModel</code> objects with vehicles to store and delete all other vehicles.
	 */
	public void loadVehicles ( final VehicleModel[] vehicleModels ) {
		vehicleService.deleteAllVehicles();
		for ( VehicleModel vehicleModel : vehicleModels ) {
			vehicleService.saveVehicle(vehicleModel);
		}
	}

}
