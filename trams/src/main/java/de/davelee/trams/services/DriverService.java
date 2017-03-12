package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Driver;
import de.davelee.trams.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DriverService {

	@Autowired
	private DriverRepository driverRepository;

	/**
     * Check if the driver has started work or not.
     * @param currentDate a <code>Calendar</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork ( Calendar startDate, Calendar currentDate ) {
    	if ( currentDate.after(startDate) || currentDate.equals(startDate) ) {
    		return true;
    	}
    	return false;
    }
    
    public Driver createDriver ( String name, int hours, Calendar startDate ) {
    	Driver driver = new Driver();
    	driver.setName(name);
    	driver.setContractedHours(hours);
    	driver.setStartDate(startDate);
    	return driver;
    }

    public Driver getDriverById(long id) {
    	return driverRepository.findOne(id);
    }
    
    public List<Driver> getAllDrivers ( ) {
		return driverRepository.findAll();
    }
    
    public void saveDriver ( final Driver driver ) {
		driverRepository.saveAndFlush(driver);
    }

    public void removeDriver ( final Driver driver ) {
		driverRepository.delete(driver);
    }
    
}
