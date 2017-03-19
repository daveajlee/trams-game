package de.davelee.trams.db;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import de.davelee.trams.data.*;
import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.model.VehicleModel;

@XmlRootElement
public class TramsFile {

    private DriverModel[] driverModels;
    private Game game;
    private List<Journey> journeys;
    private List<JourneyPattern> journeyPatterns;
	private List<Message> messages;
    private RouteModel[] routeModels;
	private List<RouteSchedule> routeSchedules;
    private List<Stop> stops;
    private List<StopTime> stopTimes;
    private TimetableModel[] timetableModels;
    private VehicleModel[] vehicleModels;

    public TramsFile ( ) { }

    public TramsFile ( DriverModel[] driverModels, Game game, List<Journey> journeys, List<JourneyPattern> journeyPatterns,
			List<Message> messages, RouteModel[] routeModels, List<RouteSchedule> routeSchedules,
			List<Stop> stops, List<StopTime> stopTimes, TimetableModel[] timetableModels, VehicleModel[] vehicleModels) {
                this.driverModels = driverModels;
                /*this.game = game;*/
				this.journeys = journeys;
                this.journeyPatterns = journeyPatterns;
                this.messages = messages;
                this.routeModels = routeModels;
                this.routeSchedules = routeSchedules;
                this.stops = stops;
                this.stopTimes = stopTimes;
                this.timetableModels = timetableModels;
                this.vehicleModels = vehicleModels;
    }

    public DriverModel[] getDrivers() {
        return driverModels;
    }

    public void setDrivers(DriverModel[] driverModels) {
        this.driverModels = driverModels;
    }

    public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<Journey> getJourneys() {
		return journeys;
	}

	public void setJourneys(List<Journey> journeys) {
		this.journeys = journeys;
	}

    public List<JourneyPattern> getJourneyPatterns() {
        return journeyPatterns;
    }

    public void setJourneyPatterns(List<JourneyPattern> journeyPatterns) {
        this.journeyPatterns = journeyPatterns;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public RouteModel[] getRoutes() {
        return routeModels;
    }

    public void setRoutes(RouteModel[] routeModels) {
        this.routeModels = routeModels;
    }

    public List<RouteSchedule> getRouteSchedules() {
		return routeSchedules;
	}

	public void setRouteSchedules(List<RouteSchedule> routeSchedules) {
		this.routeSchedules = routeSchedules;
	}

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<StopTime> getStopTimes() {
        return stopTimes;
    }

	public void setStopTimes(List<StopTime> stopTimes) {
        this.stopTimes = stopTimes;
    }

    public TimetableModel[] getTimetableModels() {
        return timetableModels;
    }

    public void setTimetableModels(TimetableModel[] timetableModels) {
        this.timetableModels = timetableModels;
    }

    public VehicleModel[] getVehicleModels() {
        return vehicleModels;
    }

    public void setVehicleModels(VehicleModel[] vehicleModels) {
        this.vehicleModels = vehicleModels;
    }
}