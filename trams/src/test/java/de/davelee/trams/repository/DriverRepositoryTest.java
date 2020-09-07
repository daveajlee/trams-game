package de.davelee.trams.repository;

import java.time.LocalDate;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.Driver;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class DriverRepositoryTest {
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Test
	public void driverTest() {
		driverRepository.deleteAll();
		Driver driver = new Driver();
		driver.setName("Dave Lee");
		driver.setContractedHours(40);
		driver.setStartDate(LocalDate.now());
		driverRepository.saveAndFlush(driver);
		Driver driver2 = driverRepository.findByName("Dave Lee");
		Assertions.assertNotNull(driver2);
		assertEquals(driver2.getName(), "Dave Lee");
		Assertions.assertEquals(driver2.getContractedHours(), 40);
		Assertions.assertEquals(driverRepository.findAll().size(), 1);
		//Test remove
		driverRepository.delete(driver2);
		Assertions.assertEquals(driverRepository.findAll().size(), 0);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
