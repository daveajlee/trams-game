package de.davelee.trams.services;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.beans.TramsFile;
import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.MessageModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.StopTimeModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.util.MessageFolder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
@Disabled
public class FileServiceTest {
	
	@Autowired
	private DriverService driverService;
	
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
		//Delete temporary content
		deleteTemporaryContent();
		//Now create and save file.
		driverService.saveDriver(DriverModel.builder()
				.name("Chris Lee")
				.contractedHours(40)
				.startDate(LocalDate.of(2014,4,20)).build());
		vehicleService.saveVehicle(vehicleService.createVehicleObject("MyBus Single Decker", "SDF3", LocalDate.now()));
		routeService.saveRoute(RouteModel.builder()
				.routeNumber("M2")
				.stopNames(List.of("Heinersdorf", "Am Steinberg", "Alexanderplatz"))
				.build());
		messageService.saveMessage(MessageModel.builder()
				.subject("Test Message")
				.sender("Dave Lee")
				.messageFolder(MessageFolder.INBOX)
				.text("This is a test message")
				.date(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()))
				.build());
		journeyService.saveStop("Danziger Strasse");
		journeyPatternService.saveJourneyPattern(JourneyPatternModel.builder()
				.name("Mon-Fri")
				.daysOfOperation(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
				.outgoingTerminus("S+U Pankow")
				.returnTerminus("Rathaus Pankow")
				.startTime(LocalTime.now())
				.endTime(LocalTime.now())
				.frequency(15)
				.duration(3)
				.timetableName("Mon-Fri Times")
				.routeNumber("155")
				.build());
		timetableService.saveTimetable(TimetableModel.builder()
				.name("Test")
				.validFromDate(LocalDate.of(2014,4,20))
				.validToDate(LocalDate.of(2014,4,20))
				.routeNumber("M1")
				.build());
		routeScheduleService.saveRouteSchedule(RouteScheduleModel.builder()
				.delay(10)
				.routeNumber("M1")
				.scheduleNumber(1)
				.build());
		JourneyModel journeyModel = JourneyModel.builder()
				.journeyNumber(1)
				.routeNumber("M2")
				.routeScheduleNumber(1)
				.build();
		journeyModel.addStopTimeToList(StopTimeModel.builder()
				.journeyNumber(1)
				.stopName("Heinersdorf")
				.time(LocalTime.now())
				.build());
		journeyService.saveJourney(journeyModel);
		gameService.saveGame(GameModel.builder()
				.playerName("Dave Lee")
				.scenarioName("Landuff Transport Company")
				.build());
		
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
		
		assertNotNull(tramsFile.getDriverModels());
		assertNotNull(tramsFile.getVehicleModels());
		assertNotNull(tramsFile.getRouteModels());
		assertNotNull(tramsFile.getMessageModels());
		assertNotNull(tramsFile.getStops());
		assertNotNull(tramsFile.getJourneyPatternModels());
		assertNotNull(tramsFile.getTimetableModels());
		assertNotNull(tramsFile.getRouteScheduleModels());
		assertNotNull(tramsFile.getJourneyModels());
		assertNotNull(tramsFile.getGameModel());
		
		fileService.saveFile(new File("test-game-service.json"), tramsFile);
	}

	private void deleteTemporaryContent ( ) {
		//Delete temporary content.
		driverService.removeAllDrivers();
		routeService.deleteAllRoutes();
		messageService.deleteAllMessages();
		vehicleService.deleteAllVehicles();
		journeyService.deleteAllJourneys();
		journeyService.deleteAllStops();
		journeyPatternService.deleteAllJourneyPatterns();
		timetableService.deleteAllTimetables();
		routeScheduleService.deleteAllRouteSchedules();
	}
	
	@Test
	public void testLoadFile ( ) {
		TramsFile tramsFile2 = fileService.loadFile(new File("test-game-service.json"));
		assertNotNull(tramsFile2);
		Assertions.assertEquals(tramsFile2.getDriverModels().length, 1);
		assertEquals(tramsFile2.getDriverModels()[0].getName(), "Chris Lee");
		Assertions.assertEquals(tramsFile2.getVehicleModels().length, 1);
		Assertions.assertEquals(tramsFile2.getRouteScheduleModels().length, 1);
		Assertions.assertEquals(tramsFile2.getGameModel().length, 1);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertNotNull ( final Object actual ){
		Assertions.assertNotNull(actual);
	}

}
