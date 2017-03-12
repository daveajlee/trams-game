package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.davelee.trams.data.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

}
