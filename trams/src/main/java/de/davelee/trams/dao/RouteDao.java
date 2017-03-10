package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Route;

public class RouteDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public void createAndStoreRoute ( Route route ) {
		Session session = sessionFactory.getCurrentSession();
	    session.saveOrUpdate(route);
	}
	
	@Transactional
	public Route getRouteById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Route where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Route) list.get(0);
	}
	
	@Transactional
	public List<Route> getAllRoutes ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Route");
		@SuppressWarnings("unchecked")
		List<Route> list = (List<Route>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public void removeRoute ( Route route ) {
		Session session = sessionFactory.getCurrentSession();
        session.delete(route);
	}

}
