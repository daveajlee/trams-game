package de.davelee.trams.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class to represent a journey (i.e. one run of a route from terminus to terminus) in the TraMS program.
 * @author Dave
 */
@Entity
@Table(name="JOURNEY")
public class Journey {
    
    /**
	 * 
	 */
	@Id
	@GeneratedValue
	@Column(name="JOURNEY_ID")
	private long id;

    @Column(name="ROUTE_SCHEDULE_ID")
	private long routeScheduleId;
    
    /**
     * Create a new journey.
     */
    public Journey ( ) {
    }
    
    /**
     * Get the journey id.
     * @return a <code>long</code> with the journey id.
     */
    public long getId () {
        return id;
    }
    
    public void setId(long id) {
		this.id = id;
	}

    public long getRouteScheduleId() {
        return routeScheduleId;
    }

    public void setRouteScheduleId(long routeScheduleId) {
        this.routeScheduleId = routeScheduleId;
    }
    
}
