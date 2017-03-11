package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.StopTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class StopTimeDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
    public void createAndStoreStopTime(final StopTime stopTime) {
		entityManager.persist(stopTime);
    }
	
	@Transactional
	public List<StopTime> getStopTimesByJourneyId ( final long journeyId ) {
		Query query = entityManager.createQuery("SELECT st from StopTime st where journeyId = :journeyId");
		query.setParameter("journeyId", journeyId);
		@SuppressWarnings("unchecked")
		List<StopTime> stopTimes = (List<StopTime>) query.getResultList();
		if ( stopTimes.isEmpty() ) { return null; }
		return stopTimes;
	}
	
	@Transactional
	public List<StopTime> getAllStopTimes ( ) {
		Query query = entityManager.createQuery("SELECT st from StopTime st");
		@SuppressWarnings("unchecked")
		List<StopTime> stopTimes = (List<StopTime>) query.getResultList();
		if ( stopTimes.isEmpty() ) { return null; }
		return stopTimes;
	}
	
	@Transactional
	public void removeStopTime ( final StopTime stopTime ) {
		entityManager.remove(stopTime);
	}

}
