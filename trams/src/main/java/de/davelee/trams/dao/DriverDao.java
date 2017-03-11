package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Driver;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DriverDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public void createAndStoreDriver(Driver driver) {
		entityManager.persist(driver);
    }
	
	@Transactional
	public Driver getDriverById ( long id )  {
		return entityManager.find(Driver.class, id);
	}
	
	@Transactional
	public List<Driver> getAllDrivers ( ) {
		Query query = entityManager.createQuery("SELECT d from Driver d");
		List<Driver> drivers = (List<Driver>) query.getResultList();
		if ( drivers.isEmpty() ) { return null; }
		return drivers;
	}
	
	@Transactional
	public void removeDriver ( Driver driver ) {
		entityManager.remove(entityManager.merge(driver));
	}

}
