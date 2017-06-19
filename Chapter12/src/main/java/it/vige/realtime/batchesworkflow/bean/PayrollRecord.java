package it.vige.realtime.batchesworkflow.bean;

import java.io.Serializable;

public class PayrollRecord implements Serializable {

	private static final long serialVersionUID = 249637703137489864L;

	private int empID;

	private int base;
	private float tax;
	private float bonus;
	private float net;

	public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	public float getNet() {
		return net;
	}

	public void setNet(float net) {
		this.net = net;
	}
}
