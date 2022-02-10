package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.ExportResponse;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.VehicleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class ExportControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExportController exportController;

    @Test
    public void testExport() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(ExportResponse.class))).thenReturn(
                ExportResponse.builder()
                        .vehicleResponses(new VehicleResponse[0])
                        .routeResponses(new RouteResponse[0])
                        .build());
        assertNotNull(exportController.getExport("Mustermann GmbH"));
    }

}
