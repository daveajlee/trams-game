package de.davelee.trams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

	public List<Timetable> findByRouteId ( @Param("routeId") long routeId);
	
	public Timetable findByRouteIdAndName ( @Param("routeId") long routeId, @Param("timetableName") String timetableName );
 	
}
