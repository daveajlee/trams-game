package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import de.davelee.trams.api.request.UserRequest;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.api.response.UsersResponse;
import de.davelee.trams.model.DriverModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DriverService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${server.personalman.url}")
	private String personalManServerUrl;

	/**
     * Check if the driver has started work or not.
	 * @param startDate a <code>LocalDate</code> object with the start date.
     * @param currentDate a <code>LocalDate</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork (final LocalDate startDate, final LocalDate currentDate ) {
    	return currentDate.isAfter(startDate) || currentDate.isEqual(startDate);
    }

	private DriverModel convertToDriverModel ( final UserResponse userResponse ) {
		return DriverModel.builder()
				.name(userResponse.getFirstName() + " " + userResponse.getSurname())
				.contractedHours(userResponse.getContractedHoursPerWeek())
				.startDate(convertDateToLocalDate(userResponse.getStartDate()))
				.build();
	}

	public DriverModel getDriverByName(final String name, final String company, final String token) {
		UserResponse userResponse = restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username=" + name + "&token=" + token, UserResponse.class);
		if ( userResponse != null ) {
			return convertToDriverModel(userResponse);
		}
		return null;
	}

	public DriverModel[] getAllDrivers (final String company, final String token) {
		UsersResponse usersResponse = restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&token=" + token, UsersResponse.class);
		if ( usersResponse != null && usersResponse.getUserResponses() != null ) {
			DriverModel[] driverModels = new DriverModel[usersResponse.getUserResponses().length];
			for (int i = 0; i < driverModels.length; i++) {
				driverModels[i] = convertToDriverModel(usersResponse.getUserResponses()[i]);
			}
			return driverModels;
		}
		return null;
	}

	public void saveDriver ( final DriverModel driverModel ) {
		restTemplate.postForObject(personalManServerUrl + "user/",
				UserRequest.builder()
						.dateOfBirth("01-01-1990")
						.firstName(driverModel.getName().split(" ")[0])
						.surname(driverModel.getName().split(" ")[1])
						.leaveEntitlementPerYear(driverModel.getLeaveEntitlementPerYear())
						.company(driverModel.getCompany())
						.password("test")
						.position("Tester")
						.role("ADMIN")
						.username(driverModel.getName().split(" ")[0].substring(0,1) + driverModel.getName().split(" ")[1])
						.workingDays("Monday,Tuesday")
						.startDate(driverModel.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
						.build(),
				Void.class);
	}

	public void removeDriver ( final DriverModel driverModel, final String token ) {
		restTemplate.delete(personalManServerUrl + "user/?company=" + driverModel.getCompany() + "&username=" + driverModel.getName() + "&token=" + token);
	}

	/**
	 * Delete all stored drivers (primarily used for loading a game)
	 * @param company a <code>String</code> containing the name of the company to delete drivers for.
	 * @param token a <code>String</code> containing the authentification token.
	 */
	public void removeAllDrivers ( final String company, final String token ) {
		restTemplate.delete(personalManServerUrl + "api/company/?name=" + company + "&token=" + token);
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
