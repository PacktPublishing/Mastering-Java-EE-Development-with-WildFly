package it.vige.businesscomponents.persistence.inheritance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Pet {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;

	private String name;
	private String type;

	public Pet() {
	}

	public Pet(String name, String type) {
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
