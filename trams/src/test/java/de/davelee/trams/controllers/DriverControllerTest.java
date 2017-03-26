package de.davelee.trams.controllers;

import de.davelee.trams.model.DriverModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by davelee on 20.09.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class DriverControllerTest {

    @Autowired
    private DriverController driverController;

    @Autowired
    private GameController gameController;

    @Test
    public void testDrivers() {
        gameController.createGameModel("John Smith", "Landuff");
        assertFalse(driverController.hasSomeDriversBeenEmployed());
        DriverModel driverModel = new DriverModel();
        driverModel.setContractedHours(20);
        driverModel.setName("Max Mustermann");
        driverModel.setStartDate(Calendar.getInstance());
        driverController.employDriver(driverModel);
        assertFalse(driverController.hasSomeDriversBeenEmployed());
        DriverModel driverModel2 = new DriverModel();
        driverModel2.setContractedHours(20);
        driverModel2.setName("Micha Mustermann");
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2013,4,20);
        driverModel2.setStartDate(currentDate);
        driverController.employDriver(driverModel2);
        assertTrue(driverController.hasSomeDriversBeenEmployed());
    }

}
