package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Vehicle;

public class VehicleDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreVehicle(Vehicle vehicle) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(vehicle);
    }
	
	@Transactional
	public Vehicle getVehicleById ( long id ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Vehicle where id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Vehicle) list.get(0);
	}
	
	@Transactional
	public Vehicle getVehicleByRouteScheduleId ( long routeScheduleId ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Vehicle where routeScheduleId = :routeScheduleId");
		query.setParameter("routeScheduleId", routeScheduleId);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if ( list.isEmpty() ) { return null; }
		return (Vehicle) list.get(0);
	}

	@Transactional
	public List<Vehicle> getAllVehicles ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Vehicle");
		@SuppressWarnings("unchecked")
		List<Vehicle> list = (List<Vehicle>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}
	
	@Transactional
	public void removeVehicle ( Vehicle vehicle ) {
		Session session = sessionFactory.getCurrentSession();
        session.delete(vehicle);
	}

}
