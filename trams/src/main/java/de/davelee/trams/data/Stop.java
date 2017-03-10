package de.davelee.trams.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents a Stop in the TraMS program.
 * @author Dave Lee
 */
@Entity
@Table(name="STOP")
public class Stop {
    
    /**
	 * 
	 */
	@Id
	@GeneratedValue
	@Column(name="STOP_ID", nullable=false)
	private long id;
	
	@Column(name="STOP_NAME")
	private String stopName;
    
	/**
     * Create a new stop. 
     */
    public Stop ( ) {
    	
    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	/**
     * Get the stop name.
     * @return a <code>String</code> with the stop name.
     */
    public String getStopName() {
        return stopName;
    }

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
    
    /**
     * Return a string representation of this Stop object.
     * @return a <code>String</code> with the stop name.
     */
    public String toString ( ) {
        return stopName;
    }

}
