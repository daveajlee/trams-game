package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.repository.RouteScheduleRepository;
import de.davelee.trams.services.RouteScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
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
        assertNotNull(schedule2);
        assertEquals(schedule2.getDelayInMins(), 5);
        assertEquals(schedule2.getRouteNumber(), "155");
        assertEquals(schedule2.getScheduleNumber(), 1);
	}

}
