package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopsResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StopsResponseTest {

    @Test
    public void testSetters() {
        StopsResponse stopsResponse = new StopsResponse();
        stopsResponse.setCount(1L);
        stopsResponse.setStopResponses(new StopResponse[] {
                StopResponse.builder()
                        .company("Mustermann Bus GmbH")
                        .name("Greenfield")
                        .latitude(50.03)
                        .longitude(123.04)
                        .build()
        });
        assertEquals(1L, stopsResponse.getCount());
        assertEquals("Mustermann Bus GmbH", stopsResponse.getStopResponses()[0].getCompany());
    }

}
