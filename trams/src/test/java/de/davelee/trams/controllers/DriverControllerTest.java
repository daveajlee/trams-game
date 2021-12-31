package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.GameModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
@Disabled
public class DriverControllerTest {

    @Autowired
    private DriverController driverController;

    @Test
    public void testDrivers() {
        GameController gameControllerMock = mock(GameController.class);
        when(gameControllerMock.getGameModel()).thenReturn(GameModel.builder().currentDateTime(LocalDateTime.now()).build());
        driverController.setGameController(gameControllerMock);
        assertFalse(driverController.hasSomeDriversBeenEmployed("Mustermann GmbH"));
        driverController.employDriver("Max Mustermann", "Mustermann GmbH", LocalDate.now());
        assertTrue(driverController.hasSomeDriversBeenEmployed("Mustermann GmbH"));
        LocalDate startDate = LocalDate.of(2013,4,20);
        driverController.employDriver("Micha Mustermann", "Mustermann GmbH", startDate);
        assertTrue(driverController.hasSomeDriversBeenEmployed("Mustermann GmbH"));
    }

}
