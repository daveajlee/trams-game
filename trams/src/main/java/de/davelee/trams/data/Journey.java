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
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Stop> journeyStops;

    @Column(name="ROUTE_SCHEDULE_ID")
	private long routeScheduleId;
    
    /**
     * Create a new journey.
     */
    public Journey ( ) {
        journeyStops = new ArrayList<Stop>();
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
    
    public List<Stop> getJourneyStops() {
		return journeyStops;
	}

	public void setJourneyStops(List<Stop> journeyStops) {
		this.journeyStops = journeyStops;
	}

	/**
     * Add stop.
     * @param newStop a <code>Stop</code> object with the stop to add.
     */
    public void addStop ( Stop newStop ) {
        journeyStops.add(newStop);
    }
    
    /**
     * Delete stop.
     * @param oldStop a <code>Stop</code> object with the stop to delete.
     */
    public void removeStop ( Stop oldStop ) {
        journeyStops.remove(oldStop);
    }
    
    public int compareTo ( Journey journey ) {
    	if ( this.getJourneyStops().equals(journey.getJourneyStops()) ) {
    		return 0;
    	}
    	return -1;
    }
    
}
