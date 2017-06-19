package it.vige.clustering.sharedata.singleton;

import java.io.Serializable;

public class Environment implements Serializable {
	private static final long serialVersionUID = -7845251073515304583L;

	private final String nodeName;

	public Environment(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return this.nodeName;
	}
}
