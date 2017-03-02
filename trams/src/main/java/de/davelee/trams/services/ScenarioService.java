package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.davelee.trams.data.Scenario;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.db.DatabaseManager;

public class ScenarioService {
	
	public ScenarioService() {
		
	}
	
	private DatabaseManager databaseManager;
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	/**
     * Get the stop names as a String array plus a - and return it.
     * @return a <code>String</code> array with the stop names plus a -. 
     */
    public String[] getStopNames ( long scenarioId ) {
    	Scenario scenario = getScenarioById(scenarioId);
        String[] possStops = new String[scenario.getStopDistances().size() + 1];
        //Add all stops.
        for ( int i = 0; i < scenario.getStopDistances().size(); i++ ) {
            possStops[i] = scenario.getStopDistances().get(i).split(":")[0];
        }
        //Add dash at end.
        possStops[scenario.getStopDistances().size()] = "-";
        //Return stop names.
        return possStops;
    }
    
    public List<Vehicle> createSuppliedVehicles( HashMap<String, Integer> suppliedVehicles, Calendar currentTime,
    		VehicleService vehicleService, FactoryService factoryService) {
    	List<Vehicle> vehicles = new ArrayList<Vehicle>();
    	Iterator<String> vehicleModels = suppliedVehicles.keySet().iterator();
    	while (vehicleModels.hasNext()) {
    		String vehicleModel = vehicleModels.next();
    		for ( int i = 0; i < suppliedVehicles.get(vehicleModel); i++ )  {
    			vehicles.add(factoryService.createVehicleObject(vehicleModel, vehicleService.generateRandomReg(
    					currentTime.get(Calendar.YEAR)), currentTime));
    		}
    	}
    	return vehicles;
    }

    public Scenario getScenarioById(long id) {
    	return databaseManager.getScenarioById(id);
    }

}
