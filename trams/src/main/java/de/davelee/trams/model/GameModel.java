package de.davelee.trams.model;

import de.davelee.trams.util.DifficultyLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class GameModel {
	
	private String playerName;
	private int passengerSatisfaction;
	private double balance;
	private DifficultyLevel difficultyLevel;
	private String scenarioName;
	private LocalDateTime currentDateTime;
	private int timeIncrement;
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(final String playerName) {
		this.playerName = playerName;
	}
	
	public int getPassengerSatisfaction() {
		return passengerSatisfaction;
	}
	
	public void setPassengerSatisfaction(final int passengerSatisfaction) {
		this.passengerSatisfaction = passengerSatisfaction;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(final double balance) {
		this.balance = balance;
	}
	
	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}
	
	public void setDifficultyLevel(final DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
	
	public String getScenarioName() {
		return scenarioName;
	}
	
	public void setScenarioName(final String scenarioName) {
		this.scenarioName = scenarioName;
	}
	
	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}
	
	public void setCurrentDateTime(final LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}
	
	public int getTimeIncrement() {
		return timeIncrement;
	}
	
	public void setTimeIncrement(final int timeIncrement) {
		this.timeIncrement = timeIncrement;
	}

}
