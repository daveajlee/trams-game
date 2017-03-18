package de.davelee.trams.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import de.davelee.trams.data.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

	public List<Timetable> findByRouteNumber ( @Param("routeNumber") String routeNumber);

	public Timetable findByRouteNumberAndName ( @Param("routeNumber") String routeNumber, @Param("timetableName") String timetableName );

	public Timetable findByName ( @Param("timetableName") String timetableName );

}
