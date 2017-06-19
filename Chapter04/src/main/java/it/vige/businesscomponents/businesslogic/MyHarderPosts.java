package it.vige.businesscomponents.businesslogic;

import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import static javax.ejb.LockType.READ;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Remote
@Harder
@Startup
@ConcurrencyManagement(CONTAINER)
@Lock(READ)
@DependsOn({ "messages", "notifications" })
public class MyHarderPosts extends MyPosts {

	@PersistenceContext
	private EntityManager entityManager;

	private List<Post> posts;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		posts = entityManager.createQuery("from Post").getResultList();
	}

	public List<Post> getPosts() {
		return posts;
	}
}
