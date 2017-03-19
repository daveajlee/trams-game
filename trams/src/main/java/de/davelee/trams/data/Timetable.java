package de.davelee.trams.data;

import java.util.*;

import javax.persistence.*;

/**
 * This class represents timetable outlines for the Easy Timetable Generator in TraMS.
 * @author Dave Lee
 */

@Entity
@Table(name="TIMETABLE", uniqueConstraints=@UniqueConstraint(columnNames = {"routeNumber", "name"}))
public class Timetable {

	@Id
	@GeneratedValue
	@Column
	private long id;
	
	@Column
    private String name;
	
	@Column
    private Calendar validFromDate;
	
	@Column
    private Calendar validToDate;

	@Column
	private String routeNumber;
    
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

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}
    
}
