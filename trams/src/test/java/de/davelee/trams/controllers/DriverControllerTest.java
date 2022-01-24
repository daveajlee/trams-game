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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class DriverControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DriverController driverController;

    @Test
    public void testGetAllDrivers() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(UsersResponse.class))).thenReturn(
                UsersResponse.builder()
                        .userResponses(new UserResponse[] {
                                UserResponse.builder()
                                        .company("Mustermann GmbH")
                                        .firstName("Max")
                                        .surname("Mustermann")
                                        .build()
                        }).build()
        );
        assertEquals(1, driverController.getAllDrivers("Mustermann GmbH").length);
        Mockito.when(restTemplate.getForObject(anyString(), eq(UsersResponse.class))).thenReturn(null);
        assertNull(driverController.getAllDrivers("Mustermann GmbH"));
        Mockito.when(restTemplate.getForObject(anyString(), eq(UsersResponse.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        assertNull(driverController.getAllDrivers("Mustermann GmbH"));
    }

    @Test
    public void testEmployDriver() {
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        driverController.employDriver("Max Mustermann", "Mustermann GmbH", "01-01-2020 02:00");
        driverController.createSuppliedDrivers(List.of("Max Mustermann", "Erica Mustermann"), "Mustermann GmbH", "01-01-2020 02:00");
    }

    @Test
    public void testLoadDrivers() {
        Mockito.doNothing().when(restTemplate).delete(anyString());
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        driverController.loadDrivers(new UserResponse[] {
                UserResponse.builder()
                        .firstName("Max")
                        .surname("Mustermann")
                        .username("mmustermann")
                        .startDate("01-01-2020 02:00").build()
        }, "Mustermann GmbH");
    }

}
