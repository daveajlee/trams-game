package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import de.davelee.trams.api.request.AdjustVehicleDelayRequest;
import de.davelee.trams.api.request.AllocateVehicleRequest;
import de.davelee.trams.api.request.PurchaseVehicleRequest;
import de.davelee.trams.api.response.PurchaseVehicleResponse;
import de.davelee.trams.api.response.VehicleDelayResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.api.response.VehiclesResponse;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.DifficultyLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RestTemplate restTemplate;

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

    public VehicleResponse[] getVehicleModels ( final String company ) {
        VehiclesResponse vehiclesResponse = restTemplate.getForObject(operationsServerUrl + "vehicles/?company=" + company, VehiclesResponse.class);
        if ( vehiclesResponse != null && vehiclesResponse.getVehicleResponses() != null ) {
            return vehiclesResponse.getVehicleResponses();
        }
        return null;
    }

    public double saveVehicle ( final PurchaseVehicleRequest purchaseVehicleRequest ) {
        PurchaseVehicleResponse purchaseVehicleResponse = restTemplate.postForObject(operationsServerUrl + "vehicle/", purchaseVehicleRequest, PurchaseVehicleResponse.class);
        if ( purchaseVehicleResponse != null && purchaseVehicleResponse.isPurchased() ) {
            return purchaseVehicleResponse.getPurchasePrice();
        }
        return 0.0;
    }

    public void removeVehicle ( final String company, final String fleetNumber ) {
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
        VehicleResponse[] vehicleModels = getVehicleModels(company);
        for ( VehicleResponse vehicleModel : vehicleModels ) {
            if ( vehicleModel.getAllocatedTour() != null ) {
                allocations.add(vehicleModel.getAllocatedTour() + " & " + vehicleModel.getAdditionalTypeInformationMap().get("Registration Number"));
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
            VehicleResponse[] vehicleModels = getVehicleModels(company);
            if ( vehicleModels != null ) {
                for ( VehicleResponse vehicleModel : vehicleModels ) {
                    if ( vehicleModel.getAdditionalTypeInformationMap().get("Registration Number").equalsIgnoreCase(randomReg) ) {
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
     * @param allocatedTour a <code>String</code> with the tour name that the vehicle should be running.
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>VehicleModel</code> which contains information about the vehicle found.
     */
    public VehicleResponse getVehicleByAllocatedTour ( final String allocatedTour, final String company ) {
        return restTemplate.getForObject(operationsServerUrl + "vehicle/allocate/?company=" + company + "&allocatedTour=" + allocatedTour, VehicleResponse.class);
    }

    public VehicleModel createVehicleObject ( final String model, final String registrationNumber, final LocalDate deliveryDate, final String company ) throws NoSuchElementException  {
        return VehicleModel.builder()
                .model(model)
                .registrationNumber(registrationNumber)
                .deliveryDate(deliveryDate)
                .company(company)
                .build();
    }

    public VehicleResponse getVehicleByRegistrationNumber ( final String registrationNumber, final String company ) {
        VehicleResponse[] vehicleModels = getVehicleModels(company);
        for ( VehicleResponse vehicleModel : vehicleModels ) {
            if ( vehicleModel.getAdditionalTypeInformationMap().get("Registration Number").contentEquals(registrationNumber) ) {
                return vehicleModel;
            }
        }
        return null;
    }

    public String getFirstVehicleModel ( final String company ) {
        return getVehicleModels(company)[0].getModelName();
    }

    public String getLastVehicleModel ( final String company ) {
        VehicleResponse[] vehicleModels = getVehicleModels(company);
        return vehicleModels[vehicleModels.length-1].getModelName();
    }

    public String getNextVehicleModel ( final String model, final String company ) {
        VehicleResponse[] vehicleModels = getVehicleModels(company);
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( vehicleModels[i].getModelName().contentEquals(model) ) {
                return vehicleModels[i+1].getModelName();
            }
        }
        return "";
    }

    public String getPreviousVehicleModel ( final String model, final String company ) {
        VehicleResponse[] vehicleModels = getVehicleModels(company);
        for ( int i = 0; i < vehicleModels.length; i++ ) {
            if ( vehicleModels[i].getModelName().contentEquals(model) ) {
                return vehicleModels[i-1].getModelName();
            }
        }
        return "";
    }

     public void assignVehicleToTour ( final String fleetNumber, final String allocatedTour, final String company ) {
        restTemplate.patchForObject(operationsServerUrl + "vehicle/allocate/",
                AllocateVehicleRequest.builder()
                        .allocatedTour(allocatedTour)
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
     * Reduces the current delay by a certain number of minutes.
     * @param vehicleModel a <code>VehicleModel</code> with the vehicle to determine delay for.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void reduceDelay(final VehicleModel vehicleModel, final int mins) {
        //We can only reduce delay if delay is not currently 0.
        if (vehicleModel.getDelay() != 0) {
            VehicleDelayResponse vehicleDelayResponse = restTemplate.patchForObject(operationsServerUrl + "vehicle/delay",
                    AdjustVehicleDelayRequest.builder()
                            .company(vehicleModel.getCompany())
                            .delayInMinutes(-mins)
                            .fleetNumber(vehicleModel.getFleetNumber())
                            .build(),
                    VehicleDelayResponse.class);
            vehicleModel.setDelay(vehicleDelayResponse.getDelayInMinutes());
        }
    }

    /**
     * Increases the vehicles current delay by a certain number of minutes.
     * @param vehicleModel a <code>VehicleModel</code> with the vehicle to determine delay for.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void increaseDelay(final VehicleModel vehicleModel, final int mins) {
        VehicleDelayResponse vehicleDelayResponse = restTemplate.patchForObject(operationsServerUrl + "vehicle/delay",
                AdjustVehicleDelayRequest.builder()
                        .company(vehicleModel.getCompany())
                        .delayInMinutes(mins)
                        .fleetNumber(vehicleModel.getFleetNumber())
                        .build(),
                VehicleDelayResponse.class);
        vehicleModel.setDelay(vehicleDelayResponse.getDelayInMinutes());
    }

    /**
     * Calculate a new random delay for this route schedule.
     * @param vehicleModel a <code>VehicleModel</code> with the vehicle to calculate the delay for.
     * @param difficultyLevel a <code>DifficultyLevel</code> object representing the difficulty level.
     */
    public void calculateNewDelay (final VehicleModel vehicleModel, final DifficultyLevel difficultyLevel ) {
        //Generate a random number between 0 and 1.
        Random randNumGen = new Random();
        int val = randNumGen.nextInt(100);
        //Create probability array.
        int[] ratioArray;
        //Set ratios according to difficulty level - default is easy.
        switch (difficultyLevel) {
            case INTERMEDIATE:
                ratioArray = new int[] { 20, 85, 95 };
                break;
            case MEDIUM:
                ratioArray = new int[] { 20, 75, 90 };
                break;
            case HARD:
                ratioArray = new int[] { 30, 60, 85 };
                break;
            default: //easy
                ratioArray = new int[] { 25, 85, 95 };
                break;
        }
        //With ratioArray[0] probability no delay change.
        if ( val < ratioArray[0] ) { return; }
        //With ratioArray[1] probability - reduce delay by 1-5 mins.
        if ( val >= ratioArray[0] && val < ratioArray[1] ) {
            int delayReduction = randNumGen.nextInt(5) + 1;
            reduceDelay(vehicleModel, delayReduction);
            return;
        }
        //With 10% probability - increase delay by 1-5 mins.
        if ( val >= ratioArray[1] && val < ratioArray[2] ) {
            int delayIncrease = randNumGen.nextInt(5) + 1;
            increaseDelay(vehicleModel, delayIncrease);
            return;
        }
        //Remaining probability - generate delay between 5 and 20 mins.
        int delayIncrease = randNumGen.nextInt(15) + 6;
        increaseDelay(vehicleModel, delayIncrease);
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
