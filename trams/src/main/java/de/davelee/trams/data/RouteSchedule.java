package de.davelee.trams.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column(name="SCHEDULE_NUMBER")
	private int scheduleNumber;
	
    @Column(name="DELAY_IN_MINS")
    private int delayInMins;

	@Column(name="ROUTE_ID")
	private long routeId;
    
    /**
     * Create a new route schedule.
     */
    public RouteSchedule() {
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

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}
    
}
