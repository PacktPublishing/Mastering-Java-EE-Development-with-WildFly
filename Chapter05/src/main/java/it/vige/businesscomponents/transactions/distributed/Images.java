package it.vige.businesscomponents.transactions.distributed;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class Images implements Magazine<Image> {

	@PersistenceContext(unitName="mainImages")
	private EntityManager images;
	
	@Override
	public void write(Image element) {
		images.persist(element);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Image> read() {
		return images.createQuery("from Image").getResultList();
	}

}