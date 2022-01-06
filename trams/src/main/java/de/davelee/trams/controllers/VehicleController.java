package de.davelee.trams.controllers;

import de.davelee.trams.api.request.AllocateVehicleRequest;
import de.davelee.trams.api.request.PurchaseVehicleRequest;
import de.davelee.trams.api.response.PurchaseVehicleResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.api.response.VehiclesResponse;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.SortedVehicleResponses;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class enables access to Vehicle data via REST endpoints to the TraMS Operations microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class VehicleController {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${server.operations.url}")
	private String operationsServerUrl;

	private final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	/**
	 * Assign the supplied vehicle to the specified tour for the specified company.
	 * @param registrationNumber a <code>String</code> containing the registration number of the vehicle to assign.
	 * @param allocatedTour a <code>String</code> containing the id of the tour to assign the vehicle to.
	 * @param company a <code>String</code> with the name of the company to assign the vehicle for.
	 */
	public void assignVehicleToTour ( final String registrationNumber, final String allocatedTour, final String company ) {
		VehicleResponse[] vehicleModels = getVehicles(company).getVehicleResponses();
		for ( VehicleResponse vehicleModel : vehicleModels ) {
			if ( vehicleModel.getAdditionalTypeInformationMap().get("Registration Number").contentEquals(registrationNumber) ) {
				restTemplate.patchForObject(operationsServerUrl + "vehicle/allocate/",
						AllocateVehicleRequest.builder()
								.allocatedTour(allocatedTour)
								.fleetNumber(vehicleModel.getFleetNumber())
								.company(company)
								.build(),
						Void.class);
			}
		}
	}

	/**
	 * Return all vehicles for the specified company.
	 * @param company a <code>String</code> with the name of the company to retrieve the vehicles for.
	 * @return a <code>VehiclesResponse</code> object containing all the vehicles which exist for the specified company.
	 */
	public VehiclesResponse getAllCreatedVehicles (final String company ) {
		VehiclesResponse vehiclesResponse = restTemplate.getForObject(operationsServerUrl + "vehicles/?company=" + company, VehiclesResponse.class);
		if ( vehiclesResponse != null && vehiclesResponse.getVehicleResponses() != null ) {
			Arrays.sort(vehiclesResponse.getVehicleResponses(), new SortedVehicleResponses());
			return vehiclesResponse;
		}
		return null;
	}

	/**
	 * Check if the vehicle has been delivered i.e. if the delivery date has already past or the delivery date is today.
	 * @param deliveryDate a <code>String</code> with the delivery date of the vehicle.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
	 * @return a <code>boolean</code> which is true iff the vehicle has been delivered.
	 */
	public boolean hasVehicleBeenDelivered (final String deliveryDate, final String currentDate ) {
		return LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).isAfter(LocalDate.parse(deliveryDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")))
				|| LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).isEqual(LocalDate.parse(deliveryDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	}

	/**
	 * This method checks if any vehicles have been delivered to the company yet!
	 * @param company a <code>String</code> with the name of the company.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
	 * @return a <code>boolean</code> which is true iff some vehicles have been delivered!
	 */
	public boolean hasSomeVehiclesBeenDelivered ( final String company, final String currentDate) {
		VehicleResponse[] vehicleResponses = getAllCreatedVehicles(company).getVehicleResponses();
		if ( vehicleResponses.length == 0 ) { return false; }
		for ( VehicleResponse vehicleResponse : vehicleResponses ) {
			if ( hasVehicleBeenDelivered(vehicleResponse.getDeliveryDate(), currentDate)) { return true; }
		}
		return false;
	}

	/**
	 * Get a vehicle based on its registration number.
	 * @param registrationNumber a <code>String</code> with the registration number.
	 * @param company a <code>String</code> with the name of the company.
	 * @return a <code>VehicleModel</code> object.
	 */
	public VehicleResponse getVehicleByRegistrationNumber ( final String registrationNumber, final String company ) {
		VehicleResponse[] vehicleResponses = getVehicles(company).getVehicleResponses();
		for ( VehicleResponse vehicleResponse : vehicleResponses ) {
			if ( vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number").contentEquals(registrationNumber) ) {
				return vehicleResponse;
			}
		}
		return null;
	}

	/**
	 * Return the number of months since a vehicle has been delivered.
	 * @param deliveryDate a <code>String</code> with the delivery date of the vehicle.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
	 * @return a <code>int</code> with the number of months since a vehicle has been delivered.
	 */
	public int getAge (final String deliveryDate, final String currentDate ) {
		LocalDate myCurrentDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalDate myDeliveryDate = LocalDate.parse(deliveryDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		if ( myCurrentDate.isBefore(myDeliveryDate) ) return -1;
		int yearDiff = Math.abs(myCurrentDate.getYear() - myDeliveryDate.getYear());
		int monthDiff = Math.abs(myCurrentDate.getMonthValue() - myDeliveryDate.getMonthValue());
		return (yearDiff * 12) + monthDiff;
	}

	/**
	 * Return the value of this vehicle on the present date including depreciation.
	 * @param vehicleResponse a <code>VehicleResponse</code> with details of the vehicle to get the value of.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
	 * @return a <code>double</code> with the value of this vehicle.
	 */
	public double getValue ( final VehicleResponse vehicleResponse, final String currentDate ) {
		logger.info("getValue method with " + vehicleResponse + " & " + currentDate);
		//TODO: Calculate depreciation factor in the server.
		//return vehicleResponse.getPurchasePrice() - ((vehicleResponse.getDepreciationFactor() * getAge(vehicleResponse.getDeliveryDate(), currentDate)) * vehicleResponse.getPurchasePrice());
		return 0.0;
	}

	/**
	 * Sell a vehicle.
	 * @param vehicleResponse a <code>VehicleResponse</code> object representing the vehicle to sell.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
	 * @return a <code>double</code> containing the balance that the user received for selling the vehicle.
	 */
	public double sellVehicle ( final VehicleResponse vehicleResponse, final String currentDate ) {
		restTemplate.delete(operationsServerUrl + "vehicle/?company=" + vehicleResponse.getCompany() + "&fleetNumber=" + vehicleResponse.getFleetNumber());
		return getValue(vehicleResponse, currentDate);
	}

	/**
	 * Purchase a new vehicle.
	 * @param type a <code>String</code> with the vehicle type.
	 * @param company a <code>String</code> with the name of the company.
	 * @param year a <code>int</code> with the year that the vehicle was purchased in format yyyy
	 * @param fleetNumber a <code>Optional</code> of <code>int</code> with the desired fleet number (if null, will be generated randomly)
	 * @return a <code>double</code> with the purchase price of the vehicle.
	 */
	public double purchaseVehicle ( final String type, final String company, final int year, final Optional<Integer> fleetNumber) {
		Random random = new Random();
		int fleetNum = fleetNumber.orElse(random.nextInt());
		PurchaseVehicleResponse purchaseVehicleResponse = restTemplate.postForObject(operationsServerUrl + "vehicle/", PurchaseVehicleRequest.builder()
				.company(company)
				.fleetNumber("" + fleetNum)
				.livery("Green with Red text")
				.vehicleType("BUS")
				.modelName(type)
				.additionalTypeInformationMap(Map.of("Registration Number", generateRandomReg(
						year, company)))
				.seatingCapacity(50)
				.standingCapacity(95)
				.build(), PurchaseVehicleResponse.class);
		if ( purchaseVehicleResponse != null && purchaseVehicleResponse.isPurchased() ) {
			return purchaseVehicleResponse.getPurchasePrice();
		}
		return 0.0;
	}

	/**
	 * Return the vehicle which is allocated to a particular tour for a particular company.
	 * @param allocatedTour a <code>String</code> with the tour that the vehicle should be allocated to.
	 * @param company a <code>String</code> containing the name of the company that the vehicle should belong to.
	 * @return a <code>VehicleResponse</code> object with information about the vehicle which may be null if no vehicle matches.
	 */
	public VehicleResponse getVehicleByAllocatedTour ( final String allocatedTour, final String company ) {
		return restTemplate.getForObject(operationsServerUrl + "vehicle/allocate/?company=" + company + "&allocatedTour=" + allocatedTour, VehicleResponse.class);
	}

	/**
	 * Purchase the vehicles with the supplied vehicles in the scenario.
	 * @param suppliedVehicles a <code>HashMap</code> containing per entry the model that should be purchased as String and the number that should be purchased as Integer.
	 * @param currentDate a <code>String</code> containing the current date in the format dd-MM-yyyy
	 * @param company a <code>String</code> containing the name of the company that the vehicle should belong to.
	 * @return a <code>int</code> containing the number of vehicles that have been purchased.
	 */
	public int createSuppliedVehicles(final HashMap<String, Integer> suppliedVehicles, final String currentDate, final String company) {
		Iterator<String> vehicleModels = suppliedVehicles.keySet().iterator();
		int numCreatedVehicles = 0;
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < suppliedVehicles.get(vehicleModel); i++ )  {
				int fleetNumber = 100 + i;
				purchaseVehicle(vehicleModel, company, LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).getYear(), Optional.of(fleetNumber));
				numCreatedVehicles++;
			}
		}
		return numCreatedVehicles;
	}

	/**
	 * Return all vehicles for a particular company.
	 * @param company a <code>String</code> containing the name of the company that the vehicle should belong to.
	 * @return a <code>VehiclesResponse</code> containing the vehicles belonging to the company including a count.
	 */
	public VehiclesResponse getVehicles ( final String company ) {
		VehiclesResponse vehiclesResponse = restTemplate.getForObject(operationsServerUrl + "vehicles/?company=" + company, VehiclesResponse.class);
		if ( vehiclesResponse != null && vehiclesResponse.getVehicleResponses() != null ) {
			return vehiclesResponse;
		}
		return null;
	}

	/**
	 * Return all vehicles for a particular route run by a particular company.
	 * @param company a <code>String</code> containing the name of the company that the vehicle should belong to.
	 * @param routeNumber a <code>String</code> containing the route
	 * @return a <code>VehiclesResponse</code> containing the vehicles belonging to the company and route including a count.
	 */
	public VehiclesResponse getVehiclesForRoute ( final String company, final String routeNumber ) {
		logger.info("getVehiclesForRoute method with " + company + " & " + routeNumber);
		//TODO: implement call to REST service to return the vehicles.
		return VehiclesResponse.builder().vehicleResponses(new VehicleResponse[0]).count(0L).build();
	}

	/**
	 * Return the current position of the vehicle for the specified date time and configured difficulty level.
	 * @param vehicleResponse a <code>VehicleResponse</code> containing the information about the vehicle to get the position for.
	 * @param currentDateTime a <code>String</code> containing the date and time in the format dd-MM-yyyy HH:mm
	 * @param difficultyLevel a <code>String</code> containing the difficulty level which can be either EASY, MEDIUM, HARD.
	 * @return a <code>String</code> containing the current stop name as the position of the vehicle.
	 */
	public String getCurrentStopName (final VehicleResponse vehicleResponse, final String currentDateTime, final String difficultyLevel ) {
		logger.info("getCurrentStopName method with " + vehicleResponse + " & " + currentDateTime + " & " + difficultyLevel);
		//TODO: implement method completely.
		return "";
	}

	/**
	 * Return the destination of the vehicle for the specified date time and configured difficulty level.
	 * @param vehicleResponse a <code>VehicleResponse</code> containing the information about the vehicle to get destination for.
	 * @param currentDateTime a <code>String</code> containing the date and time in the format dd-MM-yyyy HH:mm
	 * @param difficultyLevel a <code>String</code> containing the difficulty level which can be either EASY, MEDIUM, HARD.
	 * @return a <code>String</code> containing the current destination of the vehicle.
	 */
	public String getDestination ( final VehicleResponse vehicleResponse, final String currentDateTime, final DifficultyLevel difficultyLevel ) {
		logger.info("getDestination method with " + vehicleResponse + " & " + currentDateTime + " & " + difficultyLevel);
		//TODO: implement method completely.
		return "";
	}

	/**
	 * Shorten the tour of the specified vehicle so that it ends at the specified destination.
	 * @param vehicleResponse a <code>VehicleResponse</code> containing the information about the vehicle to shorten.
	 * @param newDestination a <code>String</code> containing the new destination.
	 * @param currentDateTime a <code>String</code> containing the date and time in the format dd-MM-yyyy HH:mm
	 */
	public void shortenSchedule ( final VehicleResponse vehicleResponse, final String newDestination, final String currentDateTime ) {
		logger.info("getDestination method with " + vehicleResponse + " & " + currentDateTime + " & " + newDestination);
		//TODO: implement shorten schedule correctly.
	}

	/**
	 * Put the vehicle out of service until the specified stop for the specified date time and configured difficulty level.
	 * @param vehicleResponse a <code>VehicleResponse</code> containing the information about the vehicle to put out of service.
	 * @param restartStop a <code>String</code> containing the stop name where the vehicle should resume service.
	 * @param currentDateTime a <code>String</code> containing the date and time in the format dd-MM-yyyy HH:mm
	 * @param difficultyLevel a <code>String</code> containing the difficulty level which can be either EASY, MEDIUM, HARD.
	 */
	public void outOfService ( final VehicleResponse vehicleResponse, final String restartStop, final String currentDateTime,
							   final String difficultyLevel ) {
		logger.info("outOfService method with " + vehicleResponse + " & " + currentDateTime + " & " + difficultyLevel + " & " + restartStop);
		final String currentStopName = getCurrentStopName(vehicleResponse, currentDateTime, difficultyLevel);
		logger.info("currentStopName is " + currentStopName);
	}

	/**
	 * Return all the allocations for a particular company.
	 * @param company a <code>String</code> containing the name of the company to return allocations for.
	 * @return a <code>List</code> of <code>String</code> containing each allocation in the format tour &amp; registration number.
	 */
	public List<String> getAllocations ( final String company ) {
		//Allocations list.
		ArrayList<String> allocations = new ArrayList<>();
		//Now go through and add their allocation if they already have an allocation.
		VehicleResponse[] vehicleModels = getVehicles(company).getVehicleResponses();
		for ( VehicleResponse vehicleModel : vehicleModels ) {
			if ( vehicleModel.getAllocatedTour() != null ) {
				allocations.add(vehicleModel.getAllocatedTour() + " & " + vehicleModel.getAdditionalTypeInformationMap().get("Registration Number"));
			}
		}
		//Return allocations list.
		return allocations;
	}

	/**
	 * Return the previous vehicle model in the list for a particular company.
	 * @param model a <code>String</code> the current model name shown to the user.
	 * @param company a <code>String</code> containing the name of the company.
	 * @return a <code>String</code> containing the new model name to display to the user.
	 */
	public String getPreviousVehicleModel ( final String model, final String company ) {
		VehicleResponse[] vehicleModels = getVehicles(company).getVehicleResponses();
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( vehicleModels[i].getModelName().contentEquals(model) ) {
				return vehicleModels[i-1].getModelName();
			}
		}
		return "";
	}

	/**
	 * Return the next vehicle model in the list for a particular company.
	 * @param model a <code>String</code> the current model name shown to the user.
	 * @param company a <code>String</code> containing the name of the company.
	 * @return a <code>String</code> containing the new model name to display to the user.
	 */
	public String getNextVehicleModel ( final String model, final String company) {
		VehicleResponse[] vehicleModels = getVehicles(company).getVehicleResponses();
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( vehicleModels[i].getModelName().contentEquals(model) ) {
				return vehicleModels[i+1].getModelName();
			}
		}
		return "";
	}

	/**
	 * Load Vehicles.
	 * @param vehicleModels an array of <code>VehicleModel</code> objects with vehicles to store and delete all other vehicles.
	 * @param company a <code>String</code> with the name of the company.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
	 */
	public void loadVehicles ( final VehicleResponse[] vehicleModels, final String company, final LocalDate currentDate ) {
		restTemplate.delete(operationsServerUrl + "vehicles/?company=" + company);
		for ( VehicleResponse vehicleModel : vehicleModels ) {
			purchaseVehicle(vehicleModel.getModelName(), vehicleModel.getCompany(), currentDate.getYear(), Optional.of(Integer.parseInt(vehicleModel.getFleetNumber())));
		}
	}

	/**
	 * Helper method to generate random vehicle registration.
	 * @param year a <code>String</code> with the current simulation year.
	 * @param company a <code>String</code> with the name of the company.
	 * @return a <code>String</code> with the generated vehicle registration.
	 */
	public String generateRandomReg ( final int year, final String company ) {
		//Generate random registration - in form 2 digit year - then 5 random letters.
		boolean isUniqueReg = true;
		String randomReg = "" + year + "-";
		//This is our loop - till we get unique reg.
		do {
			//Here is random reg.
			randomReg += RandomStringUtils.randomAlphabetic(5);
			//Now check that random reg not been generated before.
			VehicleResponse[] vehicleModels = getVehicles(company).getVehicleResponses();
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

}
