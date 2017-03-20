package de.davelee.trams.db;

import javax.xml.bind.annotation.XmlRootElement;

import de.davelee.trams.data.*;
import de.davelee.trams.model.*;

@XmlRootElement
public class TramsFile {

    private DriverModel[] driverModels;
    private Game game;
    private JourneyModel[] journeyModels;
    private JourneyPatternModel[] journeyPatternModels;
    private MessageModel[] messageModels;
    private RouteModel[] routeModels;
    private RouteScheduleModel[] routeScheduleModels;
	private String[] stops;
	private StopTimeModel[] stopTimeModels;
    private TimetableModel[] timetableModels;
    private VehicleModel[] vehicleModels;

    public TramsFile ( ) { }

    public TramsFile ( final DriverModel[] driverModels, final Game game, final JourneyModel[] journeyModels, final JourneyPatternModel[] journeyPatternModels,
                       final MessageModel[] messageModels, final RouteModel[] routeModels, final RouteScheduleModel[] routeScheduleModels,
                       final String[] stops, final StopTimeModel[] stopTimeModels, final TimetableModel[] timetableModels, final VehicleModel[] vehicleModels) {
                this.driverModels = driverModels;
                /*this.game = game;*/
                this.journeyModels = journeyModels;
                this.journeyPatternModels = journeyPatternModels;
                this.messageModels = messageModels;
                this.routeModels = routeModels;
                this.routeScheduleModels = routeScheduleModels;
                this.stops = stops;
                this.stopTimeModels = stopTimeModels;
                this.timetableModels = timetableModels;
                this.vehicleModels = vehicleModels;
    }

    public DriverModel[] getDrivers() {
        return driverModels;
    }

    public void setDrivers(final DriverModel[] driverModels) {
        this.driverModels = driverModels;
    }

    public Game getGame() {
		return game;
	}

	public void setGame(final Game game) {
		this.game = game;
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

    public RouteModel[] getRoutes() {
        return routeModels;
    }

    public void setRoutes(final RouteModel[] routeModels) {
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

    public StopTimeModel[] getStopTimeModels() {
        return stopTimeModels;
    }

    public void setStopTimeModels(final StopTimeModel[] stopTimeModels) {
        this.stopTimeModels = stopTimeModels;
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