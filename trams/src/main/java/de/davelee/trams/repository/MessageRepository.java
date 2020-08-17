package de.davelee.trams.repository;

import de.davelee.trams.util.MessageFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import de.davelee.trams.data.Message;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

	public List<Message> findByFolderAndSenderAndDate (@Param("folder") MessageFolder folder, @Param("sender") String sender, @Param("date") String date);

	public List<Message> findByFolder (@Param("folder") MessageFolder folder);

}
