package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Timetable;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    public void saveTimetable ( final TimetableModel timetableModel ) {
        timetableRepository.saveAndFlush(convertToTimetable(timetableModel));
    }

    private Timetable convertToTimetable ( final TimetableModel timetableModel ) {
    	Timetable timetable = new Timetable();
        timetable.setName(timetableModel.getName());
        timetable.setRouteNumber(timetableModel.getRouteNumber());
        timetable.setValidFromDate(timetableModel.getValidFromDate());
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
        Timetable timetable = timetableRepository.findByRouteNumberAndName(routeNumber, timetableName);
        if ( timetable != null ) { return convertToTimetableModel(timetable); }
        return null;
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
     * @param routeNumber a <code>String</code> with the route number to get the current timetable for.
     * @param today a <code>Calendar</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public TimetableModel getCurrentTimetable ( final String routeNumber, final Calendar today ) {
        List<Timetable> timetables = timetableRepository.findByRouteNumber(routeNumber);
        for ( Timetable myTimetable : timetables ) {
            boolean clause1 = today.after(myTimetable.getValidFromDate());
            boolean clause2 = compareDayMonthYear(myTimetable.getValidFromDate(), today);
            boolean clause3 = today.before(myTimetable.getValidToDate());
            boolean clause4 = compareDayMonthYear(myTimetable.getValidToDate(), today);
            if ( (clause1 || clause2) && ( clause3 || clause4 ) ) {
                return convertToTimetableModel(myTimetable);
            }
        }
        return null; //If can't find timetable.
    }

    private boolean compareDayMonthYear ( final Calendar cal1, final Calendar cal2 ) {
        return cal1.get(Calendar.DAY_OF_MONTH)==cal2.get(Calendar.DAY_OF_MONTH) && cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)
            && cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR);
    }

    /**
     * Delete all timetables (only used as part of load function)
     */
    public void deleteAllTimetables() {
        timetableRepository.deleteAll();
    }

}
