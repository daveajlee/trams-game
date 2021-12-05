package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import de.davelee.trams.api.request.AllocateVehicleRequest;
import de.davelee.trams.api.request.PurchaseVehicleRequest;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.api.response.VehiclesResponse;
import de.davelee.trams.model.VehicleModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@ConfigurationProperties(prefix="vehicles")
@PropertySource("classpath:vehicles.properties")
@Getter
@Setter
public class VehicleService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

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

    public VehicleModel[] getVehicleModels ( final String company ) {
        VehiclesResponse vehiclesResponse = restTemplate.getForObject(operationsServerUrl + "vehicles/?company=" + company, VehiclesResponse.class);
        VehicleModel[] vehicleModels = new VehicleModel[vehiclesResponse.getVehicleResponses().length];
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            vehicleModels[i] = convertToVehicleModel(vehiclesResponse.getVehicleResponses()[i]);
        }
        return vehicleModels;
    }

    private VehicleModel convertToVehicleModel ( final VehicleResponse vehicleResponse ) {
        return VehicleModel.builder()
                .registrationNumber(vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"))
                .deliveryDate(convertDateToLocalDate(vehicleResponse.getDeliveryDate()))
                .model(vehicleResponse.getModelName())
                .routeScheduleNumber(Long.parseLong(vehicleResponse.getAllocatedTour()))
                .seatingCapacity(vehicleResponse.getSeatingCapacity())
                .standingCapacity(vehicleResponse.getStandingCapacity())
                .build();
    }

    public void saveVehicle ( final VehicleModel vehicle, final String company, final String fleetNumber, final String livery ) {
        restTemplate.postForObject(operationsServerUrl + "vehicle/",
                PurchaseVehicleRequest.builder()
                        .company(company)
                        .fleetNumber(fleetNumber)
                        .livery(livery)
                        .vehicleType("BUS")
                        .modelName(vehicle.getModel())
                        .additionalTypeInformationMap(Map.of("Registration Number", vehicle.getRegistrationNumber()))
                        .seatingCapacity(vehicle.getSeatingCapacity())
                        .standingCapacity(vehicle.getStandingCapacity())
                        .build(),
                Void.class);
    }

    public void removeVehicle ( final VehicleModel vehicleModel, final String company, final String fleetNumber ) {
        restTemplate.delete(operationsServerUrl + "vehicle/?company=" + company + "&fleetNumber=" + fleetNumber);
    }
    
    /**
     * Get current allocations for the simulation.
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getAllocations ( final String company ) {
        //Allocations list.
        ArrayList<String> allocations = new ArrayList<>();
        //Now go through and add their allocation if they already have an allocation.
        VehicleModel[] vehicleModels = getVehicleModels(company);
        for ( VehicleModel vehicleModel : vehicleModels ) {
            if ( vehicleModel.getRouteScheduleNumber() != 0 ) {
                allocations.add(vehicleModel.getRouteNumber() + "/" + vehicleModel.getRouteScheduleNumber() + " & " + vehicleModel.getRegistrationNumber());
            }
        }
        //Return allocations list.
        return allocations;
    }
    
    /**
     * Helper method to generate random vehicle registration.
     * @param year a <code>String</code> with the current simulation year.
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>String</code> with the generated vehicle registration.
     */
    public String generateRandomReg ( final int year, final String company ) {
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
            VehicleModel[] vehicleModels = getVehicleModels(company);
            if ( vehicleModels != null ) {
                for ( VehicleModel vehicleModel : vehicleModels ) {
                    if ( vehicleModel.getRegistrationNumber().equalsIgnoreCase(randomReg) ) {
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
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>VehicleModel</code> which contains information about the vehicle found.
     */
    public VehicleModel getVehicleByRouteNumberAndRouteScheduleNumber ( final String routeNumber, final long routeScheduleNumber, final String company ) {
        VehicleResponse vehicleResponse = restTemplate.getForObject(operationsServerUrl + "vehicle/allocate/?company=" + company + "&allocatedTour=" + routeScheduleNumber, VehicleResponse.class);
        if ( vehicleResponse == null ) {
            throw new NoSuchElementException();
        }
        return convertToVehicleModel(vehicleResponse);
    }

    public VehicleModel createVehicleObject ( final String model, final String registrationNumber, final LocalDate deliveryDate, final String company ) throws NoSuchElementException  {
        VehicleModel[] vehicleModels = getVehicleModels(company);
        for ( VehicleModel vehicleModel : vehicleModels ) {
            if ( vehicleModel.getModel().contentEquals(model) ) {
                vehicleModel.setRegistrationNumber(registrationNumber);
                vehicleModel.setDeliveryDate(deliveryDate);
                return vehicleModel;
            }
        }
        throw new NoSuchElementException();
    }

    public int getNumberVehicleTypes ( final String company ) {
        return getVehicleModels(company).length;
    }

    public VehicleModel getVehicleByRegistrationNumber ( final String registrationNumber, final String company ) {
        VehicleModel[] vehicleModels = getVehicleModels(company);
        for ( VehicleModel vehicleModel : vehicleModels ) {
            if ( vehicleModel.getRegistrationNumber().contentEquals(registrationNumber) ) {
                return vehicleModel;
            }
        }
        return null;
    }

    public String getFirstVehicleModel ( final String company ) {
        return getVehicleModels(company)[0].getModel();
    }

    public String getLastVehicleModel ( final String company ) {
        VehicleModel[] vehicleModels = getVehicleModels(company);
        return vehicleModels[vehicleModels.length-1].getModel();
    }

    public String getNextVehicleModel ( final String model, final String company ) {
        VehicleModel[] vehicleModels = getVehicleModels(company);
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( vehicleModels[i].getModel().contentEquals(model) ) {
                return vehicleModels[i+1].getModel();
            }
        }
        return "";
    }

    public String getPreviousVehicleModel ( final String model, final String company ) {
        VehicleModel[] vehicleModels = getVehicleModels(company);
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( vehicleModels[i].getModel().contentEquals(model) ) {
                return vehicleModels[i-1].getModel();
            }
        }
        return "";
    }

     public void assignVehicleToRouteScheduleNumber ( final VehicleModel vehicleModel, final String routeNumber, final String scheduleNumber, final String company, final String fleetNumber ) {
        restTemplate.patchForObject(operationsServerUrl + "vehicle/allocate/",
                AllocateVehicleRequest.builder()
                        .allocatedTour(routeNumber + "/" + scheduleNumber)
                        .fleetNumber(fleetNumber)
                        .company(company)
                        .build(),
                Void.class);
    }

    /**
     * Delete all vehicles (only used as part of load function)
     * @param company a <code>String</code> with the name of the company.
     */
    public void deleteAllVehicles(final String company) {
        restTemplate.delete(operationsServerUrl + "vehicles/?company=" + company);
    }

    /**
     * This method converts a string date in the format dd-mm-yyyy to a LocalDate object. If the conversion is not
     * successful then return null.
     * @param date a <code>String</code> in the form dd-mm-yyyy
     * @return a <code>LocaLDate</code> with the converted date or null if no conversion is possible.
     */
    private LocalDate convertDateToLocalDate ( final String date ) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch ( DateTimeParseException dateTimeParseException ) {
            return null;
        }
    }
    
}
