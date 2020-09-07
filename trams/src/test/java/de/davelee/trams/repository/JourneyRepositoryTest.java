package de.davelee.trams.repository;

import java.time.LocalTime;
import java.util.List;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.StopTimeModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.services.JourneyService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class JourneyRepositoryTest {
	
	@Autowired
	private JourneyService journeyService;
	
	@Autowired
	private JourneyRepository journeyRepository;
	
	@Test
	public void journeyTest() {
		List<StopTimeModel> stopTimeModelList = List.of(
				StopTimeModel.builder()
						.stopName("Rathaus Pankow")
						.time(LocalTime.now())
						.build(),
				StopTimeModel.builder()
						.stopName("Pankow Kirche")
						.time(LocalTime.now())
						.build(),
				StopTimeModel.builder()
						.stopName("S+U Pankow")
						.time(LocalTime.now())
						.build());
        journeyService.saveJourney(JourneyModel.builder()
				.journeyNumber(1)
				.routeScheduleNumber(1)
				.routeNumber("155")
				.stopTimeModelList(stopTimeModelList).build());
		assertEquals(journeyRepository.findByRouteScheduleNumberAndRouteNumber(1, "155").get(0).getRouteScheduleNumber(), 1);
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
