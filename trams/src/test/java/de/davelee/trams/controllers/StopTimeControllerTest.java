package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.StopTimeResponse;
import de.davelee.trams.api.response.StopTimesResponse;
import de.davelee.trams.util.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class StopTimeControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StopTimeController stopTimeController;

    @Test
    public void testGenerateStopTimes() {
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        stopTimeController.generateStopTimes("Mustermann GmbH", new String[] { "Airport", "City Centre"}, "1A",
                "06:00", "19:00", 15, "01-01-2020",
                "01-03-2020", "Monday,Tuesday,Wednesday,Thursday,Friday");
    }

    @Test
    public void testGetStopTimes() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(StopTimesResponse.class))).thenReturn(
                StopTimesResponse.builder()
                        .count(0L)
                        .stopTimeResponses(new StopTimeResponse[0])
                        .build());
        assertNotNull(stopTimeController.getStopTimes(Optional.of(Direction.OUTGOING), "1A", "01-01-2020",
                "Mustermann GmbH", Optional.of("06:00"), "City Centre", true, false));
        assertNotNull(stopTimeController.getStopTimes(Optional.of(Direction.OUTGOING), "1A", "01-01-2020",
                "Mustermann GmbH", Optional.empty(), "City Centre", false, true));
        Mockito.when(restTemplate.getForObject(anyString(), eq(StopTimesResponse.class))).thenReturn(null);
        assertNull(stopTimeController.getStopTimes(Optional.of(Direction.OUTGOING), "1A", "01-01-2020",
                "Mustermann GmbH", Optional.empty(), "City Centre", true, false));
    }

}
