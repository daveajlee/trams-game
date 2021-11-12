package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class to represent a journey (i.e. one run of a route from terminus to terminus) in the TraMS program.
 * @author Dave
 */
@Getter
@Setter
public class Journey {

	private long id;
	private int journeyNumber;
	private int routeScheduleNumber;
	private String routeNumber;
    private List<StopTime> stopTimes;

    public void addStopTimeToList ( StopTime stopTime ) {
        stopTimes.add(stopTime);
    }

}
