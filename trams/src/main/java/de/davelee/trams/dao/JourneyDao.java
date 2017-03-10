package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Journey;

public class JourneyDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreJourney(Journey journey) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(journey);
    }
	
	@Transactional
	public Journey getJourneyById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Journey where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Journey) list.get(0);
	}
	
	@Transactional
	public List<Journey> getJourneysByRouteScheduleId ( long routeScheduleId ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Journey where routeScheduleId = :routeScheduleId");
		query.setParameter("routeScheduleId", routeScheduleId);
		@SuppressWarnings({ "unchecked" })
		List<Journey> list = (List<Journey>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public List<Journey> getAllJourneys ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Journey");
		@SuppressWarnings("unchecked")
		List<Journey> list = (List<Journey>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}

}
