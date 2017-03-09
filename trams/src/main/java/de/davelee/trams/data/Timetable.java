package de.davelee.trams.data;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents timetable outlines for the Easy Timetable Generator in TraMS.
 * @author Dave Lee
 */

@Entity
@Table(name="TIMETABLE")
public class Timetable {

	@Id
	@GeneratedValue
	@Column(name="TIMETABLE_ID")
	private long id;
	
	@Column(name="NAME")
    private String name;
	
	@Column(name="VALID_FROM_DATE")
    private Calendar validFromDate;
	
	@Column(name="VALID_TO_DATE")
    private Calendar validToDate;

	@Column(name="ROUTE_ID")
	private long routeId;
    
    public Timetable() {

    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Calendar validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Calendar getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Calendar validToDate) {
		this.validToDate = validToDate;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
     * Method to return the name of this timetable.
     * @return a <code>String</code> with the timetable name.
     */
    public String getName () {
        return name;
    }

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}
    
}
