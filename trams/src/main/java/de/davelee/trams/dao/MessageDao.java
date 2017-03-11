package de.davelee.trams.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Message;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MessageDao {

	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
    public Message getMessageById ( final long id ) {
		return entityManager.find(Message.class, id);
    }
    
    @Transactional
    public void createAndStoreMessage ( final Message message ) {
		entityManager.persist(message);
    }
    
    @Transactional
	public List<Message> getAllMessages ( ) {
		Query query = entityManager.createQuery("SELECT m from Message m", Message.class);
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) query.getResultList();
		if ( messages.isEmpty() ) { return null; }
		return messages;
	}

}
