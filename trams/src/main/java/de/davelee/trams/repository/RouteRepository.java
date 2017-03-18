package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.davelee.trams.data.Route;
import org.springframework.data.repository.query.Param;

public interface RouteRepository extends JpaRepository<Route, Long> {

    public Route findRouteByNumber ( @Param("number") String number );

}
