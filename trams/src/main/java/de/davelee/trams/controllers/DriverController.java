package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import de.davelee.trams.data.Driver;

import de.davelee.trams.services.DriverService;

public class DriverController {
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private FileController fileController;
	
	public List<Driver> getAllDrivers () {
		return driverService.getAllDrivers();
	}
	
	/**
     * Employ a new driver.
     * @param name a <code>String</code> with the driver's name.
     * @param hours a <code>int</code> with the contracted hours.
     * @param startDate a <code>Calendar</code> with the start date.
     * @return a <code>boolean</code> which is true iff the driver has been successfully employed.
     */
    public void employDriver ( String name, int hours, Calendar startDate ) {
    	//TODO: Employing drivers should cost money.
    	gameController.withdrawBalance(0);
    	driverService.saveDriver(driverService.createDriver(name, hours, startDate));
    }
    
    public int getNumberDrivers ( ) {
        return driverService.getAllDrivers().size();
    }

    /**
     * This method checks if any employees have started working for the company!
     * @return a <code>boolean</code> which is true iff some drivers have started working.
     */
    public boolean hasSomeDriversBeenEmployed ( ) {
        if ( getNumberDrivers() == 0 ) { return false; }
        for ( int i = 0; i < getNumberDrivers(); i++ ) {
        	if ( driverService.hasStartedWork(driverService.getDriverById(getDriver(i)).getStartDate(), gameController.getCurrentSimTime()) ) { return true; }
        }
        return false;
    }
    
    /**
     * Get a driver based on its position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Driver</code> object.
     */
    public long getDriver ( int pos ) {
        return driverService.getAllDrivers().get(pos).getId();
    }

}
