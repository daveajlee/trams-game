package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Timetable;
import de.davelee.trams.repository.TimetableRepository;
import de.davelee.trams.util.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;

public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;
	
	public TimetableService() {
		
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
        return timetableRepository.findByRouteId(routeId);
    }

    public Timetable getTimetableByRouteIdAndName ( long routeId, String timetableName ) {
        return timetableRepository.findByRouteIdAndName(routeId, timetableName);
    }

    public void deleteTimetable ( Timetable timetable ) {
        timetableRepository.delete(timetable);
    }

    public List<Timetable> getAllTimetables ( ) {
        return timetableRepository.findAll();
    }

    /**
     * This method gets the current timetable which is valid for day.
     * It is specifically used for getting the days which this timetable is valid for.
     * @param today a <code>Calendar</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public Timetable getCurrentTimetable ( long routeId, Calendar today ) {
        List<Timetable> timetables = timetableRepository.findByRouteId(routeId);
        for ( Timetable myTimetable : timetables ) {
            if ( (myTimetable.getValidFromDate().before(today) || myTimetable.getValidFromDate().equals(today)) && (myTimetable.getValidToDate().after(today) || myTimetable.getValidToDate().equals(today))  ) {
                return myTimetable;
            }
        }
        return null; //If can't find timetable.
    }

    public Timetable getTimetableByName ( final String timetableName ) {
        return timetableRepository.findByName(timetableName);
    }

}
