package de.davelee.trams.services;

import java.time.LocalDate;
import java.util.List;

import de.davelee.trams.data.Driver;
import de.davelee.trams.model.DriverModel;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

	/**
     * Check if the driver has started work or not.
	 * @param startDate a <code>LocalDate</code> object with the start date.
     * @param currentDate a <code>LocalDate</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork (final LocalDate startDate, final LocalDate currentDate ) {
    	return currentDate.isAfter(startDate) || currentDate.isEqual(startDate);
    }

	private Driver convertToDriver ( final DriverModel driverModel ) {
    	Driver driver = new Driver();
		driver.setName(driverModel.getName());
		driver.setContractedHours(driverModel.getContractedHours());
		driver.setStartDate(driverModel.getStartDate());
    	return driver;
    }

	private DriverModel convertToDriverModel ( final Driver driver ) {
		return DriverModel.builder()
				.name(driver.getName())
				.contractedHours(driver.getContractedHours())
				.startDate(driver.getStartDate())
				.build();
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
