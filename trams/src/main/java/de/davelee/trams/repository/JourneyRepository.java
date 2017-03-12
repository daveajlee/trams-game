package de.davelee.trams.repository;

import de.davelee.trams.data.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {
	
	public List<Journey> findByRouteScheduleId(@Param("routeScheduleId") long routeScheduleId);

}
