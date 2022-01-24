package de.davelee.trams.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 * This class exists to store the controllers and supply them to the GUI.
 * It acts as a bridge between Spring Framework and Swing GUI to allow controllers to only be initialised once but
 * GUI classes can be more than once initialised.
 * @author Dave Lee
 */
@Controller
@Getter
@Setter
public class ControllerHandler {

    @Autowired
    private FileController fileController;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private ScenarioController scenarioController;

    @Autowired
    private VehicleController vehicleController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private RouteController routeController;

    @Autowired
    private DriverController driverController;

    @Autowired
    private TipController tipController;

    @Autowired
    private StopTimeController stopTimeController;

    @Autowired
    private SimulationController simulationController;

    @Autowired
    private StopController stopController;

    @Value("${trams.version}")
    private String version;

    @Value("${simulation.speed}")
    private int simulationSpeed;

}
