package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Topic implements EntityBean {

	private static final long serialVersionUID = 9029203219849374268L;
	private static final Logger logger = getLogger(Topic.class.getName());

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@OneToMany
	private List<Post> posts;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public void addPost(Post post) {
		posts.add(post);
	}

	public Integer getId() {
		return id;
	}

	@Override
	public void setEntityContext(EntityContext ctx) throws EJBException, RemoteException {
		logger.info("setEntityContext");
	}

	@Override
	public void unsetEntityContext() throws EJBException, RemoteException {
		logger.info("unsetEntityContext");
	}

	@Override
	public void ejbRemove() throws RemoveException, EJBException, RemoteException {
		logger.info("ejbRemove");
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		logger.info("ejbActivate");
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		logger.info("ejbPassivate");
	}

	@Override
	public void ejbLoad() throws EJBException, RemoteException {
		logger.info("ejbLoad");
	}

	@Override
	public void ejbStore() throws EJBException, RemoteException {
		logger.info("ejbStore");
	}
}
