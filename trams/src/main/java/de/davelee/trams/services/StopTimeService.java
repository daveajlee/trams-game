package de.davelee.trams.services;

import de.davelee.trams.api.request.GenerateStopTimesRequest;
import de.davelee.trams.api.request.StopPatternRequest;
import de.davelee.trams.api.response.StopTimesResponse;
import de.davelee.trams.model.StopTimeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class StopTimeService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

    public void generateStopTimes (final String company, final int[] stoppingTimes, final String[] stopNames,
                                   final String routeNumber, final int[] distances, final String startTime,
                                   final String endTime, final int frequency, final String validFromDate,
                                   final String validToDate, final String operatingDays ) {
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

    public StopTimeModel[] getStopTimes (final String company, final String date, final boolean isDepartures,
                                                         final boolean isArrivals, final Optional<String> startingTime, final String stopName) {
        StopTimesResponse stopTimesResponse;
        if (startingTime.isPresent()) {
            stopTimesResponse = restTemplate.getForObject(operationsServerUrl + "stopTimes/?company=" + company
                    + "&date=" + date + "&departures=" + isDepartures + "&arrivals=" + isArrivals + "&startingTime=" + startingTime.get()
                    + "&stopName=" + stopName, StopTimesResponse.class);
        } else {
            stopTimesResponse = restTemplate.getForObject(operationsServerUrl + "stopTimes/?company=" + company
                    + "&date=" + date + "&departures=" + isDepartures + "&arrivals=" + isArrivals + "&stopName=" + stopName,
                    StopTimesResponse.class);
        }
        if ( stopTimesResponse != null && stopTimesResponse.getStopTimeResponses() != null ) {
            StopTimeModel[] stopTimesModels = new StopTimeModel[stopTimesResponse.getStopTimeResponses().length];
            for (int i = 0; i < stopTimesResponse.getStopTimeResponses().length; i++) {
                stopTimesModels[i] = StopTimeModel.builder()
                        .time(LocalTime.parse(stopTimesResponse.getStopTimeResponses()[i].getDepartureTime(), DateTimeFormatter.ofPattern("HH:mm")))
                        .company(stopTimesResponse.getStopTimeResponses()[i].getCompany())
                        .journeyNumber(Integer.parseInt(stopTimesResponse.getStopTimeResponses()[i].getJourneyNumber()))
                        .routeNumber(stopTimesResponse.getStopTimeResponses()[i].getRouteNumber())
                        .stopName(stopTimesResponse.getStopTimeResponses()[i].getStopName())
                        .validFromDate(stopTimesResponse.getStopTimeResponses()[i].getValidFromDate())
                        .validToDate(stopTimesResponse.getStopTimeResponses()[i].getValidToDate())
                        .build();
            }
            return stopTimesModels;
        }
        return null;
    }

}
