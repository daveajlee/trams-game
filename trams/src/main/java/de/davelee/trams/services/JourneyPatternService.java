package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.dao.JourneyPatternDao;
import de.davelee.trams.data.JourneyPattern;

public class JourneyPatternService {

    private JourneyPatternDao journeyPatternDao;
	
	public JourneyPatternService() {
		
	}

    public JourneyPatternDao getJourneyPatternDao() {
	    return journeyPatternDao;
    }

    public void setJourneyPatternDao(JourneyPatternDao journeyPatternDao) {
        this.journeyPatternDao = journeyPatternDao;
    }
    
    public JourneyPattern getJourneyPatternById(long id) {
    	return journeyPatternDao.getJourneyPatternById(id);
    }
    
    public JourneyPattern createJourneyPattern ( String name, List<Integer> operatingDays, String outgoingTerminus, String returnTerminus,
    		Calendar timeFrom, Calendar timeTo, int frequency, int routeDuration, long timetableId ) {
    	JourneyPattern journeyPattern = new JourneyPattern();
        journeyPattern.setName(name);
        journeyPattern.setDaysOfOperation(operatingDays);
        journeyPattern.setOutgoingTerminus(outgoingTerminus);
        journeyPattern.setReturnTerminus(returnTerminus);
        journeyPattern.setStartTime(timeFrom);
        journeyPattern.setEndTime(timeTo);
        journeyPattern.setFrequency(frequency);
        journeyPattern.setRouteDuration(routeDuration);
        journeyPattern.setTimetableId(timetableId);
        return journeyPattern;
    }

    public List<JourneyPattern> getAllJourneyPatterns() {
        return journeyPatternDao.getAllJourneyPatterns();
    }

    public List<JourneyPattern> getJourneyPatternsByTimetableId ( long timetableId ) {
        return journeyPatternDao.getJourneyPatternsByTimetableId(timetableId);
    }

}
