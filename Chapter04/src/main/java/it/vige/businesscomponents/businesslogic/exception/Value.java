package it.vige.businesscomponents.businesslogic.exception;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Value {

	@Id
	private int id;

	public Value() {
	}

	public Value(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
