package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
@Disabled
public class DriverControllerTest {

    @InjectMocks
    private DriverController driverController;

    @Mock
    private GameController gameController;

    @Test
    public void testDrivers() {
        Mockito.doNothing().when(gameController).withdrawBalance(any(), any());
        assertFalse(driverController.hasSomeDriversBeenEmployed("Mustermann GmbH", "Dave Lee"));
        driverController.employDriver("Max Mustermann", "Mustermann GmbH", "01-01-2020", "Dave Lee");
        assertTrue(driverController.hasSomeDriversBeenEmployed("Mustermann GmbH", "Dave Lee"));
        LocalDate startDate = LocalDate.of(2013,4,20);
        driverController.employDriver("Micha Mustermann", "Mustermann GmbH", "20-04-2013", "Dave Lee");
        assertTrue(driverController.hasSomeDriversBeenEmployed("Mustermann GmbH", "Dave Lee"));
    }

}
