package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * This class represents a journey pattern for a timetable - used by Easy Timetable Generator.
 * @author Dave Lee.
 */
@Getter
@Setter
public class JourneyPattern {

	private long id;
    private String name;
    private List<DayOfWeek> daysOfOperation;
    private String returnTerminus;
    private String outgoingTerminus;
    private LocalTime startTime;
    private LocalTime endTime;
    private int frequency;
    private int routeDuration;
	private String timetableName;
	private String routeNumber;

}
