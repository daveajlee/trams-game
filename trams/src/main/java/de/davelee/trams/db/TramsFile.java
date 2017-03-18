package de.davelee.trams.db;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import de.davelee.trams.data.*;
import de.davelee.trams.model.DriverModel;

@XmlRootElement
public class TramsFile {

    private DriverModel[] driverModels;
    private Game game;
    private List<Journey> journeys;
    private List<JourneyPattern> journeyPatterns;
	private List<Message> messages;
	private List<Route> routes;
	private List<RouteSchedule> routeSchedules;
    private List<Stop> stops;
    private List<StopTime> stopTimes;
	private List<Timetable> timetables;
	private List<Vehicle> vehicles;

    public TramsFile ( ) { }

    public TramsFile ( DriverModel[] driverModels, Game game, List<Journey> journeys, List<JourneyPattern> journeyPatterns,
			List<Message> messages, List<Route> routes, List<RouteSchedule> routeSchedules,
			List<Stop> stops, List<StopTime> stopTimes, List<Timetable> timetables, List<Vehicle> vehicles) {
                this.driverModels = driverModels;
                /*this.game = game;*/
				this.journeys = journeys;
                this.journeyPatterns = journeyPatterns;
                this.messages = messages;
                this.routes = routes;
                this.routeSchedules = routeSchedules;
                this.stops = stops;
                this.stopTimes = stopTimes;
                this.timetables = timetables;
                this.vehicles = vehicles;
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

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
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

    public List<Timetable> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<Timetable> timetables) {
        this.timetables = timetables;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}