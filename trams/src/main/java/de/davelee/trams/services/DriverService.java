package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.dao.DriverDao;
import de.davelee.trams.data.Driver;

public class DriverService {

	private DriverDao driverDao;

	public DriverDao getDriverDao() {
		return driverDao;
	}

	public void setDriverDao(DriverDao driverDao) {
		this.driverDao = driverDao;
	}

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
    	return driverDao.getDriverById(id);
    }
    
    public List<Driver> getAllDrivers ( ) {
    	return driverDao.getAllDrivers();
    }
    
    public void saveDriver ( final Driver driver ) {
    	driverDao.createAndStoreDriver(driver);
    }

    public void removeDriver ( final Driver driver ) {
    	driverDao.removeDriver(driver);
    }
    
}
