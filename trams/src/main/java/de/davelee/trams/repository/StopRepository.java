package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.Stop;

public interface StopRepository extends JpaRepository<Stop, Long> {

	public Stop findByStopName(@Param("stopName") String stopName);

}
