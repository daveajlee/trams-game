package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.*;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.repository.RouteScheduleRepository;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteScheduleService {

    @Autowired
    private RouteScheduleRepository routeScheduleRepository;
	
	public RouteScheduleService() {
	}

    /**
     * Calculate a new random delay for this route schedule.
     * @param scheduleModel a <code>RouteScheduleModel</code> object representing the route schedule to calculate the delay for.
     * @param difficultyLevel a <code>DifficultyLevel</code> object representing the difficulty level.
     */
    public void calculateNewDelay (final RouteScheduleModel scheduleModel, final DifficultyLevel difficultyLevel ) {

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
            reduceDelay(scheduleModel, delayReduction);
            return;
        }
        //With 10% probability - increase delay by 1-5 mins.
        if ( prob >= ratioArray[1] && prob < ratioArray[2] ) {
            int delayIncrease = randNumGen.nextInt(5) + 1;
            increaseDelay(scheduleModel, delayIncrease);
            return;
        }
        //Remaining probability - generate delay between 5 and 20 mins.
        int delayIncrease = randNumGen.nextInt(15) + 6;
        increaseDelay(scheduleModel, delayIncrease);
    }

    /**
     * Reduces the current delay by a certain number of minutes.
     * @param scheduleModel a <code>RouteScheduleModel</code> with the route schedule to determine delay for.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void reduceDelay(final RouteScheduleModel scheduleModel, final int mins) {
        //If no delay, then can't reduce it so just return.
        if (scheduleModel.getDelay() == 0) { return; }
        //Otherwise, reduce delay by that number of minutes.
        else {
            routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleModel.getScheduleNumber(),
                    scheduleModel.getRouteNumber()).setDelayInMins(scheduleModel.getDelay()-mins);
            //Now check if delay falls below 0, if it does then delay is 0.
            if (scheduleModel.getDelay() < 0) {
                routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleModel.getScheduleNumber(),
                        scheduleModel.getRouteNumber()).setDelayInMins(0);
            }
        }    
    }

    /**
     * Increases the vehicles current delay by a certain number of minutes.
     * @param scheduleModel a <code>RouteScheduleModel</code> with the route schedule to determine delay for.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void increaseDelay(final RouteScheduleModel scheduleModel, final int mins) {
        //This is easy because increasing delay has no special processing!!!!
        routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleModel.getScheduleNumber(),
                scheduleModel.getRouteNumber()).setDelayInMins(mins);
    }

    public RouteScheduleModel getRouteScheduleByScheduleNumberAndRouteNumber(final int scheduleNumber, final String routeNumber) {
        return convertToRouteScheduleModel(routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleNumber, routeNumber));
    }

    private RouteScheduleModel convertToRouteScheduleModel ( final RouteSchedule routeSchedule ) {
        RouteScheduleModel routeScheduleModel = new RouteScheduleModel();
        routeScheduleModel.setDelay(routeSchedule.getDelayInMins());
        routeScheduleModel.setRouteNumber(routeSchedule.getRouteNumber());
        routeScheduleModel.setScheduleNumber(routeSchedule.getScheduleNumber());
        return routeScheduleModel;
    }

    private RouteSchedule convertToRouteSchedule ( final RouteScheduleModel routeScheduleModel ) {
        RouteSchedule routeSchedule = new RouteSchedule();
        routeSchedule.setDelayInMins(routeScheduleModel.getDelay());
        routeSchedule.setRouteNumber(routeScheduleModel.getRouteNumber());
        routeSchedule.setScheduleNumber(routeScheduleModel.getScheduleNumber());
        return routeSchedule;
    }

    public void saveRouteSchedule ( final RouteScheduleModel scheduleModel ) {
        routeScheduleRepository.saveAndFlush(convertToRouteSchedule(scheduleModel));
    }

    public RouteScheduleModel[] getRouteSchedulesByRouteNumber ( final String routeNumber ) {
        List<RouteSchedule> routeSchedules = routeScheduleRepository.findByRouteNumber(routeNumber);
        RouteScheduleModel[] routeScheduleModels = new RouteScheduleModel[routeSchedules.size()];
        for ( int i = 0; i < routeScheduleModels.length; i++ ) {
            routeScheduleModels[i] = convertToRouteScheduleModel(routeSchedules.get(i));
        }
        return routeScheduleModels;
    }

    public RouteScheduleModel[] getAllRouteSchedules ( ) {
        List<RouteSchedule> routeSchedules = routeScheduleRepository.findAll();
        RouteScheduleModel[] routeScheduleModels = new RouteScheduleModel[routeSchedules.size()];
        for ( int i = 0; i < routeScheduleModels.length; i++ ) {
            routeScheduleModels[i] = convertToRouteScheduleModel(routeSchedules.get(i));
        }
        return routeScheduleModels;
    }

    /**
     * Delete all route schedules (only used as part of load function)
     */
    public void deleteAllRouteSchedules() {
        routeScheduleRepository.deleteAll();
    }

}
