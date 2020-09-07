package de.davelee.trams.services;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class RouteServiceTest {
	
	@Autowired
	private RouteService routeService;
	
	@Test
	public void testRouteService() {
		assertNotNull(routeService);
	}

	private void assertNotNull ( final Object actual ){
		Assertions.assertNotNull(actual);
	}
	
}
