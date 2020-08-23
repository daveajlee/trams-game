package de.davelee.trams.services;

import java.time.LocalDate;
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
        return TimetableModel.builder()
                .name(timetable.getName())
                .routeNumber(timetable.getRouteNumber())
                .validFromDate(timetable.getValidFromDate())
                .validToDate(timetable.getValidToDate())
                .build();
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
     * @param today a <code>LocalDate</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public TimetableModel getCurrentTimetable ( final String routeNumber, final LocalDate today ) {
        List<Timetable> timetables = timetableRepository.findByRouteNumber(routeNumber);
        for ( Timetable myTimetable : timetables ) {
            boolean clause1 = today.isAfter(myTimetable.getValidFromDate());
            boolean clause2 = today.isEqual(myTimetable.getValidFromDate());
            boolean clause3 = today.isBefore(myTimetable.getValidToDate());
            boolean clause4 = today.isEqual(myTimetable.getValidToDate());
            if ( (clause1 || clause2) && ( clause3 || clause4 ) ) {
                return convertToTimetableModel(myTimetable);
            }
        }
        return null; //If can't find timetable.
    }

    /**
     * Delete all timetables (only used as part of load function)
     */
    public void deleteAllTimetables() {
        timetableRepository.deleteAll();
    }

}
