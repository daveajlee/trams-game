package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.*;
import de.davelee.trams.repository.RouteScheduleRepository;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;

public class RouteScheduleService {

    @Autowired
    private RouteScheduleRepository routeScheduleRepository;
	
	public RouteScheduleService() {
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
        return routeScheduleRepository.findOne(id);
    }

    public RouteSchedule createRouteSchedule ( final long routeId, final int scheduleNumber, final int delayInMins ) {
    	RouteSchedule schedule = new RouteSchedule();
    	schedule.setScheduleNumber(scheduleNumber);
    	schedule.setDelayInMins(delayInMins);
        schedule.setRouteId(routeId);
    	return schedule;
    }

    public void saveRouteSchedule ( final RouteSchedule schedule ) {
        routeScheduleRepository.saveAndFlush(schedule);
    }

    public List<RouteSchedule> getRouteSchedulesByRouteId ( long routeId ) {
        return routeScheduleRepository.findByRouteId(routeId);
    }

    public List<RouteSchedule> getAllRouteSchedules ( ) {
        return routeScheduleRepository.findAll();
    }

}
