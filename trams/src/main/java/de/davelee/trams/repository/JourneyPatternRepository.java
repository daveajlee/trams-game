package de.davelee.trams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import de.davelee.trams.data.JourneyPattern;
import org.springframework.data.repository.query.Param;

public interface JourneyPatternRepository extends JpaRepository<JourneyPattern, Long> {

	public List<JourneyPattern> findByTimetableNameAndRouteNumber(@Param("timetableName") String timetableName, @Param("routeNumber") String routeNumber);

	public JourneyPattern findByNameAndTimetableNameAndRouteNumber(@Param("name") String name, @Param("timetableName") String timetableName, @Param("routeNumber") String routeNumber);

}
