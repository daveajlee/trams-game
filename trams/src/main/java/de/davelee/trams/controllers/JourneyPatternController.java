package de.davelee.trams.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.util.DateFormats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.JourneyPatternService;
import org.springframework.stereotype.Controller;

@Controller
public class JourneyPatternController {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private TimetableController timetableController;

	private static final Logger logger = LoggerFactory.getLogger(JourneyPatternController.class);
	
	public JourneyPatternModel[] getJourneyPatternModels (final TimetableModel timetableModel, final String routeNumber ) {
		return journeyPatternService.getJourneyPatternsByTimetableNameAndRouteNumber(timetableModel.getName(), routeNumber);
	}
	
	public void createJourneyPattern ( final String name, final List<Integer> operatingDays, final String outgoingTerminus, 
			final String returnTerminus, final Calendar timeFrom, final Calendar timeTo, final int frequency, final int routeDuration,
			final TimetableModel timetableModel, final String routeNumber ) {
		JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
		journeyPatternModel.setName(name);
		journeyPatternModel.setDaysOfOperation(operatingDays);
		journeyPatternModel.setOutgoingTerminus(outgoingTerminus);
		journeyPatternModel.setReturnTerminus(returnTerminus);
		journeyPatternModel.setStartTime(timeFrom);
		journeyPatternModel.setEndTime(timeTo);
		journeyPatternModel.setFrequency(frequency);
		journeyPatternModel.setDuration(routeDuration);
		journeyPatternModel.setTimetableName(timetableModel.getName());
		journeyPatternModel.setRouteNumber(routeNumber);
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
	}

	public JourneyPatternModel[] getAllJourneyPatterns ( ) {
		return journeyPatternService.getAllJourneyPatterns();
	}

	/**
	 * Return a formatted String array of schedule dates from today.
	 * @param routeModel a <code>RouteModel</code> representing details of the route.
	 * @param today a <code>Calendar</code> object with the current date.
	 * @return a <code>String</code> array of possible schedule dates.
	 */
	public String[] getPossibleSchedulesDates (final RouteModel routeModel, Calendar today ) {
		//Create the list.
		List<Calendar> myCalendar = new ArrayList<Calendar>();
		//Go through all of the timetables and add them if they are not already in.
		TimetableModel[] timetableModels = timetableController.getRouteTimetables(routeModel);
		Calendar thisDate;
		for (TimetableModel timeT : timetableModels) {
			thisDate = (Calendar) today.clone();
			//Now check if we have passed the valid to date.
			while ( !thisDate.after(timeT.getValidToDate()) ) {
				//Check if we have added this date before...
				if ( !myCalendar.contains(thisDate) ) {
					//Finally check that at least one of the journey patterns has an operating service on this day.
					JourneyPatternModel[] journeyPatternModels = getJourneyPatternModels(timeT, routeModel.getRouteNumber());
					for ( JourneyPatternModel jpm : journeyPatternModels ) {
						if ( jpm.getDaysOfOperation().contains(thisDate.get(Calendar.DAY_OF_WEEK)) ) {
							myCalendar.add(thisDate);
							break;
						}
					}
				}
				thisDate = ((Calendar) (thisDate.clone()));
				thisDate.add(Calendar.HOUR, 24);
			}
		}
		Collections.sort(myCalendar);
		String[] myCalDates = new String[myCalendar.size()];
		logger.debug("MyCalDates length is " + myCalDates.length);
		for ( int i = 0; i < myCalDates.length; i++ ) {
			myCalDates[i] = DateFormats.FULL_FORMAT.getFormat().format(myCalendar.get(i).getTime());
		}
		return myCalDates;
	}

	/**
	 * Load Journey Patterns.
	 * @param journeyPatternModels an array of <code>JourneyPatternModel</code> objects with journey patterns to store and delete all other journey patterns.
	 */
	public void loadJourneyPatterns ( final JourneyPatternModel[] journeyPatternModels ) {
		journeyPatternService.deleteAllJourneyPatterns();
		for ( JourneyPatternModel journeyPatternModel : journeyPatternModels ) {
			journeyPatternService.saveJourneyPattern(journeyPatternModel);
		}
	}

}
