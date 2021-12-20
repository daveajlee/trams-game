package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 * This class exists to store the controllers and supply them to the GUI.
 * It acts as a bridge between Spring Framework and Swing GUI to allow controllers to only be initialised once but
 * GUI classes can be more than once initialised.
 * Created by davelee on 27.03.17.
 */
@Controller
public class ControllerHandler {

    @Autowired
    private FileController fileController;

    @Autowired
    private GameController gameController;

    @Autowired
    private ScenarioController scenarioController;

    @Autowired
    private VehicleController vehicleController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private StopController journeyController;

    @Autowired
    private RouteController routeController;

    @Autowired
    private DriverController driverController;

    @Autowired
    private TipController tipController;

    @Autowired
    private StopTimeController stopTimeController;

    @Value("${trams.version}")
    private String version;

    public FileController getFileController() {
        return fileController;
    }

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public ScenarioController getScenarioController() {
        return scenarioController;
    }

    public VehicleController getVehicleController() {
        return vehicleController;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public StopController getJourneyController() {
        return journeyController;
    }

    public RouteController getRouteController() {
        return routeController;
    }

    public DriverController getDriverController() {
        return driverController;
    }

    public void setDriverController(DriverController driverController) {
        this.driverController = driverController;
    }

    public TipController getTipController() {
        return tipController;
    }

    public void setTipController(TipController tipController) {
        this.tipController = tipController;
    }

    public StopTimeController getStopTimeController() {
        return stopTimeController;
    }

    public void setStopTimeController(StopTimeController stopTimeController) {
        this.stopTimeController = stopTimeController;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
