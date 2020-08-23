package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.TimetableService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
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
	
	public void createTimetable (final String name, final LocalDate validFromDate, final LocalDate validToDate, final RouteModel routeModel ) {
		timetableService.saveTimetable(TimetableModel.builder()
				.name(name)
				.validFromDate(validFromDate)
				.validToDate(validToDate)
				.routeNumber(routeModel.getRouteNumber())
				.build());
	}
	
	public TimetableModel getCurrentTimetable ( final RouteModel routeModel, final LocalDate currentDate) {
		return timetableService.getCurrentTimetable(routeModel.getRouteNumber(), currentDate);
	}
	
	/**
     * Format timetable date.
	 * @param myDate a <code>LocalDate</code> representing the value to format.
     * @return a <code>String</code> object.
     */
    public String getDateInfo ( final LocalDate myDate ) {
    	return DateTimeFormatter.RFC_1123_DATE_TIME.format(myDate);
    }

	public TimetableModel[] getAllTimetableModels ( ) {
		return timetableService.getAllTimetableModels();
	}

	/**
	 * Load Timetables.
	 * @param timetableModels an array of <code>TimetableModel</code> objects with timetables to store and delete all other timetables.
	 */
	public void loadTimetables ( final TimetableModel[] timetableModels ) {
		timetableService.deleteAllTimetables();
		for ( TimetableModel timetableModel : timetableModels ) {
			timetableService.saveTimetable(timetableModel);
		}
	}
 
}
