package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.repository.JourneyPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class JourneyPatternService {

    @Autowired
    private JourneyPatternRepository journeyPatternRepository;
	
	public JourneyPatternService() {
		
	}
    
    public JourneyPattern getJourneyPatternById(long id) {
    	return journeyPatternRepository.findOne(id);
    }
    
    public JourneyPattern createJourneyPattern ( String name, List<Integer> operatingDays, String outgoingTerminus, String returnTerminus,
    		Calendar timeFrom, Calendar timeTo, int frequency, int routeDuration, String timetableName, String routeNumber ) {
    	JourneyPattern journeyPattern = new JourneyPattern();
        journeyPattern.setName(name);
        journeyPattern.setDaysOfOperation(operatingDays);
        journeyPattern.setOutgoingTerminus(outgoingTerminus);
        journeyPattern.setReturnTerminus(returnTerminus);
        journeyPattern.setStartTime(timeFrom);
        journeyPattern.setEndTime(timeTo);
        journeyPattern.setFrequency(frequency);
        journeyPattern.setRouteDuration(routeDuration);
        journeyPattern.setTimetableName(timetableName);
        journeyPattern.setRouteNumber(routeNumber);
        return journeyPattern;
    }

    public List<JourneyPattern> getAllJourneyPatterns() {
        return journeyPatternRepository.findAll();
    }

    public List<JourneyPattern> getJourneyPatternsByTimetableNameAndRouteNumber ( final String timetableName, final String routeNumber ) {
        return journeyPatternRepository.findByTimetableNameAndRouteNumber(timetableName, routeNumber);
    }

}
