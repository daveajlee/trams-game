package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteModel {
	
	private String routeNumber;
	private List<String> stopNames;
	private String company;

}
