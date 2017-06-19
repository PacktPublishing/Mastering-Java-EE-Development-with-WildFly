package it.vige.realtime.messaging.publishsubscribe;

import java.io.Serializable;

public class BUSStop implements Serializable {
	
	private static final long serialVersionUID = 1657567657L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
