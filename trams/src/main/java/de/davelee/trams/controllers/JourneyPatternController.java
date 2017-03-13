package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.JourneyPatternService;

public class JourneyPatternController {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private TimetableController timetableController;
	
	public String[] getJourneyPatternNames ( final TimetableModel timetableModel ) {
		List<JourneyPattern> journeyPatterns = journeyPatternService.getJourneyPatternsByTimetableId(timetableController.getIdFromName(timetableModel.getName()));
		String[] journeyPatternNames = new String[journeyPatterns.size()];
		for ( int i = 0; i < journeyPatternNames.length; i++ ) {
			journeyPatternNames[i] = journeyPatterns.get(i).getName();
		}
		return journeyPatternNames;
	}
	
	public void createJourneyPattern ( final String name, final List<Integer> operatingDays, final String outgoingTerminus, 
			final String returnTerminus, final Calendar timeFrom, final Calendar timeTo, final int frequency, final int routeDuration,
			final TimetableModel timetableModel ) {
		journeyPatternService.createJourneyPattern(name, operatingDays, outgoingTerminus, returnTerminus, timeFrom, timeTo, 
				frequency, routeDuration, timetableController.getIdFromName(timetableModel.getName()));
	}

}
