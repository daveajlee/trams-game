package de.davelee.trams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.davelee.trams.data.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
