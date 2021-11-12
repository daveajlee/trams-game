package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

/**
 * This represents a Route object in the TraMS program.
 * @author Dave Lee
 */
@Getter
@Setter
public class Route {

	private long id;
	private String number;
	private List<Stop> stops;

    /**
     * Create a new route.
     */
    public Route ( ) {
        stops = new ArrayList<>();
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

    /**
     * Set route number.
     * @param number a <code>String</code> with the route number.
     */
    public void setNumber ( String number ) {
        this.number = number;
    } 
    
    /**
     * Get route number.
     * @return a <code>String</code> with the route number.
     */
    public String getNumber ( ) {
        return number;
    }
    
}
