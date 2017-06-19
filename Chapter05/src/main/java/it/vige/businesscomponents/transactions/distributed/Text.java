package it.vige.businesscomponents.transactions.distributed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;

@PersistenceUnit(unitName="mainTexts")
@Entity
public class Text {

	@Id
	@GeneratedValue
	private int id;
	
	private Format format;
	
	private String value;
	
	public Text() {
		
	}

	public Text(Format format, String value) {
		this.format = format;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
