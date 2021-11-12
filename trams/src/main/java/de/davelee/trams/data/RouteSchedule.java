package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

/**
 * Class represents a route schedule (i.e. a particular timetable instance of a route) in the TraMS program.
 * @author Dave Lee.
 */
@Getter
@Setter
public class RouteSchedule {

	private long id;
	private int scheduleNumber;
    private int delayInMins;
    private String routeNumber;
    
}
