package de.davelee.trams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.Stop;

public interface StopRepository extends JpaRepository<Stop, Long> {
	
	public List<Stop> findByStopName(@Param("stopName") String stopName);

}