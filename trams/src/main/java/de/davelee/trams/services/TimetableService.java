package de.davelee.trams.services;

import java.util.Calendar;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.data.Timetable;
import de.davelee.trams.util.DateFormats;

public class TimetableService {
	
	public TimetableService() {
		
	}
	
	/**
     * Format timetable date.
     * @return a <code>String</code> object.
     */
    public String getDateInfo ( Calendar myCalendar ) {
    	return DateFormats.FULL_FORMAT.getFormat().format(myCalendar.getTime());
    }
    
    public Timetable createTimetable ( final String name, final Calendar validFromDate, final Calendar validToDate, 
    		final JourneyPattern journeyPattern) {
    	Timetable timetable = new Timetable();
        timetable.setName(name);
        timetable.setValidFromDate(validFromDate);
        timetable.setValidToDate(validToDate);
        timetable.addJourneyPattern(journeyPattern.getName(), journeyPattern);
        return timetable;
    }

}
