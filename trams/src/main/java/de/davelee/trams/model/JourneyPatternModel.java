package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@Setter
public class JourneyPatternModel {
	
	private String name;
	private List<DayOfWeek> daysOfOperation;
	private String returnTerminus;
	private String outgoingTerminus;
	private LocalTime startTime;
	private LocalTime endTime;
	private int frequency;
	private int duration;
	private String routeNumber;
	private String timetableName;

}
