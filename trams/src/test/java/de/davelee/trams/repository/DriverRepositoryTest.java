package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Driver;
import de.davelee.trams.repository.DriverRepository;
import de.davelee.trams.services.DriverService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class DriverRepositoryTest {
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Test
	public void driverTest() {
		Driver driver = new Driver();
		driver.setName("Dave Lee");
		driver.setContractedHours(40);
		driver.setStartDate(Calendar.getInstance());
		driverRepository.saveAndFlush(driver);
		Driver driver2 = driverRepository.findByName("Dave Lee");
		assertNotNull(driver2);
		assertEquals(driver2.getName(), "Dave Lee");
		assertEquals(driver2.getContractedHours(), 40);
		assertEquals(driverRepository.findAll().size(), 3);
		//Test remove
		driverRepository.delete(driver2);
		assertEquals(driverRepository.findAll().size(), 2);
	}

}
