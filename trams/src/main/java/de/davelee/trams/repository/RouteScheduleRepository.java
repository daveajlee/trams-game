package de.davelee.trams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.RouteSchedule;

public interface RouteScheduleRepository extends JpaRepository<RouteSchedule, Long> {

	public List<RouteSchedule> findByRouteNumber(@Param("routeNumber") String routeNumber);

	public RouteSchedule findByScheduleNumberAndRouteNumber(@Param("scheduleNumber") int scheduleNumber, @Param("routeNumber") String routeNumber);

}
