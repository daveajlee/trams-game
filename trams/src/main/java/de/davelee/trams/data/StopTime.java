package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Class to represent a time for a journey to arrive at a stop.
 * @author Dave Lee
 */
@Getter
@Setter
public class StopTime {

	private long id;
	private int journeyNumber;
	private int routeScheduleNumber;
	private String routeNumber;
	private String stopName;
	private LocalTime time;

}