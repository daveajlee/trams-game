package de.davelee.trams.controllers;

import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.SortedVehicleModels;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.VehicleService;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Controller
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private RouteScheduleController routeScheduleController;

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

	public boolean hasVehicleBeenDelivered ( final Calendar deliveryDate, final Calendar currentTime ) {
		return vehicleService.hasBeenDelivered(deliveryDate, currentTime);
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
			if ( hasVehicleBeenDelivered(vehicleModels[i].getDeliveryDate(), gameModel.getCurrentTime()) ) { return true; }
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

	public int getAge (final Calendar deliveryDate, final Calendar currentDate ) {
		return vehicleService.getAge(deliveryDate, currentDate);
	}

	public double getValue ( final VehicleModel vehicleModel, final Calendar currentDate ) {
		return vehicleService.getValue(vehicleModel.getPurchasePrice(), vehicleModel.getDepreciationFactor(),
			vehicleModel.getDeliveryDate(), currentDate);
	}

	/**
	 * Sell a vehicle.
	 * @param vehicleModel a <code>VehicleModel</code> object representing the vehicle to sell.
	 */
	public void sellVehicle ( final VehicleModel vehicleModel ) {
		GameModel gameModel = gameController.getGameModel();
		gameController.creditBalance(vehicleService.getValue(vehicleModel.getPurchasePrice(), vehicleModel.getDepreciationFactor(), vehicleModel.getDeliveryDate(), gameModel.getCurrentTime()), gameModel.getPlayerName());
		vehicleService.removeVehicle(vehicleModel);
	}

	/**
	 * Purchase a new vehicle.
	 * @param type a <code>String</code> with the vehicle type.
	 * @param deliveryDate a <code>Calendar</code> with the delivery date.
	 */
	public void purchaseVehicle ( final String type, final Calendar deliveryDate ) {
		GameModel gameModel = gameController.getGameModel();
		VehicleModel vehicle = vehicleService.createVehicleObject(type, vehicleService.generateRandomReg(
		gameModel.getCurrentTime().get(Calendar.YEAR)), deliveryDate);
		gameController.withdrawBalance(vehicle.getPurchasePrice(), gameModel.getPlayerName());
		vehicleService.saveVehicle(vehicle);
	}

	public VehicleModel getVehicleByRouteNumberAndRouteScheduleNumber ( final String routeNumber, final String scheduleNumber ) {
		return vehicleService.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, Long.parseLong(scheduleNumber));
	}

	public void createSuppliedVehicles(final ScenarioModel scenarioModel, Calendar currentTime) {
		Iterator<String> vehicleModels = scenarioModel.getSuppliedVehicles().keySet().iterator();
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < scenarioModel.getSuppliedVehicles().get(vehicleModel); i++ )  {
				vehicleService.saveVehicle(vehicleService.createVehicleObject(vehicleModel, vehicleService.generateRandomReg(
						currentTime.get(Calendar.YEAR)), currentTime));
			}
		}
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
		return vehicleService.createVehicleObject(model, vehicleService.generateRandomReg(Calendar.getInstance().get(Calendar.YEAR)), Calendar.getInstance());
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

}
