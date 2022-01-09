package de.davelee.trams.controllers;

import de.davelee.trams.api.request.UserRequest;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.api.response.UsersResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This class enables access to Driver data via REST endpoints to the TraMS Operations microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
@Getter
@Setter
public class DriverController {

    private RestTemplate restTemplate;

    @Value("${server.personalman.url}")
    private String personalManServerUrl;

    private String token;

    /**
     * Return all drivers belonging to the supplied company.
     * @param company a <code>String</code> containing the name of the company to return drivers for.
     * @param username a <code>String</code> containing the username of the user requesting this feature.
     * @return an array of <code>UserResponse</code> objects containing all drivers belonging to the supplied company.
     */
    public UserResponse[] getAllDrivers (final String company, final String username) {
        try {
            UsersResponse usersResponse = restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username="+ username + "&token=" + token, UsersResponse.class);
            if (usersResponse != null && usersResponse.getUserResponses() != null) {
                return usersResponse.getUserResponses();
            }
            return null;
        } catch ( HttpClientErrorException exception ) {
            //If forbidden then return null.
            return null;
        }
    }
	
	/**
     * Employ a new driver.
     * @param name a <code>String</code> with name of driver.
     * @param company a <code>String</code> with company that driver should work for.
     * @param startDate a <code>String</code> with the date that the driver should start in dd-MM-yyyy format.
     */
    public void employDriver ( final String name, final String company, final String startDate ) {
        restTemplate.postForObject(personalManServerUrl + "user/", UserRequest.builder()
                .dateOfBirth("01-01-1990")
                .firstName(name.split(" ")[0])
                .surname(name.split(" ")[1])
                .leaveEntitlementPerYear(25)
                .company(company)
                .password("test")
                .position("Tester")
                .role("ADMIN")
                .username(name.split(" ")[0].charAt(0) + name.split(" ")[1])
                .workingDays("Monday,Tuesday")
                .startDate(startDate)
                .build(), Void.class);
    }

    /**
     * Employ a number of drivers so that the company has some drivers at the beginning.
     * @param suppliedDriverNames a <code>String</code> with the names of the drivers who should be employed.
     * @param startDate a <code>String</code> with the date that the drivers should begin in the format dd-MM-yyyy
     * @param company a <code>String</code> with the name of the company that the drivers should work for.
     */
    public void createSuppliedDrivers(final List<String> suppliedDriverNames, final String startDate, final String company ) {
        for ( String suppliedDriver : suppliedDriverNames) {
            employDriver(suppliedDriver, company, startDate);
        }
    }

    /**
     * This method loads the supplied drivers list and deletes all previous drivers.
     * @param userResponses a <code>UserResponse</code> array containing the drivers to load.
     * @param company a <code>String</code> with company that drivers should work for.
     */
    public void loadDrivers ( final UserResponse[] userResponses, final String company ) {
        restTemplate.delete(personalManServerUrl + "api/company/?name=" + company + "&token=" + token);
        for ( UserResponse driverModel : userResponses ) {
            restTemplate.postForObject(personalManServerUrl + "user/", UserRequest.builder()
                    .dateOfBirth("01-01-1990")
                    .firstName(driverModel.getFirstName())
                    .surname(driverModel.getSurname())
                    .leaveEntitlementPerYear(25)
                    .company(company)
                    .password("test")
                    .position("Tester")
                    .role("ADMIN")
                    .username(driverModel.getUsername())
                    .workingDays("Monday,Tuesday")
                    .startDate(driverModel.getStartDate())
                    .build(), Void.class);
        }
    }

    /**
     * This method checks if any employees have started working for the company!
     * @param companyResponse a <code>CompanyResponse</code> object containing information about the company.
     * @return a <code>boolean</code> which is true iff some drivers have started working.
     */
    public boolean hasSomeDriversBeenEmployed ( final CompanyResponse companyResponse ) {
        //TODO: determine how to get username.
        UserResponse[] userResponses = getAllDrivers(companyResponse.getName(), "mmustermann");
        System.out.println("Attempted to get responses");
        if (userResponses != null && userResponses.length > 0) {
            System.out.println("I have responses...");
            for (UserResponse userResponse : userResponses) {
                LocalDate currentDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                LocalDate startDate = LocalDate.parse(userResponse.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                if (currentDate.isAfter(startDate) || currentDate.isEqual(startDate)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Get a driver based on its name.
     * @param name a <code>String</code> with the name of the driver.
     * @param company a <code>String</code> with the name of the company that the driver should work for.
     * @return a <code>UserResponse</code> object.
     */
    public UserResponse getDriverByName (final String name, final String company ) {
        return restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username=" + name + "&token=" + token, UserResponse.class);
    }

    /**
     * Sack a driver.
     * @param company a <code>String</code> with the name of the company sacking the driver.
     * @param username a <code>String</code> with the username of the driver being sacked.
     */
    public void sackDriver ( final String company, final String username ) {
        restTemplate.delete(personalManServerUrl + "user/?company=" + company + "&username=" + username + "&token=" + token);
    }

    @Autowired
    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
