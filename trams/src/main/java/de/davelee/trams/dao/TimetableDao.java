package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Timetable;

public class TimetableDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreTimetable(Timetable timetable) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(timetable);
    }
	
	@Transactional
	public Timetable getTimetableById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Timetable where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Timetable) list.get(0);
	}
	
	@Transactional
	public List<Timetable> getAllTimetables ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Timetable");
		@SuppressWarnings("unchecked")
		List<Timetable> list = (List<Timetable>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public List<Timetable> getTimetablesByRouteId ( long routeId ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Timetable where routeId = :routeId");
		query.setParameter("routeId", routeId);
		@SuppressWarnings("unchecked")
		List<Timetable> list = (List<Timetable>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public Timetable getTimetableByRouteIdAndName ( long routeId, String timetableName ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Timetable where routeId = :routeId and timetableName = :timetableName");
		query.setParameter("routeId", routeId);
		query.setParameter("timetableName", timetableName);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Timetable) list.get(0);
	}
	
	@Transactional
	public void deleteTimetable ( Timetable timetable ) {
		Session session = sessionFactory.getCurrentSession();
        session.delete(timetable);
	}

}
