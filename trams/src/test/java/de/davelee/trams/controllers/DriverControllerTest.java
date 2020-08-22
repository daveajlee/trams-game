package de.davelee.trams.controllers;

import de.davelee.trams.model.DriverModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;

/**
 * Created by davelee on 20.09.16.
 */
@ExtendWith(SpringExtension.class)
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
        driverController.employDriver(DriverModel.builder()
                .name("Max Mustermann")
                .contractedHours(20)
                .startDate(Calendar.getInstance())
                .build());
        assertFalse(driverController.hasSomeDriversBeenEmployed());
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2013,4,20);
        driverController.employDriver(DriverModel.builder()
                .name("Micha Mustermann")
                .contractedHours(20)
                .startDate(currentDate).build());
        assertTrue(driverController.hasSomeDriversBeenEmployed());
    }

    private void assertFalse ( final boolean condition ) {
        Assertions.assertFalse(condition);
    }

    private void assertTrue ( final boolean condition ) {
        Assertions.assertTrue(condition);
    }

}
