package de.davelee.trams.controllers;

import de.davelee.trams.api.request.UserRequest;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.ScenarioModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.DriverService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Getter
@Setter
public class DriverController {
	
	@Autowired
	private DriverService driverService;

	@Autowired
	private GameController gameController;

    private String token;

    public UserResponse[] getAllDrivers (final String company) {
		return driverService.getAllDrivers(company, token);
	}
	
	/**
     * Employ a new driver.
     * @param name a <code>String</code> with name.
     */
    public void employDriver ( final String name, final String company, final LocalDate startDate ) {
    	//TODO: Employing drivers should cost money.
        gameController.withdrawBalance(0);
        driverService.saveDriver(UserRequest.builder()
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
                .startDate(startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .build());
    }

    public void createSuppliedDrivers(final ScenarioModel scenarioModel, final LocalDate startDate, final String company ) {
        for ( String suppliedDriver : scenarioModel.getSuppliedDrivers()) {
            driverService.saveDriver(UserRequest.builder()
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
                    .startDate(startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .build());
        }
    }

    /**
     * This method loads the supplied drivers list and deletes all previous drivers.
     * @param driverModels a <code>DriverModel</code> array containing the drivers to load.
     */
    public void loadDrivers ( final UserResponse[] driverModels, final String company ) {
        driverService.removeAllDrivers(company, token);
        for ( UserResponse driverModel : driverModels ) {
            driverService.saveDriver(UserRequest.builder()
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
                    .build());
        }
    }
    
    public int getNumberDrivers ( final String company ) {
        return driverService.getAllDrivers(company, token).length;
    }

    /**
     * This method checks if any employees have started working for the company!
     * @return a <code>boolean</code> which is true iff some drivers have started working.
     */
    public boolean hasSomeDriversBeenEmployed ( final String company ) {
        UserResponse[] driverModels = driverService.getAllDrivers(company, token);
        if (driverModels != null && driverModels.length > 0) {
            GameModel gameModel = gameController.getGameModel();
            for (int i = 0; i < driverModels.length; i++) {
                if (driverService.hasStartedWork(LocalDate.parse(driverModels[i].getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")), gameModel.getCurrentDateTime().toLocalDate())) {
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
        return driverService.getDriverByName(name, company, token);
    }

    /**
     * Sack a driver.
     * @param company a <code>String</code> wi.
     */
    public void sackDriver ( final String company, final String username ) {
        driverService.removeDriver(company, username, token);
    }

}
