package it.vige.businesscomponents.transactions.distributed;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class Texts implements Magazine<Text> {

	@PersistenceContext(unitName="mainTexts")
	private EntityManager texts;
	
	@Override
	public void write(Text element) {
		texts.persist(element);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Text> read() {
		return texts.createQuery("from Text").getResultList();
	}

}
