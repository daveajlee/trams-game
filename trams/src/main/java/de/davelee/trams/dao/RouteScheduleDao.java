package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.RouteSchedule;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class RouteScheduleDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreRouteSchedule(final RouteSchedule routeSchedule) {
		entityManager.persist(routeSchedule);
    }
    
	@Transactional
	public RouteSchedule getRouteScheduleById ( final long id ) {
		return entityManager.find(RouteSchedule.class, id);
	}
	
	@Transactional
	public List<RouteSchedule> getRouteSchedulesByRouteId ( long routeId ) {
		Query query = entityManager.createQuery("SELECT rs from RouteSchedule rs where routeId = :routeId");
		query.setParameter("routeId", routeId);
		@SuppressWarnings({ "unchecked" })
		List<RouteSchedule> routeSchedules = (List<RouteSchedule>) query.getResultList();
		if ( routeSchedules.isEmpty() ) { return null; }
		return routeSchedules;
	}
	
	@Transactional
	public List<RouteSchedule> getAllRouteSchedules ( ) {
		Query query = entityManager.createQuery("SELECT rs from RouteSchedule rs");
		@SuppressWarnings("unchecked")
		List<RouteSchedule> routeSchedules = (List<RouteSchedule>) query.getResultList();
		if ( routeSchedules.isEmpty() ) { return null; }
		return routeSchedules;
	}

}
