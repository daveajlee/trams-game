package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class StopControllerTest {

	@Autowired
	private StopController stopController;

	@Test
	public void testStopDistances() {
		assertEquals(stopController.getDistance("Landuff Transport Company", "Airport", "Cargo Terminal"), 12);
	}

	@Test
	public void testStopDistanceNull1() {
		assertThrows(IndexOutOfBoundsException.class, () -> stopController.getDistance("Landuff Transport Company", "Strasse 201", "Cargo Terminal"));
	}

	@Test
	public void testStopDistanceNull2() {
		assertThrows(IndexOutOfBoundsException.class, () -> stopController.getDistance("Landuff Transport Company", "Airport", "Strasse 201"));
	}
	
}
