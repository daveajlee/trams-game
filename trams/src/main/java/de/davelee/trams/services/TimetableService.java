package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.data.Timetable;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.util.DateFormats;

public class TimetableService {

    private DatabaseManager databaseManager;
	
	public TimetableService() {
		
	}

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
	
	/**
     * Format timetable date.
     * @return a <code>String</code> object.
     */
    public String getDateInfo ( Calendar myCalendar ) {
    	return DateFormats.FULL_FORMAT.getFormat().format(myCalendar.getTime());
    }
    
    public Timetable createTimetable ( final String name, final Calendar validFromDate, final Calendar validToDate, final long routeId) {
    	Timetable timetable = new Timetable();
        timetable.setName(name);
        timetable.setValidFromDate(validFromDate);
        timetable.setValidToDate(validToDate);
        timetable.setRouteId(routeId);
        return timetable;
    }

    public List<Timetable> getTimetablesByRouteId (long routeId ) {
        return databaseManager.getTimetablesByRouteId(routeId);
    }

    public Timetable getTimetableByRouteIdAndName ( long routeId, String timetableName ) {
        return databaseManager.getTimetableByRouteIdAndName(routeId, timetableName);
    }

    public void deleteTimetable ( Timetable timetable ) {
        databaseManager.deleteTimetable(timetable);
    }


}
