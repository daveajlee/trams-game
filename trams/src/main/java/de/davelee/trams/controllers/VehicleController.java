package de.davelee.trams.controllers;

import de.davelee.trams.api.request.AllocateVehicleRequest;
import de.davelee.trams.api.request.PurchaseVehicleRequest;
import de.davelee.trams.api.response.PurchaseVehicleResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.api.response.VehiclesResponse;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.SortedVehicleResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class VehicleController {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${server.operations.url}")
	private String operationsServerUrl;

	public void assignVehicleToTour ( final String registrationNumber, final String allocatedTour, final String company ) {
		VehicleResponse[] vehicleModels = getVehicleModels(company);
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

	public VehicleResponse[] getAllCreatedVehicles (final String company ) {
		VehiclesResponse vehiclesResponse = restTemplate.getForObject(operationsServerUrl + "vehicles/?company=" + company, VehiclesResponse.class);
		if ( vehiclesResponse != null && vehiclesResponse.getVehicleResponses() != null ) {
			Arrays.sort(vehiclesResponse.getVehicleResponses(), new SortedVehicleResponses());
			return vehiclesResponse.getVehicleResponses();
		}
		return null;
	}

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
		VehicleResponse[] vehicleModels = getAllCreatedVehicles(company);
		if ( vehicleModels.length == 0 ) { return false; }
		for ( int i = 0; i < vehicleModels.length; i++ ) {
			if ( hasVehicleBeenDelivered(vehicleModels[i].getDeliveryDate(), currentDate)) { return true; }
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
		VehicleResponse[] vehicleModels = getVehicleModels(company);
		for ( VehicleResponse vehicleModel : vehicleModels ) {
			if ( vehicleModel.getAdditionalTypeInformationMap().get("Registration Number").contentEquals(registrationNumber) ) {
				return vehicleModel;
			}
		}
		return null;
	}

	public int getAge (final String deliveryDate, final String currentDate ) {
		LocalDate myCurrentDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalDate myDeliveryDate = LocalDate.parse(deliveryDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		if ( myCurrentDate.isBefore(myDeliveryDate) ) return -1;
		int yearDiff = Math.abs(myCurrentDate.getYear() - myDeliveryDate.getYear());
		int monthDiff = Math.abs(myCurrentDate.getMonthValue() - myDeliveryDate.getMonthValue());
		return (yearDiff * 12) + monthDiff;
	}

	public double getValue ( final VehicleResponse vehicleModel, final String currentDate ) {
		//TODO: Calculate depreciation factor in the server.
		//return vehicleModel.getPurchasePrice() - ((vehicleModel.getDepreciationFactor() * getAge(vehicleModel.getDeliveryDate(), currentDate)) * vehicleModel.getPurchasePrice());
		return 0.0;
	}

	/**
	 * Sell a vehicle.
	 * @param vehicleResponse a <code>VehicleResponse</code> object representing the vehicle to sell.
	 * @param currentDate a <code>String</code> with the current date in format dd-MM-yyyy.
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
		int fleetNum = fleetNumber.isPresent() ? fleetNumber.get() : random.nextInt();
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

	public VehicleResponse getVehicleByAllocatedTour ( final String allocatedTour, final String company ) {
		return restTemplate.getForObject(operationsServerUrl + "vehicle/allocate/?company=" + company + "&allocatedTour=" + allocatedTour, VehicleResponse.class);
	}

	public int createSuppliedVehicles(final Scenario scenario, final String currentDate, final String company) {
		Iterator<String> vehicleModels = scenario.getSuppliedVehicles().keySet().iterator();
		int numCreatedVehicles = 0;
		while (vehicleModels.hasNext()) {
			String vehicleModel = vehicleModels.next();
			for ( int i = 0; i < scenario.getSuppliedVehicles().get(vehicleModel); i++ )  {
				int fleetNumber = 100 + i;
				purchaseVehicle(vehicleModel, company, LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).getYear(), Optional.of(fleetNumber));
				numCreatedVehicles++;
			}
		}
		return numCreatedVehicles;
	}

	public VehicleResponse[] getVehicleModels ( final String company ) {
		VehiclesResponse vehiclesResponse = restTemplate.getForObject(operationsServerUrl + "vehicles/?company=" + company, VehiclesResponse.class);
		if ( vehiclesResponse != null && vehiclesResponse.getVehicleResponses() != null ) {
			return vehiclesResponse.getVehicleResponses();
		}
		return null;
	}

	public VehicleResponse[] getVehicleModelsForRoute ( final String company, final String routeNumber ) {
		//TODO: implement call to REST service to return the vehicles.
		return new VehicleResponse[0];
	}

	public String getCurrentStopName (final VehicleResponse vehicleModel, final String currentDateTime, final String difficultyLevel ) {
		//TODO: implement method completely.
		return "";
	}

	public String getDestination ( final VehicleResponse vehicleModel, final LocalDateTime currentDateTime, final DifficultyLevel difficultyLevel ) {
		//TODO: implement method completely.
		return "";
	}

	public void shortenSchedule ( final VehicleResponse vehicleModel, final String newDestination, final String currentDateTime ) {
		//TODO: implement shorten schedule correctly.
	}

	public void outOfService ( final VehicleResponse vehicleModel, final String restartStop, final String currentDateTime,
							   final String difficultyLevel ) {
		final String currentStopName = getCurrentStopName(vehicleModel, currentDateTime, difficultyLevel);
	}

	public List<String> getAllocations ( final String company ) {
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

	public String getFirstVehicleModel ( final String company ) {
		return getVehicleModels(company)[0].getModelName();
	}

	public String getLastVehicleModel ( final String company ) {
		VehicleResponse[] vehicleModels = getVehicleModels(company);
		return vehicleModels[vehicleModels.length-1].getModelName();
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

	public String getNextVehicleModel ( final String model, final String company) {
		VehicleResponse[] vehicleModels = getVehicleModels(company);
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

}
