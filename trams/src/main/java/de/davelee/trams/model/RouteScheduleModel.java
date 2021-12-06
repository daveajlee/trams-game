package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteScheduleModel {
	
	private int delay;
	private int scheduleNumber;
	private String routeNumber;
	private String company;
	private String fleetNumber;

}
