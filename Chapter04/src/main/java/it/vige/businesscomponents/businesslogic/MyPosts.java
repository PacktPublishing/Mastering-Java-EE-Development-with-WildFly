package it.vige.businesscomponents.businesslogic;

import static java.util.Arrays.asList;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Remote
public class MyPosts {

	@PersistenceContext
	private EntityManager entityManager;

	private List<Post> lastRequestedPosts;
	
	public MyPosts() {
		
	}

	public List<Post> getLastPosts() {
		return entityManager.createQuery("from Post", Post.class).getResultList();
	}

	public List<Post> getPostsByDay(int day) {
		lastRequestedPosts = asList(new Post[] { new Post("first message"), new Post("second message") });
		return entityManager.createQuery("from Post where day= :day", Post.class).setParameter("day", day)
				.getResultList();
	}

	public void addPost(String topicName, String message) {
		Topic topic = (Topic) entityManager.createQuery("from Topic where name = :name").setParameter("name", topicName)
				.getSingleResult();
		topic.addPost(new Post(message));
	}

	public List<Post> getLastRequestedPosts() {
		return lastRequestedPosts;
	}
}
