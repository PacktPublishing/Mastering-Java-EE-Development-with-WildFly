package it.vige.businesscomponents.transactions.distributed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;

@PersistenceUnit(unitName="mainImages")
@Entity
public class Image {

	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	private byte[] file;

	public Image() {
	}
	
	public Image(String name, byte[] file) {
		this.name = name;
		this.file = file;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

}
