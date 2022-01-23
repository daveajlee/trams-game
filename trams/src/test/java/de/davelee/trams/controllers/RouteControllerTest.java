package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.RoutesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class RouteControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RouteController routeController;

    @Test
    public void testGetRoutes() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(RoutesResponse.class))).thenReturn(RoutesResponse.builder()
                .count(1L)
                .routeResponses(new RouteResponse[] {
                        RouteResponse.builder().routeNumber("1A").company("Mustermann GmbH").build()
                }).build());
        assertNotNull(routeController.getRoutes("Mustermann GmbH"));
        Mockito.when(restTemplate.getForObject(anyString(), eq(RoutesResponse.class))).thenReturn(null);
        assertNull(routeController.getRoutes("Mustermann GmbH"));
    }

    @Test
    public void testGetRoute() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(RouteResponse.class))).thenReturn(
                        RouteResponse.builder().routeNumber("1A").company("Mustermann GmbH").build()
               );
        assertNotNull(routeController.getRoute("1A", "Mustermann GmbH"));
    }

    @Test
    public void testAddRoute() {
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(
                null
        );
        routeController.addNewRoute("1A", "Mustermann GmbH");
    }

    @Test
    public void testLoadRoutes() {
        Mockito.doNothing().when(restTemplate).delete(anyString());
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(
                null
        );
        routeController.loadRoutes(new RouteResponse[] {
                RouteResponse.builder().routeNumber("1A").company("Mustermann GmbH").build()
        }, "Mustermann GmbH");
    }

}
