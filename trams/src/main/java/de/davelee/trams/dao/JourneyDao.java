package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Journey;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JourneyDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreJourney(Journey journey) {
		entityManager.persist(journey);
    }
	
	@Transactional
	public Journey getJourneyById ( long id ) {
		return entityManager.find(Journey.class, id);
	}
	
	@Transactional
	public List<Journey> getJourneysByRouteScheduleId ( long routeScheduleId ) {
		Query query = entityManager.createQuery("SELECT j from Journey j where routeScheduleId = :routeScheduleId", Journey.class);
		query.setParameter("routeScheduleId", routeScheduleId);
		@SuppressWarnings({ "unchecked" })
		List<Journey> journeys = (List<Journey>) query.getResultList();
		if ( journeys.isEmpty() ) { return null; }
		return journeys;
	}
	
	@Transactional
	public List<Journey> getAllJourneys ( ) {
		Query query = entityManager.createQuery("SELECT j from Journey j", Journey.class);
		@SuppressWarnings("unchecked")
		List<Journey> journeys = (List<Journey>) query.getResultList();
		if ( journeys.isEmpty() ) { return null; }
		return journeys;
	}

}
