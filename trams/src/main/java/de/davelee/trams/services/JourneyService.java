package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.Journey;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.data.Stop;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.JourneyStatus;
import de.davelee.trams.util.SortedJourneys;

public class JourneyService {

    private DatabaseManager databaseManager;
	
	public JourneyService() {
		
	}

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
	
	/**
     * Get the status of the journey based on the current time - either not yet run, running or finished.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>JourneyStatus</code> which has the current status of the journey.
     */
    public JourneyStatus checkJourneyStatus(Journey journey, Calendar currentTime) {
    	//Did the journey start?
        if ( checkTimeDiff(currentTime, journey.getJourneyStops().get(0).getStopTime() ) > -1 ) {
        	//Has the journey already ended?
        	if ( checkTimeDiff(currentTime, journey.getJourneyStops().get(journey.getJourneyStops().size()-1).getStopTime() ) > 0 ) {
                return JourneyStatus.FINISHED;
            }
            return JourneyStatus.RUNNING;
        }
        return JourneyStatus.YET_TO_RUN;
    }
    
    /**
     * Get the current stop name based on the current time.
     * @param currentTime a <code>Calendar</code> object. 
     * @return a <code>String</code> array with the current stop.
     */
    public String getCurrentStopName(List<Journey> journeyList, Calendar currentTime) {
        for (int i = 0; i < journeyList.size(); i++) {
            if (checkJourneyStatus(journeyList.get(i), currentTime) == JourneyStatus.RUNNING) {
                for (Stop myJourneyStop : journeyList.get(i).getJourneyStops()) {
                    if (checkTimeDiff(myJourneyStop.getStopTime(), currentTime) >= 0) {
                        //logger.debug("I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.");
                        return myJourneyStop.getStopName();
                        //return "I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.";
                    }
                }
                return null;
            }
            if (checkJourneyStatus(journeyList.get(i), currentTime) == JourneyStatus.YET_TO_RUN) {
                if ( journeyList.get(i).getId() != 1 ) {
                    return getStartTerminus(journeyList.get(i));
                }
            }
        }
        return "Depot";
    }
    
    /**
     * Get the start terminus of this journey.
     * @return a <code>String</code> with the start terminus.
     */
    public String getStartTerminus (Journey journey) {
        return journey.getJourneyStops().get(0).getStopName();
    }
    
    /**
     * Get stop object based on stop name.
     * @param name a <code>String</code> with the stop name.
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( long journeyId, String name ) {
        for ( Stop myStop : getJourneyById(journeyId).getJourneyStops() ) {
            if ( myStop.getStopName().equalsIgnoreCase(name) ) {
                return myStop;
            }
        }
        return null;
    }

    /**
     * Get the last stop.
     * @return a <code>Stop</code> object representing the last stop in this journey.
     */
    public String getLastStopName ( List<Journey> journeyList, Calendar currentTime ) {
        for (int i = 0; i < journeyList.size(); i++) {
            if (checkJourneyStatus(journeyList.get(i), currentTime) == JourneyStatus.RUNNING) {
                return journeyList.get(i).getJourneyStops().get(journeyList.get(i).getJourneyStops().size()-1).getStopName();
            }
            else if (checkJourneyStatus(journeyList.get(i), currentTime) == JourneyStatus.YET_TO_RUN) {
                if ( journeyList.get(i).getId() != 1 ) {
                    return journeyList.get(i).getJourneyStops().get(journeyList.get(i).getJourneyStops().size()-1).getStopName();
                }
            }
        }
        return "Depot";
    }

    /**
     * Get the current journey running on this schedule based on the current date.
     * @param currentTime a <code>Calendar</code> object with current time.
     * @return a <code>Service</code> object.
     */
    public Journey getCurrentJourney ( List<Journey> journeyList, Calendar currentTime ) {
        for ( int i = 0; i < journeyList.size(); i++ ) {
            if ( checkJourneyStatus(journeyList.get(i), currentTime) == JourneyStatus.RUNNING) {
                //TODO: Clean up for loop.
                if (  i != (journeyList.size()-1) && checkJourneyStatus(journeyList.get(i+1), currentTime) == JourneyStatus.YET_TO_RUN )  {
                    return journeyList.get(i);
                }
                return journeyList.get(i);
            }
        }
        return null;
    }

    public Journey getNextJourney ( List<Journey> journeyList, Calendar currentTime ) {
        boolean returnNextJourney = false;
        for ( Journey myJourney : journeyList ) {
            if ( returnNextJourney ) {
                return myJourney;
            }
            if ( checkJourneyStatus(myJourney, currentTime) == JourneyStatus.RUNNING) {
                returnNextJourney = true;
            }
        }
        return null;
    }
    
    /**
     * Get the number of stops belonging to this journey.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( Journey journey ) {
        return journey.getJourneyStops().size();
    }
    
    /**
     * Get stop based on location.
     * @param pos a <code>int</code> with the location. 
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( long journeyId, int pos ) {
        return getJourneyById(journeyId).getJourneyStops().get(pos);
    }
    
    /**
     * Remove stops between two stops.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @param includeFirst a <code>boolean</code> which is true iff the first stop should be deleted.
     * @param includeLast a <code>boolean</code> which is true iff the second stop should be deleted.
     * @return a <code>long</code> with the amount of minutes saved.
     */
    public long removeStopsBetween ( Journey journey, String firstStop, String secondStop, boolean includeFirst, boolean includeLast ) {
        //Get long to represent time diff between the two stops for delay.
        long timeDiff = checkTimeDiff(getStop(journey.getId(), firstStop).getStopTime(), getStop(journey.getId(), secondStop).getStopTime());
        //Now remove stops between the two and possibly first and last as appropriate.
        boolean removeFlag = false;
        for ( Stop myStop : journey.getJourneyStops() ) {
            if ( myStop.getStopName().equalsIgnoreCase(secondStop) ) {
                if ( includeLast ) { journey.removeStop(myStop); }
                removeFlag = false;
            }
            if ( removeFlag ) {
                journey.removeStop(myStop);
            }
            if ( myStop.getStopName().equalsIgnoreCase(firstStop) ) {
                if ( includeFirst ) { journey.removeStop(myStop); }
                removeFlag = true;
            }
        }
        //Now return time difference.
        return timeDiff;
    }
    
    /**
     * Get the time difference between two stops.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @return a <code>long</code> with the time difference.
     */
    public long getStopTimeDifference ( Journey journey, String firstStop, String secondStop ) {
        return checkTimeDiff(getStop(journey.getId(), firstStop).getStopTime(), getStop(journey.getId(), secondStop).getStopTime());
    }
    
    /**
     * Check if this is an outward journey.
     * @param outwardStops a <code>LinkedList</code> with list of outward stops.
     * @return a <code>boolean</code> which is true iff this is an outward journey.
     */
    public boolean isOutwardJourney ( Journey journey, List<String> outwardStops ) {
        //First of all get the index of the first stop of this journey in outwardStops.
        int firstIndex = outwardStops.indexOf(journey.getJourneyStops().get(0).getStopName());
        //Now get the index of the second stop.
        int secondIndex = outwardStops.indexOf(journey.getJourneyStops().get(1).getStopName());
        //If the indexes are consecutive i.e. difference of 1 then it is an outward journey.
        if ( secondIndex - firstIndex == 1 ) { return true; }
        //Otherwise it is not.
        return false;
    }
    
    /**
     * Check the time diff between two calendar objects.
     * @param firstTime a <code>Calendar</code> object with the first time.
     * @param secondTime a <code>Calendar</code> object with the second time.
     * @return a <code>long</code> with the time difference.
     */
    private long checkTimeDiff(Calendar firstTime, Calendar secondTime) {
        //Store time diff.
        long timeDiff = 0;
        if ( firstTime.get(Calendar.AM_PM) == Calendar.AM && secondTime.get(Calendar.AM_PM) == Calendar.PM ) {
            timeDiff = -(12 * (60 * 60));
        }
        else if ( secondTime.get(Calendar.AM_PM) == Calendar.AM && firstTime.get(Calendar.AM_PM) == Calendar.PM ) {
            timeDiff = 12 *(60 * 60);
        }
        long diffHour = (firstTime.get(Calendar.HOUR) - secondTime.get(Calendar.HOUR) ) * (60 * 60);
        long diffMins = (firstTime.get(Calendar.MINUTE) - secondTime.get(Calendar.MINUTE)) * 60;
        long diffSecs = (firstTime.get(Calendar.SECOND) - secondTime.get(Calendar.SECOND));
        //Calculate timeDiff.
        timeDiff += diffHour + diffMins + diffSecs;
        return timeDiff;
    }
    
    /**
     * Get the maximum time difference between two stops. 
     * @param prevStopName a <code>String</code> with first stop.
     * @param thisStopName a <code>String</code> with second stop.
     * @return a <code>long</code> with the time difference.
     */
    public long getStopMaxTimeDiff ( Journey journey, String prevStopName, String thisStopName ) {
        try {
            return checkTimeDiff ( getStop(journey.getId(), prevStopName).getStopTime(), getStop(journey.getId(), thisStopName).getStopTime() );
        } catch ( NullPointerException npe ) {
            return Integer.MAX_VALUE;
        }
    }
    
    /**
     * Get date info based on Calendar object.
     * @param stopTime a <code>Calendar</code> object with stop time.
     * @return a <code>String</code> with a formatted time String.
     */
    public String getDateInfo ( Calendar stopTime ) {
    	return DateFormats.FULL_FORMAT.getFormat().format(stopTime.getTime());
    }

    public Journey getJourneyById(long id) {
        return databaseManager.getJourneyById(id);
    }

    public Journey createJourney ( HashMap<String, Calendar> stops, long routeScheduleId ) {
    	Journey journey = new Journey();
    	Iterator<String> stopKeysIterator = stops.keySet().iterator();
    	while ( stopKeysIterator.hasNext() ) {
    		Stop stop = new Stop();
    		stop.setStopName(stopKeysIterator.next()); stop.setStopTime(stops.get(stop.getStopName()));
    		journey.addStop(stop);
    	}
        journey.setRouteScheduleId(routeScheduleId);
    	return journey;
    }

    /**
     * Return all outgoing journeys for a particular day.
     */
    public List<Journey> getAllOutgoingJourneys ( List<RouteSchedule> schedules, List<Stop> stops, String day ) {
        //Initialise list to store the journeys.
        List<Journey> outgoingJourneys = new ArrayList<Journey>();
        //Get the route schedules for that day!
        for ( int h = 0;  h < schedules.size(); h++ ) {
            for ( int i = 0; i < databaseManager.getJourneysByRouteScheduleId(schedules.get(h).getId()).size(); i++ ) {
                Journey myJourney = databaseManager.getJourneysByRouteScheduleId(schedules.get(h).getId()).get(i);
                if ( isOutwardJourney(stops, myJourney.getJourneyStops().get(0).getStopName(), myJourney.getJourneyStops().get(1).getStopName()) ) {
                    outgoingJourneys.add(myJourney);
                }
            }
        }
        Collections.sort(outgoingJourneys, new SortedJourneys());
        return outgoingJourneys;
    }

    /**
     * This method checks using two stops if the service is an outward or inward service.
     * @return a <code>boolean</code> which is true iff the service is an outward service.
     */
    public boolean isOutwardJourney ( List<Stop> stops, String stop1, String stop2 ) {
        //Go through the stops - if we find the 1st one before the 2nd one - it is outward.
        //Otherwise it is inward.
        for ( int i = 0; i < stops.size(); i++ ) {
            if ( stops.get(i).getStopName().equalsIgnoreCase(stop1) ) {
                return true;
            }
            else if ( stops.get(i).getStopName().equalsIgnoreCase(stop2) ) {
                return false;
            }
        }
        return false;
    }

    /**
     * Return all return services for a particular day.
     */
    public List<Journey> getAllReturnJourneys (List<RouteSchedule> schedules, List<Stop> stops, String day ) {
        //Initialise list to store the journeys.
        List<Journey> returnServices = new ArrayList<Journey>();
        //Get the route schedules for that day!
        for ( int h = 0; h < schedules.size(); h++ ) {
            for ( int i = 0; i < databaseManager.getJourneysByRouteScheduleId(schedules.get(h).getId()).size(); i++ ) {
                Journey myJourney = databaseManager.getJourneysByRouteScheduleId(schedules.get(h).getId()).get(i);
                if ( !isOutwardJourney(stops, myJourney.getJourneyStops().get(0).getStopName(), myJourney.getJourneyStops().get(1).getStopName()) ) {
                    returnServices.add(myJourney);
                }
            }
        }
        Collections.sort(returnServices, new SortedJourneys());
        return returnServices;
    }

    public List<Journey> getJourneysByRouteScheduleId ( long routeScheduleId ) {
        return databaseManager.getJourneysByRouteScheduleId(routeScheduleId);
    }

}
