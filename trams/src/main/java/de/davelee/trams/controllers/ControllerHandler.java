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

    private FileController fileController;

    private CompanyController companyController;

    private ScenarioController scenarioController;

    private VehicleController vehicleController;

    private MessageController messageController;

    private RouteController routeController;

    private DriverController driverController;

    private TipController tipController;

    private StopTimeController stopTimeController;

    private SimulationController simulationController;

    private StopController stopController;

    @Value("${trams.version}")
    private String version;

    @Value("${simulation.speed}")
    private int simulationSpeed;

    @Autowired
    public void setFileController(final FileController fileController) {
        this.fileController = fileController;
    }

    @Autowired
    public void setCompanyController(final CompanyController companyController) {
        this.companyController = companyController;
    }

    @Autowired
    public void setScenarioController(final ScenarioController scenarioController) {
        this.scenarioController = scenarioController;
    }

    @Autowired
    public void setVehicleController(final VehicleController vehicleController) {
        this.vehicleController = vehicleController;
    }

    @Autowired
    public void setMessageController(final MessageController messageController) {
        this.messageController = messageController;
    }

    @Autowired
    public void setRouteController(final RouteController routeController) {
        this.routeController = routeController;
    }

    @Autowired
    public void setDriverController(final DriverController driverController) {
        this.driverController = driverController;
    }

    @Autowired
    public void setTipController(final TipController tipController) {
        this.tipController = tipController;
    }

    @Autowired
    public void setStopTimeController(final StopTimeController stopTimeController) {
        this.stopTimeController = stopTimeController;
    }

    @Autowired
    public void setSimulationController(final SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    @Autowired
    public void setStopController(final StopController stopController) {
        this.stopController = stopController;
    }
}
