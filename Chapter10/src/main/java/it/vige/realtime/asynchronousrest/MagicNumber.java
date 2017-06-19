package it.vige.realtime.asynchronousrest;

import java.io.Serializable;

public class MagicNumber implements Serializable {
	
	private static final long serialVersionUID = -6793541176244882569L;
	private int value;

	public MagicNumber(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MagicNumber [value=" + value + "]";
	}
	
}
