package de.davelee.trams.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import de.davelee.trams.data.Game;

public class GameDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
    public void createAndStoreGame ( final Game game ) {
    	Session session = sessionFactory.getCurrentSession();
    	session.saveOrUpdate(game);
    }
    
    @Transactional
	public Game getCurrentGame ( ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Game");
		@SuppressWarnings("unchecked")
		List<Game> list = (List<Game>) query.list();
		if ( list.isEmpty() ) { return null; }
		//TODO: If more than one game: exception?
		if ( list.size() > 1 ) { return list.get(0); }
		return list.get(0);
	}

}
