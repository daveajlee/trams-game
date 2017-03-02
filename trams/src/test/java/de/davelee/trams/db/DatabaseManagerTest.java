package de.davelee.trams.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Driver;
import de.davelee.trams.data.Journey;
import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.data.Route;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.data.Scenario;
import de.davelee.trams.data.Stop;
import de.davelee.trams.data.Timetable;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.services.DriverService;
import de.davelee.trams.services.FactoryService;
import de.davelee.trams.services.JourneyPatternService;
import de.davelee.trams.services.JourneyService;
import de.davelee.trams.services.RouteScheduleService;
import de.davelee.trams.services.RouteService;
import de.davelee.trams.services.ScenarioService;
import de.davelee.trams.services.StopService;
import de.davelee.trams.services.TimetableService;
import de.davelee.trams.services.VehicleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class DatabaseManagerTest {

	@Autowired
	private DatabaseManager databaseManager;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private RouteScheduleService routeScheduleService;
	
	@Autowired
	private JourneyService journeyService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private StopService stopService;
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private FactoryService factoryService;
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Test
	public void driverTest() {
		Driver driver = driverService.createDriver("Dave Lee", 40, Calendar.getInstance());
		databaseManager.createAndStoreDriver(driver);
		assertEquals(driver.getId(), 1);
		Driver driver2 = databaseManager.getDriverById(1);
		assertNotNull(driver2);
		assertEquals(driver2.getName(), "Dave Lee");
		assertEquals(driver2.getContractedHours(), 40);
		assertEquals(databaseManager.getAllDrivers().size(), 1);
		//Test remove
		databaseManager.removeDriver(driver2);
		assertNull(databaseManager.getAllDrivers());
	}
	
	@Test
	public void routeTest() {
		String[] outwardStopNames = new String[] { "Rathaus Pankow", "Pankow Kirche", "S+U Pankow" };
		Route route = routeService.createRoute("155", outwardStopNames);
		databaseManager.createAndStoreRoute(route);
		assertEquals(route.getId(), 1);
		Route route2 = databaseManager.getRouteById(1);
		assertNotNull(route2);
		assertEquals(route2.getRouteNumber(), "155");
		assertEquals(route2.getStops().size(), 3);
	}
	
	@Test
	public void routeScheduleTest() {
		HashMap<String, Calendar> stops = new HashMap<String, Calendar>();
		stops.put("Rathaus Pankow", Calendar.getInstance());
		stops.put("Pankow Kirche", Calendar.getInstance());
		stops.put("S+U Pankow", Calendar.getInstance());
		List<Journey> journeyList = new ArrayList<Journey>();
        journeyList.add(journeyService.createJourney(stops));
        RouteSchedule schedule = routeScheduleService.createRouteSchedule("2A", 1, journeyList, 5);
        databaseManager.createAndStoreRouteSchedule(schedule);
        assertEquals(schedule.getId(), 1);
        RouteSchedule schedule2 = databaseManager.getRouteScheduleById(1);
        assertNotNull(schedule2);
        assertEquals(schedule2.getDelayInMins(), 5);
        assertEquals(schedule2.getRouteNumber(), "2A");
        assertEquals(schedule2.getScheduleNumber(), 1);
        assertEquals(schedule2.getJourneyList().size(), 1);
	}
	
	@Test
	public void vehicleTest() {
		Vehicle vehicle = vehicleService.createVehicle("CV58 2XD", Calendar.getInstance(), 0.006, 
				"image.png", "Mercedes", -1, 40, 60, 200.99);
		databaseManager.createAndStoreVehicle(vehicle);
		assertEquals(vehicle.getId(), 1);
		Vehicle vehicle2 = databaseManager.getVehicleById(1);
		assertNotNull(vehicle2);
		assertEquals(vehicle2.getRegistrationNumber(), "CV58 2XD");
		assertEquals(vehicle2.getDepreciationFactor(), 0.006, 0.01);
		assertEquals(vehicle2.getImagePath(), "image.png");
		assertEquals(vehicle2.getModel(), "Mercedes");
		assertEquals(vehicle2.getSeatingCapacity(), 40);
		assertEquals(vehicle2.getStandingCapacity(), 60);
		assertEquals(vehicle2.getPurchasePrice(), 200.99, 0.01);
		List<Vehicle> vehicles = databaseManager.getAllVehicles();
		assertEquals(vehicles.size(), 1);
		//Test remove
		databaseManager.removeVehicle(vehicle2);
		assertNull(databaseManager.getAllVehicles());
	}
	
	@Test
	public void stopTest() {
		Stop stop = stopService.createStop("Rathaus Pankow", Calendar.getInstance());
		databaseManager.createAndStoreStop(stop);
		assertEquals(stop.getId(), 7);
		Stop stop2 = databaseManager.getStopById(7);
		assertNotNull(stop2);
		assertEquals(stop2.getStopName(), "Rathaus Pankow");
	}
	
	@Test
	public void journeyTest() {
		HashMap<String, Calendar> stops = new HashMap<String, Calendar>();
		stops.put("Rathaus Pankow", Calendar.getInstance());
		stops.put("Pankow Kirche", Calendar.getInstance());
		stops.put("S+U Pankow", Calendar.getInstance());
        Journey journey = journeyService.createJourney(stops);
		databaseManager.createAndStoreJourney(journey);
		assertEquals(journey.getId(), 2);
		Journey journey2 = databaseManager.getJourneyById(2);
		assertNotNull(journey2);
		assertEquals(journey2.getJourneyStops().size(), 3);
	}
	
	@Test
	public void journeyPatternTest() {
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		JourneyPattern journeyPattern = journeyPatternService.createJourneyPattern("Mon-Fri", daysOfOperation, "Rathaus Pankow", "S+U Pankow", Calendar.getInstance(), 
				Calendar.getInstance(), 15, 3);
		databaseManager.createAndStoreJourneyPattern(journeyPattern);
		assertEquals(journeyPattern.getId(), 1);
		JourneyPattern journeyPattern2 = databaseManager.getJourneyPatternById(1);
		assertNotNull(journeyPattern2);
		assertEquals(journeyPattern2.getName(), "Mon-Fri");
		assertEquals(journeyPattern2.getDaysOfOperation().size(), 5);
		assertEquals(journeyPattern2.getOutgoingTerminus(), "Rathaus Pankow");
		assertEquals(journeyPattern2.getReturnTerminus(), "S+U Pankow");
		assertEquals(journeyPattern2.getRouteDuration(), 3);
		assertEquals(journeyPattern2.getFrequency(), 15);
	}
	
	@Test
	public void timetableTest() {
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		Timetable timetable = timetableService.createTimetable("myTimetable", 
				Calendar.getInstance(), Calendar.getInstance(), journeyPatternService.createJourneyPattern("Mon-Fri", 
						daysOfOperation, "Rathaus Pankow", "S+U Pankow", Calendar.getInstance(), 
        				Calendar.getInstance(), 15, 3));
		databaseManager.createAndStoreTimetable(timetable);
		assertEquals(timetable.getId(), 1);
		Timetable timetable2 = databaseManager.getTimetableById(1);
		assertNotNull(timetable2);
		assertEquals(timetable2.getName(), "myTimetable");
	}
	
	@Test
	public void scenarioTest() {
		Scenario scenario = factoryService.createScenarioObject("Landuff Transport Company");
		databaseManager.createAndStoreScenario(scenario);
		assertEquals(scenario.getId(), 1);
		Scenario scenario2 = databaseManager.getScenarioById(1);
		assertNotNull(scenario2);
		assertEquals(scenario2.getScenarioName(), "Landuff Transport Company");
		assertEquals(scenario2.getLocationMapFileName(), "landuffmappic.jpg");
	}
	
}
