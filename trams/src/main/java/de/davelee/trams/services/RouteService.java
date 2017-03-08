package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import de.davelee.trams.factory.ScenarioFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davelee.trams.data.Journey;
import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.data.Route;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.data.Stop;
import de.davelee.trams.data.Timetable;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.SortedJourneys;
import de.davelee.trams.util.SortedRoutes;

public class RouteService {
	
	private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    private ScenarioFactory scenarioFactory;
    
    private DatabaseManager databaseManager;
    
    public static int OUTWARD_DIRECTION = 0;
    public static int RETURN_DIRECTION = 1;
	
	public RouteService() {
	}
	
    public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

    public ScenarioFactory getScenarioFactory() {
        return scenarioFactory;
    }

    public void setScenarioFactory(ScenarioFactory scenarioFactory) {
        this.scenarioFactory = scenarioFactory;
    }

	/**
     * This method generates the route timetables for a particular day - it is a very important method.
     * @param today a <code>Calendar</code> object with today's date.
     */
    public long[] generateJourneyTimetables ( long routeId, Calendar today, String scenarioName, int direction ) {
        logger.debug("I'm generating timetable for routeId " + routeId + " for " + DateFormats.DAY_MONTH_YEAR_FORMAT.getFormat().format(today.getTime()));
        //First of all, get the current timetable.
        Timetable currentTimetable = getCurrentTimetable(routeId, today);
        //Create a list to store journeys.
        List<Journey> allJourneys = new ArrayList<Journey>();
        //Now we need to go through the journey patterns.
        List<JourneyPattern> journeyPatterns = databaseManager.getJourneyPatternsByTimetableId(currentTimetable.getId());
        for ( JourneyPattern myJourneyPattern : journeyPatterns ) {
            //Clone the time so that we can add to it but keep it the same for next iteration.
            Calendar myTime = (Calendar) today.clone();
            //If this service pattern is not valid for this date then don't bother.
            if ( myJourneyPattern.getDaysOfOperation().contains(myTime.get(Calendar.DAY_OF_WEEK))) { continue; }
            //Set myTime hour and minute to the start time of the service pattern.
            myTime.set(Calendar.HOUR_OF_DAY, myJourneyPattern.getStartTime().get(Calendar.HOUR_OF_DAY));
            myTime.set(Calendar.MINUTE, myJourneyPattern.getStartTime().get(Calendar.MINUTE));
            int diffDurationFreq = myJourneyPattern.getRouteDuration() % myJourneyPattern.getFrequency();
            if ( direction == RETURN_DIRECTION && diffDurationFreq <= (myJourneyPattern.getFrequency()/2)) {
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
                    Journey newJourney = new Journey();
                    //Add stops - we also need to create a separate calendar to ensure we don't advance more than we want!!!!
                    Calendar journeyTime = (Calendar) myTime.clone();
                    List<Stop> journeyStops = new ArrayList<Stop>();
                    if ( direction == OUTWARD_DIRECTION ) {
                        journeyStops = getStopsBetween(getRouteById(routeId), myJourneyPattern.getReturnTerminus(), myJourneyPattern.getOutgoingTerminus(), direction);
                    }
                    else {
                        journeyStops = getStopsBetween(getRouteById(routeId), myJourneyPattern.getOutgoingTerminus(), myJourneyPattern.getReturnTerminus(), direction);
                    }
                    Stop newStop = new Stop();
                    newStop.setStopName(journeyStops.get(0).getStopName());
                    newStop.setStopTime( (Calendar) journeyTime.clone());
                    for ( int i = 1; i < journeyStops.size(); i++ ) {
                        //Now add to journey time the difference between the two stops.
                        journeyTime.add(Calendar.MINUTE, getDistance(scenarioName, journeyStops.get(i-1).getStopName(), journeyStops.get(i).getStopName()));
                        //Create stop.
                        Stop newStop2 = new Stop();
                        newStop2.setStopName(journeyStops.get(i).getStopName());
                        newStop2.setStopTime( (Calendar) journeyTime.clone());
                    }
                    //logger.debug("Service #" + serviceId + ": " + newService.getAllDisplayStops());
                    if ( !isDuplicateJourney(allJourneys, newJourney) ) {
                        allJourneys.add(newJourney);
                    }
                    //Increment calendar.
                    myTime.add(Calendar.MINUTE, myJourneyPattern.getFrequency());
                }
            }
        }
        //Sort all journeys.
        Collections.sort(allJourneys, new SortedJourneys());
        //Return the journeys.
        long[] journeyIds = new long[allJourneys.size()];
        for ( int i = 0; i < allJourneys.size(); i++ ) {
        	journeyIds[i] = allJourneys.get(i).getId();
        }
        return journeyIds;
    }

    /**
     * Get the distance between two stops.
     * @param stop1 a <code>String</code> with the name of the first stop.
     * @param stop2 a <code>String</code> with the name of the second stop.
     * @return a <code>int</code> with the distance between two stops.
     */
    public int getDistance ( String scenarioName, String stop1, String stop2 ) {
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

    /**
     * Check for duplicate journeys.
     * @param allJourneys a <code>LinkedList</code> with all journeys.
     * @param newJourney a <code>Journey</code> with the new journey.
     */
    public boolean isDuplicateJourney ( List<Journey> allJourneys, Journey newJourney ) {
        //Go through all journey and see if one equals it.
        for ( int i = 0; i < allJourneys.size(); i++ ) {
        	if ( newJourney.compareTo(allJourneys.get(i)) == 0 ) {
        		return true;
        	}
        }
        //Otherwise return false.
        return false;
    }

    /**
     * Generate route schedules.
     * @param outgoingJourneys a <code>LinkedList</code> with all outgoing journeys.
     * @param returnJourneys a <code>LinkedList</code> with all return journeys.
     * @param sim a <code>Simulator</code> object for reference.
     */
    public void generateRouteSchedules ( Route route, List<Journey> outgoingJourneys, List<Journey> returnJourneys ) {
        //Initialise list.
    	List<RouteSchedule> mySchedules = new ArrayList<RouteSchedule>();
        //We need to repeat this loop until both outgoingJourneys and returnJourneys are empty!
        int counter = 1;
        while ( outgoingJourneys.size() > 0 || returnJourneys.size() > 0 ) {
            //Control what journey we want - initially we don't care.
            boolean wantOutgoing = true; boolean wantReturn = true;
            //Create a new route schedule.
            RouteSchedule mySchedule = new RouteSchedule ( );
            mySchedule.setRouteNumber(route.getRouteNumber());
            mySchedule.setScheduleNumber(counter);
            //Create our calendar object and set it to midnight.
            Calendar myCal = new GregorianCalendar(2009,7,7,0,0);
            //Find whether the first outgoing journey time is before the first return journey time.
            logger.debug("Outgoing journeys has size: " + outgoingJourneys.size());
            logger.debug("Return journeys has size: " + returnJourneys.size());
            if ( returnJourneys.size() > 0 && outgoingJourneys.size() > 0 && outgoingJourneys.get(0).getJourneyStops().get(0).getStopTime().after(returnJourneys.get(0).getJourneyStops().get(0).getStopTime()) ) {
                myCal = (Calendar) returnJourneys.get(0).getJourneyStops().get(0).getStopTime().clone();
            }
            else if ( outgoingJourneys.size() == 0 ) {
                myCal = (Calendar) returnJourneys.get(0).getJourneyStops().get(0).getStopTime().clone();
            }
            else {
                myCal = (Calendar) outgoingJourneys.get(0).getJourneyStops().get(0).getStopTime().clone();
            }
            //Here's the loop.
            while ( true ) {
                //logger.debug("Schedule " + counter + " Time is now " + myCal.get(Calendar.HOUR_OF_DAY) + ":" + myCal.get(Calendar.MINUTE));
                if ( outgoingJourneys.size() > 0 && returnJourneys.size() > 0 ) {
                    if ( myCal.after(outgoingJourneys.get(outgoingJourneys.size()-1).getJourneyStops().get(0).getStopTime()) && myCal.after(returnJourneys.get(returnJourneys.size()-1).getJourneyStops().get(0).getStopTime())) {
                        break;
                    }
                } else if ( outgoingJourneys.size() > 0 ) {
                    if ( myCal.after(outgoingJourneys.get(outgoingJourneys.size()-1).getJourneyStops().get(0).getStopTime()) ) {
                        break;
                    }
                } else if ( returnJourneys.size() > 0 ) {
                    if ( myCal.after(returnJourneys.get(outgoingJourneys.size()-1).getJourneyStops().get(0).getStopTime()) ) {
                        break;
                    }
                } else {
                    break; //Both outgoing journeys and return journeys were 0 so finished.
                }
                int loopPos = 0;
                while ( true ) {
                    if ( loopPos >= outgoingJourneys.size() && loopPos >= returnJourneys.size() ) { break; }
                    if ( loopPos < outgoingJourneys.size() ) {
                        //if ( wantOutgoing ) { logger.debug("I want an outgoing service so trying: " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter); }
                        if ( wantOutgoing && outgoingJourneys.get(loopPos).getJourneyStops().get(0).getStopTime().get(Calendar.HOUR_OF_DAY) == myCal.get(Calendar.HOUR_OF_DAY) && outgoingJourneys.get(loopPos).getJourneyStops().get(0).getStopTime().get(Calendar.MINUTE) == myCal.get(Calendar.MINUTE)) {
                            //logger.debug("Adding service " + outgoingServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
                            //We have found our journey - its an outgoing one!!!
                            mySchedule.addJourney(outgoingJourneys.get(loopPos));
                            //Set calendar equal to last stop time.
                            myCal = (Calendar) outgoingJourneys.get(loopPos).getJourneyStops().get(outgoingJourneys.get(loopPos).getJourneyStops().size()-1).getStopTime().clone();
                            //myCal.add(Calendar.MINUTE, -1); //This prevents bad effect of adding one later!
                            //Remove this journey from the list.
                            outgoingJourneys.remove(loopPos);
                            //Note that we next want a return one.
                            wantOutgoing = false; wantReturn = true;
                            //Continue loop.
                            continue;
                        }
                    }
                    if ( loopPos < returnJourneys.size() ) {
                        //if ( wantReturn ) { logger.debug("I want a return service so trying: " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter); }
                        if ( wantReturn && returnJourneys.get(loopPos).getJourneyStops().get(0).getStopTime().get(Calendar.HOUR_OF_DAY) == myCal.get(Calendar.HOUR_OF_DAY) && returnJourneys.get(loopPos).getJourneyStops().get(0).getStopTime().get(Calendar.MINUTE) == myCal.get(Calendar.MINUTE)) {
                            //logger.debug("Adding service " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
                            //We have found our journey - its a return one!!!
                            mySchedule.addJourney(returnJourneys.get(loopPos));
                            //Set calendar equal to last stop time.
                            myCal = (Calendar) returnJourneys.get(loopPos).getJourneyStops().get(returnJourneys.get(loopPos).getJourneyStops().size()-1).getStopTime().clone();
                            //myCal.add(Calendar.MINUTE, -1); //This prevents bad effect of adding one later!
                            //Remove this journey from the list.
                            returnJourneys.remove(loopPos);
                            //Note that we next want an outgoing one.
                            wantReturn = false; wantOutgoing = true;
                            //Continue loop.
                            continue;
                        }
                    }
                    //Increment loopPos.
                    loopPos++;
                }
                //Increment calendar and loopPos.
                myCal.add(Calendar.MINUTE, 1);
            }
            //Add route schedule to route.
            mySchedules.add(mySchedule);
            //Increment counter.
            counter++;
        }
        route.setRouteSchedules(mySchedules);
    }
    
    /**
     * Return a formatted String array of schedule dates from today.
     * @param today a <code>Calendar</code> object with the current date.
     * @return a <code>String</code> array of possible schedule dates.
     */
    public String[] getPossibleSchedulesDates ( long routeId, Calendar today ) {
        //Create the list.
        List<Calendar> myCalendar = new ArrayList<Calendar>();
        //Go through all of the timetables and add them if they are not already in.
        Iterator<String> timeNames = getRouteById(routeId).getTimetableNames();
        Calendar thisDate;
        while (timeNames.hasNext()) {
            Timetable timeT = getRouteById(routeId).getTimetable(timeNames.next());
            thisDate = (Calendar) today.clone();
            //Now check if we have passed the valid to date.
            while ( !thisDate.after(timeT.getValidToDate()) ) {
                //Check if we have added this date before...
                if ( !myCalendar.contains(thisDate) ) {
                    //Finally check that at least one of the journey patterns has an operating service on this day.
                    List<JourneyPattern> journeyPatterns = databaseManager.getJourneyPatternsByTimetableId(timeT.getId());
                    for ( JourneyPattern jp : journeyPatterns ) {
                        if ( jp.getDaysOfOperation().contains(thisDate.get(Calendar.DAY_OF_WEEK)) ) {
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
    
    /**
     * Move stops in the ordering list for a route.
     * @param stopName a <code>String</code> with the name of the stop. 
     * @param moveup a <code>boolean</code> which is true iff the stop should be moved up.
     * @return a <code>boolean</code> which is true iff the stop was moved successfully.
     */
    public boolean moveStops ( Route route, String stopName, boolean moveup ) {
    	//Search for stop in linked list.
        List<Stop> stops = route.getStops();
        for ( int i = 0; i < stops.size(); i++ ) {
        	if ( stops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
        		//Here is the swap.
                if ( moveup && i != 0 ) {
                	Stop currentStop = stops.get(i);
                    Stop prevStop = stops.get(i-1);
                    stops.remove(prevStop); stops.remove(currentStop);
                    stops.add(i-1, currentStop);
                    stops.add(i, prevStop);
                }
                else if ( !moveup && i != stops.size()-1 ) {
                	Stop nextStop = stops.get(i+1);
                    Stop currentStop = stops.get(i);
                    stops.remove(nextStop); stops.remove(currentStop);
                    try {
                    	stops.add(i+1, currentStop);
                        stops.add(i, nextStop);
                    }
                    catch ( IndexOutOfBoundsException ioe ) {
                    	stops.add(nextStop);
                        stops.add(currentStop);
                    }
                }
            }
            route.setStops(stops);
        } 
        return true;
    }
    
    /**
     * Delete stop.
     * @param stopName a <code>String</code> with the stop name.
     * @return a <code>boolean</code> which is true iff the stop was deleted successfully.
     */
    public boolean deleteStop ( Route route, String stopName ) {
        List<Stop> stops = route.getStops(); boolean result = false;
        for ( int i = 0; i < stops.size(); i++ ) {
        	if ( stops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
        		stops.remove(i); result = true;
        	}
        }
        if ( result == true ) {
        	route.setStops(stops);
        }
        return result;
    }
    
    /**
     * Add stop.
     * @param stopName a <code>String</code> with the stop name.
     * @return a <code>boolean</code> which is true iff the stop was added successfully.
     */
    public boolean addStop ( Route route, String stopName ) {
        Stop stop = new Stop();
        stop.setStopName(stopName);
        List<Stop> stops = route.getStops();
        boolean result = stops.add(stop);
        if ( result ) {
        	route.setStops(stops);
            }
        return result;
    }
    
    /**
     * This method gets the current timetable which is valid for day.
     * It is specifically used for getting the days which this timetable is valid for.
     * @param today a <code>Calendar</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public Timetable getCurrentTimetable ( long routeId, Calendar today ) {
        Iterator<String> timetableNames = getRouteById(routeId).getTimetableNames();
        while ( timetableNames.hasNext() ) {
            Timetable myTimetable = getRouteById(routeId).getTimetable(timetableNames.next());
            if ( (myTimetable.getValidFromDate().before(today) || myTimetable.getValidFromDate().equals(today)) && (myTimetable.getValidToDate().after(today) || myTimetable.getValidToDate().equals(today))  ) {
                return myTimetable;
            }
        }
        return null; //If can't find timetable.
    }

    /**
     * This method returns the stops between two supplied stops in the route including the two stops.
     * @param startStop a <code>String</code> with the start stop name.
     * @param endStop a <code>String</code> with the end stop name.
     * @param direction a <code>int</code> with the direction.
     * @return a <code>LinkedList</code> of stops.
     */
    public List<Stop> getStopsBetween ( Route route, String startStop, String endStop, int direction ) {
        //Create blank list to add things to.
        List<Stop> myStops = new ArrayList<Stop>();
        //Control whether to add or not.
        boolean shouldAddStop = false;
        //Now go through route stops in the direction and add those as appropriate.
        List<Stop> relevantStops = route.getStops();
        if ( direction == OUTWARD_DIRECTION ) { 
        	for ( int i = 0; i < relevantStops.size(); i++ ) {
                if ( relevantStops.get(i).getStopName().equalsIgnoreCase(startStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = true; }
                else if ( relevantStops.get(i).getStopName().equalsIgnoreCase(endStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = false; }
                else if ( shouldAddStop ) { myStops.add(relevantStops.get(i)); }
            }
        }
        else { 
        	for ( int i = relevantStops.size()-1; i >= 0; i-- ) {
                if ( relevantStops.get(i).getStopName().equalsIgnoreCase(startStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = true; }
                else if ( relevantStops.get(i).getStopName().equalsIgnoreCase(endStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = false; }
                else if ( shouldAddStop ) { myStops.add(relevantStops.get(i)); }
            }
        }
        return myStops;
    }
    
    public Route getRouteById(long id) {
    	return databaseManager.getRouteById(id);
    }
    
    public Route createRoute ( String routeNumber, String[] stopNames ) {
    	Route route = new Route();
    	route.setRouteNumber(routeNumber);
    	List<Stop> stops = new ArrayList<Stop>();
    	for ( int i = 0; i < stopNames.length; i++ ) {
    		Stop stop = new Stop();
    		stop.setStopName(stopNames[i]);
    		stops.add(stop);
    	}
    	route.setStops(stops);
    	return route;
    }
    
    /**
     * Get the route object based on the route number,
     * @param routeNumber a <code>String</code> with the route number,
     * @return a <code>Route</code> object.
     */
    public Route getRoute ( String routeNumber ) {
        for ( Route myRoute : getAllRoutes() ) {
            if ( myRoute.getRouteNumber().equalsIgnoreCase(routeNumber) ) {
                return myRoute;
            }
        }
        return null;
    }
    
    /**
     * Sort routes into alphabetical order by route number,
     */
    public void sortRoutes ( List<Route> routes ) {
        Collections.sort(routes, new SortedRoutes());
    }
    
    public List<Route> getAllRoutes ( ) {
    	return databaseManager.getAllRoutes();
    }
    
    public void saveRoute ( final Route route ) {
    	databaseManager.createAndStoreRoute(route);
    }

    public void removeRoute ( final Route route ) {
    	databaseManager.removeRoute(route);
    }

}
