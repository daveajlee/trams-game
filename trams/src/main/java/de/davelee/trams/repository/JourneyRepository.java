package de.davelee.trams.repository;

import de.davelee.trams.data.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

	public List<Journey> findByRouteScheduleNumberAndRouteNumber(@Param("routeScheduleNumber") int routeScheduleNumber, @Param("routeNumber") String routeNumber);

	public Journey findByJourneyNumberAndRouteScheduleNumberAndRouteNumber(@Param("journeyNumber") int journeyNumber, @Param("routeScheduleNumber") int routeScheduleNumber, @Param("routeNumber") String routeNumber);

}
