package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class TimetableModel {
	
	private String name;
	private String routeNumber;
	private LocalDate validFromDate;
	private LocalDate validToDate;

}
