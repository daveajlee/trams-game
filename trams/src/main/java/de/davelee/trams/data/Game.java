package de.davelee.trams.data;

import de.davelee.trams.util.DifficultyLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class representing a game in TraMS.
 * @author Dave Lee
 */
@Getter
@Setter
public class Game {

	private int id;
    private int passengerSatisfaction;
    private String playerName;
    private double balance;
	private DifficultyLevel difficultyLevel = DifficultyLevel.EASY; //Default to easy.
	private String scenarioName;
	private LocalDateTime currentDateTime;
    private int timeIncrement;
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPassengerSatisfaction(int passengerSatisfaction) {
		this.passengerSatisfaction = passengerSatisfaction;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
    
    /**
     * Set the player name.
     * @param name a <code>String</code> with the player's name.
     */
    public void setPlayerName ( String name ) {
        playerName = name;
    }
    
    /**
     * Get the player name.
     * @return a <code>String</code> with the player's name.
     */
    public String getPlayerName ( ) {
        return playerName;
    }
    
    /**
     * Get the balance.
     * @return a <code>double</code> with the balance amount.
     */
    public double getBalance ( ) {
        return balance;
    }

    /**
     * Get the passenger satisfaction without recalculating it.
     * @return a <code>int</code> with the passenger satisfaction.
     */
    public int getPassengerSatisfaction ( ) {
        return passengerSatisfaction;
    }
    
    /**
     * Get the current difficulty level.
     * @return a <code>Enum</code> with the difficulty level.
     */
    public DifficultyLevel getDifficultyLevel ( ) {
        return difficultyLevel;
    }
    
    /**
     * Set the current difficulty level.
     * @param diffLevel a <code>Enum</code> with the difficulty level.
     */
    public void setDifficultyLevel ( DifficultyLevel diffLevel ) {
        difficultyLevel = diffLevel;
    }
    
    
    /**
     * Set the current scenario name.
     * @param scenarioName a <code>String</code> with the new scenario name.
     */
    public void setScenarioName ( String scenarioName ) {
        this.scenarioName = scenarioName;
    }
    
    /**
     * Get the current scenario name.
     * @return a <code>String</code> with the current scenario name.
     */
    public String getScenarioName ( ) {
        return scenarioName;
    }

    /**
     * Get the time increment.
     * @return a <code>int</code> with the time increment.
      */
    public int getTimeIncrement ( ) {
        return timeIncrement;
    }

    /**
     * Set a new time increment
     * @param newIncrement a <code>int</code> with the new time increment.
     */
    public void setTimeIncrement ( int newIncrement ) {
        timeIncrement = newIncrement;
    }

    /**
     * Get the current time.
     * @return a <code>LocalDateTime</code> representing the current time.
     */
    public LocalDateTime getCurrentDateTime ( ) {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

}
