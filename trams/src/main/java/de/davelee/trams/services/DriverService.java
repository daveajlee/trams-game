package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Driver;
import de.davelee.trams.model.DriverModel;
import de.davelee.trams.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

	@Autowired
	private DriverRepository driverRepository;

	/**
     * Check if the driver has started work or not.
	 * @param startDate a <code>Calendar</code> object with the start date.
     * @param currentDate a <code>Calendar</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork ( Calendar startDate, Calendar currentDate ) {
    	if ( currentDate.after(startDate) || currentDate.equals(startDate) ) {
    		return true;
    	}
    	return false;
    }

	private Driver convertToDriver ( final DriverModel driverModel ) {
    	Driver driver = new Driver();
		driver.setName(driverModel.getName());
		driver.setContractedHours(driverModel.getContractedHours());
		driver.setStartDate(driverModel.getStartDate());
    	return driver;
    }

	private DriverModel convertToDriverModel ( final Driver driver ) {
		DriverModel driverModel = new DriverModel();
		driverModel.setName(driver.getName());
		driverModel.setContractedHours(driver.getContractedHours());
		driverModel.setStartDate(driver.getStartDate());
		return driverModel;
	}

	public DriverModel getDriverByName(final String name) {
		Driver driver = driverRepository.findByName(name);
		if ( driver != null ) {
			return convertToDriverModel(driver);
		}
		return null;
	}

	public DriverModel[] getAllDrivers ( ) {
		List<Driver> drivers = driverRepository.findAll();
		DriverModel[] driverModels = new DriverModel[drivers.size()];
		for ( int i = 0; i < driverModels.length; i++ ) {
			driverModels[i] = convertToDriverModel(drivers.get(i));
		}
		return driverModels;
	}

	public void saveDriver ( final DriverModel driverModel ) {
		driverRepository.saveAndFlush(convertToDriver(driverModel));
	}

	public void removeDriver ( final DriverModel driverModel ) {
		driverRepository.delete(driverRepository.findByName(driverModel.getName()));
	}

	/**
	 * Delete all stored drivers (primarily used for loading a game)
	 */
	public void removeAllDrivers ( ) {
    	driverRepository.deleteAll();
	}
    
}
