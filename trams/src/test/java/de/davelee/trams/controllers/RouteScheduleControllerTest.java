package de.davelee.trams.controllers;

import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by davelee on 12.04.17.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class RouteScheduleControllerTest {

    @Autowired
    private RouteScheduleController routeScheduleController;

    @Autowired
    private TimetableController timetableController;

    @Test
    public void testGenerateRouteSchedules() {
        //Test route.
        RouteModel routeModel = new RouteModel();
        routeModel.setRouteNumber("X1");
        List<String> stopList = new ArrayList<String>();
        stopList.add("Airport");
        stopList.add("Bus Station");
        routeModel.setStopNames(stopList);
        //Add test timetable.
        timetableController.createTimetable("RegularTimetable", generateCalendarDateTime(1,3,2017,4,0),
                generateCalendarDateTime(1,4,2017,23,0), routeModel);
        //Add test journey pattern.
        List<Integer> operatingDays = new ArrayList<Integer>();
        operatingDays.add(1); operatingDays.add(2); operatingDays.add(3); operatingDays.add(4);
        operatingDays.add(5); operatingDays.add(6); operatingDays.add(7);
        JourneyPatternModel.builder()
                .name("Regular")
                .daysOfOperation(operatingDays)
                .outgoingTerminus("Airport")
                .returnTerminus("Bus Station")
                .startTime(generateCalendarTime(6,0))
                .endTime(generateCalendarTime(18,30))
                .frequency(15)
                .duration(30)
                .timetableName(timetableController.getRouteTimetable(routeModel, "RegularTimetable").getName())
                .routeNumber("X1")
                .build();
        //Test current time.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 0);
        //Test generate route schedules.
        List<RouteScheduleModel> routeScheduleModels = routeScheduleController.generateRouteSchedules(routeModel, generateCalendarDateTime(1,3,2017,4,0), "Landuff Transport Company");
        Assertions.assertNotNull(routeScheduleModels);
        Assertions.assertEquals(3, routeScheduleModels.size());
        Assertions.assertEquals(1, routeScheduleModels.get(0).getScheduleNumber());
        Assertions.assertEquals(2, routeScheduleModels.get(1).getScheduleNumber());
        Assertions.assertEquals(3, routeScheduleModels.get(2).getScheduleNumber());
        Assertions.assertEquals(0, routeScheduleModels.get(0).getDelay());
        assertEquals("X1", routeScheduleModels.get(0).getRouteNumber());
    }

    private Calendar generateCalendarDateTime ( final int day, final int month, final int year, final int hour, final int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        return calendar;
    }

    private Calendar generateCalendarTime ( final int hour, final int min ) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        return calendar;
    }

    private void assertEquals ( final String expected, final String actual ) {
        Assertions.assertEquals(expected, actual);
    }

}
