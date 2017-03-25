package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import de.davelee.trams.data.Vehicle;
import de.davelee.trams.factory.VehicleFactory;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleFactory vehicleFactory;
	
	public VehicleService() {
	}

	/**
     * Check if the vehicle has been delivered yet!
     * @param deliveryDate a <code>Calendar</code> object with the deliveryDate.
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
     * @param purchasePrice a <code>double</code> representing the purchase price.
     * @param depreciationFactor a <code>double</code> representing the depreciatonFactor as percentage.
     * @param deliveryDate a <code>Calendar</code> object with the deliveryDate.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>double</code> with the value.
     */
    public double getValue ( double purchasePrice, double depreciationFactor, Calendar deliveryDate, Calendar currentDate ) {
        return purchasePrice - ((depreciationFactor * getAge(deliveryDate, currentDate)) * purchasePrice);
    }
    
    /**
     * Get the vehicle age based on the difference between deliveryDate and currentDate in months.
     * @param deliveryDate a <code>Calendar</code> object with the deliveryDate.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>int</code> with the vehicle age.
     */
    public int getAge ( Calendar deliveryDate, Calendar currentDate ) {
    	if ( currentDate.before(deliveryDate) ) return -1; 
    	int yearDiff = Math.abs(currentDate.get(Calendar.YEAR) - deliveryDate.get(Calendar.YEAR));
        int monthDiff = Math.abs(currentDate.get(Calendar.MONTH) - deliveryDate.get(Calendar.MONTH));
        return (yearDiff * 12) + monthDiff;
    }

    public VehicleModel[] getVehicleModels ( ) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        VehicleModel[] vehicleModels = new VehicleModel[vehicles.size()];
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            vehicleModels[i] = convertToVehicleModel(vehicles.get(i));
        }
        return vehicleModels;
    }

    private Vehicle convertToVehicle ( final VehicleModel vehicleModel ) {
    	Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(vehicleModel.getRegistrationNumber());
        vehicle.setDeliveryDate(vehicleModel.getDeliveryDate());
        vehicle.setDepreciationFactor(vehicleModel.getDepreciationFactor());
        vehicle.setImagePath(vehicleModel.getImagePath());
        vehicle.setModel(vehicleModel.getModel());
        vehicle.setRouteNumber(vehicleModel.getRouteNumber());
        vehicle.setRouteScheduleNumber(vehicleModel.getRouteScheduleNumber());
        vehicle.setSeatingCapacity(Integer.parseInt(vehicleModel.getSeatingCapacity()));
        vehicle.setStandingCapacity(Integer.parseInt(vehicleModel.getStandingCapacity()));
        vehicle.setPurchasePrice(vehicleModel.getPurchasePrice());
        return vehicle;
    }

    private VehicleModel convertToVehicleModel ( final Vehicle vehicle ) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setRegistrationNumber(vehicle.getRegistrationNumber());
        vehicleModel.setDeliveryDate(vehicle.getDeliveryDate());
        vehicleModel.setDepreciationFactor(vehicle.getDepreciationFactor());
        vehicleModel.setImagePath(vehicle.getImagePath());
        vehicleModel.setModel(vehicle.getModel());
        vehicleModel.setRouteNumber(vehicle.getRouteNumber());
        vehicleModel.setRouteScheduleNumber(vehicle.getRouteScheduleNumber());
        vehicleModel.setSeatingCapacity("" + vehicle.getSeatingCapacity());
        vehicleModel.setStandingCapacity("" + vehicle.getStandingCapacity());
        vehicleModel.setPurchasePrice(vehicle.getPurchasePrice());
        return vehicleModel;
    }

    public void saveVehicle ( final VehicleModel vehicle ) {
        vehicleRepository.saveAndFlush(convertToVehicle(vehicle));
    }

    public void removeVehicle ( final VehicleModel vehicleModel ) {
        vehicleRepository.delete(vehicleRepository.findByRegistrationNumber(vehicleModel.getRegistrationNumber()));
    }
    
    /**
     * Get current allocations for the simulation.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getAllocations ( ) {
        //Allocations list.
        ArrayList<String> allocations = new ArrayList<String>();
        //Now go through and add their allocation if they already have an allocation.
        VehicleModel[] vehicleModels = getVehicleModels();
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( vehicleModels[i].getRouteScheduleNumber() != 0 ) {
                allocations.add(vehicleModels[i].getRouteNumber() + "/" + vehicleModels[i].getRouteScheduleNumber() + " & " + vehicleModels[i].getRegistrationNumber());
            }
        }
        //Return allocations list.
        return allocations;
    }
    
    /**
     * Helper method to generate random vehicle registration.
     * @param year a <code>String</code> with the current simulation year.
     * @return a <code>String</code> with the generated vehicle registration.
     */
    public String generateRandomReg ( final int year ) {
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
            VehicleModel[] vehicleModels = getVehicleModels();
            if ( vehicleModels != null ) {
                for ( int i = 0; i < vehicleModels.length; i++ ) {
                    if ( vehicleModels[i].getRegistrationNumber().equalsIgnoreCase(randomReg) ) {
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
    public VehicleModel getVehicleByRouteNumberAndRouteScheduleNumber ( final String routeNumber, final long routeScheduleNumber ) {
        return convertToVehicleModel(vehicleRepository.findByRouteNumberAndRouteScheduleNumber(routeNumber, routeScheduleNumber));
    }

    public VehicleModel createVehicleObject ( final String model, final String registrationNumber, final Calendar deliveryDate ) {
        Vehicle vehicle = vehicleFactory.createVehicleByModel(model);
        if ( vehicle != null ) {
            vehicle.setRegistrationNumber(registrationNumber);
            vehicle.setDeliveryDate(deliveryDate);
            return convertToVehicleModel(vehicle);
        }
        return null;
    }

    public int getNumberVehicleTypes ( ) {
        return vehicleFactory.getAvailableVehicles().size();
    }

    public VehicleModel getVehicleByRegistrationNumber ( final String registrationNumber ) {
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber);
        if ( vehicle != null ) {
            return convertToVehicleModel(vehicle);
        }
        return null;
    }

    public String getFirstVehicleModel ( ) {
        return vehicleFactory.getAvailableVehicles().get(0).getModel();
    }

    public String getLastVehicleModel ( ) {
        return vehicleFactory.getAvailableVehicles().get(vehicleFactory.getAvailableVehicles().size()-1).getModel();
    }

    public String getNextVehicleModel ( final String model ) {
        for ( int i = 0; i < vehicleFactory.getAvailableVehicles().size(); i++ ) {
            if ( vehicleFactory.getAvailableVehicles().get(i).getModel().contentEquals(model) ) {
                return vehicleFactory.getAvailableVehicles().get(i+1).getModel();
            }
        }
        return "";
    }

    public String getPreviousVehicleModel ( final String model ) {
        for ( int i = 0; i < vehicleFactory.getAvailableVehicles().size(); i++ ) {
            if ( vehicleFactory.getAvailableVehicles().get(i).getModel().contentEquals(model) ) {
                return vehicleFactory.getAvailableVehicles().get(i-1).getModel();
            }
        }
        return "";
    }

     public void assignVehicleToRouteScheduleNumber ( final VehicleModel vehicleModel, final String routeNumber, final String scheduleNumber ) {
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(vehicleModel.getRegistrationNumber());
        vehicle.setRouteNumber(routeNumber);
        vehicle.setRouteScheduleNumber(Long.parseLong(scheduleNumber));
    }
    
}
