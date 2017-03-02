package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.*;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.JourneyStatus;
import de.davelee.trams.util.SortedJourneys;

public class RouteScheduleService {
    
	private JourneyService journeyService;
	
	public RouteScheduleService() {
		journeyService = new JourneyService();
	}
	
    /**
     * Get the current stop name which this route schedule is on based on the current date.
     * @param currentDate a <code>Calendar</code> object.
     * @return a <code>String</code> array with the stop details.
     */
    public String getCurrentStopName ( long routeScheduleId, Calendar currentDate, DifficultyLevel difficultyLevel ) {
        //Copy current Date to current Time and then use delay to determine position.
        Calendar currentTime = (Calendar) currentDate.clone();
        currentTime.add(Calendar.MINUTE, -getRouteScheduleById(routeScheduleId).getDelayInMins());
        for (Journey myJourney : getJourneyList(getRouteScheduleById(routeScheduleId))) {
            if (journeyService.checkJourneyStatus(myJourney, currentTime) == JourneyStatus.RUNNING) {
                //Now fiddle delay!
                calculateNewDelay(getRouteScheduleById(routeScheduleId), difficultyLevel);
                return journeyService.getCurrentStopName(myJourney, currentTime);
            }
            if (journeyService.checkJourneyStatus(myJourney, currentTime) == JourneyStatus.YET_TO_RUN) {
                if ( myJourney.getId() == 1 ) {
                	return "Depot";
                }
                else {
                	return journeyService.getStartTerminus(myJourney);
                }
            }
        }
        getRouteScheduleById(routeScheduleId).setDelayInMins(0); //Finished for the day.
        return "Depot";
    }

    public List<Journey> getJourneyList (RouteSchedule schedule ) {
        return schedule.getJourneyList();
    }
    
    public String[] getLastStop ( long routeScheduleId, Calendar currentDate, DifficultyLevel difficultyLevel) {
        //Copy current Date to current Time and then use delay to determine position.
        Calendar currentTime = (Calendar) currentDate.clone();
        currentTime.add(Calendar.MINUTE, -getRouteScheduleById(routeScheduleId).getDelayInMins());
        for (Journey myJourney : getJourneyList(getRouteScheduleById(routeScheduleId))) {
            if (journeyService.checkJourneyStatus(myJourney, currentTime) == JourneyStatus.RUNNING) {
                //Now fiddle delay!
                calculateNewDelay(getRouteScheduleById(routeScheduleId), difficultyLevel);
                return new String[] { journeyService.getLastStop(myJourney).getStopName(), "" + 0 };
            }
            else if (journeyService.checkJourneyStatus(myJourney, currentTime) == JourneyStatus.YET_TO_RUN) {
            	if ( myJourney.getId() == 1 ) {
            		return new String[] { "Depot", "" + 0 };
            	}
            	else {
            		return new String[] { journeyService.getLastStop(myJourney).getStopName(), "" + 0 };
            	}
                
            }
        }
        getRouteScheduleById(routeScheduleId).setDelayInMins(0); //Finished for the day.
        return new String[] { "Depot", "" + 0 };
    }
    
    /**
     * Get the current journey running on this schedule based on the current date.
     * @param currentTime a <code>Calendar</code> object with current time.
     * @return a <code>Service</code> object.
     */
    public Journey getCurrentJourney ( long routeScheduleId, Calendar currentTime ) {
    	RouteSchedule schedule = getRouteScheduleById(routeScheduleId);
        for ( int i = 0; i < schedule.getJourneyList().size(); i++ ) {
            if ( journeyService.checkJourneyStatus(schedule.getJourneyList().get(i), currentTime) == JourneyStatus.RUNNING) {
                //TODO: Clean up for loop.
            	if (  i != (schedule.getJourneyList().size()-1) && journeyService.checkJourneyStatus(schedule.getJourneyList().get(i+1), currentTime) == JourneyStatus.YET_TO_RUN )  {
                	return schedule.getJourneyList().get(i);
                }
            	return schedule.getJourneyList().get(i);
            }
        }
        return null;
    }
    
    public Journey getNextJourney ( RouteSchedule schedule, Calendar currentTime ) {
        boolean returnNextJourney = false;
        for ( Journey myJourney : schedule.getJourneyList() ) {
            if ( returnNextJourney ) {
                return myJourney;
            }
            if ( journeyService.checkJourneyStatus(myJourney, currentTime) == JourneyStatus.RUNNING) {
                returnNextJourney = true;
            }
        }
        return null;
    }
    
    /**
     * Calculate a new random delay for this route schedule.
     */
    public void calculateNewDelay ( RouteSchedule schedule, DifficultyLevel difficultyLevel ) {

        //Generate a random number between 0 and 1.
        Random randNumGen = new Random();
        double prob = randNumGen.nextDouble();
        //Create probability array.
        double[] ratioArray = new double[0];
        //Set ratios according to difficulty level.
        switch (difficultyLevel) {
        case EASY:
        	ratioArray = new double[] { 0.25, 0.85, 0.95 };
        	break;
        case INTERMEDIATE:
        	ratioArray = new double[] { 0.20, 0.85, 0.95 };
        	break;
        case MEDIUM:
        	ratioArray = new double[] { 0.20, 0.75, 0.90 };
        	break;
        case HARD:
        	ratioArray = new double[] { 0.30, 0.60, 0.85 };
        	break;
        };
        //With ratioArray[0] probability no delay change.
        if ( prob < ratioArray[0] ) { return; }
        //With ratioArray[1] probability - reduce delay by 1-5 mins.
        if ( prob >= ratioArray[0] && prob < ratioArray[1] ) {
            int delayReduction = randNumGen.nextInt(5) + 1;
            reduceDelay(schedule, delayReduction);
            return;
        }
        //With 10% probability - increase delay by 1-5 mins.
        if ( prob >= ratioArray[1] && prob < ratioArray[2] ) {
            int delayIncrease = randNumGen.nextInt(5) + 1;
            increaseDelay(schedule, delayIncrease);
            return;
        }
        //Remaining probability - generate delay between 5 and 20 mins.
        int delayIncrease = randNumGen.nextInt(15) + 6;
        increaseDelay(schedule, delayIncrease);
    }
    
    /**
     * Get the current delay of this route schedule in minutes.
     * @return a <code>int</code> with the delay in minutes.
     */
    public int getDelay(long id) {
        return getRouteScheduleById(id).getDelayInMins();
    }
    
    /**
     * Check if this vehicle has a delay.
     * @return a <code>boolean>/code> with the delay of this vehicle.
     */
    public boolean hasDelay(long id) {
        if ( getRouteScheduleById(id).getDelayInMins() != 0 ) {
            return true;
        }
        return false;
    }

    /**
     * Reduces the current delay by a certain number of minutes.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void reduceDelay(RouteSchedule schedule, int mins) {
        //If no delay, then can't reduce it so just return.
        if (schedule.getDelayInMins() == 0) { return; }
        //Otherwise, reduce delay by that number of minutes.
        else {
        	schedule.setDelayInMins(schedule.getDelayInMins()-mins);
            //Now check if delay falls below 0, if it does then delay is 0.
            if (schedule.getDelayInMins() < 0) { schedule.setDelayInMins(0); }
        }    
    }

    /**
     * Increases the vehicles current delay by a certain number of minutes.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void increaseDelay(RouteSchedule schedule, int mins) {
        //This is easy because increasing delay has no special processing!!!!
        schedule.setDelayInMins(mins);
    }
    
    /**
     * Get the number of journeys in this route schedule.
     * @return a <code>int</code> with the number of services.
     */
    public int getNumJourneys ( RouteSchedule schedule ) {
        return schedule.getJourneyList().size();
    }
    
    /**
     * Get a journey based on its location in the list.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Service</code> object.
     */
    public Journey getJourney ( RouteSchedule schedule, int pos ) {
        return schedule.getJourneyList().get(pos);
    }
    
    /**
     * Shorten schedule to the specific stop stated and reduce the delay accordingly.
     * @param stop a <code>String</code> with the stop to terminate at.
     * @param currentTime a <code>Calendar</code> with the current time.
     */
    public void shortenSchedule ( long routeScheduleId, String stop, Calendar currentTime ) {
    	RouteSchedule schedule = getRouteScheduleById(routeScheduleId);
        //Shorten schedule to the specific stop stated and reduce the delay accordingly - for current service remove stops after the specified stop.
        //logger.debug("Service was ending at: " + theAssignedSchedule.getCurrentService().getEndDestination());
        String oldEnd = journeyService.getLastStop(getCurrentJourney(routeScheduleId, currentTime)).getStopName();
        //Now we need to remove the stops in beteen!
        long timeDiff = journeyService.removeStopsBetween(getCurrentJourney(routeScheduleId, currentTime), stop, oldEnd, false, true);
        //Now for the next service we need to remove stops between first stop and stop.
        long timeDiff2 = journeyService.removeStopsBetween(getNextJourney(schedule, currentTime), journeyService.getStartTerminus(getNextJourney(schedule, currentTime)), stop, false, true);
        //Divide both timeDiff's by 60 to convert to minutes and then use that to reduce vehicle delay.
        long delayReduction = (timeDiff/60) + (timeDiff2/60);
        //Reduce delay!
        reduceDelay(schedule, (int) delayReduction);
    }
    
    /**
     * Put this vehicle out of service from the current stop until the new stop.
     * @param currentStop a <code>String</code> with the stop to go out of service from.
     * @param newStop a <code>String</code> with the stop to resume service from.
     * @param currentTime a <code>Calendar</code> object with the current time.
     */
    public void outOfService ( long routeScheduleId, String currentStop, String newStop, Calendar currentTime ) {
        //Get the time difference between current stop and new stop.
    	RouteSchedule schedule = getRouteScheduleById(routeScheduleId);
        long timeDiff = journeyService.getStopTimeDifference(getCurrentJourney(routeScheduleId, currentTime), currentStop, newStop);
        reduceDelay(schedule, (int) (timeDiff/2));
        //logger.debug("Vehicle delay reduced from " + oldDelay + " mins to " + getVehicleDelay() + " mins.");
    }
    
    //TODO: Implement properly and not mock!
    public RouteSchedule getRouteScheduleById(long id) {
    	return new RouteSchedule();
    }
    
    public RouteSchedule createRouteSchedule ( final String routeNumber, final int scheduleNumber, final List<Journey> journeyList, final int delayInMins ) {
    	RouteSchedule schedule = new RouteSchedule();
    	schedule.setRouteNumber(routeNumber);
    	schedule.setScheduleNumber(scheduleNumber);
    	schedule.setJourneyList(journeyList);
    	schedule.setDelayInMins(delayInMins);
    	return schedule;
    }

    /**
     * Check if any vehicles are presently running based on the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @return a <code>boolean</code> which is true iff at least one vehicle is running.
     */
    public boolean areAnyVehiclesRunning (Calendar currentTime, Simulator simulator, List<Vehicle> vehicles, DifficultyLevel difficultyLevel) {
        //Check if any vehicles are running....
        for ( Vehicle myVehicle : vehicles ) {
            //First one that is not in depot indicates that vehicles are running.
            if ( !getCurrentStopName(myVehicle.getRouteScheduleId(), currentTime, difficultyLevel).equalsIgnoreCase("Depot") ) {
                return true;
            }
        }
        //Otherwise, return false;
        return false;
     }

    /**
     * This method checks using two stops if the service is an outward or inward service.
     * @return a <code>boolean</code> which is true iff the service is an outward service.
     */
    public boolean isOutwardService (List<Stop> stops, String stop1, String stop2 ) {
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
     * Return all outgoing journeys for a particular day.
     */
     public List<Journey> getAllOutgoingJourneys ( List<RouteSchedule> schedules, List<Stop> stops, String day ) {
         //Initialise list to store the journeys.
         List<Journey> outgoingJourneys = new ArrayList<Journey>();
         //Get the route schedules for that day!
         for ( int h = 0;  h < schedules.size(); h++ ) {
            for ( int i = 0; i < getNumJourneys(schedules.get(h)); i++ ) {
                Journey myJourney = getJourney(schedules.get(h), i);
                if ( isOutwardService(stops, myJourney.getJourneyStops().get(0).getStopName(), myJourney.getJourneyStops().get(1).getStopName()) ) {
                    outgoingJourneys.add(myJourney);
                }
            }
         }
         Collections.sort(outgoingJourneys, new SortedJourneys());
         return outgoingJourneys;
     }

     /**
      * Return all return services for a particular day.
      */
     public List<Journey> getAllReturnServices ( List<RouteSchedule> schedules, List<Stop> stops, String day ) {
         //Initialise list to store the journeys.
         List<Journey> returnServices = new ArrayList<Journey>();
         //Get the route schedules for that day!
         for ( int h = 0; h < schedules.size(); h++ ) {
             for ( int i = 0; i < getNumJourneys(schedules.get(h)); i++ ) {
                 Journey myJourney = getJourney(schedules.get(h), i);
                 if ( !isOutwardService(stops, myJourney.getJourneyStops().get(0).getStopName(), myJourney.getJourneyStops().get(1).getStopName()) ) {
                    returnServices.add(myJourney);
                 }
             }
         }
         Collections.sort(returnServices, new SortedJourneys());
         return returnServices;
     }

}
