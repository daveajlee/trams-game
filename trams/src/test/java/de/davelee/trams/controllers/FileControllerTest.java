package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.*;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @Mock
    private ExportController exportController;

    @Mock
    private CompanyController companyController;

    @Mock
    private DriverController driverController;

    @Mock
    private MessageController messageController;

    @Mock
    private RouteController routeController;

    @Mock
    private ScenarioController scenarioController;

    @Mock
    private SimulationController simulationController;

    @Mock
    private VehicleController vehicleController;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void testSaveFile() {
        assertNotNull(scenarioController);
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        companyController.createCompany("Dave Lee", "Landuff Transport Company", "Mustermann GmbH");
        Mockito.when(companyController.getCompany(any(), any())).thenReturn(CompanyResponse.builder()
                        .playerName("Dave Lee")
                .build());
        assertEquals("Dave Lee", companyController.getCompany("Mustermann GmbH", "Dave Lee").getPlayerName());
        Mockito.when(driverController.getAllDrivers("Mustermann GmbH")).thenReturn(new UserResponse[] {
                UserResponse.builder()
                        .company("Mustermann GmbH")
                        .firstName("Max")
                        .surname("Mustermann")
                        .build()
        });
        Mockito.when(messageController.getAllMessages("Mustermann GmbH")).thenReturn(new MessageResponse[] {
                MessageResponse.builder()
                        .company("Mustermann GmbH")
                        .folder("INBOX")
                        .sender("Council")
                        .subject("Test Message")
                        .text("This is a test message")
                        .dateTime("01-01-2020 02:00").build()
        });
        Mockito.when(exportController.getExport("Mustermann GmbH")).thenReturn(ExportResponse.builder()
                .routeResponses(new RouteResponse[] {
                        RouteResponse.builder().routeNumber("1A").company("Mustermann GmbH").build()
                })
                .vehicleResponses(new VehicleResponse[] {
                        VehicleResponse.builder()
                                .company("Mustermann GmbH")
                                .modelName("BendyBus 2000")
                                .fleetNumber("2003")
                                .build()
                })
                .build());
        Mockito.when(companyController.exportCompany(eq("Mustermann GmbH"), eq("Dave Lee"), any(), any(), any(), any())).thenReturn(
                ExportCompanyResponse.builder()
                        .routes("[{\"routeNumber\":\"1A\",\"company\":\"Mustermann GmbH\"}]")
                        .messages("[{\"company\":\"Mustermann GmbH\",\"subject\":\"Test Message\",\"text\":\"This is a test message\",\"sender\":\"Council\",\"folder\":\"INBOX\",\"dateTime\":\"01-01-2020 02:00\"}]")
                        .drivers("[{\"firstName\":\"Max\",\"surname\":\"Mustermann\",\"username\":null,\"company\":\"Mustermann GmbH\",\"leaveEntitlementPerYear\":0,\"workingDays\":null,\"position\":null,\"startDate\":null,\"endDate\":null,\"role\":null,\"dateOfBirth\":null,\"hourlyWage\":0.0,\"contractedHoursPerWeek\":0,\"trainings\":null,\"userHistory\":null}]")
                        .vehicles("[{\"fleetNumber\":\"2003\",\"company\":\"Mustermann GmbH\",\"deliveryDate\":null,\"inspectionDate\":null,\"vehicleType\":null,\"purchasePrice\":0.0,\"vehicleStatus\":null,\"seatingCapacity\":0,\"standingCapacity\":0,\"modelName\":\"BendyBus 2000\",\"livery\":null,\"allocatedTour\":null,\"delayInMinutes\":0,\"inspectionStatus\":null,\"nextInspectionDueInDays\":0,\"additionalTypeInformationMap\":null,\"userHistory\":null,\"timesheet\":null}]")
                        .playerName("Dave Lee")
                        .name("Mustermann GmbH")
                        .balance(80000.0)
                        .difficultyLevel("EASY")
                        .time("24-01-2022 04:00")
                        .scenarioName("Landuff")
                        .build());
        fileController.saveFile(new File("test-game.json"), "Mustermann GmbH", "Dave Lee");
        companyController.createCompany("Bob Smith", "Landuff Transport Company", "Mustermann GmbH");
        Mockito.when(companyController.getCompany(any(), any())).thenReturn(CompanyResponse.builder()
                .playerName("Bob Smith")
                .build());
        assertEquals("Bob Smith", companyController.getCompany("Mustermann GmbH", "Bob Smith").getPlayerName());
    }

    @Test
    public void testLoadFile ( ) {
        fileController.loadFile(new File("test-game-service.json"));
        //TODO: Fix save and load file logic and replace test with correct test.
    }

}
