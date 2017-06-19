package it.vige.businesscomponents.persistence.cascade;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class InfoDetails {

	@Id
	private Long id;

	@Column(name = "created_on")
	@Temporal(TIMESTAMP)
	private Date createdOn = new Date();
	
    private boolean visible;

	@OneToOne
	@JoinColumn(name = "id")
	@MapsId
	private Info info;

	public Long getId() {
		return id;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
