package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.*;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.util.DifficultyLevel;

public class RouteScheduleService {

    private DatabaseManager databaseManager;
	
	public RouteScheduleService() {
	}

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
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

    public RouteSchedule getRouteScheduleById(long id) {
        return databaseManager.getRouteScheduleById(id);
    }

    public RouteSchedule createRouteSchedule ( final long routeId, final int scheduleNumber, final int delayInMins ) {
    	RouteSchedule schedule = new RouteSchedule();
    	schedule.setScheduleNumber(scheduleNumber);
    	schedule.setDelayInMins(delayInMins);
        schedule.setRouteId(routeId);
    	return schedule;
    }

    /**
     * Generate route schedules.
     * @param outgoingJourneys a <code>LinkedList</code> with all outgoing journeys.
     * @param returnJourneys a <code>LinkedList</code> with all return journeys.
     * @param sim a <code>Simulator</code> object for reference.
     */
    public void generateRouteSchedules ( long routeId, List<Journey> outgoingJourneys, List<Journey> returnJourneys ) {
        //We need to repeat this loop until both outgoingJourneys and returnJourneys are empty!
        int counter = 1;
        while ( outgoingJourneys.size() > 0 || returnJourneys.size() > 0 ) {
            //Control what journey we want - initially we don't care.
            boolean wantOutgoing = true; boolean wantReturn = true;
            //Create a new route schedule.
            RouteSchedule mySchedule = new RouteSchedule ( );
            mySchedule.setScheduleNumber(counter);
            //Create our calendar object and set it to midnight.
            Calendar myCal = new GregorianCalendar(2009,7,7,0,0);
            //Find whether the first outgoing journey time is before the first return journey time.
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
                            outgoingJourneys.get(loopPos).setRouteScheduleId(mySchedule.getId());
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
                            returnJourneys.get(loopPos).setRouteScheduleId(mySchedule.getId());
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
            //Add route schedule to database.
            databaseManager.createAndStoreRouteSchedule(mySchedule);
            //Increment counter.
            counter++;
        }
    }

    public List<RouteSchedule> getRouteSchedulesByRouteId ( long routeId ) {
        return databaseManager.getRouteSchedulesByRouteId(routeId);
    }

}
