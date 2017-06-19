package it.vige.businesscomponents.businesslogic.remote;

import javax.ejb.Stateful;

@Stateful(name = "stateMachine")
public class StateMachineBean implements StateMachine {

	private int speed;

	@Override
	public int go(int speed) {
		return this.speed += speed;
	}

	@Override
	public int retry(int speed) {
		return this.speed -= speed;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
