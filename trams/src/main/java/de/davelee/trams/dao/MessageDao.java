package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Message;

public class MessageDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
