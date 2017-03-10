package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.JourneyPattern;

public class JourneyPatternDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreJourneyPattern(JourneyPattern journeyPattern) {
        Session session = sessionFactory.getCurrentSession();
        session.save(journeyPattern);
    }
	
	@Transactional
	public JourneyPattern getJourneyPatternById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from JourneyPattern where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (JourneyPattern) list.get(0);
	}
	
	@Transactional
	public List<JourneyPattern> getJourneyPatternsByTimetableId ( long timetableId ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from JourneyPattern where timetableId = :timetableId");
		query.setParameter("timetableId", timetableId);
		@SuppressWarnings({ "unchecked" })
		List<JourneyPattern> list = (List<JourneyPattern>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public List<JourneyPattern> getAllJourneyPatterns ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from JourneyPattern");
		@SuppressWarnings("unchecked")
		List<JourneyPattern> list = (List<JourneyPattern>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}

}
