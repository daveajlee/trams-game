package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.repository.RouteScheduleRepository;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteScheduleService {

    @Autowired
    private RouteScheduleRepository routeScheduleRepository;

    /**
     * Calculate a new random delay for this route schedule.
     * @param scheduleModel a <code>RouteScheduleModel</code> object representing the route schedule to calculate the delay for.
     * @param difficultyLevel a <code>DifficultyLevel</code> object representing the difficulty level.
     */
    public void calculateNewDelay (final RouteScheduleModel scheduleModel, final DifficultyLevel difficultyLevel ) {

        //Generate a random number between 0 and 1.
        Random randNumGen = new Random();
        int val = randNumGen.nextInt(100);
        //Create probability array.
        int[] ratioArray = new int[0];
        //Set ratios according to difficulty level - default is easy.
        switch (difficultyLevel) {
        case INTERMEDIATE:
        	ratioArray = new int[] { 20, 85, 95 };
        	break;
        case MEDIUM:
        	ratioArray = new int[] { 20, 75, 90 };
        	break;
        case HARD:
        	ratioArray = new int[] { 30, 60, 85 };
        	break;
        default: //easy
                ratioArray = new int[] { 25, 85, 95 };
                break;
        }
        //With ratioArray[0] probability no delay change.
        if ( val < ratioArray[0] ) { return; }
        //With ratioArray[1] probability - reduce delay by 1-5 mins.
        if ( val >= ratioArray[0] && val < ratioArray[1] ) {
            int delayReduction = randNumGen.nextInt(5) + 1;
            reduceDelay(scheduleModel, delayReduction);
            return;
        }
        //With 10% probability - increase delay by 1-5 mins.
        if ( val >= ratioArray[1] && val < ratioArray[2] ) {
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
            RouteSchedule routeSchedule = routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleModel.getScheduleNumber(),
                    scheduleModel.getRouteNumber());
            routeSchedule.setDelayInMins(scheduleModel.getDelay()-mins);
            //Now check if delay falls below 0, if it does then delay is 0.
            if (routeSchedule.getDelayInMins() < 0) {
                routeSchedule.setDelayInMins(0);
            }
            routeScheduleRepository.save(routeSchedule);
        }    
    }

    /**
     * Increases the vehicles current delay by a certain number of minutes.
     * @param scheduleModel a <code>RouteScheduleModel</code> with the route schedule to determine delay for.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void increaseDelay(final RouteScheduleModel scheduleModel, final int mins) {
        //This is easy because increasing delay has no special processing!!!!
        RouteSchedule routeSchedule = routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleModel.getScheduleNumber(),
                scheduleModel.getRouteNumber());
        routeSchedule.setDelayInMins(routeSchedule.getDelayInMins() + mins);
        routeScheduleRepository.save(routeSchedule);
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
