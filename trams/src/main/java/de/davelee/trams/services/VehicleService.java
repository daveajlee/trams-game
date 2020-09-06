package de.davelee.trams.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

	/**
     * Check if the vehicle has been delivered yet!
     * @param deliveryDate a <code>LocalDate</code> object with the deliveryDate.
     * @param currentDate a <code>LocalDate</code> object with the currentDate.
     * @return a <code>boolean</code> which is true iff the vehicle has been delivered.
     */
    public boolean hasBeenDelivered ( LocalDate deliveryDate, LocalDate currentDate ) {
    	return currentDate.isAfter(deliveryDate) || currentDate.isEqual(deliveryDate);
    }    
    
    /**
     * Get the vehicle's value after taking depreciation into account.
     * @param purchasePrice a <code>double</code> representing the purchase price.
     * @param depreciationFactor a <code>double</code> representing the depreciatonFactor as percentage.
     * @param deliveryDate a <code>LocalDate</code> object with the deliveryDate.
     * @param currentDate a <code>LocalDate</code> object with the currentDate.
     * @return a <code>double</code> with the value.
     */
    public double getValue ( double purchasePrice, double depreciationFactor, LocalDate deliveryDate, LocalDate currentDate ) {
        return purchasePrice - ((depreciationFactor * getAge(deliveryDate, currentDate)) * purchasePrice);
    }
    
    /**
     * Get the vehicle age based on the difference between deliveryDate and currentDate in months.
     * @param deliveryDate a <code>LocalDate</code> object with the deliveryDate.
     * @param currentDate a <code>LocalDate</code> object with the currentDate.
     * @return a <code>int</code> with the vehicle age.
     */
    public int getAge ( LocalDate deliveryDate, LocalDate currentDate ) {
    	if ( currentDate.isBefore(deliveryDate) ) return -1;
    	int yearDiff = Math.abs(currentDate.getYear() - deliveryDate.getYear());
        int monthDiff = Math.abs(currentDate.getMonthValue() - deliveryDate.getMonthValue());
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
        return VehicleModel.builder()
                .registrationNumber(vehicle.getRegistrationNumber())
                .deliveryDate(vehicle.getDeliveryDate())
                .depreciationFactor(vehicle.getDepreciationFactor())
                .imagePath(vehicle.getImagePath())
                .model(vehicle.getModel())
                .routeNumber(vehicle.getRouteNumber())
                .routeScheduleNumber(vehicle.getRouteScheduleNumber())
                .seatingCapacity("" + vehicle.getSeatingCapacity())
                .standingCapacity("" + vehicle.getStandingCapacity())
                .purchasePrice(vehicle.getPurchasePrice())
                .build();
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

    /**
     * This method retrieves a vehicle running on a particular route and with a particular route schedule number.
     * Method throws an exception if no vehicle is currently running this route and route schedule number.
     * @param routeNumber a <code>String</code> with the route number that the vehicle should be running.
     * @param routeScheduleNumber a <code>String</code> with the route schedule number that the vehicle should be running.
     * @return a <code>VehicleModel</code> which contains information about the vehicle found.
     */
    public VehicleModel getVehicleByRouteNumberAndRouteScheduleNumber ( final String routeNumber, final long routeScheduleNumber ) {
        Vehicle vehicle = vehicleRepository.findByRouteNumberAndRouteScheduleNumber(routeNumber, routeScheduleNumber);
        if ( vehicle == null ) {
            throw new NoSuchElementException();
        }
        return convertToVehicleModel(vehicle);
    }

    public VehicleModel createVehicleObject ( final String model, final String registrationNumber, final LocalDate deliveryDate ) {
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
        vehicleRepository.save(vehicle);
    }

    /**
     * Delete all vehicles (only used as part of load function)
     */
    public void deleteAllVehicles() {
        vehicleRepository.deleteAll();
    }
    
}
