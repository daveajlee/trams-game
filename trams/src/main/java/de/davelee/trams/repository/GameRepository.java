package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.davelee.trams.data.Game;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    public Game findByPlayerName (@Param("playerName") String playerName);

}
