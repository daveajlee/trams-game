package de.davelee.trams.controllers;

import de.davelee.trams.data.Vehicle;
import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.model.VehicleModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.VehicleService;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private RouteScheduleController routeScheduleController;

	@Autowired
	private GameController gameController;
	
	public void assignVehicleToRouteSchedule ( final String registrationNumber, final String scheduleNumber ) {
		vehicleService.getVehicleByRegistrationNumber(registrationNumber).setRouteScheduleId(routeScheduleController.getIdFromNumber(scheduleNumber));
	}

	/**
	 * Create vehicle object based on type.
	 * This method needs to be updated in order to new vehicle types to TraMS.
	 * @param pos a <code>int</code> with the supplied vehicle type position in the array.
	 * @return a <code>Vehicle</code> object.
	 */
    public VehicleModel createVehicleObject ( int pos ) {
		Vehicle vehicle = vehicleService.createVehicleObject ( vehicleService.getVehicleModel(pos), "AA", gameController.getCurrentSimTime() );
		return convertToVehicleModel(vehicle);
	}

	private VehicleModel convertToVehicleModel ( final Vehicle vehicle ) {
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setDeliveryDate(vehicle.getDeliveryDate());
		vehicleModel.setImagePath(vehicle.getImagePath());
		vehicleModel.setModel(vehicle.getModel());
		vehicleModel.setPurchasePrice(vehicle.getPurchasePrice());
		vehicleModel.setRegistrationNumber(vehicle.getRegistrationNumber());
		vehicleModel.setSeatingCapacity("" + vehicle.getSeatingCapacity());
		vehicleModel.setStandingCapacity("" + vehicle.getStandingCapacity());
		vehicleModel.setRouteScheduleId(vehicle.getRouteScheduleId());
		return vehicleModel;
	}

	public VehicleModel[] getAllCreatedVehicles ( ) {
		List<Vehicle> vehicles = vehicleService.getAllVehicles();
		VehicleModel[] vehicleModels = new VehicleModel[vehicles.size()];
		for ( int i = 0; i < vehicles.size(); i++ ) {
			vehicleModels[i] = new VehicleModel();
			vehicleModels[i].setDeliveryDate(vehicles.get(i).getDeliveryDate());
			vehicleModels[i].setRegistrationNumber(vehicles.get(i).getRegistrationNumber());
		}
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
		if ( vehicleModels.length == 0 ) { return false; }
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( hasVehicleBeenDelivered(vehicleModels[i].getDeliveryDate(), gameController.getCurrentSimTime()) ) { return true; }
		}
		return false;
	}

	/**
	 * Get a vehicle based on its id.
	 * @param id a <code>String</code> with the id.
	 * @return a <code>Vehicle</code> object.
	 */
	public VehicleModel getVehicle (final String id ) {
		return convertToVehicleModel(vehicleService.getVehicle(id));
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
	 * @param v a <code>Vehicle</code> to sell.
	 * @return a <code>boolean</code> which is true iff the vehicle was sold.
	 */
	public void sellVehicle ( VehicleModel vehicleModel ) {
		Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(vehicleModel.getRegistrationNumber());
		gameController.creditBalance(vehicleService.getValue(vehicle.getPurchasePrice(), vehicle.getDepreciationFactor(), vehicle.getDeliveryDate(), gameController.getCurrentSimTime()));
		vehicleService.removeVehicle(vehicle);
	}

	/**
	 * Purchase a new vehicle.
	 * @param type a <code>String</code> with the vehicle type.
	 * @param deliveryDate a <code>Calendar</code> with the delivery date.
	 * @return a <code>boolean</code> which is true iff the vehicle has been purchased successfully.
	 */
    public void purchaseVehicle ( String type, Calendar deliveryDate ) {
		Vehicle vehicle = vehicleService.createVehicleObject(type, vehicleService.generateRandomReg(
			gameController.getCurrentSimTime().get(Calendar.YEAR)), deliveryDate);
		gameController.withdrawBalance(vehicle.getPurchasePrice());
		vehicleService.saveVehicle(vehicle);
	}

	public VehicleModel retrieveModel ( long routeScheduleId ) {
		return convertToVehicleModel(vehicleService.getVehicleByRouteScheduleId(routeScheduleId));
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

	public List<Vehicle> getAllVehicles ( ) {
		return vehicleService.getAllVehicles();
	}

}
