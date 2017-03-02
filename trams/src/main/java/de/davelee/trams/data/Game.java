package de.davelee.trams.data;

import java.util.Hashtable;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.davelee.trams.util.DifficultyLevel;

/**
 * Class representing a game in TraMS.
 * @author Dave Lee
 */
@Entity
@Table(name="GAME")
public class Game {
	
	@Id
	@GeneratedValue
	@Column(name="GAME_ID", nullable=false)
	private int id;
	
	@Column(name="PASSENGER_SATISFACTION")
    private int passengerSatisfaction;
	
	@Column(name="PLAYER_NAME")
    private String playerName;
	
	@Column(name="BALANCE")
    private double balance;
	
	@Column(name="DIFFICULTY_LEVEL")
	private DifficultyLevel difficultyLevel = DifficultyLevel.EASY; //Default to easy.
	
	@Column(name="SCENARIO_NAME")
	private String scenarioName;
	
	@Transient
	//TODO: Transient ausbauen!
	private Hashtable<String, LinkedList<Integer>> scheduleIds;
    
    public Game() {
    	scheduleIds = new Hashtable<String, LinkedList<Integer>>();
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

	public Hashtable<String, LinkedList<Integer>> getScheduleIds() {
		return scheduleIds;
	}

	public void setScheduleIds(Hashtable<String, LinkedList<Integer>> scheduleIds) {
		this.scheduleIds = scheduleIds;
	}
	
}
