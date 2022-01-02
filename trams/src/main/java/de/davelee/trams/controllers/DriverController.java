package de.davelee.trams.controllers;

import de.davelee.trams.api.request.UserRequest;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.api.response.UsersResponse;
import de.davelee.trams.beans.Scenario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Getter
@Setter
public class DriverController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.personalman.url}")
    private String personalManServerUrl;

    private String token;

    public UserResponse[] getAllDrivers (final String company) {
        try {
            UsersResponse usersResponse = restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username=mmustermann&token=" + token, UsersResponse.class);
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
     * @param name a <code>String</code> with name.
     */
    public void employDriver ( final String name, final String company, final String startDate, final String playerName ) {
        restTemplate.postForObject(personalManServerUrl + "user/", UserRequest.builder()
                .dateOfBirth("01-01-1990")
                .firstName(name.split(" ")[0])
                .surname(name.split(" ")[1])
                .leaveEntitlementPerYear(25)
                .company(company)
                .password("test")
                .position("Tester")
                .role("ADMIN")
                .username(name.split(" ")[0].substring(0,1) + name.split(" ")[1])
                .workingDays("Monday,Tuesday")
                .startDate(startDate)
                .build(), Void.class);
    }

    public void createSuppliedDrivers(final Scenario scenario, final String startDate, final String company ) {
        for ( String suppliedDriver : scenario.getSuppliedDrivers()) {
            restTemplate.postForObject(personalManServerUrl + "user/", UserRequest.builder()
                    .dateOfBirth("01-01-1990")
                    .firstName(suppliedDriver.split(" ")[0])
                    .surname(suppliedDriver.split(" ")[1])
                    .leaveEntitlementPerYear(25)
                    .company(company)
                    .password("test")
                    .position("Tester")
                    .role("ADMIN")
                    .username(suppliedDriver.split(" ")[0].substring(0,1) + suppliedDriver.split(" ")[1])
                    .workingDays("Monday,Tuesday")
                    .startDate(startDate)
                    .build(), Void.class);
        }
    }

    /**
     * This method loads the supplied drivers list and deletes all previous drivers.
     * @param driverModels a <code>DriverModel</code> array containing the drivers to load.
     */
    public void loadDrivers ( final UserResponse[] driverModels, final String company ) {
        restTemplate.delete(personalManServerUrl + "api/company/?name=" + company + "&token=" + token);
        for ( UserResponse driverModel : driverModels ) {
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
     * @return a <code>boolean</code> which is true iff some drivers have started working.
     */
    public boolean hasSomeDriversBeenEmployed ( final CompanyResponse companyResponse ) {
        UserResponse[] driverModels = getAllDrivers(companyResponse.getName());
        System.out.println("Attempted to get responses");
        if (driverModels != null && driverModels.length > 0) {
            System.out.println("I have responses...");
            for (int i = 0; i < driverModels.length; i++) {
                LocalDate currentDate = LocalDate.parse(companyResponse.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                LocalDate startDate = LocalDate.parse(driverModels[i].getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                if ( currentDate.isAfter(startDate) || currentDate.isEqual(startDate) ) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Get a driver based on its name.
     * @param name a <code>String</code> with the name.
     * @return a <code>UserResponse</code> object.
     */
    public UserResponse getDriverByName (final String name, final String company ) {
        return restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username=" + name + "&token=" + token, UserResponse.class);
    }

    /**
     * Sack a driver.
     * @param company a <code>String</code> wi.
     */
    public void sackDriver ( final String company, final String username ) {
        restTemplate.delete(personalManServerUrl + "user/?company=" + company + "&username=" + username + "&token=" + token);
    }

}
