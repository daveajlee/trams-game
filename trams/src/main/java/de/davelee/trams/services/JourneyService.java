package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.Journey;
import de.davelee.trams.data.Stop;
import de.davelee.trams.data.StopTime;
import de.davelee.trams.factory.ScenarioFactory;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.StopTimeModel;
import de.davelee.trams.repository.JourneyRepository;
import de.davelee.trams.repository.StopRepository;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.JourneyStatus;
import de.davelee.trams.util.TramsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyService {

    @Autowired
	private JourneyRepository journeyRepository;

    @Autowired
	private StopRepository stopRepository;

    @Autowired
	private ScenarioFactory scenarioFactory;
	
	/**
     * Get the status of the journey based on the current time - either not yet run, running or finished.
     * @param journeyModel a <code>JourneyModel</code> object with the journey details.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>JourneyStatus</code> which has the current status of the journey.
     */
    private JourneyStatus checkJourneyStatus(final JourneyModel journeyModel, final Calendar currentTime) {
    	//Did the journey start?
        if ( checkTimeDiff(currentTime, journeyModel.getStopTimeModelList().get(0).getTime() ) > -1 ) {
        	//Has the journey already ended?
            if ( checkTimeDiff(currentTime, journeyModel.getStopTimeModelList().get(journeyModel.getStopTimeModelList().size()-1).getTime() ) > 0 ) {
                return JourneyStatus.FINISHED;
            }
            return JourneyStatus.RUNNING;
        }
        return JourneyStatus.YET_TO_RUN;
    }
    
    /**
     * Get the current stop name based on the current time.
     * @param journeyModels a <code>JourneyModel</code> array with all available journeys.
     * @param currentTime a <code>Calendar</code> object. 
     * @return a <code>String</code> array with the current stop.
     */
    public String getCurrentStopName(final JourneyModel[] journeyModels, final Calendar currentTime) {
        for (int i = 0; i < journeyModels.length; i++) {
            if (checkJourneyStatus(journeyModels[i], currentTime) == JourneyStatus.RUNNING) {
                for ( StopTimeModel myJourneyStop : journeyModels[i].getStopTimeModelList() ) {
                    if ( checkTimeDiff(myJourneyStop.getTime(), currentTime) >= 0 ) {
                        //logger.debug("I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.");
                        return myJourneyStop.getStopName();
                        //return "I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.";
                    }
                }
                return null;
            }
            if (checkJourneyStatus(journeyModels[i], currentTime) == JourneyStatus.YET_TO_RUN) {
                if ( i == 0 ) {
                    return "Depot";
                } else {
                    return getStartTerminus(journeyModels[i]);
                }
            }
        }
        return "Depot";
    }
    
    /**
     * Get the start terminus of this journey.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @return a <code>String</code> with the start terminus.
     */
    public String getStartTerminus ( final JourneyModel journeyModel ) {
        return journeyModel.getStopTimeModelList().get(0).getStopName();
    }
    
    /**
     * Get stop object based on stop name.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @param name a <code>String</code> with the stop name.
     * @return a <code>Stop</code> object.
     */
    public StopTimeModel getStopTime ( final JourneyModel journeyModel, final String name ) {
        for ( StopTimeModel stopTimeModel : journeyModel.getStopTimeModelList()) {
            if ( stopTimeModel.getStopName().equalsIgnoreCase(name) ) {
                return stopTimeModel;
            }
        }
        return null;
    }

    private StopTimeModel convertToStopTimeModel ( final StopTime stopTime ) {
        StopTimeModel stopTimeModel = new StopTimeModel();
        stopTimeModel.setJourneyNumber(stopTime.getJourneyNumber());
        stopTimeModel.setStopName(stopTime.getStopName());
        stopTimeModel.setTime(stopTime.getTime());
        stopTimeModel.setRouteNumber(stopTime.getRouteNumber());
        stopTimeModel.setRouteScheduleNumber(stopTime.getRouteScheduleNumber());
        return stopTimeModel;
    }

    private StopTime convertToStopTime ( final StopTimeModel stopTimeModel ) {
        StopTime stopTime = new StopTime();
        stopTime.setJourneyNumber(stopTimeModel.getJourneyNumber());
        stopTime.setStopName(stopTimeModel.getStopName());
        stopTime.setTime(stopTimeModel.getTime());
        stopTime.setRouteNumber(stopTimeModel.getRouteNumber());
        stopTime.setRouteScheduleNumber(stopTimeModel.getRouteScheduleNumber());
        return stopTime;
    }

    /**
     * Get the last stop.
     * @param journeyModels a <code>JourneyModel</code> array with all available journeys.
     * @param currentTime a <code>Calendar</code> representing the current time.
     * @return a <code>Stop</code> object representing the last stop in this journey.
     */
    public String getLastStopName ( final JourneyModel[] journeyModels, final Calendar currentTime ) {
        for (int i = 0; i < journeyModels.length; i++) {
            if (checkJourneyStatus(journeyModels[i], currentTime) == JourneyStatus.RUNNING) {
                StopTimeModel stopTime = journeyModels[i].getStopTimeModelList().get(journeyModels[i].getStopTimeModelList().size()-1);
                return stopTime.getStopName();
            }
            else if (checkJourneyStatus(journeyModels[i], currentTime) == JourneyStatus.YET_TO_RUN) {
                if ( journeyModels[i].getJourneyNumber() != 1 ) {
                    StopTimeModel stopTime = journeyModels[i].getStopTimeModelList().get(journeyModels[i].getStopTimeModelList().size()-1);
                    return stopTime.getStopName();
                }
            }
        }
        return "Depot";
    }

    /**
     * Get the current journey running on this schedule based on the current date.
     * @param journeyModels a <code>JourneyModel</code> array with all available journeys.
     * @param currentTime a <code>Calendar</code> object with current time.
     * @return a <code>Service</code> object.
     */
    public JourneyModel getCurrentJourney ( final JourneyModel[] journeyModels, final Calendar currentTime ) {
        for ( int i = 0; i < journeyModels.length; i++ ) {
            if ( checkJourneyStatus(journeyModels[i], currentTime) == JourneyStatus.RUNNING) {
                return journeyModels[i];
            }
        }
        return null;
    }

    public JourneyModel getNextJourney ( final JourneyModel[] journeyModels, final Calendar currentTime ) {
        boolean returnNextJourney = false;
        for ( JourneyModel myJourneyModel : journeyModels ) {
            if ( returnNextJourney ) {
                return myJourneyModel;
            }
            if ( checkJourneyStatus(myJourneyModel, currentTime) == JourneyStatus.RUNNING) {
                returnNextJourney = true;
            }
        }
        return null;
    }
    
    /**
     * Get the number of stops belonging to this journey.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( final JourneyModel journeyModel ) {
        return journeyModel.getStopTimeModelList().size();
    }
    
    /**
     * Remove stops between two stops.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @param includeFirst a <code>boolean</code> which is true iff the first stop should be deleted.
     * @param includeLast a <code>boolean</code> which is true iff the second stop should be deleted.
     * @return a <code>long</code> with the amount of minutes saved.
     */
    public long removeStopsBetween ( final JourneyModel journeyModel, final String firstStop, final String secondStop, final boolean includeFirst, final boolean includeLast ) {
        //Get long to represent time diff between the two stops for delay.
        long timeDiff = checkTimeDiff(getStopTime(journeyModel, firstStop).getTime(), getStopTime(journeyModel, secondStop).getTime());
        //Now remove stops between the two and possibly first and last as appropriate.
        boolean removeFlag = false;
        for ( StopTimeModel myStop : journeyModel.getStopTimeModelList() ) {
            if ( myStop.getStopName().equalsIgnoreCase(secondStop) ) {
                if ( includeLast ) { journeyModel.removeStopTimeInList(secondStop); }
                removeFlag = false;
            }
            if ( removeFlag ) {
                journeyModel.removeStopTimeInList(secondStop);
            }
            if ( myStop.getStopName().equalsIgnoreCase(firstStop) ) {
                if ( includeFirst ) { journeyModel.removeStopTimeInList(secondStop); }
                removeFlag = true;
            }
        }
        //Now return time difference.
        return timeDiff;
    }
    
    /**
     * Get the time difference between two stops.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @return a <code>long</code> with the time difference.
     */
    public long getStopTimeDifference ( final JourneyModel journeyModel, final String firstStop, final String secondStop ) {
        return checkTimeDiff(getStopTime(journeyModel, firstStop).getTime(), getStopTime(journeyModel, secondStop).getTime());
    }
    
    /**
     * Check if this is an outward journey.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @param outwardStops a <code>LinkedList</code> with list of outward stops.
     * @return a <code>boolean</code> which is true iff this is an outward journey.
     */
    public boolean isOutwardJourney ( final JourneyModel journeyModel, final List<String> outwardStops ) {
        //First of all get the index of the first stop of this journey in outwardStops.
        int firstIndex = outwardStops.indexOf(journeyModel.getStopTimeModelList().get(0).getStopName());
        //Now get the index of the second stop.
        int secondIndex = outwardStops.indexOf(journeyModel.getStopTimeModelList().get(1).getStopName());
        //If the indexes are consecutive i.e. difference of 1 then it is an outward journey.
        return secondIndex - firstIndex == 1;
    }
    
    /**
     * Check the time diff between two calendar objects.
     * @param firstTime a <code>Calendar</code> object with the first time.
     * @param secondTime a <code>Calendar</code> object with the second time.
     * @return a <code>long</code> with the time difference.
     */
    private long checkTimeDiff(final Calendar firstTime, final Calendar secondTime) {
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
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @param prevStopName a <code>String</code> with first stop.
     * @param thisStopName a <code>String</code> with second stop.
     * @return a <code>long</code> with the time difference.
     */
    public long getStopMaxTimeDiff ( final JourneyModel journeyModel, final String prevStopName, final String thisStopName ) {
        try {
            return checkTimeDiff ( getStopTime(journeyModel, prevStopName).getTime(), getStopTime(journeyModel, thisStopName).getTime() );
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
        return journeyRepository.getOne(Long.valueOf(id));
    }

    public void saveJourney ( final JourneyModel journeyModel ) {
        journeyRepository.save(convertToJourney(journeyModel));
    }

    private Journey convertToJourney ( final JourneyModel journeyModel ) {
    	Journey journey = new Journey();
        journey.setJourneyNumber(journeyModel.getJourneyNumber());
        journey.setRouteNumber(journeyModel.getRouteNumber());
        journey.setRouteScheduleNumber(journeyModel.getRouteScheduleNumber());
        List<StopTimeModel> stopTimeModelList = journeyModel.getStopTimeModelList();
        List<StopTime> stopTimes = new ArrayList<StopTime>();
        for ( StopTimeModel stopTimeModel : stopTimeModelList ) {
            stopTimes.add(convertToStopTime(stopTimeModel));
        }
        journey.setStopTimes(stopTimes);
    	return journey;
    }

    /**
     * Return all outgoing journeys for a particular day.
     * @param routeScheduleModels a <code>RouteScheduleModel</code> array with all available route schedules.
     * @param stops a <code>String</code> list with the stops to be served.
     * @param day a <code>String</code> with the day to get the outgoing journeys for.
     * @return a <code>JourneyModel</code> list with all outgoing journeys.
     */
    public JourneyModel[] getAllOutgoingJourneys (final RouteScheduleModel[] routeScheduleModels, final List<String> stops, final String day ) {
        //Initialise list to store the journeys.
        List<Journey> outgoingJourneys = new ArrayList<Journey>();
        //Get the route schedules for that day!
        for ( int h = 0;  h < routeScheduleModels.length; h++ ) {
            for ( int i = 0; i < journeyRepository.findByRouteScheduleNumberAndRouteNumber(routeScheduleModels[h].getScheduleNumber(), routeScheduleModels[h].getRouteNumber()).size(); i++ ) {
                Journey myJourney = journeyRepository.findByRouteScheduleNumberAndRouteNumber(routeScheduleModels[h].getScheduleNumber(), routeScheduleModels[h].getRouteNumber()).get(i);
                if ( isOutwardJourney(stops,
                        myJourney.getStopTimes().get(0).getStopName(),
                        myJourney.getStopTimes().get(1).getStopName()) ) {
                    outgoingJourneys.add(myJourney);
                }
            }
        }
        JourneyModel[] journeyModels = new JourneyModel[outgoingJourneys.size()];
        for ( int i = 0; i < journeyModels.length; i++ ) {
            journeyModels[i] = convertToJourneyModel(outgoingJourneys.get(i));
        }
        return journeyModels;
    }

    /**
     * This method checks using two stops if the service is an outward or inward service.
     * @param stops a <code>String</code> list with the stops to be served.
     * @param stop1 a <code>String</code> containing the name of a stop.
     * @param stop2 a <code>String</code> containing the name of another stop.
     * @return a <code>boolean</code> which is true iff the service is an outward service.
     */
    public boolean isOutwardJourney ( final List<String> stops, final String stop1, final String stop2 ) {
        //Go through the stops - if we find the 1st one before the 2nd one - it is outward.
        //Otherwise it is inward.
        for ( int i = 0; i < stops.size(); i++ ) {
            if ( stops.get(i).equalsIgnoreCase(stop1) ) {
                return true;
            }
            else if ( stops.get(i).equalsIgnoreCase(stop2) ) {
                return false;
            }
        }
        return false;
    }

    /**
     * Return all return services for a particular day.
     * @param scheduleModels a <code>RouteScheduleModel</code> array with all available route schedules.
     * @param stops a <code>String</code> list with the stops to be served.
     * @param day a <code>String</code> with the day to get the outgoing journeys for.
     * @return a <code>JourneyModel</code> list with all return journeys.
     */
    public JourneyModel[] getAllReturnJourneys ( final RouteScheduleModel[] scheduleModels, final List<String> stops, final String day ) {
        //Initialise list to store the journeys.
        List<Journey> returnServices = new ArrayList<Journey>();
        //Get the route schedules for that day!
        for ( int h = 0; h < scheduleModels.length; h++ ) {
            for ( int i = 0; i < journeyRepository.findByRouteScheduleNumberAndRouteNumber(scheduleModels[h].getScheduleNumber(), scheduleModels[h].getRouteNumber()).size(); i++ ) {
                Journey myJourney = journeyRepository.findByRouteScheduleNumberAndRouteNumber(scheduleModels[h].getScheduleNumber(), scheduleModels[h].getRouteNumber()).get(i);
                if ( !isOutwardJourney(stops,
                        myJourney.getStopTimes().get(0).getStopName(),
                        myJourney.getStopTimes().get(1).getStopName()) ) {
                    returnServices.add(myJourney);
                }
            }
        }
        JourneyModel[] journeyModels = new JourneyModel[returnServices.size()];
        for ( int i = 0; i < journeyModels.length; i++ ) {
            journeyModels[i] = convertToJourneyModel(returnServices.get(i));
        }
        return journeyModels;
    }

    public JourneyModel[] getJourneysByRouteScheduleNumberAndRouteNumber ( final int routeScheduleNumber, final String routeNumber ) {
        List<Journey> journeys = journeyRepository.findByRouteScheduleNumberAndRouteNumber(routeScheduleNumber, routeNumber);
        JourneyModel[] journeyModels = new JourneyModel[journeys.size()];
        for ( int i = 0; i < journeyModels.length; i++ ) {
            journeyModels[i] = convertToJourneyModel(journeys.get(i));
        }
        return journeyModels;
    }

    public void saveStopTime ( final StopTimeModel stopTimeModel, final JourneyModel journeyModel) {
        StopTime stopTime = new StopTime();
        stopTime.setJourneyNumber(stopTimeModel.getJourneyNumber());
        stopTime.setStopName(stopTimeModel.getStopName());
        stopTime.setTime(stopTimeModel.getTime());
        stopTime.setRouteNumber(stopTimeModel.getRouteNumber());
        stopTime.setRouteScheduleNumber(stopTimeModel.getRouteScheduleNumber());
        Journey journey = journeyRepository.findByJourneyNumberAndRouteScheduleNumberAndRouteNumber(journeyModel.getJourneyNumber(), journeyModel.getRouteScheduleNumber(), journeyModel.getRouteNumber());
        journey.addStopTimeToList(stopTime);
        journeyRepository.save(journey);
    }

    public void saveStop ( final String stopName ) {
        Stop stop = new Stop();
        stop.setStopName(stopName);
        stopRepository.saveAndFlush(stop);
    }

    public Stop getStopByStopName ( final String stopName ) {
        return stopRepository.findByStopName(stopName);
    }

    public JourneyModel[] getAllJourneys() {
        List<Journey> journeys = journeyRepository.findAll();
        JourneyModel[] journeyModels = new JourneyModel[journeys.size()];
        for ( int i = 0; i < journeyModels.length; i++ ) {
            journeyModels[i] = convertToJourneyModel(journeys.get(i));
        }
        return journeyModels;
    }

    public String[] getAllStops() {
        List<Stop> stops = stopRepository.findAll();
        String[] stopNames = new String[stops.size()];
        for ( int i = 0; i < stopNames.length; i++ ) {
            stopNames[i] = stops.get(i).getStopName();
        }
        return stopNames;
    }

    public List<JourneyModel> generateJourneyTimetables ( final JourneyPatternModel[] journeyPatternModels,
                                                          final Calendar today, final int direction, final List<String> stops, final String scenarioName, final int journeyNumberToStart ) {
        int routeScheduleNumber = 0;
        if ( direction == TramsConstants.RETURN_DIRECTION ) { routeScheduleNumber = 1; }
        //Create a list to store journeys.
        List<JourneyModel> allJourneys = new ArrayList<JourneyModel>();
        //Now we need to go through the journey patterns.
        for ( JourneyPatternModel myJourneyPattern : journeyPatternModels ) {
            int journeyNumber = journeyNumberToStart;
            //Clone the time so that we can add to it but keep it the same for next iteration.
            Calendar myTime = (Calendar) today.clone();
            //If this service pattern is not valid for this date then don't bother.
            if ( !myJourneyPattern.getDaysOfOperation().contains(myTime.get(Calendar.DAY_OF_WEEK))) { continue; }
            //Set myTime hour and minute to the start time of the service pattern.
            myTime.set(Calendar.HOUR_OF_DAY, myJourneyPattern.getStartTime().get(Calendar.HOUR_OF_DAY));
            myTime.set(Calendar.MINUTE, myJourneyPattern.getStartTime().get(Calendar.MINUTE));
            int diffDurationFreq = myJourneyPattern.getDuration() % myJourneyPattern.getFrequency();
            if ( direction == TramsConstants.RETURN_DIRECTION && diffDurationFreq <= (myJourneyPattern.getFrequency()/2)) {
                myTime.add(Calendar.MINUTE, myJourneyPattern.getFrequency()/2);
            }
            //logger.debug("End time is " + myJourneyPattern.getEndTime().get(Calendar.HOUR_OF_DAY) + ":" + myJourneyPattern.getEndTime().get(Calendar.MINUTE) );
            //Now repeat this loop until myTime is after the journey pattern end time.
            while ( true ) {
                if ( (myTime.get(Calendar.HOUR_OF_DAY) > myJourneyPattern.getEndTime().get(Calendar.HOUR_OF_DAY)) ) { break; }
                else if ( (myTime.get(Calendar.HOUR_OF_DAY) == myJourneyPattern.getEndTime().get(Calendar.HOUR_OF_DAY)) && (myTime.get(Calendar.MINUTE) > myJourneyPattern.getEndTime().get(Calendar.MINUTE)) ) { break; }
                else {
                    //logger.debug("I want a journey starting from both terminuses at " + myTime.get(Calendar.HOUR_OF_DAY) + ":" + myTime.get(Calendar.MINUTE));
                    //Create an outgoing service.
                    JourneyModel newJourney = new JourneyModel();
                    newJourney.setRouteNumber(myJourneyPattern.getRouteNumber());
                    newJourney.setRouteScheduleNumber(routeScheduleNumber);
                    newJourney.setJourneyNumber(journeyNumber);
                    //Add stops - we also need to create a separate calendar to ensure we don't advance more than we want!!!!
                    Calendar journeyTime = (Calendar) myTime.clone();
                    List<String> journeyStops = new ArrayList<String>();
                    if ( direction == TramsConstants.OUTWARD_DIRECTION ) {
                        journeyStops = getStopsBetween(stops, myJourneyPattern.getOutgoingTerminus(), myJourneyPattern.getReturnTerminus(), direction);
                    }
                    else {
                        journeyStops = getStopsBetween(stops, myJourneyPattern.getReturnTerminus(), myJourneyPattern.getOutgoingTerminus(), direction);
                    }
                    StopTimeModel newStopTime = new StopTimeModel();
                    newStopTime.setJourneyNumber(newJourney.getJourneyNumber());
                    newStopTime.setStopName(journeyStops.get(0));
                    newStopTime.setTime( (Calendar) journeyTime.clone());
                    newStopTime.setRouteNumber(myJourneyPattern.getRouteNumber());
                    newStopTime.setRouteScheduleNumber(routeScheduleNumber);
                    newJourney.addStopTimeToList(newStopTime);
                    for ( int i = 1; i < journeyStops.size(); i++ ) {
                        //Now add to journey time the difference between the two stops.
                        journeyTime.add(Calendar.MINUTE, getDistance(scenarioName, journeyStops.get(i-1), journeyStops.get(i)));
                        //Create stop.
                        StopTimeModel newStopTime2 = new StopTimeModel();
                        newStopTime2.setJourneyNumber(newJourney.getJourneyNumber());
                        newStopTime2.setStopName(journeyStops.get(i));
                        newStopTime2.setTime( (Calendar) journeyTime.clone());
                        newStopTime2.setRouteNumber(myJourneyPattern.getRouteNumber());
                        newStopTime2.setRouteScheduleNumber(routeScheduleNumber);
                        newJourney.addStopTimeToList(newStopTime2);
                    }
                    //logger.debug("Service #" + serviceId + ": " + newService.getAllDisplayStops());{
                    saveJourney(newJourney);
                    allJourneys.add(newJourney);
                    //Increment calendar.
                    myTime.add(Calendar.MINUTE, myJourneyPattern.getFrequency());
                }
                journeyNumber++;
            }
            routeScheduleNumber+=2;
        }
        return allJourneys;
    }

    private JourneyModel convertToJourneyModel ( final Journey journey ) {
        JourneyModel journeyModel = new JourneyModel();
        journeyModel.setJourneyNumber(journey.getJourneyNumber());
        journeyModel.setRouteNumber(journey.getRouteNumber());
        journeyModel.setRouteScheduleNumber(journey.getRouteScheduleNumber());
        List<StopTime> stopTimes = journey.getStopTimes();
        List<StopTimeModel> stopTimeModels = new ArrayList<StopTimeModel>();
        for ( StopTime stopTime : stopTimes ) {
            stopTimeModels.add(convertToStopTimeModel(stopTime));
        }
        journeyModel.setStopTimeModelList(stopTimeModels);
        return journeyModel;
    }

    private List<String> getStopsBetween ( final List<String> stops, final String startStop, final String endStop, final int direction ) {
        List<String> relevantStops = new ArrayList<String>();
        if ( direction == TramsConstants.OUTWARD_DIRECTION ) {
            boolean foundStartStop = false;
            for ( int i = 0; i < stops.size(); i++ ) {
                if ( !foundStartStop & stops.get(i).contentEquals(startStop) ) {
                    foundStartStop = true;
                    relevantStops.add(stops.get(i));
                }
                else if ( foundStartStop & !stops.get(i).contentEquals(endStop)) {
                    relevantStops.add(stops.get(i));
                }
                else if ( foundStartStop & stops.get(i).contentEquals(endStop)) {
                    relevantStops.add(stops.get(i));
                    break;
                }
            }
        }
        else if ( direction == TramsConstants.RETURN_DIRECTION ) {
            boolean foundStartStop = false;
            for ( int i = stops.size()-1; i > -1; i-- ) {
                if ( !foundStartStop & stops.get(i).contentEquals(startStop) ) {
                    foundStartStop = true;
                    relevantStops.add(stops.get(i));
                }
                else if ( foundStartStop & !stops.get(i).contentEquals(endStop)) {
                    relevantStops.add(stops.get(i));
                }
                else if ( foundStartStop & stops.get(i).contentEquals(endStop)) {
                    relevantStops.add(stops.get(i));
                    break;
                }
            }
        }
        return relevantStops;
    }

    /**
     * Get the distance between two stops.
     * @param scenarioName a <code>String</code> with the name of the scenario.
     * @param stop1 a <code>String</code> with the name of the first stop.
     * @param stop2 a <code>String</code> with the name of the second stop.
     * @return a <code>int</code> with the distance between two stops.
     */
    public int getDistance ( final String scenarioName, final String stop1, final String stop2 ) {
        int stop1Pos = -1; int stop2Pos = -1; int count = 0;
        List<String> stopDistanceList = scenarioFactory.createScenarioByName(scenarioName).getStopDistances();
        for ( String stopDistance : stopDistanceList ) {
            String stopName = stopDistance.split(":")[0];
            if ( stopName.equalsIgnoreCase(stop1) ) { stop1Pos = count; }
            else if ( stopName.equalsIgnoreCase(stop2) ) { stop2Pos = count; }
            count++;
        }
        return Integer.parseInt(stopDistanceList.get(stop1Pos).split(":")[1].split(",")[stop2Pos]);
    }

    public void assignRouteAndRouteSchedule ( final JourneyModel journeyModel, final RouteScheduleModel routeScheduleModel ) {
        if ( journeyRepository.findByJourneyNumberAndRouteScheduleNumberAndRouteNumber(journeyModel.getJourneyNumber(),
                routeScheduleModel.getScheduleNumber(), routeScheduleModel.getRouteNumber()) == null) {
            journeyModel.setRouteNumber(routeScheduleModel.getRouteNumber());
            journeyModel.setRouteScheduleNumber(routeScheduleModel.getScheduleNumber());
            saveJourney(journeyModel);
        }
    }

    /**
     * Remove all existing journeys (used only for the load function)
     */
    public void deleteAllJourneys ( ) {
        journeyRepository.deleteAll();
    }

    /**
     * Remove all existing stops (used only for the load function)
     */
    public void deleteAllStops ( ) {
        stopRepository.deleteAll();
    }

}
