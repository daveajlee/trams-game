package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.JourneyPattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JourneyPatternDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
	//TODO: Separate this in create and update method.
    public void createAndStoreJourneyPattern(JourneyPattern journeyPattern) {
		entityManager.merge(journeyPattern);
    }
	
	@Transactional
	public JourneyPattern getJourneyPatternById ( long id ) {
		return entityManager.find(JourneyPattern.class, id);
	}
	
	@Transactional
	public List<JourneyPattern> getJourneyPatternsByTimetableId ( long timetableId ) {
		Query query = entityManager.createQuery("SELECT j from JourneyPattern j where timetableId = :timetableId", JourneyPattern.class);
		query.setParameter("timetableId", timetableId);
		@SuppressWarnings({ "unchecked" })
		List<JourneyPattern> journeyPatterns = (List<JourneyPattern>) query.getResultList();
		if ( journeyPatterns.isEmpty() ) { return null; }
		return journeyPatterns;
	}
	
	@Transactional
	public List<JourneyPattern> getAllJourneyPatterns ( ) {
		Query query = entityManager.createQuery("SELECT j from JourneyPattern j", JourneyPattern.class);
		@SuppressWarnings("unchecked")
		List<JourneyPattern> journeyPatterns = (List<JourneyPattern>) query.getResultList();
		if ( journeyPatterns.isEmpty() ) { return null; }
		return journeyPatterns;
	}

}
