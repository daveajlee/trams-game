package de.davelee.trams.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import de.davelee.trams.util.DifficultyLevel;

import java.util.Calendar;

/**
 * Class representing a game in TraMS.
 * @author Dave Lee
 */
@Entity
@Table(name="GAME")
public class Game {
	
	@Id
	@GeneratedValue
	@Column(nullable=false)
	private int id;
	
	@Column
    private int passengerSatisfaction;
	
	@Column(unique=true)
    private String playerName;
	
	@Column
    private double balance;
	
	@Column
	private DifficultyLevel difficultyLevel = DifficultyLevel.EASY; //Default to easy.
	
	@Column
	private String scenarioName;

    @Column
	private Calendar currentTime;

    @Column
    private int timeIncrement;
    
    public Game() {
    }
    
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
     * @return a <code>Calendar</code> representing the current time.
     */
    public Calendar getCurrentTime ( ) {
        return currentTime;
    }

    public void setCurrentTime(Calendar currentTime) {
        this.currentTime = currentTime;
    }

}
