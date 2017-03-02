package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.*;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.SortedJourneys;

public class RouteScheduleService {
	
	public RouteScheduleService() {
	}

    public List<Journey> getJourneyList (RouteSchedule schedule ) {
        return schedule.getJourneyList();
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
