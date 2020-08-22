package de.davelee.trams.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.Route;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.services.RouteService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class RouteRepositoryTest {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Test
	public void routeTest() {
		routeService.saveRoute(RouteModel.builder()
				.routeNumber("155")
				.stopNames(List.of("Rathaus Pankow", "Pankow Kirche", "S+U Pankow"))
				.build());
		Route route2 = routeRepository.findRouteByNumber("155");
		Assertions.assertNotNull(route2);
		assertEquals(route2.getNumber(), "155");
		//assertEquals(route2.getStops().size(), 3);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
