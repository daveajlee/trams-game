package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Timetable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class TimetableDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreTimetable(Timetable timetable) {
		entityManager.persist(timetable);
    }
	
	@Transactional
	public Timetable getTimetableById ( long id ) {
		return entityManager.find(Timetable.class, id);
	}
	
	@Transactional
	public List<Timetable> getAllTimetables ( ) {
		Query query = entityManager.createQuery("SELECT t from Timetable t");
		@SuppressWarnings("unchecked")
		List<Timetable> timetables = (List<Timetable>) query.getResultList();
		if ( timetables.isEmpty() ) { return null; }
		return timetables;
	}
	
	@Transactional
	public List<Timetable> getTimetablesByRouteId ( final long routeId ) {
		Query query = entityManager.createQuery("SELECT t from Timetable t where routeId = :routeId");
		query.setParameter("routeId", routeId);
		@SuppressWarnings("unchecked")
		List<Timetable> timetables = (List<Timetable>) query.getResultList();
		if ( timetables.isEmpty() ) { return null; }
		return timetables;
	}
	
	@Transactional
	public Timetable getTimetableByRouteIdAndName ( final long routeId, final String timetableName ) {
		Query query = entityManager.createQuery("SELECT t from Timetable t where routeId = :routeId AND timetableName = :timetableName");
		query.setParameter("routeId", routeId);
		query.setParameter("timetableName", timetableName);
		@SuppressWarnings("unchecked")
		List<Timetable> list = (List<Timetable>) query.getResultList();
		if ( list.isEmpty() ) { return null; }
		return list.get(0);
	}
	
	@Transactional
	public void deleteTimetable ( final Timetable timetable ) {
		entityManager.remove(timetable);
	}

}
