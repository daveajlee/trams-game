package de.davelee.trams.data;

import java.util.*;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * This class represents a journey pattern for a timetable - used by Easy Timetable Generator.
 * @author Dave Lee.
 */
@Entity
@Table(name="JOURNEY_PATTERN")
public class JourneyPattern {
    
	@Id
	@GeneratedValue
	@Column(name="JOURNEY_PATTERN_ID")
	private long id;
	
	@Column(name="NAME")
    private String name;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="DAYS_OF_OPERATIONS")
	@Fetch(value = FetchMode.SUBSELECT)
	@Column(name="DAYS_OF_OPERATION")
    private List<Integer> daysOfOperation;
	
	@Column(name="RETURN_TERMINUS")
    private String returnTerminus;
	
	@Column(name="OUTGOING_TERMINUS")
    private String outgoingTerminus;
	
	@Column(name="START_TIME")
    private Calendar startTime;
	
	@Column(name="END_TIME")
    private Calendar endTime;
	
	@Column(name="FREQUENCY")
    private int frequency;
	
	@Column(name="ROUTE_DURATION")
    private int routeDuration;
    
	/**
     * Create a new JourneyPattern object.
     */
    public JourneyPattern ( ) { }
    
    /**
     * Get the name of the journey pattern.
     * @return a <code>String</code> with the name.
     */
    public String getName ( ) {
        return name;
    }
    
    public void setName(String name) {
		this.name = name;
	}
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRouteDuration() {
		return routeDuration;
	}

	public void setRouteDuration(int routeDuration) {
		this.routeDuration = routeDuration;
	}
	
	/**
     * Get the days of operations for this journey pattern.
     * @return a <code>List</code> with the days of operation.
     */
    public List<Integer> getDaysOfOperation ( ) {
        return daysOfOperation;
    }

	public void setDaysOfOperation(List<Integer> daysOfOperation) {
		this.daysOfOperation = daysOfOperation;
	}
	
	/**
     * Get the return terminus - also the 1st selected stop.
     * @return a <code>String</code> with the return terminus.
     */
    public String getReturnTerminus ( ) {
        return returnTerminus;
    }

	public void setReturnTerminus(String returnTerminus) {
		this.returnTerminus = returnTerminus;
	}
	
	/**
     * Get the outgoing terminus - also the 2nd selected stop.
     * @return a <code>String</code> with the outgoing terminus.
     */
    public String getOutgoingTerminus ( ) {
        return outgoingTerminus;
    }

	public void setOutgoingTerminus(String outgoingTerminus) {
		this.outgoingTerminus = outgoingTerminus;
	}

	/**
     * Get the start time of this service journey.
     * @return a <code>Calendar</code> with the start time.
     */
    public Calendar getStartTime ( ) {
        return startTime;
    }
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	/**
     * Get the end time of this service journey.
     * @return a <code>Calendar</code> with the end time.
     */
    public Calendar getEndTime ( ) {
        return endTime;
    }

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	
	/**
     * Get the frequency of this service journey.
     * @return a <code>int</code> with the frequency.
     */
    public int getFrequency ( ) {
        return frequency;
    }

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
