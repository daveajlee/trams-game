package de.davelee.trams.controllers;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.TimetableService;
import de.davelee.trams.util.DateFormats;

public class TimetableController {
	
	@Autowired
	private TimetableService timetableService;
	
	public void deleteTimetable ( final RouteModel routeModel, final String timetableName ) {
		timetableService.deleteTimetable(getRouteTimetableModelObject(routeModel, timetableName));
	}
	
	public TimetableModel getRouteTimetable ( final RouteModel routeModel, final String timetableName ) {
		return getRouteTimetableModelObject(routeModel, timetableName);
	}

	public TimetableModel[] getRouteTimetables ( final RouteModel routeModel ) {
		return timetableService.getTimetablesByRouteNumber(routeModel.getRouteNumber());
	}
	
	private TimetableModel getRouteTimetableModelObject ( final RouteModel routeModel, final String timetableName ) {
		return timetableService.getTimetableByRouteNumberAndName(routeModel.getRouteNumber(), timetableName);
	}
	
	public void createTimetable ( final String name, final Calendar validFromDate, final Calendar validToDate, final RouteModel routeModel ) {
		TimetableModel timetableModel = new TimetableModel();
		timetableModel.setName(name);
		timetableModel.setValidFromDate(validFromDate);
		timetableModel.setValidToDate(validToDate);
		timetableModel.setRouteNumber(routeModel.getRouteNumber());
		timetableService.saveTimetable(timetableModel);
	}
	
	public TimetableModel getCurrentTimetable ( final RouteModel routeModel, final Calendar currentDate) {
		return timetableService.getCurrentTimetable(routeModel.getRouteNumber(), currentDate);
	}
	
	/**
     * Format timetable date.
     * @return a <code>String</code> object.
     */
    public String getDateInfo ( final Calendar myCalendar ) {
    	return DateFormats.FULL_FORMAT.getFormat().format(myCalendar.getTime());
    }

	public TimetableModel[] getAllTimetableModels ( ) {
		return timetableService.getAllTimetableModels();
	}
 
}
