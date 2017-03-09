package de.davelee.trams.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.*;

public class DatabaseManager {

	private SessionFactory sessionFactory;
	
	public void setSessionFactory ( SessionFactory sessionFactory ) {
		this.sessionFactory = sessionFactory;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
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
	public List<Stop> getAllStops ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Stop");
		@SuppressWarnings("unchecked")
		List<Stop> list = (List<Stop>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
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
		@SuppressWarnings("rawtypes")
		List list = query.list();
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
    
	@Transactional
    public void createAndStoreTimetable(Timetable timetable) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(timetable);
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
    
    @Transactional
    public void createAndStoreScenario ( Scenario scenario ) {
    	Session session = sessionFactory.getCurrentSession();
    	session.saveOrUpdate(scenario);
    }
    
    @Transactional
    public Scenario getScenarioById ( long id ) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Scenario where id = :id");
    	query.setParameter("id", id);
    	@SuppressWarnings("rawtypes")
    	List list = query.list();
    	if ( list.isEmpty() ) { return null; }
    	return (Scenario) list.get(0);
    }
    
    public void closeSession() {
    	sessionFactory.close();
    }
    
    @Transactional
    public Message getMessageById ( long id ) {
    	Session session = sessionFactory.getCurrentSession();
    	Query query = session.createQuery("from Message where id = :id");
    	query.setParameter("id", id);
    	@SuppressWarnings("rawtypes")
    	List list = query.list();
    	if ( list.isEmpty() ) { return null; }
    	return (Message) list.get(0);
    }
    
    @Transactional
    public void createAndStoreMessage ( Message message ) {
    	Session session = sessionFactory.getCurrentSession();
    	session.saveOrUpdate(message);
    }
    
    @Transactional
	public List<Message> getAllMessages ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Message");
		@SuppressWarnings("unchecked")
		List<Message> list = (List<Message>) query.list();
		if ( list.isEmpty() ) { return null; }
		return list;
	}

}