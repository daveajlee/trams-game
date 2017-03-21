package de.davelee.trams.factory;

import java.util.List;

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
	
	public Vehicle createVehicleByModel(String model) {
		for ( Vehicle availableVehicle : availableVehicles ) {
			if ( availableVehicle.getModel().equalsIgnoreCase(model) ) {
				return availableVehicle.clone();
			}
		}
		//TODO: throw exception
		return null;
	}
		
}
