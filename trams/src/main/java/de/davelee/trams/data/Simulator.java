package de.davelee.trams.data;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents the simulator in the TraMS program.
 * @author Dave Lee.
 */
@Entity
@Table(name="SIMULATOR")
public class Simulator {
	
	@Id
	@GeneratedValue
	@Column(name="SIMULATOR_ID", nullable=false)
	private int id;
	
	@Column(name="CURRENT_TIME")
	private Calendar currentTime;
	
	@Column(name="TIME_INCREMENT")
    private int timeIncrement;
	
	@Column(name="PREVIOUS_TIME")
    private Calendar previousTime;
    
    /**
     * Create a new simulator.
     */
    public Simulator ( ) {
        setCurrentTime(new GregorianCalendar(2009,Calendar.AUGUST,20,5,0,0));
        setTimeIncrement(15);
        setPreviousTime((Calendar) getCurrentTime().clone());
        
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	/**
     * Get the previous time.
     * @return a <code>Calendar</code> representing the previous time.
     */
    public Calendar getPreviousTime ( ) {
        return previousTime;
    }

	public void setPreviousTime(Calendar previousTime) {
		this.previousTime = currentTime;
	}
    
}
