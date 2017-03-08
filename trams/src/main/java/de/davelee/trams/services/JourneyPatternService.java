package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.db.DatabaseManager;

public class JourneyPatternService {
	
	private DatabaseManager databaseManager;
	
	public JourneyPatternService() {
		
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
    
    public JourneyPattern getJourneyPatternById(long id) {
    	return databaseManager.getJourneyPatternById(id);
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

    public List<JourneyPattern> getJourneyPatterns ( long timetableId ) {
        return databaseManager.getJourneyPatternsByTimetableId(timetableId);
    }

}
