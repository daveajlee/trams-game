package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Class representing a driver in the TraMS program.
 * @author Dave
 */
@Getter
@Setter
public class Driver {

	private long id;
    private String name;
    private int contractedHours;
    private LocalDate startDate;

}
