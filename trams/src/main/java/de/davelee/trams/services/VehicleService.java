package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.davelee.trams.dao.VehicleDao;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.factory.VehicleFactory;
import de.davelee.trams.util.SortedVehicles;

public class VehicleService {

    private VehicleDao vehicleDao;
    private VehicleFactory vehicleFactory;
	
	public VehicleService() {
	}

    public VehicleDao getVehicleDao() {
        return vehicleDao;
    }

    public void setVehicleDao(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public VehicleFactory getVehicleFactory() {
        return vehicleFactory;
    }

    public void setVehicleFactory(VehicleFactory vehicleFactory) {
        this.vehicleFactory = vehicleFactory;
    }

	/**
     * Check if the vehicle has been delivered yet!
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>boolean</code> which is true iff the vehicle has been delivered.
     */
    public boolean hasBeenDelivered ( Calendar deliveryDate, Calendar currentDate ) {
    	if ( currentDate.after(deliveryDate) || currentDate.equals(deliveryDate) ) {
    		return true;
    	}
    	return false;
    }    
    
    /**
     * Get the vehicle's value after taking depreciation into account.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>double</code> with the value.
     */
    public double getValue ( double purchasePrice, double depreciationFactor, Calendar deliveryDate, Calendar currentDate ) {
        return purchasePrice - ((depreciationFactor * getAge(deliveryDate, currentDate)) * purchasePrice);
    }
    
    /**
     * Get the vehicle age based on the difference between deliveryDate and currentDate in months.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>int</code> with the vehicle age.
     */
    public int getAge ( Calendar deliveryDate, Calendar currentDate ) {
    	if ( currentDate.before(deliveryDate) ) return -1; 
    	int yearDiff = Math.abs(currentDate.get(Calendar.YEAR) - deliveryDate.get(Calendar.YEAR));
        int monthDiff = Math.abs(currentDate.get(Calendar.MONTH) - deliveryDate.get(Calendar.MONTH));
        return (yearDiff * 12) + monthDiff;
    }
    
    public Vehicle getVehicleById(long id) {
    	return vehicleDao.getVehicleById(id);
    }
    
    /**
     * Get vehicle with the specified id - returns null if not found.
     * @param id a <code>String</code> with the id.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getVehicle ( String id ) {
        for ( int i = 0; i < getAllVehicles().size(); i++ ) {
            if ( getAllVehicles().get(i).getRegistrationNumber().equalsIgnoreCase(id) ) {
                return getAllVehicles().get(i);
            }
        }
        return null;
    }
    
    public List<Vehicle> getAllVehicles ( ) {
    	return vehicleDao.getAllVehicles();
    }
    
    public Vehicle createVehicle ( final String registrationNumber, final Calendar deliveryDate, final double depreciationFactor,
    		final String imagePath, final String model, final long routeScheduleId, final int seatingNum, final int standingNum,
    		final double purchasePrice ) {
    	Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setDeliveryDate(deliveryDate);
        vehicle.setDepreciationFactor(depreciationFactor);
        vehicle.setImagePath(imagePath);
        vehicle.setModel(model);
        vehicle.setRouteScheduleId(routeScheduleId);
        vehicle.setSeatingCapacity(seatingNum);
        vehicle.setStandingCapacity(standingNum);
        vehicle.setPurchasePrice(purchasePrice);
        return vehicle;
    }
    
    public void saveVehicle ( final Vehicle vehicle ) {
    	vehicleDao.createAndStoreVehicle(vehicle);
    }
    
    public void removeVehicle ( final Vehicle vehicle ) {
    	vehicleDao.removeVehicle(vehicle);
    }
    
    /**
     * Get current allocations for the simulation.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getAllocations ( ) {
        //Allocations list.
        ArrayList<String> allocations = new ArrayList<String>();
        //Now go through and add their allocation if they already have an allocation.
        for ( int i = 0; i < getAllVehicles().size(); i++ ) {
            if ( getAllVehicles().get(i).getRouteScheduleId() != 0 ) {
                allocations.add(getAllVehicles().get(i).getRouteScheduleId() + " & " + getAllVehicles().get(i).getRegistrationNumber());
            }
        }
        //Return allocations list.
        return allocations;
    }
    
    /**
     * Helper method to generate random vehicle registration.
     * @param year a <code>String</code> with the current simulation year.
     */
    public String generateRandomReg ( int year ) {
        //Generate random registration - in form 2 digit year - then 5 random letters.
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //This is our loop - till we get unique reg.
        boolean isUniqueReg = true;
        String randomReg = "" + year + "-";
        do {
            //Here is random reg.
            Random r = new Random();
            for ( int i = 0; i < 5; i++ ) {
                randomReg += alphabet.charAt(r.nextInt(alphabet.length()));
            }
            //Now check that random reg not been generated before.
            if ( getAllVehicles() != null ) {
            	for ( int i = 0; i < getAllVehicles().size(); i++ ) {
            		if ( getAllVehicles().get(i).getRegistrationNumber().equalsIgnoreCase(randomReg) ) {
            			isUniqueReg = false;
            			break;
            		}
            	}
            }
            if ( !isUniqueReg ) {
                randomReg = "" + year + "-";
            }
        } while ( !isUniqueReg );
        return randomReg;
    }
    
    //TODO: Exception when null?
    public Vehicle getVehicleByRouteScheduleId ( long routeScheduleId ) {
        return vehicleDao.getVehicleByRouteScheduleId(routeScheduleId);
    }
    
    /**
     * Sort vehicles by vehicle id. 
     */
    public void sortVehicles ( List<Vehicle> vehicles ) {
        Collections.sort(vehicles, new SortedVehicles());
    }

    public Vehicle createVehicleObject ( String model, String registrationNumber, Calendar deliveryDate ) {
        Vehicle vehicle = vehicleFactory.createVehicleByModel(model);
        if ( vehicle != null ) {
            vehicle.setRegistrationNumber(registrationNumber);
            vehicle.setDeliveryDate(deliveryDate);
        }
        return vehicle;
    }

    public String getVehicleModel ( int pos ) {
        if ( pos < getNumberAvailableVehicles() ) {
            return vehicleFactory.getAvailableVehicles().get(pos).getModel();
        }
        return null;
    }

    public int getNumberAvailableVehicles ( ) {
        return vehicleFactory.getAvailableVehicles().size();
    }
    
}
