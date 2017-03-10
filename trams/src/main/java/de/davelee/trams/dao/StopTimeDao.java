package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.StopTime;

public class StopTimeDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	@Transactional
    public void createAndStoreStopTime(StopTime stopTime) {
        Session session = sessionFactory.getCurrentSession();
        session.save(stopTime);
    }
	
	@Transactional
	public List<StopTime> getStopTimesByJourneyId ( long journeyId ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from StopTime where journeyId = :journeyId");
		query.setParameter("journeyId", journeyId);
		@SuppressWarnings("unchecked")
		List<StopTime> list = (List<StopTime>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public List<StopTime> getAllStopTimes ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from StopTime");
		@SuppressWarnings("unchecked")
		List<StopTime> list = (List<StopTime>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public void removeStopTime ( final StopTime stopTime ) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(stopTime);
	}

}
