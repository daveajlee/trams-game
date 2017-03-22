package de.davelee.trams.data;

import java.util.List;
import java.util.ArrayList;

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

	@Column(name="NUMBER", unique=true)
	private String number;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
    @Column(name="ROUTE_STOPS")
	private List<Stop> stops;

    /**
     * Create a new route.
     */
    public Route ( ) {
        stops = new ArrayList<Stop>();
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
      
    /**
     * Return a String representation of this object.
     * @return a <code>String</code> object.
     */
    /*public String toString ( ) {
        return number + ": " + stops.get(0).getStopName() + " - " + stops.get(stops.size()-1).getStopName();
    }*/
    
}
