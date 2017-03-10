package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.RouteSchedule;

public class RouteScheduleDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreRouteSchedule(RouteSchedule routeSchedule) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(routeSchedule);
    }
    
	@Transactional
	public RouteSchedule getRouteScheduleById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from RouteSchedule where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (RouteSchedule) list.get(0);
	}
	
	@Transactional
	public List<RouteSchedule> getRouteSchedulesByRouteId ( long routeId ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from RouteSchedule where routeId = :routeId");
		query.setParameter("routeId", routeId);
		@SuppressWarnings({ "unchecked" })
		List<RouteSchedule> list = (List<RouteSchedule>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public List<RouteSchedule> getAllRouteSchedules ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from RouteSchedule");
		@SuppressWarnings("unchecked")
		List<RouteSchedule> list = (List<RouteSchedule>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}

}
