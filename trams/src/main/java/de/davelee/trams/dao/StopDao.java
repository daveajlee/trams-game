package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Stop;

public class StopDao {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreStop(Stop stop) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(stop);
    }
	
	@Transactional
	public Stop getStopById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Stop where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Stop) list.get(0);
	}
	
	@Transactional
	public Stop getStopByStopName ( final String stopName ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Stop where stopName = :stopName");
		query.setParameter("stopName", stopName);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Stop) list.get(0);
	}
	
	@Transactional
	public List<Stop> getAllStops ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Stop");
		@SuppressWarnings("unchecked")
		List<Stop> list = (List<Stop>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
}
