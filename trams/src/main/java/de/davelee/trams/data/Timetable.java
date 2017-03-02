package de.davelee.trams.data;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.MapKey;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents timetable outlines for the Easy Timetable Generator in TraMS.
 * @author Dave Lee
 */

@Entity
@Table(name="TIMETABLE")
public class Timetable {
	
	private static final Logger logger = LoggerFactory.getLogger(Timetable.class);

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
    
    @OneToMany(cascade = CascadeType.ALL)
    @MapKey(name = "name")
    private Map<String, JourneyPattern> journeyPatterns;
    
    public Timetable() {
    	this.journeyPatterns = new HashMap<String, JourneyPattern>();
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

	public Map<String, JourneyPattern> getJourneyPatterns() {
		return journeyPatterns;
	}

	public void setJourneyPatterns(Map<String, JourneyPattern> journeyPatterns) {
		this.journeyPatterns = journeyPatterns;
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
    
    /**
     * Method to add a journey pattern to this timetable.
     * @param name a <code>String</code> with the journey pattern name.
     * @param sp a <code>JourneyPattern</code> object representing the journey pattern.
     */
    public void addJourneyPattern ( String name, JourneyPattern jp ) {
        logger.debug("Current journey pattern size is: " + journeyPatterns.size() + " and new name is " + name);
        journeyPatterns.put(name, jp);
    }
    
    /**
     * Get journey pattern by the supplied name.
     * @param name a <code>String</code> with the journey pattern name.
     * @return a <code>JourneyPattern</code> object with the the supplied name.
     */
    public JourneyPattern getJourneyPattern ( String name ) {
        return journeyPatterns.get(name);
    }
    
    /**
     * Get the list of journey pattern names to iterate through.
     * @return a <code>Set</code> with journey pattern list.
     */
    public Set<String> getJourneyPatternNames ( ) {
        return journeyPatterns.keySet();
    }
    
    /**
     * Delete journey pattern with the supplied name.
     * @param name a <code>String</code> with the journey pattern name to delete.
     */
    public void deleteJourneyPattern ( String name ) {
        journeyPatterns.remove(name);
    }
    
}
