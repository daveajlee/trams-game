package trams.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trams.constants.DifficultyLevel;
import trams.simulation.Simulator;
import trams.util.SortedDrivers;
import trams.util.SortedRoutes;
import trams.util.SortedVehicles;

/**
 * Class representing a scenario (i.e. transport company in TraMS).
 * @author Dave Lee
 */
public class Scenario {
    
	private int id;
    private List<Route> routes;
    private List<Vehicle> vehicles;
    private List<Driver> drivers;
    private int passengerSatisfaction;
    private String playerName;
    private double balance;
    private String scenarioName;
    private String description;
    private String targets;
    private int minimumSatisfaction;
    private String locationMapFileName;
    private int numberSuppliedVehicles;
    private int numberSuppliedDrivers;
    private String dataFileName;
    //Create the stops that this scenario has - used for picking route stops etc.
    protected List<Stop> stops;
    protected Map<String, Distances> distances;
    
    /**
     * Create a new scenario - for load function.
     * @param playerName a <code>String</code> with the name of the player.
     * @param balance a <code>double</code> with the available balance.
     * @param psgSatisfaction a <code>int</code> with the passenger satisfaction value.
     */
    public Scenario ( String playerName, double balance, int psgSatisfaction ) {
        routes = new ArrayList<Route>();
        vehicles = new ArrayList<Vehicle>();
        drivers = new ArrayList<Driver>();
        this.playerName = playerName;
        this.balance = balance;
        passengerSatisfaction = psgSatisfaction;
    }
    
    public Scenario ( ) {
    	routes = new ArrayList<Route>();
    	vehicles = new ArrayList<Vehicle>();
    	drivers = new ArrayList<Driver>();
    	stops = new ArrayList<Stop>();
    	distances = new HashMap<String, Distances>();
    }
    
    public void init ( ) {
    	//Load text file.
    	InputStream fileStream = getClass().getResourceAsStream("/trams/data/" + dataFileName);
    	ArrayList<String> fileContents = new ArrayList<String>();
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream, "UTF-8"));
    		String nextLine;
    		while ( (nextLine = reader.readLine()) != null ) {
    			fileContents.add(nextLine);
    		}
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    	//Process contents - first line is stops.
    	String[] myStops = fileContents.get(0).split(",");
    	for ( int i = 0; i < myStops.length; i++ ) {
    		stops.add(new Stop(myStops[i]));
    	}
    	//Rest is then distances.
    	for ( int i = 1; i < fileContents.size(); i+=2 ) {
    		//Get stop name.
    		String stopName = fileContents.get(i).split(":")[0];
    		Distances myDistances = new Distances();
    		String[] stopNames = fileContents.get(i).split(":")[1].split(",");
    		String[] stopTimes = fileContents.get((i+1)).split(":")[1].split(",");
    		for ( int j = 0; j < stopNames.length; j++ ) {
    			myDistances.setStopDistance(stopNames[j], Integer.parseInt(stopTimes[j]));
    		}
    		distances.put(stopName, myDistances);
    	}
    } 
    
    public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public int getNumberSuppliedVehicles() {
		return numberSuppliedVehicles;
	}

	public void setNumberSuppliedVehicles(int numberSuppliedVehicles) {
		this.numberSuppliedVehicles = numberSuppliedVehicles;
	}
	
	public int getNumberSuppliedDrivers() {
		return numberSuppliedDrivers;
	}
	
	public void setNumberSuppliedDrivers(int numberSuppliedDrivers) {
		this.numberSuppliedDrivers = numberSuppliedDrivers;
	}

	public String getLocationMapFileName() {
		return locationMapFileName;
	}

	public void setLocationMapFileName(String locationMapFileName) {
		this.locationMapFileName = locationMapFileName;
	}

	public int getMinimumSatisfaction() {
		return minimumSatisfaction;
	}

	public void setMinimumSatisfaction(int minimumSatisfaction) {
		this.minimumSatisfaction = minimumSatisfaction;
	}

	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public String getDescription() {
		return description;
	}
	
	public String getFullDescription() {
		return getPlayerName() + getDescription();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	public Map<String, Distances> getDistances() {
		return distances;
	}

	public void setDistances(Map<String, Distances> distances) {
		this.distances = distances;
	}

	public void setPassengerSatisfaction(int passengerSatisfaction) {
		this.passengerSatisfaction = passengerSatisfaction;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
    
    /**
     * Add a route.
     * @param r a <code>Route</code> object with the route to add.
     * @return a <code>boolean</code> which is true iff the route was successfully added.
     */
    public boolean addRoute ( Route r ) {
        return routes.add(r);
    }
    
    /**
     * Delete a route.
     * @param r a <code>Route</code> object with the route to delete.
     * @return a <code>boolean</code> which is true iff the route was successfully deleted.
     */
    public boolean deleteRoute ( Route r ) {
        return routes.remove(r);
    }
    
    /**
     * Get the number of routes.
     * @return a <code>int</code> with the number of routes.
     */
    public int getNumberRoutes ( ) {
        return routes.size();
    }
    
    /**
     * Get the route object located at the specified position.
     * @param pos a <code>int</code> with the position,
     * @return a <code>Route</code> object.
     */
    public Route getRoute ( int pos ) {
        return routes.get(pos);
    }
    
    /**
     * Get the route object based on the route number,
     * @param routeNumber a <code>String</code> with the route number,
     * @return a <code>Route</code> object.
     */
    public Route getRoute ( String routeNumber ) {
        for ( Route myRoute : routes ) {
            if ( myRoute.getRouteNumber().equalsIgnoreCase(routeNumber) ) {
                return myRoute;
            }
        }
        return null;
    }
    
    /**
     * Sort routes into alphabetical order by route number,
     */
    public void sortRoutes ( ) {
        Collections.sort(routes, new SortedRoutes());
    }
    
    /**
     * Set the player name.
     * @param name a <code>String</code> with the player's name.
     */
    public void setPlayerName ( String name ) {
        playerName = name;
    }
    
    /**
     * Get the player name.
     * @return a <code>String</code> with the player's name.
     */
    public String getPlayerName ( ) {
        return playerName;
    }
    
    /**
     * Deduct money from the balance.
     * @param amount a <code>double</code> with the amount to deduct from the balance.
     */
    public void withdrawBalance ( double amount ) {
        balance -= amount;
    }
    
    /**
     * Add money to the balance.
     * @param amount a <code>double</code> with the amount to credit the balance.
     */
    public void creditBalance ( double amount ) {
        balance += amount;
    }
    
    /**
     * Get the balance.
     * @return a <code>double</code> with the balance amount.
     */
    public double getBalance ( ) {
        return balance;
    }
    
    /**
     * Purchase a vehicle
     * @param v a <code>Vehicle</code> object with the vehicle to add.
     * @return a <code>boolean</code> which is true iff the vehicle was added successfully.
     */
    public boolean purchaseVehicle ( Vehicle v ) {
        withdrawBalance(v.getPurchasePrice());
        return vehicles.add(v);
    }
    
    /**
     * Add a vehicle - this is used for load!
     * @param v a <code>Vehicle</code> object with the vehicle to add.
     * @return a <code>boolean</code> which is true iff the vehicle was added successfully.
     */
    public boolean addVehicle ( Vehicle v ) {
    	System.out.println("Adding vehicle with registration " + v.getRegistrationNumber() + " ... vehicle count before " + vehicles.size());
        System.out.println(vehicles.toString());
    	return vehicles.add(v);
    }
    
    /**
     * Sell a vehicle
     * @param v a <code>Vehicle</code> object with the vehicle to sell.
     * @param currentDate a <code>Calendar</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the vehicle was sold successfully.
     */
    public boolean sellVehicle ( Vehicle v, Calendar currentDate ) {
        creditBalance(v.getValue(currentDate));
        return vehicles.remove(v);
    }
    
    /**
     * Get the number of vehicles.
     * @return a <code>int</code> with the number of vehicles.
     */
    public int getNumberVehicles ( ) {
        return vehicles.size();
    }
    
    /**
     * Get vehicle located at the specified position.
     * @param pos a <code>int</code> with the specified position.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getVehicle ( int pos ) {
        return vehicles.get(pos);
    }
    
    /**
     * Get vehicle with the specified id - returns null if not found.
     * @param id a <code>String</code> with the id.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getVehicle ( String id ) {
        for ( int i = 0; i < vehicles.size(); i++ ) {
            if ( vehicles.get(i).getRegistrationNumber().equalsIgnoreCase(id) ) {
                return vehicles.get(i);
            }
        }
        return null;
    }
    
    /**
     * Sort vehicles by vehicle id.
     */
    public void sortVehicles ( ) {
        Collections.sort(vehicles, new SortedVehicles());
    }
    
    /**
     * Sort drivers by driver id.
     */
    public void sortDrivers ( ) {
    	Collections.sort(drivers, new SortedDrivers());
    }

    /**
     * Employ a driver
     * @param d a <code>Driver</code> object with the driver to add.
     * @return a <code>boolean</code> which is true iff the driver was added successfully.
     */
    public boolean employDriver ( Driver d ) {
        withdrawBalance(0); //At the moment, drivers come free!
        return drivers.add(d);
    }

    /**
     * Add a driver - this is used for load!
     * @param d a <code>Driver</code> object with the driver to add.
     * @return a <code>boolean</code> which is true iff the driver was added successfully.
     */
    public boolean addDriver ( Driver d ) {
        return drivers.add(d);
    }

    /**
     * Sack a driver
     * @param d a <code>Driver</code> object with the driver to sack.
     * @param currentDate a <code>Calendar</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver was sacked successfully.
     */
    public boolean sackDriver ( Driver d, Calendar currentDate ) {
        return drivers.remove(d);
    }

    /**
     * Get the number of drivers.
     * @return a <code>int</code> with the number of drivers.
     */
    public int getNumberDrivers ( ) {
        return drivers.size();
    }

    /**
     * Get driver located at the specified position.
     * @param pos a <code>int</code> with the specified position.
     * @return a <code>Driver</code> object.
     */
    public Driver getDriver ( int pos ) {
        return drivers.get(pos);
    }
    
    /**
     * Get driver with specified id.
     * @param id a <code>int</code> with the specified id.
     * @return a <code>Driver</code> object.
     */
    public Driver getDriverById ( int id ) {
    	for ( int i = 0; i < drivers.size(); i++ ) {
    		if ( drivers.get(i).getIdNumber() == id ) {
    			return drivers.get(i);
    		}
    	}
        return null;
    }

    /**
     * Get the passenger satisfaction without recalculating it.
     * @return a <code>int</code> with the passenger satisfaction.
     */
    public int getPassengerSatisfaction ( ) {
        return passengerSatisfaction;
    }

    /**
     * Compute passenger satisfaction for the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @param difficultyLevel a <code>String</code> with the difficulty level.
     * @return a <code>int</code> with the passenger satisfaction level.
     */
    public int computePassengerSatisfaction ( Calendar currentTime, DifficultyLevel difficultyLevel ) {
        //Essentially satisfaction is determined by the ability for vehicles to run on time.
        //Now count number of vehicles into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
        int numSmallLateVehicles = 0; int numMediumLateVehicles = 0; int numLargeLateVehicles = 0;
        //Now go through all vehicles.
        for ( Vehicle myVehicle : vehicles ) {
            //Check if the vehicle has actually left depot if it hasn't then continue.
            if ( !myVehicle.hasAssignedSchedule() ) {
                continue;
            }
            //Running... 1 - 5 minutes late.
            if ( myVehicle.getDelay() > 0 && myVehicle.getDelay() < 6 ) {
                numSmallLateVehicles++;
            } else if ( myVehicle.getDelay() > 5 && myVehicle.getDelay() < 16 ) {
                numMediumLateVehicles++;
            } else if ( myVehicle.getDelay() > 15 ) {
                numLargeLateVehicles++;
            }
        }
        int totalSubtract = 0;

        //Easy: numSmallLateVehicles / 2 and numMediumLateVehicles and numLargeLateVehicles*2.
        switch ( difficultyLevel ) {
        	case EASY:
        		totalSubtract = (numSmallLateVehicles/2) + numMediumLateVehicles + (numLargeLateVehicles*2);
        		break;
        	case INTERMEDIATE:
        		totalSubtract = (numSmallLateVehicles) + (numMediumLateVehicles*2) + (numLargeLateVehicles*3);
        		break;
        	case MEDIUM:
        		 totalSubtract = (numSmallLateVehicles*2) + (numMediumLateVehicles*3) + (numLargeLateVehicles*4);
        		 break;
        	case HARD:
        		totalSubtract = (numSmallLateVehicles*3) + (numMediumLateVehicles*4) + (numLargeLateVehicles*5);
        		break;
        }
        //Subtract from passenger satisfaction.
        passengerSatisfaction -= totalSubtract;
        //After all of this check that passenger satisfaction is greater than 0.
        if ( passengerSatisfaction < 0 ) { passengerSatisfaction = 0; }
        //Return passenger satisfaction.
        return passengerSatisfaction;
    }
    
    /**
     * Check if any vehicles are presently running based on the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @return a <code>boolean</code> which is true iff at least one vehicle is running.
     */
    public boolean areAnyVehiclesRunning (Calendar currentTime, Simulator simulator) {
        //Check if any vehicles are running....
        for ( Vehicle myVehicle : vehicles ) {
            //First one that is not in depot indicates that vehicles are running.
            if ( !myVehicle.getCurrentPosition(currentTime, simulator)[0].equalsIgnoreCase("Depot") ) {
                return true;
            }
        }
        //Otherwise, return false;
        return false;
    }
    
    /**
     * Get the stop names as a String array plus a - and return it.
     * @return a <code>String</code> array with the stop names plus a -. 
     */
    public String[] getStopNames ( ) {
        String[] possStops = new String[stops.size() + 1];
        //Add all stops.
        for ( int i = 0; i < stops.size(); i++ ) {
            possStops[i] = stops.get(i).getStopName();
        }
        //Return stop names.
        return possStops;
    }
    
    /**
     * Get the distance between two stops.
     * @param stop1 a <code>String</code> with the name of the first stop.
     * @param stop2 a <code>String</code> with the name of the second stop.
     * @return a <code>int</code> with the distance between two stops.
     */
    public int getDistance ( String stop1, String stop2 ) {
        return distances.get(stop1).getStopDistance(stop2);
    }


}
