package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.beans.TramsFile;
import de.davelee.trams.factory.VehicleFactory;
import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.MessageModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.StopTimeModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.repository.GameRepository;
import de.davelee.trams.repository.JourneyRepository;
import de.davelee.trams.repository.MessageRepository;
import de.davelee.trams.repository.RouteRepository;
import de.davelee.trams.repository.RouteScheduleRepository;
import de.davelee.trams.repository.StopRepository;
import de.davelee.trams.repository.StopTimeRepository;
import de.davelee.trams.repository.TimetableRepository;
import de.davelee.trams.repository.VehicleRepository;
import de.davelee.trams.services.DriverService;
import de.davelee.trams.services.GameService;
import de.davelee.trams.services.JourneyPatternService;
import de.davelee.trams.services.JourneyService;
import de.davelee.trams.services.MessageService;
import de.davelee.trams.services.RouteService;
import de.davelee.trams.services.RouteScheduleService;
import de.davelee.trams.services.TimetableService;
import de.davelee.trams.util.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class FileServiceTest {
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private StopRepository stopRepository;
	
	@Autowired
	private TimetableRepository timetableRepository;
	
	@Autowired
	private RouteScheduleRepository routeScheduleRepository;
	
	@Autowired
	private JourneyRepository journeyRepository;
	
	@Autowired
	private StopTimeRepository stopTimeRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private VehicleFactory vehicleFactory;
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private JourneyService journeyService;
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private RouteScheduleService routeScheduleService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private FileService fileService;
	
	@Test
	public void testSaveFile ( ) {
		Calendar startDate = Calendar.getInstance(); startDate.set(2014, 4, 20);
		DriverModel driver = new DriverModel();
		driver.setName("Chris Lee");
		driver.setContractedHours(40);
		driver.setStartDate(startDate);
		driverService.saveDriver(driver);
		vehicleRepository.saveAndFlush(vehicleFactory.createVehicleByModel("MyBus Single Decker"));
		RouteModel routeModel = new RouteModel();
		routeModel.setRouteNumber("M2");
		List<String> stops = new ArrayList<String>();
		stops.add("Heinersdorf"); stops.add("Am Steinberg"); stops.add("Alexanderplatz");
		routeModel.setStopNames(stops);
		routeService.createAndSaveRoute(routeModel);
		MessageModel messageModel = new MessageModel();
		messageModel.setSubject("Test Message");
		messageModel.setSender("Dave Lee");
		messageModel.setMessageFolder(MessageFolder.INBOX);
		messageModel.setText("This is a test message");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(Calendar.getInstance().getTime());
		messageModel.setDate(date);
		messageService.saveMessage(messageModel);
		journeyService.saveStop("Danziger Strasse");
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
		journeyPatternModel.setName("Mon-Fri");
		journeyPatternModel.setDaysOfOperation(daysOfOperation);
		journeyPatternModel.setOutgoingTerminus("S+U Pankow");
		journeyPatternModel.setReturnTerminus("Rathaus Pankow");
		journeyPatternModel.setStartTime(Calendar.getInstance());
		journeyPatternModel.setEndTime(Calendar.getInstance());
		journeyPatternModel.setFrequency(15);
		journeyPatternModel.setDuration(3);
		journeyPatternModel.setTimetableName("Mon-Fri Times");
		journeyPatternModel.setRouteNumber("155");
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
		TimetableModel timetableModel = new TimetableModel();
		timetableModel.setName("Test");
		timetableModel.setValidFromDate(startDate);
		timetableModel.setValidToDate(startDate);
		timetableModel.setRouteNumber("M1");
		timetableService.saveTimetable(timetableModel);
		RouteScheduleModel routeScheduleModel = new RouteScheduleModel();
		routeScheduleModel.setDelay(10);
		routeScheduleModel.setRouteNumber("M1");
		routeScheduleModel.setScheduleNumber(1);
		routeScheduleService.saveRouteSchedule(routeScheduleModel);
		JourneyModel journeyModel = new JourneyModel();
		journeyModel.setJourneyNumber(1);
		journeyModel.setRouteNumber("M2");
		journeyModel.setRouteScheduleNumber(1);
		journeyService.saveJourney(journeyModel);
		StopTimeModel stopTimeModel = new StopTimeModel();
		stopTimeModel.setJourneyNumber(1);
		stopTimeModel.setStopName("Heinersdorf");
		stopTimeModel.setTime(Calendar.getInstance());
		journeyService.saveStopTime(stopTimeModel);
		GameModel gameModel = new GameModel();
		gameModel.setPlayerName("Dave Lee");
		gameModel.setScenarioName("Landuff Transport Company");
		gameService.saveGame(gameModel);
		
		TramsFile tramsFile = new TramsFile(driverService.getAllDrivers(), gameService.getAllGames(), journeyService.getAllJourneys(), 
				journeyPatternService.getAllJourneyPatterns(), messageService.getAllMessages(), routeService.getAllRoutes(), 
				routeScheduleService.getAllRouteSchedules(), journeyService.getAllStops(), journeyService.getAllStopTimes(),
				timetableService.getAllTimetableModels(), vehicleService.getVehicleModels());
		
		assertNotNull(tramsFile.getDriverModels());
		assertNotNull(tramsFile.getVehicleModels());
		assertNotNull(tramsFile.getRouteModels());
		assertNotNull(tramsFile.getMessageModels());
		assertNotNull(tramsFile.getStops());
		assertNotNull(tramsFile.getJourneyPatternModels());
		assertNotNull(tramsFile.getTimetableModels());
		assertNotNull(tramsFile.getRouteScheduleModels());
		assertNotNull(tramsFile.getJourneyModels());
		assertNotNull(tramsFile.getStopTimeModels());
		assertNotNull(tramsFile.getGameModel());
		
		fileService.saveFile(new File("test-trams.xml"), tramsFile);
		
		driverService.removeDriver(driver);
	}
	
	@Test
	//TODO: Recomment lines in after database is emptied in user interface.
	public void testLoadFile ( ) {
		TramsFile tramsFile2 = fileService.loadFile(new File("test-trams.xml"));
		assertNotNull(tramsFile2);
		//assertEquals(tramsFile2.getDrivers().size(), 1);
		//assertEquals(tramsFile2.getDrivers()[0].getName(), "Dave Lee");
		//assertEquals(tramsFile2.getVehicles().size(), 1);
		assertEquals(tramsFile2.getRouteScheduleModels().length, 1);
		assertEquals(tramsFile2.getGameModel().length, 1);
	}

}
