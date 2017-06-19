package it.vige.businesscomponents.persistence.converter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "citizen")
@Table(name = "CITIZEN")
public class Citizen {

	@Id
	@GeneratedValue
	private Integer id;

	@Column
	@Convert(converter = PasswordConverter.class)
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
