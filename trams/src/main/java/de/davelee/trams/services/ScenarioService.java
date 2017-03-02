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
    
    /**
     * Get the distance between two stops.
     * @param stop1 a <code>String</code> with the name of the first stop.
     * @param stop2 a <code>String</code> with the name of the second stop.
     * @return a <code>int</code> with the distance between two stops.
     */
    public int getDistance ( long scenarioId, String stop1, String stop2 ) {
    	Scenario scenario = getScenarioById(scenarioId);
    	int stop1Pos = -1; int stop2Pos = -1; int count = 0;
    	for ( String stopDistance : scenario.getStopDistances() ) {
    		String stopName = stopDistance.split(":")[0];
    		if ( stopName.equalsIgnoreCase(stop1) ) { stop1Pos = count; }
    		else if ( stopName.equalsIgnoreCase(stop2) ) { stop2Pos = count; }
    		count++;
    	}
    	return Integer.parseInt(scenario.getStopDistances().get(stop1Pos).split(":")[1].split(",")[stop2Pos]);
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
