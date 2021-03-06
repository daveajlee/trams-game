package de.davelee.trams.data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.FetchType;

/**
 * This class represents a journey pattern for a timetable - used by Easy Timetable Generator.
 * @author Dave Lee.
 */
@Entity
@Table(name="JOURNEY_PATTERN", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "routeNumber", "timetableName"}))
public class JourneyPattern {
    
	@Id
	@GeneratedValue
	@Column
	private long id;
	
	@Column
    private String name;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="DAYS_OF_OPERATIONS")
	@Fetch(value = FetchMode.SUBSELECT)
	@Column
    private List<DayOfWeek> daysOfOperation;
	
	@Column
    private String returnTerminus;
	
	@Column
    private String outgoingTerminus;
	
	@Column
    private LocalTime startTime;
	
	@Column
    private LocalTime endTime;
	
	@Column
    private int frequency;
	
	@Column
    private int routeDuration;

	@Column
	private String timetableName;

	@Column
	private String routeNumber;
    
    /**
     * Get the name of the journey pattern.
     * @return a <code>String</code> with the name.
     */
    public String getName ( ) {
        return name;
    }
    
    public void setName(final String name) {
		this.name = name;
	}
    
    public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public int getRouteDuration() {
		return routeDuration;
	}

	public void setRouteDuration(final int routeDuration) {
		this.routeDuration = routeDuration;
	}
	
	/**
     * Get the days of operations for this journey pattern.
     * @return a <code>List</code> with the days of operation.
     */
    public List<DayOfWeek> getDaysOfOperation ( ) {
        return daysOfOperation;
    }

	public void setDaysOfOperation(final List<DayOfWeek> daysOfOperation) {
		this.daysOfOperation = daysOfOperation;
	}
	
	/**
     * Get the return terminus - also the 1st selected stop.
     * @return a <code>String</code> with the return terminus.
     */
    public String getReturnTerminus ( ) {
        return returnTerminus;
    }

	public void setReturnTerminus(final String returnTerminus) {
		this.returnTerminus = returnTerminus;
	}
	
	/**
     * Get the outgoing terminus - also the 2nd selected stop.
     * @return a <code>String</code> with the outgoing terminus.
     */
    public String getOutgoingTerminus ( ) {
        return outgoingTerminus;
    }

	public void setOutgoingTerminus(final String outgoingTerminus) {
		this.outgoingTerminus = outgoingTerminus;
	}

	/**
     * Get the start time of this service journey.
     * @return a <code>LocalTime</code> with the start time.
     */
    public LocalTime getStartTime ( ) {
        return startTime;
    }
	
	public void setStartTime(final LocalTime startTime) {
		this.startTime = startTime;
	}
	
	/**
     * Get the end time of this service journey.
     * @return a <code>LocalTime</code> with the end time.
     */
    public LocalTime getEndTime ( ) {
        return endTime;
    }

	public void setEndTime(final LocalTime endTime) {
		this.endTime = endTime;
	}
	
	/**
     * Get the frequency of this service journey.
     * @return a <code>int</code> with the frequency.
     */
    public int getFrequency ( ) {
        return frequency;
    }

	public void setFrequency(final int frequency) {
		this.frequency = frequency;
	}

	public String getTimetableName() {
		return timetableName;
	}

	public void setTimetableName(final String timetableName) {
		this.timetableName = timetableName;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

}
