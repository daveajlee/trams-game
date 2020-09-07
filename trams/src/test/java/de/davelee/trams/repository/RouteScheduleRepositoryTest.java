package de.davelee.trams.repository;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.services.RouteScheduleService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class RouteScheduleRepositoryTest {
	
	@Autowired
	private RouteScheduleService routeScheduleService;
	
	@Autowired
	private RouteScheduleRepository routeScheduleRepository;
	
	@Test
	public void routeScheduleTest() {
        routeScheduleService.saveRouteSchedule(RouteScheduleModel.builder()
				.delay(5)
				.routeNumber("155")
				.scheduleNumber(1)
				.build());
        RouteSchedule schedule2 = routeScheduleRepository.findByScheduleNumberAndRouteNumber(1, "155");
        Assertions.assertNotNull(schedule2);
        Assertions.assertEquals(schedule2.getDelayInMins(), 5);
        assertEquals(schedule2.getRouteNumber(), "155");
        Assertions.assertEquals(schedule2.getScheduleNumber(), 1);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
