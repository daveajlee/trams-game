package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Timetable;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.repository.TimetableRepository;
import de.davelee.trams.util.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;

public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;
	
	public TimetableService() {
		
	}

    public void saveTimetable ( final TimetableModel timetableModel ) {
        timetableRepository.saveAndFlush(convertToTimetable(timetableModel));
    }

    private Timetable convertToTimetable ( final TimetableModel timetableModel ) {
    	Timetable timetable = new Timetable();
        timetable.setName(timetableModel.getName());
        timetable.setRouteNumber(timetableModel.getRouteNumber());
        timetable.setValidFromDate(timetable.getValidFromDate());
        timetable.setValidToDate(timetableModel.getValidToDate());
        return timetable;
    }

    private TimetableModel convertToTimetableModel ( final Timetable timetable ) {
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setName(timetable.getName());
        timetableModel.setRouteNumber(timetable.getRouteNumber());
        timetableModel.setValidFromDate(timetable.getValidFromDate());
        timetableModel.setValidToDate(timetable.getValidToDate());
        return timetableModel;
    }

    public TimetableModel[] getTimetablesByRouteNumber ( final String routeNumber ) {
        List<Timetable> timetables = timetableRepository.findByRouteNumber(routeNumber);
        TimetableModel[] timetableModels = new TimetableModel[timetables.size()];
        for ( int i = 0; i < timetableModels.length; i++ ) {
            timetableModels[i] = convertToTimetableModel(timetables.get(i));
        }
        return timetableModels;
    }

    public TimetableModel getTimetableByRouteNumberAndName ( final String routeNumber, final String timetableName ) {
        return convertToTimetableModel(timetableRepository.findByRouteNumberAndName(routeNumber, timetableName));
    }

    public void deleteTimetable ( final TimetableModel timetableModel ) {
        timetableRepository.delete(timetableRepository.findByRouteNumberAndName(timetableModel.getRouteNumber(), timetableModel.getName()));
    }

    public TimetableModel[] getAllTimetableModels ( ) {
        List<Timetable> timetables = timetableRepository.findAll();
        TimetableModel[] timetableModels = new TimetableModel[timetables.size()];
        for ( int i = 0; i < timetableModels.length; i++ ) {
            timetableModels[i] = convertToTimetableModel(timetables.get(i));
        }
        return timetableModels;
    }

    /**
     * This method gets the current timetable which is valid for day.
     * It is specifically used for getting the days which this timetable is valid for.
     * @param today a <code>Calendar</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public TimetableModel getCurrentTimetable ( final String routeNumber, final Calendar today ) {
        List<Timetable> timetables = timetableRepository.findByRouteNumber(routeNumber);
        for ( Timetable myTimetable : timetables ) {
            if ( (myTimetable.getValidFromDate().before(today) || myTimetable.getValidFromDate().equals(today)) && (myTimetable.getValidToDate().after(today) || myTimetable.getValidToDate().equals(today))  ) {
                return convertToTimetableModel(myTimetable);
            }
        }
        return null; //If can't find timetable.
    }

}
