package de.davelee.trams.controllers;

import de.davelee.trams.api.request.GenerateStopTimesRequest;
import de.davelee.trams.api.response.StopTimeResponse;
import de.davelee.trams.api.response.StopTimesResponse;
import de.davelee.trams.util.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * This class enables access to StopTime data via REST endpoints to the TraMS Operations microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class StopTimeController {

    private RestTemplate restTemplate;

    @Value("${server.operations.url}")
    private String operationsServerUrl;

    private final Logger logger = LoggerFactory.getLogger(StopTimeController.class);

    /**
     * Generate a set of stop times based on the specified criteria.
     * @param company a <code>String</code> with the name of the company to generate stop times for.
     * @param stopNames a <code>String</code> array containing the names of the stops that should be served.
     * @param routeNumber a <code>String</code> with the route number to use to generate stop times.
     * @param startTime a <code>String</code> with the start time in the format HH:mm
     * @param endTime a <code>String</code> with the end time in the format HH:mm
     * @param frequency a <code>int</code> containing the frequency between departures in minutes.
     * @param validFromDate a <code>String</code> with the valid from date for departures in the format dd-MM-yyyy
     * @param validToDate a <code>String</code> with the valid to date for departures in the format dd-MM-yyyy
     * @param operatingDays a <code>String</code> with comma-separated Strings with those days when the departure runs.
     */
    public void generateStopTimes ( final String company, final String[] stopNames, final String routeNumber,
                                    final String startTime, final String endTime, final int frequency,
                                    final String validFromDate, final String validToDate, final String operatingDays  ) {
        //Send data to the server.
        GenerateStopTimesRequest generateStopTimesRequest = GenerateStopTimesRequest.builder()
                .company(company)
                .stopNames(stopNames)
                .routeNumber(routeNumber)
                .startTime(startTime)
                .endTime(endTime)
                .frequency(frequency)
                .validFromDate(validFromDate)
                .validToDate(validToDate)
                .operatingDays(operatingDays)
                .build();

        restTemplate.postForObject(operationsServerUrl + "stopTimes/generate",
                generateStopTimesRequest,
                Void.class);
    }

    /**
     * Return a set of stop times based on the specified criteria.
     * @param direction a <code>Direction</code> with the direction that stop times should be returned. Both directions will be returned if this is empty.
     * @param routeNumber a <code>String</code> with the route number to return stop times for.
     * @param date a <code>String</code> with the date to return departures or arrivals for in the format dd-MM-yyyy
     * @param company a <code>String</code> with the name of the company to return departures for.
     * @param startingTime a <code>Optional</code> of type <code>String</code> containing the time in format HH:mm to deliver departure or arrival times from.
     * @param stopName a <code>String</code> with the name of the stop to retrieve stop times for.
     * @param isDepartures a <code>boolean</code> which is true iff departure times should be returned.
     * @param isArrivals a <code>boolean</code> which is true iff arrival times should be returned.
     * @return a <code>StopTimeResponse</code> array containing all stop times which matched the specified criteria.
     */
    public StopTimeResponse[] getStopTimes (final Optional<Direction> direction, final String routeNumber, final String date, final String company,
                                            final Optional<String> startingTime, final String stopName, final boolean isDepartures, final boolean isArrivals) {
        logger.info("Direction=" + direction + "&routeNumber=" + routeNumber) ;
        //Translate date into correct format.
        String stopTimeDate = date.substring(6,10) + "-" + date.substring(3,5) + "-" + date.substring(0,2);
        //Request the stop times matching the supplied parameters.
        StopTimesResponse stopTimesResponse = startingTime.isPresent() ?
            restTemplate.getForObject(operationsServerUrl + "stopTimes/?company=" + company
                    + "&date=" + stopTimeDate + "&endDate=" + stopTimeDate + "&departures=" + isDepartures + "&arrivals=" + isArrivals + "&startingTime=" + startingTime.get()
                    + "&stopName=" + stopName, StopTimesResponse.class) :
                    restTemplate.getForObject(operationsServerUrl + "stopTimes/?company=" + company
                            + "&date=" + stopTimeDate + "&endDate=" + stopTimeDate + "&departures=" + isDepartures + "&arrivals=" + isArrivals + "&stopName=" + stopName,
                        StopTimesResponse.class);
        if ( stopTimesResponse != null && stopTimesResponse.getStopTimeResponses() != null ) {
            return stopTimesResponse.getStopTimeResponses();
        }
        return null;
    }

    /**
     * Set the rest template object via Spring.
     * @param restTemplate a <code>RestTemplate</code> object.
     */
    @Autowired
    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
