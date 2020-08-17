package de.davelee.trams.beans;

import javax.xml.bind.annotation.XmlRootElement;

import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.MessageModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.model.VehicleModel;

@XmlRootElement
public class TramsFile {

    private DriverModel[] driverModels;
    private GameModel[] gameModel;
    private JourneyModel[] journeyModels;
    private JourneyPatternModel[] journeyPatternModels;
    private MessageModel[] messageModels;
    private RouteModel[] routeModels;
    private RouteScheduleModel[] routeScheduleModels;
	private String[] stops;
    private TimetableModel[] timetableModels;
    private VehicleModel[] vehicleModels;

    public TramsFile ( ) { }

    public TramsFile ( final DriverModel[] driverModels, final GameModel[] gameModel, final JourneyModel[] journeyModels, final JourneyPatternModel[] journeyPatternModels,
                       final MessageModel[] messageModels, final RouteModel[] routeModels, final RouteScheduleModel[] routeScheduleModels,
                       final String[] stops, final TimetableModel[] timetableModels, final VehicleModel[] vehicleModels) {
                this.driverModels = driverModels;
                this.gameModel = gameModel;
                this.journeyModels = journeyModels;
                this.journeyPatternModels = journeyPatternModels;
                this.messageModels = messageModels;
                this.routeModels = routeModels;
                this.routeScheduleModels = routeScheduleModels;
                this.stops = stops;
                this.timetableModels = timetableModels;
                this.vehicleModels = vehicleModels;
    }

    public DriverModel[] getDriverModels() {
        return driverModels;
    }

    public void setDriverModels(final DriverModel[] driverModels) {
        this.driverModels = driverModels;
    }

    public GameModel[] getGameModel() {
        return gameModel;
    }

    public void setGameModel(final GameModel[] gameModel) {
       this.gameModel = gameModel;
    }

    public JourneyModel[] getJourneyModels() {
        return journeyModels;
    }

    public void setJourneyModels(final JourneyModel[] journeyModels) {
        this.journeyModels = journeyModels;
    }

    public JourneyPatternModel[] getJourneyPatternModels() {
        return journeyPatternModels;
    }

    public void setJourneyPatternModels(final JourneyPatternModel[] journeyPatternModels) {
        this.journeyPatternModels = journeyPatternModels;
    }

    public MessageModel[] getMessageModels() {
        return messageModels;
    }

    public void setMessageModels(final MessageModel[] messageModels) {
        this.messageModels = messageModels;
    }

    public RouteModel[] getRouteModels() {
        return routeModels;
    }

    public void setRouteModels(final RouteModel[] routeModels) {
        this.routeModels = routeModels;
    }

    public RouteScheduleModel[] getRouteScheduleModels() {
        return routeScheduleModels;
    }

    public void setRouteScheduleModels(final RouteScheduleModel[] routeScheduleModels) {
        this.routeScheduleModels = routeScheduleModels;
    }

    public String[] getStops() {
        return stops;
    }

    public void setStops(final String[] stops) {
        this.stops = stops;
    }

    public TimetableModel[] getTimetableModels() {
        return timetableModels;
    }

    public void setTimetableModels(final TimetableModel[] timetableModels) {
        this.timetableModels = timetableModels;
    }

    public VehicleModel[] getVehicleModels() {
        return vehicleModels;
    }

    public void setVehicleModels(final VehicleModel[] vehicleModels) {
        this.vehicleModels = vehicleModels;
    }
}