package de.davelee.trams.data;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;

/**
 * Class represents a route schedule (i.e. a particular timetable instance of a route) in the TraMS program.
 * @author Dave Lee.
 */
@Entity
@Table(name="ROUTE_SCHEDULE", uniqueConstraints=@UniqueConstraint(columnNames = {"scheduleNumber", "routeNumber"}))
public class RouteSchedule {
    
	@Id
	@GeneratedValue
	@Column()
	private long id;
	
	@Column()
	private int scheduleNumber;
	
    @Column()
    private int delayInMins;

	@Column()
    private String routeNumber;

	public int getDelayInMins() {
		return delayInMins;
	}

	public void setDelayInMins(final int delayInMins) {
		this.delayInMins = delayInMins;
	}

	public int getScheduleNumber() {
		return scheduleNumber;
	}

	public void setScheduleNumber(final int scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}

	public void setId(final long id) {
		this.id = id;
	}
	
	public long getId () {
    	return id;
    }

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}
    
}
