package de.davelee.trams.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class RouteServiceTest {
	
	@Autowired
	private RouteService routeService;
	
	@Test
	public void testRouteService() {
		assertNotNull(routeService);
	}
	
}
