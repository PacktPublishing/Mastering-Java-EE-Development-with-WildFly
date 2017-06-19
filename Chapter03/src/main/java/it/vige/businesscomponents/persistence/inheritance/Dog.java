package it.vige.businesscomponents.persistence.inheritance;

import javax.persistence.Entity;

@Entity
public class Dog extends Pet {
	
	public Dog() {
		
	}

	public Dog(String name, String type) {
		super(name, type);
	}

}
