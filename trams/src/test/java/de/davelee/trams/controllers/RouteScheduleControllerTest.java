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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        RouteModel routeModel = RouteModel.builder()
                .routeNumber("X1")
                .stopNames(List.of("Airport", "Bus Station"))
                .build();
        //Add test timetable.
        timetableController.createTimetable("RegularTimetable", LocalDate.of(2017,3,1),
                LocalDate.of(2017,4,1), RouteModel.builder()
                        .routeNumber("X1")
                        .stopNames(List.of("Airport", "Bus Station")).build());
        //Add test journey pattern.
        List<DayOfWeek> operatingDays = new ArrayList<DayOfWeek>();
        operatingDays.add(DayOfWeek.SUNDAY); operatingDays.add(DayOfWeek.MONDAY); operatingDays.add(DayOfWeek.TUESDAY); operatingDays.add(DayOfWeek.WEDNESDAY);
        operatingDays.add(DayOfWeek.THURSDAY); operatingDays.add(DayOfWeek.FRIDAY); operatingDays.add(DayOfWeek.SATURDAY);
        JourneyPatternModel.builder()
                .name("Regular")
                .daysOfOperation(operatingDays)
                .outgoingTerminus("Airport")
                .returnTerminus("Bus Station")
                .startTime(LocalTime.of(6,0))
                .endTime(LocalTime.of(18,30))
                .frequency(15)
                .duration(30)
                .timetableName(timetableController.getRouteTimetable(routeModel, "RegularTimetable").getName())
                .routeNumber("X1")
                .build();
        //Test current time.
        LocalDate testDateTime = LocalDate.of(2017,3,1);
        //Test generate route schedules.
        List<RouteScheduleModel> routeScheduleModels = routeScheduleController.generateRouteSchedules(routeModel, testDateTime, "Landuff Transport Company");
        Assertions.assertNotNull(routeScheduleModels);
        Assertions.assertEquals(3, routeScheduleModels.size());
        Assertions.assertEquals(1, routeScheduleModels.get(0).getScheduleNumber());
        Assertions.assertEquals(2, routeScheduleModels.get(1).getScheduleNumber());
        Assertions.assertEquals(3, routeScheduleModels.get(2).getScheduleNumber());
        Assertions.assertEquals(0, routeScheduleModels.get(0).getDelay());
        assertEquals("X1", routeScheduleModels.get(0).getRouteNumber());
    }

    private void assertEquals ( final String expected, final String actual ) {
        Assertions.assertEquals(expected, actual);
    }

}
