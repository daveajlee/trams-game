package de.davelee.trams.controllers;

import de.davelee.trams.model.Direction;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.StopTimeModel;
import de.davelee.trams.services.StopTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
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

    public StopTimeModel[] getStopTimes (final Optional<Direction> direction, final String routeNumber, final LocalDate date ) {
        //TODO: implement method and connect to REST interface.
        return new StopTimeModel[0];
    }

}
