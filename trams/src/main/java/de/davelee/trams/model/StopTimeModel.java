package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StopTimeModel {
	
	private int journeyNumber;
	private String routeNumber;
	private int routeScheduleNumber;
	private String stopName;
	private LocalTime time;
	private String company;


}
