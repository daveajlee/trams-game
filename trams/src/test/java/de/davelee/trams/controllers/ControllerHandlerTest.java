package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class ControllerHandlerTest {

    @Autowired
    private ControllerHandler controllerHandler;

    @Mock
    private FileController fileController;

    @Test
    public void testSetters() {
        assertNotNull(controllerHandler.getFileController());
        assertNotNull(controllerHandler.getCompanyController());
        assertNotNull(controllerHandler.getScenarioController());
        assertNotNull(controllerHandler.getVehicleController());
        assertNotNull(controllerHandler.getMessageController());
        assertNotNull(controllerHandler.getRouteController());
        assertNotNull(controllerHandler.getDriverController());
        assertNotNull(controllerHandler.getTipController());
        assertNotNull(controllerHandler.getStopController());
        assertNotNull(controllerHandler.getStopTimeController());
        assertNotNull(controllerHandler.getSimulationController());
        assertNotNull(controllerHandler.getSimulationSpeed());
        assertNotNull(controllerHandler.getVersion());
    }

}
