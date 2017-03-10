package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Driver;

public class DriverDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreDriver(Driver driver) {
		Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(driver);
    }
	
	@Transactional
	public Driver getDriverById ( long id )  {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Driver where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Driver) list.get(0);
	}
	
	@Transactional
	public List<Driver> getAllDrivers ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Driver");
		@SuppressWarnings("unchecked")
		List<Driver> list = (List<Driver>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public void removeDriver ( Driver driver ) {
		Session session = sessionFactory.getCurrentSession();
        session.delete(driver);
	}

}
