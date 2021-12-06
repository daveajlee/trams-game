package de.davelee.trams.model;

import de.davelee.trams.util.DifficultyLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameModel {
	
	private String playerName;
	private String company;
	private double passengerSatisfaction;
	private double balance;
	private DifficultyLevel difficultyLevel;
	private String scenarioName;
	private LocalDateTime currentDateTime;
	private int timeIncrement;

}
