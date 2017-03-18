package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.data.Timetable;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.TimetableService;
import de.davelee.trams.util.DateFormats;

public class TimetableController {
	
	@Autowired
	private TimetableService timetableService;
	
	public String[] getTimetableNames ( final RouteModel routeModel ) {
		List<Timetable> timetables = timetableService.getTimetablesByRouteNumber(routeModel.getRouteNumber());
		String[] timetableNames = new String[timetables.size()];
		for ( int i = 0; i < timetableNames.length; i++ ) {
			timetableNames[i] = timetables.get(i).getName();
		}
		return timetableNames;
	}
	
	public void deleteTimetable ( final RouteModel routeModel, final String timetableName ) {
		timetableService.deleteTimetable(getRouteTimetableObject(routeModel, timetableName));
	}
	
	public TimetableModel getRouteTimetable ( final RouteModel routeModel, final String timetableName ) {
		return convertToTimetableModel(getRouteTimetableObject(routeModel, timetableName));
	}

	public TimetableModel[] getRouteTimetables ( final RouteModel routeModel ) {
		List<Timetable> timetables = timetableService.getTimetablesByRouteNumber(routeModel.getRouteNumber());
		TimetableModel[] timetableModels = new TimetableModel[timetables.size()];
		for ( int i = 0; i < timetableModels.length; i++ ) {
			timetableModels[i] = convertToTimetableModel(timetables.get(i));
		}
		return timetableModels;
	}
	
	public TimetableModel getTimetable ( final String timetableName ) {
		return convertToTimetableModel(timetableService.getTimetableByName(timetableName));
	}
	
	private Timetable getRouteTimetableObject ( final RouteModel routeModel, final String timetableName ) {
		return timetableService.getTimetableByRouteNumberAndName(routeModel.getRouteNumber(), timetableName);
	}
	
	private TimetableModel convertToTimetableModel ( final Timetable timetable ) {
		TimetableModel timetableModel = new TimetableModel();
		timetableModel.setName(timetable.getName());
		timetableModel.setValidFromDate(timetable.getValidFromDate());
		timetableModel.setValidToDate(timetable.getValidToDate());
		return timetableModel;
	}
	
	public void createTimetable ( final String name, final Calendar validFromDate, final Calendar validToDate, final RouteModel routeModel ) {
		timetableService.createTimetable(name, validFromDate, validToDate, routeModel.getRouteNumber());
	}
	
	public long getIdFromName ( final String name ) {
		return timetableService.getTimetableByName(name).getId();
	}
	
	public TimetableModel getCurrentTimetable ( final RouteModel routeModel, final Calendar currentDate) {
		return convertToTimetableModel(timetableService.getCurrentTimetable(routeModel.getRouteNumber(), currentDate));
	}
	
	/**
     * Format timetable date.
     * @return a <code>String</code> object.
     */
    public String getDateInfo ( final Calendar myCalendar ) {
    	return DateFormats.FULL_FORMAT.getFormat().format(myCalendar.getTime());
    }

	public List<Timetable> getAllTimetables ( ) {
		return timetableService.getAllTimetables();
	}
 
}
