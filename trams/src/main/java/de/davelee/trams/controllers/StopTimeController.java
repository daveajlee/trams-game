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

@Controller
public class StopTimeController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

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
