package de.davelee.trams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.StopTime;

public interface StopTimeRepository extends JpaRepository<StopTime, Long> {

	public List<StopTime> findByJourneyId(@Param("journeyId") long journeyId);
	
}
