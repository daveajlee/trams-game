package de.davelee.trams.services;

import java.util.*;

import de.davelee.trams.api.request.AdjustVehicleDelayRequest;
import de.davelee.trams.api.response.VehicleDelayResponse;
import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteScheduleService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

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
        int[] ratioArray;
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
        //We can only reduce delay if delay is not currently 0.
        if (scheduleModel.getDelay() != 0) {
            VehicleDelayResponse vehicleDelayResponse = restTemplate.patchForObject(operationsServerUrl + "vehicle/delay",
                    AdjustVehicleDelayRequest.builder()
                            .company(scheduleModel.getCompany())
                            .delayInMinutes(-mins)
                            .fleetNumber(scheduleModel.getFleetNumber())
                            .build(),
                    VehicleDelayResponse.class);
            scheduleModel.setDelay(vehicleDelayResponse.getDelayInMinutes());
        }    
    }

    /**
     * Increases the vehicles current delay by a certain number of minutes.
     * @param scheduleModel a <code>RouteScheduleModel</code> with the route schedule to determine delay for.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void increaseDelay(final RouteScheduleModel scheduleModel, final int mins) {
        VehicleDelayResponse vehicleDelayResponse = restTemplate.patchForObject(operationsServerUrl + "vehicle/delay",
                AdjustVehicleDelayRequest.builder()
                        .company(scheduleModel.getCompany())
                        .delayInMinutes(mins)
                        .fleetNumber(scheduleModel.getFleetNumber())
                        .build(),
                VehicleDelayResponse.class);
        scheduleModel.setDelay(vehicleDelayResponse.getDelayInMinutes());
    }

    public RouteScheduleModel getRouteScheduleByScheduleNumberAndRouteNumber(final int scheduleNumber, final String routeNumber) {
        return convertToRouteScheduleModel(routeScheduleRepository.findByScheduleNumberAndRouteNumber(scheduleNumber, routeNumber));
    }

    private RouteScheduleModel convertToRouteScheduleModel ( final RouteSchedule routeSchedule ) {
        return RouteScheduleModel.builder()
                .scheduleNumber(routeSchedule.getScheduleNumber())
                .delay(routeSchedule.getDelayInMins())
                .routeNumber(routeSchedule.getRouteNumber())
                .build();
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
