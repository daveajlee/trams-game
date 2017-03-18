package de.davelee.trams.repository;

import de.davelee.trams.data.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    public Driver findByName ( @Param("name") String name );

}
