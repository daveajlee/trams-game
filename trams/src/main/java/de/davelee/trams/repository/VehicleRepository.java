package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	public Vehicle findByRouteScheduleId(@Param("routeScheduleId") long routeScheduleId);

	public Vehicle findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

}
