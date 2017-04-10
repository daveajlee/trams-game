package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Route;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.repository.RouteRepository;
import de.davelee.trams.services.RouteService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class RouteRepositoryTest {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Test
	public void routeTest() {
		List<String> outwardStopNames = new ArrayList<String>();
		outwardStopNames.add("Rathaus Pankow");
		outwardStopNames.add("Pankow Kirche");
		outwardStopNames.add("S+U Pankow");
		RouteModel routeModel = new RouteModel();
		routeModel.setRouteNumber("155");
		routeModel.setStopNames(outwardStopNames);
		routeService.saveRoute(routeModel);
		Route route2 = routeRepository.findRouteByNumber("155");
		assertNotNull(route2);
		assertEquals(route2.getNumber(), "155");
		//assertEquals(route2.getStops().size(), 3);
	}

}
