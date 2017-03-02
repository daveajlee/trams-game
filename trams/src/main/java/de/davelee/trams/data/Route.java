package de.davelee.trams.data;

import java.util.Calendar;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * This represents a Route object in the TraMS program.
 * @author Dave Lee
 */
@Entity
@Table(name="ROUTE")
public class Route {
    
	@Id
	@GeneratedValue
	@Column(name="ROUTE_ID")
	private long id;
	
	@Column(name="ROUTE_NUMBER")
	private String routeNumber;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
    @Column(name="ROUTE_STOPS")
	private List<Stop> stops;
    
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
    private List<RouteSchedule> routeSchedules;
    
    @Transient
    private Map<String, Vehicle> assignedSchedules; 
    
    @Transient
    private Map<String, Timetable> timetables;
    
    
    
    /**
     * Create a new route.
     */
    public Route ( ) {
        assignedSchedules = new HashMap<String, Vehicle>();
        stops = new ArrayList<Stop>();
        //Initialise the hash table.
        routeSchedules = new ArrayList<RouteSchedule>();
        timetables = new HashMap<String, Timetable>();
    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	public List<RouteSchedule> getRouteSchedules() {
		return routeSchedules;
	}

	public void setRouteSchedules(List<RouteSchedule> routeSchedules) {
		this.routeSchedules = routeSchedules;
	}

	public Map<String, Vehicle> getAssignedSchedules() {
		return assignedSchedules;
	}

	public void setAssignedSchedules(Map<String, Vehicle> assignedSchedules) {
		this.assignedSchedules = assignedSchedules;
	}

	public Map<String, Timetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(Map<String, Timetable> timetables) {
		this.timetables = timetables;
	}

	/**
     * Add a timetable to this route - this will be used to generate route schedules.
     * @param name a <code>String</code> with the timetable name.
     * @param timetable a <code>Timetable</code> object with the timetable details.
     */
    public void addTimetable ( String name, Timetable timetable ) {
        timetables.put(name, timetable);
    }
    
    /**
     * Add a timetable to this route - this will be used to generate route schedules.
     * @param name a <code>String</code> with the timetable name.
     * @param validFrom a <code>Calendar</code> object with the valid from date.
     * @param validTo a <code>Calendar</code> object with the valid from date.
     */
    public void addTimetable ( String name, Calendar validFrom, Calendar validTo ) {
    	Timetable timetable = new Timetable();
    	timetable.setName(name);
    	timetable.setValidFromDate(validFrom);
    	timetable.setValidToDate(validTo);
        timetables.put(name, timetable);
    }
    
    /**
     * Get a timetable for this route based on the supplied name.
     * @param name a <code>String</code> with the required timetable name.
     * @return a <code>Timetable</code> object with the timetable name.
     */
    public Timetable getTimetable ( String name ) {
        return timetables.get(name);
    }
    
    /**
     * Get all of the current timetable names.
     * @return a <code>Enumeration</code> with all timetable names for iteration.
     */
    public Iterator<String> getTimetableNames ( ) {
        return timetables.keySet().iterator();
    }
    
    /**
     * Delete the timetable with the specified name.
     * @param timeName a <code>String</code> with the timetable name.
     */
    public void deleteTimetable ( String timeName ) {
        timetables.remove(timeName);
    }

    /**
     * Set route number.
     * @param routeNumber a <code>String</code> with the route number.
     */
    public void setRouteNumber ( String routeNumber ) {
        this.routeNumber = routeNumber;
    } 
    
    /**
     * Get route number.
     * @return a <code>String</code> with the route number.
     */
    public String getRouteNumber ( ) {
        return routeNumber;
    }
    
    /**
     * Add an allocation to this route.
     * @param schedId a <code>String</code> with the schedule id.
     * @param v a <code>Vehicle</code> object with the vehicle running the schedule.
     */
    public void addAllocation ( String schedId, Vehicle v ) {
        assignedSchedules.put(schedId, v);
        for ( int i = 0; i < routeSchedules.size(); i++ ) {
            if ( routeSchedules.get(i).toString().equalsIgnoreCase(schedId) ) {
                v.setRouteScheduleId(routeSchedules.get(i).getId());
            }
        }
    }
    
    /**
     * Get the assigned vehicle for a schedule id.
     * @param schedId a <code>String</code> with the schedule id.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getAssignedVehicle ( String schedId ) {
        return assignedSchedules.get(schedId);
    }
      
    /**
     * Return a String representation of this object.
     * @return a <code>String</code> object.
     */
    public String toString ( ) {
        return routeNumber + ": " + stops.get(0).getStopName() + " - " + stops.get(stops.size()-1).getStopName();
    }
    
}
