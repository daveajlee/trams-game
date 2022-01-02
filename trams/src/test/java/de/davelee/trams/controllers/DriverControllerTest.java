package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.api.response.UsersResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
@Disabled
public class DriverControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DriverController driverController;

    @Test
    public void testDrivers() {
        assertFalse(driverController.hasSomeDriversBeenEmployed(CompanyResponse.builder()
                        .playerName("Dave Lee")
                        .name("Mustermann GmbH")
                        .balance(8000.0)
                .build()));
        driverController.employDriver("Max Mustermann", "Mustermann GmbH", "01-01-2020");
        Mockito.when(restTemplate.getForObject(anyString(), eq(UsersResponse.class))).thenReturn(
                UsersResponse.builder()
                        .count(1L)
                        .userResponses(new UserResponse[] { UserResponse.builder()
                        .firstName("Max")
                        .surname("Mustermann")
                        .company("Mustermann GmbH")
                        .contractedHoursPerWeek(40)
                        .startDate("20-04-2014")
                        .build() }).build()
        );
        assertTrue(driverController.hasSomeDriversBeenEmployed(CompanyResponse.builder()
                .playerName("Dave Lee")
                .name("Mustermann GmbH")
                .balance(8000.0)
                .time("01-01-2020")
                .build()));
        LocalDate startDate = LocalDate.of(2013,4,20);
        driverController.employDriver("Micha Mustermann", "Mustermann GmbH", "20-04-2013");
        assertTrue(driverController.hasSomeDriversBeenEmployed(CompanyResponse.builder()
                .playerName("Dave Lee")
                .name("Mustermann GmbH")
                .balance(8000.0)
                .time("01-01-2020")
                .build()));
    }

    @Test
    public void testCreateDriver() {
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        driverController.employDriver("Max Mustermann", "Mustermann GmbH", "20-04-2014");
        Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(
                UserResponse.builder()
                        .firstName("Max")
                        .surname("Mustermann")
                        .contractedHoursPerWeek(40)
                        .startDate("20-04-2014")
                        .build()
        );
        UserResponse userResponse = driverController.getDriverByName("Max Mustermann", "Mustermann GmbH");
        assertEquals(userResponse.getFirstName(), "Max");
        assertEquals(userResponse.getContractedHoursPerWeek(), 40);
        assertEquals(userResponse.getStartDate(), "20-04-2014");
    }

    @Test
    public void testGetDriverByName ( ) {
        //Treble needed so that test works in both Maven and JUnit.
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        driverController.employDriver("Dave Lee", "Mustermann GmbH", "20-04-2014");
        driverController.employDriver("Brian Lee", "Mustermann GmbH", "20-04-2014");
        driverController.employDriver("Chris Lee", "Mustermann GmbH", "20-04-2014");
        Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(
                UserResponse.builder()
                        .firstName("Brian")
                        .surname("Lee")
                        .contractedHoursPerWeek(40)
                        .startDate("20-04-2014")
                        .build()
        );
        Assertions.assertNotNull(driverController.getDriverByName("Brian Lee", "Mustermann GmbH"));
        assertEquals(driverController.getDriverByName("Brian Lee", "Nustermann GmbH").getFirstName(), "Brian");
        Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(null);
        Assertions.assertNull(driverController.getDriverByName("Stephan Lee", "Mustermann GmbH"));
    }

}
