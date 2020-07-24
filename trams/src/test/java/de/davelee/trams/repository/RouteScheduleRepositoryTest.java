package de.davelee.trams.repository;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.services.RouteScheduleService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class RouteScheduleRepositoryTest {
	
	@Autowired
	private RouteScheduleService routeScheduleService;
	
	@Autowired
	private RouteScheduleRepository routeScheduleRepository;
	
	@Test
	public void routeScheduleTest() {
		HashMap<String, Calendar> stops = new HashMap<String, Calendar>();
		stops.put("Rathaus Pankow", Calendar.getInstance());
		stops.put("Pankow Kirche", Calendar.getInstance());
		stops.put("S+U Pankow", Calendar.getInstance());
		RouteScheduleModel routeScheduleModel = new RouteScheduleModel();
		routeScheduleModel.setDelay(5);
		routeScheduleModel.setRouteNumber("155");
		routeScheduleModel.setScheduleNumber(1);
        routeScheduleService.saveRouteSchedule(routeScheduleModel);
        RouteSchedule schedule2 = routeScheduleRepository.findByScheduleNumberAndRouteNumber(1, "155");
        Assertions.assertNotNull(schedule2);
        Assertions.assertEquals(schedule2.getDelayInMins(), 5);
        Assertions.assertEquals(schedule2.getRouteNumber(), "155");
        Assertions.assertEquals(schedule2.getScheduleNumber(), 1);
	}

}
