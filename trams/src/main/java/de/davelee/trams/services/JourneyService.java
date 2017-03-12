package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.Journey;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.data.Stop;
import de.davelee.trams.data.StopTime;
import de.davelee.trams.repository.JourneyRepository;
import de.davelee.trams.repository.StopRepository;
import de.davelee.trams.repository.StopTimeRepository;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.JourneyStatus;
import de.davelee.trams.util.SortedJourneys;
import org.springframework.beans.factory.annotation.Autowired;

public class JourneyService {

    @Autowired
	private JourneyRepository journeyRepository;

    @Autowired
	private StopRepository stopRepository;

    @Autowired
	private StopTimeRepository stopTimeRepository;
	
	public JourneyService() {
		
	}
	
	/**
     * Get the status of the journey based on the current time - either not yet run, running or finished.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>JourneyStatus</code> which has the current status of the journey.
     */
    public JourneyStatus checkJourneyStatus(Journey journey, Calendar currentTime) {
    	//Did the journey start?
        if ( checkTimeDiff(currentTime, getStopTimesByJourneyId(journey.getId()).get(0).getTime() ) > -1 ) {
        	//Has the journey already ended?
            if ( checkTimeDiff(currentTime, getStopTimesByJourneyId(journey.getId()).get(getStopTimesByJourneyId(journey.getId()).size()-1).getTime() ) > 0 ) {
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
                for ( StopTime myJourneyStop : getStopTimesByJourneyId(journeyList.get(i).getId()) ) {
                    if ( checkTimeDiff(myJourneyStop.getTime(), currentTime) >= 0 ) {
                        //logger.debug("I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.");
                        return stopRepository.findOne(myJourneyStop.getStopId()).getStopName();
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
        return stopRepository.findOne(getStopTimesByJourneyId(journey.getId()).get(0).getStopId()).getStopName();
    }
    
    /**
     * Get stop object based on stop name.
     * @param name a <code>String</code> with the stop name.
     * @return a <code>Stop</code> object.
     */
    public StopTime getStopTime ( long journeyId, String name ) {
        for ( StopTime myStop : getStopTimesByJourneyId(journeyId) ) {
            if ( stopRepository.findOne(myStop.getStopId()).getStopName().equalsIgnoreCase(name) ) {
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
                StopTime stopTime = getStopTimesByJourneyId(journeyList.get(i).getId()).get(getStopTimesByJourneyId(journeyList.get(i).getId()).size()-1);
                return stopRepository.findOne(stopTime.getStopId()).getStopName();
            }
            else if (checkJourneyStatus(journeyList.get(i), currentTime) == JourneyStatus.YET_TO_RUN) {
                if ( journeyList.get(i).getId() != 1 ) {
                    StopTime stopTime = getStopTimesByJourneyId(journeyList.get(i).getId()).get(getStopTimesByJourneyId(journeyList.get(i).getId()).size()-1);
                    return stopRepository.findOne(stopTime.getStopId()).getStopName();
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
        return getStopTimesByJourneyId(journey.getId()).size();
    }
    
    /**
     * Get stop based on location.
     * @param pos a <code>int</code> with the location. 
     * @return a <code>Stop</code> object.
     */
    public StopTime getStopTime ( long journeyId, int pos ) {
        return getStopTimesByJourneyId(journeyId).get(pos);
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
        long timeDiff = checkTimeDiff(getStopTime(journey.getId(), firstStop).getTime(), getStopTime(journey.getId(), secondStop).getTime());
        //Now remove stops between the two and possibly first and last as appropriate.
        boolean removeFlag = false;
        for ( StopTime myStop : getStopTimesByJourneyId(journey.getId()) ) {
            if ( stopRepository.findOne(myStop.getStopId()).getStopName().equalsIgnoreCase(secondStop) ) {
                if ( includeLast ) { stopTimeRepository.delete(myStop); }
                removeFlag = false;
            }
            if ( removeFlag ) {
                stopTimeRepository.delete(myStop);
            }
            if ( stopRepository.findOne(myStop.getStopId()).getStopName().equalsIgnoreCase(firstStop) ) {
                if ( includeFirst ) { stopTimeRepository.delete(myStop); }
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
        return checkTimeDiff(getStopTime(journey.getId(), firstStop).getTime(), getStopTime(journey.getId(), secondStop).getTime());
    }
    
    /**
     * Check if this is an outward journey.
     * @param outwardStops a <code>LinkedList</code> with list of outward stops.
     * @return a <code>boolean</code> which is true iff this is an outward journey.
     */
    public boolean isOutwardJourney ( Journey journey, List<String> outwardStops ) {
        //First of all get the index of the first stop of this journey in outwardStops.
        int firstIndex = outwardStops.indexOf(stopRepository.findOne(getStopTimesByJourneyId(journey.getId()).get(0).getStopId()).getStopName());
        //Now get the index of the second stop.
        int secondIndex = outwardStops.indexOf(stopRepository.findOne(getStopTimesByJourneyId(journey.getId()).get(1).getStopId()).getStopName());
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
            return checkTimeDiff ( getStopTime(journey.getId(), prevStopName).getTime(), getStopTime(journey.getId(), thisStopName).getTime() );
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
        return journeyRepository.findOne(id);
    }

    public Journey createJourney ( long routeScheduleId ) {
    	Journey journey = new Journey();
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
            for ( int i = 0; i < journeyRepository.findByRouteScheduleId(schedules.get(h).getId()).size(); i++ ) {
                Journey myJourney = journeyRepository.findByRouteScheduleId(schedules.get(h).getId()).get(i);
                if ( isOutwardJourney(stops,
                        stopRepository.findOne(getStopTimesByJourneyId(myJourney.getId()).get(0).getId()).getStopName(),
                        stopRepository.findOne(getStopTimesByJourneyId(myJourney.getId()).get(1).getId()).getStopName()) ) {
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
            for ( int i = 0; i < journeyRepository.findByRouteScheduleId(schedules.get(h).getId()).size(); i++ ) {
                Journey myJourney = journeyRepository.findByRouteScheduleId(schedules.get(h).getId()).get(i);
                if ( !isOutwardJourney(stops,
                        stopRepository.findOne(getStopTimesByJourneyId(myJourney.getId()).get(0).getId()).getStopName(),
                        stopRepository.findOne(getStopTimesByJourneyId(myJourney.getId()).get(1).getId()).getStopName()) ) {
                    returnServices.add(myJourney);
                }
            }
        }
        Collections.sort(returnServices, new SortedJourneys());
        return returnServices;
    }

    public List<Journey> getJourneysByRouteScheduleId ( long routeScheduleId ) {
        return journeyRepository.findByRouteScheduleId(routeScheduleId);
    }

    /**
     * Get the stop time as hh:mm.
     * @return a <code>String</code> with the time as hh:mm.
     */
    public String getDisplayStopTime( Calendar stopTime ) {
        return DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(stopTime.getTime());
    }

    public Stop createStop ( final String stopName ) {
        Stop stop = new Stop();
        stop.setStopName(stopName);
        return stop;
    }

    public StopTime createStopTime ( final long stopId, final long journeyId, final Calendar time ) {
        StopTime stopTime = new StopTime();
        stopTime.setJourneyId(journeyId);
        stopTime.setStopId(stopId);
        stopTime.setTime(time);
        return stopTime;
    }

    public List<StopTime> getStopTimesByJourneyId ( final long journeyId ) {
        return stopTimeRepository.findByJourneyId(journeyId);
    }

    public String getStopNameByStopId ( final long stopId ) {
        return stopRepository.findOne(stopId).getStopName();
    }

    public Stop getStopByStopName ( final String stopName ) {
        return stopRepository.findByStopName(stopName).get(0);
    }

    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    public List<StopTime> getAllStopTimes() {
        return stopTimeRepository.findAll();
    }

}
