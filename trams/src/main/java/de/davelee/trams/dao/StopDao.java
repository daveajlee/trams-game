package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Stop;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class StopDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreStop(final Stop stop) {
		entityManager.persist(stop);
    }
	
	@Transactional
	public Stop getStopById ( final long id ) {
		return entityManager.find(Stop.class, id);
	}
	
	@Transactional
	public Stop getStopByStopName ( final String stopName ) {
		Query query = entityManager.createQuery("SELECT s from Stop s where stopName = :stopName");
		query.setParameter("stopName", stopName);
		@SuppressWarnings("rawtypes")
		List<Stop> stops = (List<Stop>) query.getResultList();
		if ( stops.isEmpty() ) { return null; }
		return (Stop) stops.get(0);
	}
	
	@Transactional
	public List<Stop> getAllStops ( ) {
		Query query = entityManager.createQuery("SELECT s from Stop s");
		@SuppressWarnings("unchecked")
		List<Stop> stops = (List<Stop>) query.getResultList();
		if ( stops.isEmpty() ) { return null; }
		return stops;
	}
	
}
