package de.davelee.trams.factory;

import java.util.List;
import java.util.NoSuchElementException;

import de.davelee.trams.data.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class VehicleFactory {

	@Autowired
	@Resource(name="availableVehiclesList")
	private List<Vehicle> availableVehicles;
	
	public VehicleFactory() {
		
	}

	public List<Vehicle> getAvailableVehicles() {
		return availableVehicles;
	}

	public void setAvailableVehicles(List<Vehicle> availableVehicles) {
		this.availableVehicles = availableVehicles;
	}

	/**
	 * Find the vehicle matching the specified model and return the vehicle information.
	 * Throws an exception if the model is not found.
	 * @param model a <code>String</code> with the vehicle to find.
	 * @return a <code>Vehicle</code> object containing the vehicle information.
	 */
	public Vehicle createVehicleByModel(final String model) {
		for ( Vehicle availableVehicle : availableVehicles ) {
			if ( availableVehicle.getModel().equalsIgnoreCase(model) ) {
				return availableVehicle.clone();
			}
		}
		throw new NoSuchElementException();
	}
		
}
