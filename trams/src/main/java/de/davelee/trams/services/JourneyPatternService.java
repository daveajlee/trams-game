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
	
	/**
     * Check if the supplied day is a day of operation for this journey pattern.
     * @param dayNum a <code>int</code> with the day number in the week.
     * @return a <code>boolean</code> which is true iff this is a day of operation.
     */
    public boolean isDayOfOperation ( List<Integer> daysOfOperation, int dayNum ) {
    	return daysOfOperation.contains(dayNum);
    }
    
    public JourneyPattern getJourneyPatternById(long id) {
    	return databaseManager.getJourneyPatternById(id);
    }
    
    public JourneyPattern createJourneyPattern ( String name, List<Integer> operatingDays, String outgoingTerminus, String returnTerminus,
    		Calendar timeFrom, Calendar timeTo, int frequency, int routeDuration ) {
    	JourneyPattern journeyPattern = new JourneyPattern();
        journeyPattern.setName(name);
        journeyPattern.setDaysOfOperation(operatingDays);
        journeyPattern.setOutgoingTerminus(outgoingTerminus);
        journeyPattern.setReturnTerminus(returnTerminus);
        journeyPattern.setStartTime(timeFrom);
        journeyPattern.setEndTime(timeTo);
        journeyPattern.setFrequency(frequency);
        journeyPattern.setRouteDuration(routeDuration);
        return journeyPattern;
    }

}
