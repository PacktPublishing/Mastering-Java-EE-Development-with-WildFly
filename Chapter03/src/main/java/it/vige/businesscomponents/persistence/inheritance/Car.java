package it.vige.businesscomponents.persistence.inheritance;

import javax.persistence.Entity;

@Entity
public class Car extends Vehicle {

	public Car() {

	}

	public Car(String name, String type) {
		super(name, type);
	}
}
