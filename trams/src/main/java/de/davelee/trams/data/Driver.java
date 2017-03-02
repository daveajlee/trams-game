package de.davelee.trams.data;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class representing a driver in the TraMS program.
 * @author Dave
 */
@Entity
@Table(name="DRIVER")
public class Driver {

	@Id
	@GeneratedValue
	@Column(name="DRIVER_ID")
	private long id;
	
	@Column(name="NAME")
    private String name;
	
	@Column(name="CONTRACTED_HOURS")
    private int contractedHours;
	
	@Column(name="START_DATE")
    private Calendar startDate;
    
    /**
     * Create a new driver.
     */
    public Driver ( ) { }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName ( ) {
    	return name;
    }

	public void setContractedHours(int contractedHours) {
		this.contractedHours = contractedHours;
	}
    
    public int getContractedHours ( ) {
    	return contractedHours;
    }

}
