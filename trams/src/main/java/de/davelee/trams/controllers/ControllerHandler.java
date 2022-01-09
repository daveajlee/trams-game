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

    /**
     * Set the file controller object via Spring.
     * @param fileController a <code>FileController</code> object.
     */
    @Autowired
    public void setFileController(final FileController fileController) {
        this.fileController = fileController;
    }

    /**
     * Set the company controller object via Spring.
     * @param companyController a <code>CompanyController</code> object.
     */
    @Autowired
    public void setCompanyController(final CompanyController companyController) {
        this.companyController = companyController;
    }

    /**
     * Set the scenario controller object via Spring.
     * @param scenarioController a <code>ScenarioController</code> object.
     */
    @Autowired
    public void setScenarioController(final ScenarioController scenarioController) {
        this.scenarioController = scenarioController;
    }

    /**
     * Set the vehicle controller object via Spring.
     * @param vehicleController a <code>VehicleController</code> object.
     */
    @Autowired
    public void setVehicleController(final VehicleController vehicleController) {
        this.vehicleController = vehicleController;
    }

    /**
     * Set the message controller object via Spring.
     * @param messageController a <code>MessageController</code> object.
     */
    @Autowired
    public void setMessageController(final MessageController messageController) {
        this.messageController = messageController;
    }

    /**
     * Set the route controller object via Spring.
     * @param routeController a <code>RouteController</code> object.
     */
    @Autowired
    public void setRouteController(final RouteController routeController) {
        this.routeController = routeController;
    }

    /**
     * Set the driver controller object via Spring.
     * @param driverController a <code>DriverController</code> object.
     */
    @Autowired
    public void setDriverController(final DriverController driverController) {
        this.driverController = driverController;
    }

    /**
     * Set the tip controller object via Spring.
     * @param tipController a <code>TipController</code> object.
     */
    @Autowired
    public void setTipController(final TipController tipController) {
        this.tipController = tipController;
    }

    /**
     * Set the stop time object via Spring.
     * @param stopTimeController a <code>StopTimeController</code> object.
     */
    @Autowired
    public void setStopTimeController(final StopTimeController stopTimeController) {
        this.stopTimeController = stopTimeController;
    }

    /**
     * Set the simulation object via Spring.
     * @param simulationController a <code>SimulationController</code> object.
     */
    @Autowired
    public void setSimulationController(final SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    /**
     * Set the stop object via Spring.
     * @param stopController a <code>StopController</code> object.
     */
    @Autowired
    public void setStopController(final StopController stopController) {
        this.stopController = stopController;
    }
}
