package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Vehicle;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class VehicleDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreVehicle(final Vehicle vehicle) {
		entityManager.persist(vehicle);
    }
	
	@Transactional
	public Vehicle getVehicleById ( final long id ) {
		return entityManager.find(Vehicle.class, id);
	}
	
	@Transactional
	public Vehicle getVehicleByRouteScheduleId ( final long routeScheduleId ) {
		Query query = entityManager.createQuery("SELECT v from Vehicle v where routeScheduleId = :routeScheduleId");
		query.setParameter("routeScheduleId", routeScheduleId);
		@SuppressWarnings("unchecked")
		List<Vehicle> vehicles = (List<Vehicle>) query.getResultList();
		if ( vehicles.isEmpty() ) { return null; }
		return vehicles.get(0);
	}

	@Transactional
	public List<Vehicle> getAllVehicles ( ) {
		Query query = entityManager.createQuery("SELECT v from Vehicle v");
		@SuppressWarnings("unchecked")
		List<Vehicle> vehicles = (List<Vehicle>) query.getResultList();
		if ( vehicles.isEmpty() ) { return null; }
		return vehicles;
	}
	
	@Transactional
	public void removeVehicle ( final Vehicle vehicle ) {
		entityManager.remove(entityManager.merge(vehicle));
	}

}
