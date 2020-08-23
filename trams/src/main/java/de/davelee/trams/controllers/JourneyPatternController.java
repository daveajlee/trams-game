package de.davelee.trams.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
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
	
	public void createJourneyPattern ( final JourneyPatternModel journeyPatternModel ) {
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
	}

	/**
	 * Delete a single journey pattern based on its name, timetable name and route number.
	 * @param name a <code>String</code> containing the name of the journey pattern.
	 * @param timetableName a <code>String</code> containing the timetable name that this journey pattern has.
	 * @param routeNumber a <code>String</code> containing the route number that this journey pattern has.
	 */
	public void deleteJourneyPattern ( final String name, final String timetableName, final String routeNumber ) {
		journeyPatternService.deleteJourneyPattern(name, timetableName, routeNumber);
	}

	/**
	 * Retrieve a single journey pattern based on its name, timetable name and route number.
	 * @param name a <code>String</code> containing the name of the journey pattern.
	 * @param timetableName a <code>String</code> containing the timetable name that this journey pattern has.
	 * @param routeNumber a <code>String</code> containing the route number that this journey pattern has.
	 * @return a <code>JourneyPatternModel</code> containing the matching journey pattern.
	 */
	public JourneyPatternModel getJourneyPattern ( final String name, final String timetableName, final String routeNumber ) {
		return journeyPatternService.getJourneyPattern(name, timetableName, routeNumber);
	}

	public JourneyPatternModel[] getAllJourneyPatterns ( ) {
		return journeyPatternService.getAllJourneyPatterns();
	}

	/**
	 * Return a formatted String array of schedule dates from today.
	 * @param routeModel a <code>RouteModel</code> representing details of the route.
	 * @param today a <code>LocalDate</code> object with the current date.
	 * @return a <code>String</code> array of possible schedule dates.
	 */
	public String[] getPossibleSchedulesDates (final RouteModel routeModel, LocalDate today ) {
		//Create the list.
		List<LocalDate> myPossibleDates = new ArrayList<LocalDate>();
		//Go through all of the timetables and add them if they are not already in.
		TimetableModel[] timetableModels = timetableController.getRouteTimetables(routeModel);
		for (TimetableModel timeT : timetableModels) {
			//Now check if we have passed the valid to date.
			while ( !today.isAfter(timeT.getValidToDate()) ) {
				//Check if we have added this date before...
				if ( !myPossibleDates.contains(today) ) {
					//Finally check that at least one of the journey patterns has an operating service on this day.
					JourneyPatternModel[] journeyPatternModels = getJourneyPatternModels(timeT, routeModel.getRouteNumber());
					for ( JourneyPatternModel jpm : journeyPatternModels ) {
						if ( jpm.getDaysOfOperation().contains(today.getDayOfWeek()) ) {
							myPossibleDates.add(today);
							break;
						}
					}
				}
				today.plusDays(1);
			}
		}
		Collections.sort(myPossibleDates);
		String[] myDatesStr = new String[myPossibleDates.size()];
		logger.debug("MyCalDates length is " + myDatesStr.length);
		for ( int i = 0; i < myDatesStr.length; i++ ) {
			myDatesStr[i] = DateFormats.FULL_FORMAT.getFormat().format(myPossibleDates.get(i));
		}
		return myDatesStr;
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
