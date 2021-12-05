package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel {
	
	private String imagePath;
	private String model;
	private int seatingCapacity;
	private int standingCapacity;
	private double purchasePrice;
	private LocalDate deliveryDate;
	private String registrationNumber;
	private double depreciationFactor;
	private String routeNumber;
	private long routeScheduleNumber;

}
