package de.davelee.trams.controllers;

import de.davelee.trams.api.request.GenerateStopTimesRequest;
import de.davelee.trams.api.request.StopPatternRequest;
import de.davelee.trams.api.response.StopTimeResponse;
import de.davelee.trams.api.response.StopTimesResponse;
import de.davelee.trams.util.Direction;
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

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

    /**
     * Generate a set of stop times based on the specified criteria.
     * @param company a <code>String</code> with the name of the company to generate stop times for.
     * @param stoppingTimes a <code>int</code> array containing the amount of minutes that a vehicle should stop at the particular stop position in the array.
     * @param stopNames a <code>String</code> array containing the list of stop names that should be served.
     * @param routeNumber a <code>String</code> with the route number to use to generate stop times.
     * @param distances a <code>int</code> array containing the distances in minutes between stops. Position 0 is the distance between stop 0 and stop 1.
     * @param startTime a <code>String</code> with the start time in the format HH:mm
     * @param endTime a <code>String</code> with the end time in the format HH:mm
     * @param frequency a <code>int</code> containing the frequency between departures in minutes.
     * @param validFromDate a <code>String</code> with the valid from date for departures in the format dd-MM-yyyy
     * @param validToDate a <code>String</code> with the valid to date for departures in the format dd-MM-yyyy
     * @param operatingDays a <code>String</code> with comma-separated Strings with those days where the departure runs.
     */
    public void generateStopTimes ( final String company, final int[] stoppingTimes, final String[] stopNames,
                                    final String routeNumber, final int[] distances, final String startTime,
                                    final String endTime, final int frequency, final String validFromDate,
                                    final String validToDate, final String operatingDays  ) {
        restTemplate.postForObject(operationsServerUrl + "stopTimes/generate",
                GenerateStopTimesRequest.builder()
                        .company(company)
                        .stopPatternRequest(StopPatternRequest.builder()
                                .stoppingTimes(stoppingTimes)
                                .stopNames(stopNames)
                                .distances(distances)
                                .build())
                        .routeNumber(routeNumber)
                        .startTime(startTime)
                        .endTime(endTime)
                        .frequency(frequency)
                        .validFromDate(validFromDate)
                        .validToDate(validToDate)
                        .operatingDays(operatingDays)
                        .build(),
                Void.class);
    }

    /**
     * Return a set of stop times based on the specified criteria.
     * @param direction a <code>Direction</code> with the direction that stop times should be returned. Both directions will be returned if this is empty.
     * @param routeNumber a <code>String</code> with the route number to return stop times for.
     * @param date a <code>String</code> with the date to return departures or arrivals for in the format dd-MM-yyyy
     * @param company a <code>String</code> with the name of the company to return departures for.
     * @return a <code>StopTimeResponse</code> array containing all stop times which matched the specified criteria.
     */
    public StopTimeResponse[] getStopTimes (final Optional<Direction> direction, final String routeNumber, final String date, final String company ) {
        //TODO: Implement correct logic with missing parameters implemented.
        final Optional<String> startingTime = Optional.empty(); final boolean isDepartures = false; final boolean isArrivals = false; final String stopName = "";
        StopTimesResponse stopTimesResponse = startingTime.isPresent() ?
            stopTimesResponse = restTemplate.getForObject(operationsServerUrl + "stopTimes/?company=" + company
                    + "&date=" + date + "&departures=" + isDepartures + "&arrivals=" + isArrivals + "&startingTime=" + startingTime.get()
                    + "&stopName=" + stopName, StopTimesResponse.class) :
                    restTemplate.getForObject(operationsServerUrl + "stopTimes/?company=" + company
                            + "&date=" + date + "&departures=" + isDepartures + "&arrivals=" + isArrivals + "&stopName=" + stopName,
                        StopTimesResponse.class);
        if ( stopTimesResponse != null && stopTimesResponse.getStopTimeResponses() != null ) {
            return stopTimesResponse.getStopTimeResponses();
        }
        return null;
    }

}
