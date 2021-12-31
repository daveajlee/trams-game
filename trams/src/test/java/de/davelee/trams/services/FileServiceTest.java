package de.davelee.trams.services;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.request.PurchaseVehicleRequest;
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
import de.davelee.trams.model.MessageModel;
import de.davelee.trams.model.RouteModel;
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
	private StopService journeyService;
	
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
		vehicleService.saveVehicle(PurchaseVehicleRequest.builder()
				.company("Mustermann GmbH")
				.fleetNumber("101")
				.livery("Green with Red text")
				.vehicleType("BUS")
				.modelName("MyBus Single Decker")
				.additionalTypeInformationMap(Map.of("Registration Number", "SDF3"))
				.seatingCapacity(50)
				.standingCapacity(95)
				.build());
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
		journeyService.saveStop("Danziger Strasse", "Mustermann GmbH");
		gameService.saveGame(GameModel.builder()
				.playerName("Dave Lee")
				.scenarioName("Landuff Transport Company")
				.build());
		
		TramsFile tramsFile = TramsFile.builder()
				.driverModels(driverService.getAllDrivers("Mustermann GmbH", "mmustermann-ghgkg"))
				.messageModels(messageService.getAllMessages("Mustermann GmbH"))
				.routeModels(routeService.getAllRoutes("Mustermann GmbH"))
				.stops(journeyService.getAllStops("Mustermann GmbH"))
				.vehicleModels(vehicleService.getVehicleModels("Mustermann GmbH"))
				.build();
		
		assertNotNull(tramsFile.getDriverModels());
		assertNotNull(tramsFile.getVehicleModels());
		assertNotNull(tramsFile.getRouteModels());
		assertNotNull(tramsFile.getMessageModels());
		assertNotNull(tramsFile.getStops());
		assertNotNull(tramsFile.getGameModel());
		
		fileService.saveFile(new File("test-game-service.json"), tramsFile);
	}

	private void deleteTemporaryContent ( ) {
		//Delete temporary content.
		driverService.removeAllDrivers("Mustermann GmbH", "mmustermann-ghgkg");
		routeService.deleteAllRoutes("Mustermann GmbH");
		messageService.deleteAllMessages("Mustermann GmbH");
		vehicleService.deleteAllVehicles("Mustermann GmbH");
		journeyService.deleteAllStops("Mustermann GmbH");
	}
	
	@Test
	public void testLoadFile ( ) {
		TramsFile tramsFile2 = fileService.loadFile(new File("test-game-service.json"));
		assertNotNull(tramsFile2);
		Assertions.assertEquals(tramsFile2.getDriverModels().length, 1);
		assertEquals(tramsFile2.getDriverModels()[0].getName(), "Chris Lee");
		Assertions.assertEquals(tramsFile2.getVehicleModels().length, 1);
		Assertions.assertEquals(tramsFile2.getGameModel().length, 1);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertNotNull ( final Object actual ){
		Assertions.assertNotNull(actual);
	}

}
