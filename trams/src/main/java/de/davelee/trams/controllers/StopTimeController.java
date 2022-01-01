package de.davelee.trams.controllers;

import de.davelee.trams.api.response.StopTimeResponse;
import de.davelee.trams.util.Direction;
import de.davelee.trams.services.StopTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class StopTimeController {

    @Autowired
    private StopTimeService stopTimeService;

    public void generateStopTimes ( final String company, final int[] stoppingTimes, final String[] stopNames,
                                    final String routeNumber, final int[] distances, final String startTime,
                                    final String endTime, final int frequency, final String validFromDate,
                                    final String validToDate, final String operatingDays  ) {
        stopTimeService.generateStopTimes(company, stoppingTimes, stopNames, routeNumber, distances,
                startTime, endTime, frequency, validFromDate, validToDate, operatingDays);
    }

    public StopTimeResponse[] getStopTimes (final Optional<Direction> direction, final String routeNumber, final String date ) {
        //TODO: implement method and connect to REST interface.
        return new StopTimeResponse[0];
    }

}
