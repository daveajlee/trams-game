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

	public void assignVehicleToRouteSchedule ( final String registrationNumber, final String routeNumber, final String scheduleNumber, final String company ) {
		VehicleModel vehicleModel = vehicleService.getVehicleByRegistrationNumber(registrationNumber, company);
		vehicleService.assignVehicleToRouteScheduleNumber(vehicleModel, routeNumber, scheduleNumber, company);
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
	public boolean hasSomeVehiclesBeenDelivered ( final String company) {
		VehicleModel[] vehicleModels = getAllCreatedVehicles(company);
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
	public void purchaseVehicle ( final String type, final LocalDate deliveryDate, final String company ) {
		GameModel gameModel = gameController.getGameModel();
		VehicleModel vehicle = vehicleService.createVehicleObject(type, vehicleService.generateRandomReg(
		gameModel.getCurrentDateTime().getYear(), company), deliveryDate, company);
		gameController.withdrawBalance(vehicle.getPurchasePrice());
		vehicleService.saveVehicle(vehicle);
	}

	public VehicleModel getVehicleByRouteNumberAndRouteScheduleNumber ( final String routeNumber, final String scheduleNumber, final String company ) {
		return vehicleService.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, Long.parseLong(scheduleNumber), company);
	}

	public int createSuppliedVehicles(final ScenarioModel scenarioModel, final LocalDate currentDate, final String company) {
		Iterator<String> vehicleModels = scenarioModel.getSuppliedVehicles().keySet().iterator();
		int numCreatedVehicles = 0;
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < scenarioModel.getSuppliedVehicles().get(vehicleModel); i++ )  {
				vehicleService.saveVehicle(vehicleService.createVehicleObject(vehicleModel, vehicleService.generateRandomReg(
						currentDate.getYear(), company), currentDate, company));
				numCreatedVehicles++;
			}
		}
		return numCreatedVehicles;
	}

	public VehicleModel[] getVehicleModels ( final String company ) {
		return vehicleService.getVehicleModels(company);
	}

	public List<String> getAllocations ( final String company ) {
		return vehicleService.getAllocations(company);
	}

    public int getNumberVehicleTypes ( final String company ) {
		return vehicleService.getNumberVehicleTypes(company);
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
