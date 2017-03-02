package de.davelee.trams.data;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Class represents a route schedule (i.e. a particular timetable instance of a route) in the TraMS program.
 * @author Dave Lee.
 */
@Entity
@Table(name="ROUTE_SCHEDULE")
public class RouteSchedule {
    
	@Id
	@GeneratedValue
	@Column(name="ID")
	private long id;
	
	@Column(name="ROUTE_NUMBER")
	private String routeNumber;
	
	@Column(name="SCHEDULE_NUMBER")
	private int scheduleNumber;
	
    @Column(name="DELAY_IN_MINS")
    private int delayInMins;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @Column(name="JOURNEY_LIST")
    private List<Journey> journeyList;
    
    /**
     * Create a new route schedule.
     */
    public RouteSchedule() {
    	journeyList = new ArrayList<Journey>();
    }
    
    public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public List<Journey> getJourneyList() {
		return journeyList;
	}

	public void setJourneyList(List<Journey> journeyList) {
		this.journeyList = journeyList;
	}
	
	/**
     * Add a pre-made journey to this route schedule.
     * @param newJourney a <code>Journey</code> object with the journey to add.
     */
    public void addJourney ( Journey newJourney ) {
        journeyList.add(newJourney);
    }
    
    /**
     * Remove the specified journey. 
     * @param oldJourney a <code>Journey</code> object to remove.
     */
    public void removeService( Journey oldJourney) {
    	journeyList.remove(oldJourney);
    }

	public int getDelayInMins() {
		return delayInMins;
	}

	public void setDelayInMins(int delayInMins) {
		this.delayInMins = delayInMins;
	}

	public int getScheduleNumber() {
		return scheduleNumber;
	}

	public void setScheduleNumber(int scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getId () {
    	return id;
    }
    
    /**
     * Return a String representation of this RouteSchedule object.
     * @return a <code>String</code> object.
     */
    public String toString() {
        return routeNumber + "/" + scheduleNumber;
    }
    
}
