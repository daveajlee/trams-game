package de.davelee.trams.controllers;

import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import de.davelee.trams.data.Driver;

import de.davelee.trams.services.DriverService;
import org.springframework.stereotype.Controller;

@Controller
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
        gameController.withdrawBalance(0, gameController.getCurrentPlayerName());
        driverService.saveDriver(driverModel);
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
            if ( driverService.hasStartedWork(driverModels[i].getStartDate(), gameModel.getCurrentTime()) ) { return true; }
        }
        return false;
    }

}
