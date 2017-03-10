package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.dao.TimetableDao;
import de.davelee.trams.data.Timetable;
import de.davelee.trams.util.DateFormats;

public class TimetableService {

    private TimetableDao timetableDao;
	
	public TimetableService() {
		
	}

    public TimetableDao getTimetableDao() {
        return timetableDao;
    }

    public void setTimetableDao(TimetableDao timetableDao) {
        this.timetableDao = timetableDao;
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
        return timetableDao.getTimetablesByRouteId(routeId);
    }

    public Timetable getTimetableByRouteIdAndName ( long routeId, String timetableName ) {
        return timetableDao.getTimetableByRouteIdAndName(routeId, timetableName);
    }

    public void deleteTimetable ( Timetable timetable ) {
        timetableDao.deleteTimetable(timetable);
    }

    public List<Timetable> getAllTimetables ( ) {
        return timetableDao.getAllTimetables();
    }

    /**
     * This method gets the current timetable which is valid for day.
     * It is specifically used for getting the days which this timetable is valid for.
     * @param today a <code>Calendar</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public Timetable getCurrentTimetable ( long routeId, Calendar today ) {
        List<Timetable> timetables = timetableDao.getTimetablesByRouteId(routeId);
        for ( Timetable myTimetable : timetables ) {
            if ( (myTimetable.getValidFromDate().before(today) || myTimetable.getValidFromDate().equals(today)) && (myTimetable.getValidToDate().after(today) || myTimetable.getValidToDate().equals(today))  ) {
                return myTimetable;
            }
        }
        return null; //If can't find timetable.
    }

}
