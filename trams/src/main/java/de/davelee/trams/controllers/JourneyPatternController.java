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

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.JourneyPatternService;

public class JourneyPatternController {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private TimetableController timetableController;

	private static final Logger logger = LoggerFactory.getLogger(JourneyPatternController.class);
	
	public JourneyPatternModel[] getJourneyPatternModels (final TimetableModel timetableModel ) {
		List<JourneyPattern> journeyPatterns = journeyPatternService.getJourneyPatternsByTimetableId(timetableController.getIdFromName(timetableModel.getName()));
		JourneyPatternModel[] journeyPatternModels = new JourneyPatternModel[journeyPatterns.size()];
		for ( int i = 0; i < journeyPatternModels.length; i++ ) {
			journeyPatternModels[i] = convertToJourneyPatternModel(journeyPatterns.get(i));
		}
		return journeyPatternModels;
	}
	
	public void createJourneyPattern ( final String name, final List<Integer> operatingDays, final String outgoingTerminus, 
			final String returnTerminus, final Calendar timeFrom, final Calendar timeTo, final int frequency, final int routeDuration,
			final TimetableModel timetableModel ) {
		journeyPatternService.createJourneyPattern(name, operatingDays, outgoingTerminus, returnTerminus, timeFrom, timeTo, 
				frequency, routeDuration, timetableController.getIdFromName(timetableModel.getName()));
	}

	public List<JourneyPattern> getAllJourneyPatterns ( ) {
		return journeyPatternService.getAllJourneyPatterns();
	}

	private JourneyPatternModel convertToJourneyPatternModel ( final JourneyPattern journeyPattern ) {
		JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
		journeyPatternModel.setDaysOfOperation(journeyPattern.getDaysOfOperation());
		journeyPatternModel.setDuration(journeyPattern.getRouteDuration());
		journeyPatternModel.setEndTime(journeyPattern.getEndTime());
		journeyPatternModel.setFrequency(journeyPattern.getFrequency());
		journeyPatternModel.setName(journeyPattern.getName());
		journeyPatternModel.setOutgoingTerminus(journeyPattern.getOutgoingTerminus());
		journeyPatternModel.setReturnTerminus(journeyPattern.getReturnTerminus());
		journeyPatternModel.setStartTime(journeyPattern.getStartTime());
		return journeyPatternModel;
	}

	/**
	 * Return a formatted String array of schedule dates from today.
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
					JourneyPatternModel[] journeyPatternModels = getJourneyPatternModels(timeT);
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
			myCalDates[i] = DateFormats.FULL_FORMAT.getFormat().format(myCalendar);
		}
		return myCalDates;
	}

}
