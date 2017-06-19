package it.vige.realtime.batchesworkflow.bean;

import java.io.Serializable;

public class PayrollInputRecord implements Serializable {

	private static final long serialVersionUID = -2747746518993366052L;

	private int id;

	private int baseSalary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(int baseSalary) {
		this.baseSalary = baseSalary;
	}
}
