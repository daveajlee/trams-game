package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class exists to store the controllers and supply them to the GUI.
 * It acts as a bridge between Spring Framework and Swing GUI to allow controllers to only be initialised once but
 * GUI classes can be more than once initialised.
 * Created by davelee on 27.03.17.
 */
public class ControllerHandler {

    @Autowired
    private GameController gameController;

    @Autowired
    private ScenarioController scenarioController;

    @Autowired
    private VehicleController vehicleController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private JourneyController journeyController;

    @Autowired
    private RouteController routeController;

    @Autowired
    private RouteScheduleController routeScheduleController;

    private String version;

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

    public JourneyController getJourneyController() {
        return journeyController;
    }

    public RouteController getRouteController() {
        return routeController;
    }

    public RouteScheduleController getRouteScheduleController() {
        return routeScheduleController;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
