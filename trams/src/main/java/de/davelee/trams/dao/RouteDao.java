package de.davelee.trams.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Route;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class RouteDao {
	
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
	public void createAndStoreRoute ( final Route route ) {
		entityManager.persist(route);
	}
	
	@Transactional
	public Route getRouteById ( final long id ) {
		return entityManager.find(Route.class, id);
	}
	
	@Transactional
	public List<Route> getAllRoutes ( ) {
		Query query = entityManager.createQuery("SELECT r from Route r");
		@SuppressWarnings("unchecked")
		List<Route> routes = (List<Route>) query.getResultList();
		if ( routes.isEmpty() ) { return null; }
		return routes;
	}
	
	@Transactional
	public void removeRoute ( Route route ) {
		entityManager.remove(route);
	}

}
