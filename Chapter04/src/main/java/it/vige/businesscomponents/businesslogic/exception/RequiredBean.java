package it.vige.businesscomponents.businesslogic.exception;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class RequiredBean {

	@PersistenceContext
	private EntityManager entityManager;

	public void throwRequiredException() throws SimpleAppException {
		throw new RequiredException();
	}

	public int getValue(int value) {
		return entityManager.find(Value.class, value).getId();
	}

	public void setValue(int value) {
		entityManager.persist(new Value(value));
	}

}
