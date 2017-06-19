package it.vige.businesscomponents.persistence.inheritance;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
public class Vehicle {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;

	private String name;
	private String type;

	public Vehicle() {
	}

	public Vehicle(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
