package de.davelee.trams.services;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
import de.davelee.trams.repository.VehicleRepository;
import de.davelee.trams.util.MessageFolder;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class FileServiceTest {
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
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
		//Delete temporary content.
		driverService.removeAllDrivers();
		routeService.deleteAllRoutes();
		messageService.deleteAllMessages();
		vehicleRepository.deleteAll();
		journeyService.deleteAllJourneys();
		journeyService.deleteAllStops();
		journeyPatternService.deleteAllJourneyPatterns();
		timetableService.deleteAllTimetables();
		routeScheduleService.deleteAllRouteSchedules();
		//Now create and save file.
		Calendar startDate = Calendar.getInstance(); startDate.set(2014, Calendar.APRIL, 20);
		DriverModel driver = new DriverModel();
		driver.setName("Chris Lee");
		driver.setContractedHours(40);
		driver.setStartDate(startDate);
		driverService.saveDriver(driver);
		vehicleRepository.saveAndFlush(vehicleFactory.createVehicleByModel("MyBus Single Decker"));
		RouteModel routeModel = new RouteModel();
		routeModel.setRouteNumber("M2");
		List<String> stops = new ArrayList<>();
		stops.add("Heinersdorf"); stops.add("Am Steinberg"); stops.add("Alexanderplatz");
		routeModel.setStopNames(stops);
		routeService.saveRoute(routeModel);
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
		List<Integer> daysOfOperation = new ArrayList<>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		JourneyPatternModel journeyPatternModel = JourneyPatternModel.builder()
				.name("Mon-Fri")
				.daysOfOperation(daysOfOperation)
				.outgoingTerminus("S+U Pankow")
				.returnTerminus("Rathaus Pankow")
				.startTime(Calendar.getInstance())
				.endTime(Calendar.getInstance())
				.frequency(15)
				.duration(3)
				.timetableName("Mon-Fri Times")
				.routeNumber("155")
				.build();
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
		StopTimeModel stopTimeModel = new StopTimeModel();
		stopTimeModel.setJourneyNumber(1);
		stopTimeModel.setStopName("Heinersdorf");
		stopTimeModel.setTime(Calendar.getInstance());
		journeyModel.addStopTimeToList(stopTimeModel);
		journeyService.saveJourney(journeyModel);
		GameModel gameModel = new GameModel();
		gameModel.setPlayerName("Dave Lee");
		gameModel.setScenarioName("Landuff Transport Company");
		gameService.saveGame(gameModel);
		
		TramsFile tramsFile = TramsFile.builder()
				.driverModels(driverService.getAllDrivers())
				.gameModel(gameService.getAllGames())
				.journeyModels(journeyService.getAllJourneys())
				.journeyPatternModels(journeyPatternService.getAllJourneyPatterns())
				.messageModels(messageService.getAllMessages())
				.routeModels(routeService.getAllRoutes())
				.routeScheduleModels(routeScheduleService.getAllRouteSchedules())
				.stops(journeyService.getAllStops())
				.timetableModels(timetableService.getAllTimetableModels())
				.vehicleModels(vehicleService.getVehicleModels())
				.build();
		
		Assertions.assertNotNull(tramsFile.getDriverModels());
		Assertions.assertNotNull(tramsFile.getVehicleModels());
		Assertions.assertNotNull(tramsFile.getRouteModels());
		Assertions.assertNotNull(tramsFile.getMessageModels());
		Assertions.assertNotNull(tramsFile.getStops());
		Assertions.assertNotNull(tramsFile.getJourneyPatternModels());
		Assertions.assertNotNull(tramsFile.getTimetableModels());
		Assertions.assertNotNull(tramsFile.getRouteScheduleModels());
		Assertions.assertNotNull(tramsFile.getJourneyModels());
		Assertions.assertNotNull(tramsFile.getGameModel());
		
		fileService.saveFile(new File("test-trams.xml"), tramsFile);
		
		driverService.removeDriver(driver);
	}
	
	@Test
	public void testLoadFile ( ) {
		TramsFile tramsFile2 = fileService.loadFile(new File("test-trams.xml"));
		Assertions.assertNotNull(tramsFile2);
		Assertions.assertEquals(tramsFile2.getDriverModels().length, 1);
		Assertions.assertEquals(tramsFile2.getDriverModels()[0].getName(), "Chris Lee");
		Assertions.assertEquals(tramsFile2.getVehicleModels().length, 1);
		Assertions.assertEquals(tramsFile2.getRouteScheduleModels().length, 1);
		Assertions.assertEquals(tramsFile2.getGameModel().length, 1);
	}

}
