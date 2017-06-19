package it.vige.businesscomponents.businesslogic;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton(name = "notifications")
public class Notifications {

	@PersistenceContext
	private EntityManager entityManager;
	
	@PostConstruct
	public void init() {
		entityManager.persist(new Post("notifications"));
	}
}
