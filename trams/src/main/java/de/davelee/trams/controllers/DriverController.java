package de.davelee.trams.controllers;

import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.ScenarioModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.DriverService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@Getter
@Setter
public class DriverController {
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private GameController gameController;

    public DriverModel[] getAllDrivers () {
		return driverService.getAllDrivers();
	}
	
	/**
     * Employ a new driver.
     * @param driverModel a <code>DriverModel</code> object including name, contracted hours and start date.
     */
    public void employDriver ( final DriverModel driverModel ) {
    	//TODO: Employing drivers should cost money.
        gameController.withdrawBalance(0);
        driverService.saveDriver(driverModel);
    }

    public void createSuppliedDrivers(final ScenarioModel scenarioModel, LocalDate startDate) {
        for ( String suppliedDriver : scenarioModel.getSuppliedDrivers()) {
            driverService.saveDriver(DriverModel.builder()
                    .contractedHours(35)
                    .name(suppliedDriver)
                    .startDate(startDate)
                    .build());
        }
    }

    /**
     * This method loads the supplied drivers list and deletes all previous drivers.
     * @param driverModels a <code>DriverModel</code> array containing the drivers to load.
     */
    public void loadDrivers ( final DriverModel[] driverModels ) {
        driverService.removeAllDrivers();
        for ( DriverModel driverModel : driverModels ) {
            driverService.saveDriver(driverModel);
        }
    }
    
    public int getNumberDrivers ( ) {
        return driverService.getAllDrivers().length;
    }

    /**
     * This method checks if any employees have started working for the company!
     * @return a <code>boolean</code> which is true iff some drivers have started working.
     */
    public boolean hasSomeDriversBeenEmployed ( ) {
        if ( getNumberDrivers() == 0 ) { return false; }
        DriverModel[] driverModels = driverService.getAllDrivers();
        GameModel gameModel = gameController.getGameModel();
        for ( int i = 0; i < driverModels.length; i++ ) {
            if ( driverService.hasStartedWork(driverModels[i].getStartDate(), gameModel.getCurrentDateTime().toLocalDate()) ) { return true; }
        }
        return false;
    }

    /**
     * Get a driver based on its name.
     * @param name a <code>String</code> with the name.
     * @return a <code>DriverModel</code> object.
     */
    public DriverModel getDriverByName (final String name ) {
        return driverService.getDriverByName(name);
    }

    /**
     * Sack a driver.
     * @param driverModel a <code>DriverModel</code> object representing the driver to sack.
     */
    public void sackDriver ( final DriverModel driverModel ) {
        driverService.removeDriver(driverModel);
    }

}
