package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.davelee.trams.data.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
