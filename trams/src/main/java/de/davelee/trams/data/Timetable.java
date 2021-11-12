package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * This class represents timetable outlines for the Easy Timetable Generator in TraMS.
 * @author Dave Lee
 */
@Getter
@Setter
public class Timetable {

	private long id;
    private String name;
    private LocalDate validFromDate;
    private LocalDate validToDate;
	private String routeNumber;

}
