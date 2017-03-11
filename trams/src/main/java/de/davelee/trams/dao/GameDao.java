package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Game;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GameDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreGame ( final Game game ) {
		entityManager.persist(game);
    }

	@Transactional
	public void updateGame ( final Game game ) {
		entityManager.merge(game);
	}
    
    @Transactional
	public Game getCurrentGame ( ) {
		Query query = entityManager.createQuery("SELECT g from Game g", Game.class);
		@SuppressWarnings("unchecked")
		List<Game> games = (List<Game>) query.getResultList();
		if ( games.isEmpty() ) { return null; }
		//TODO: If more than one game: exception?
		if ( games.size() > 1 ) { return games.get(0); }
		return games.get(0);
	}

}
