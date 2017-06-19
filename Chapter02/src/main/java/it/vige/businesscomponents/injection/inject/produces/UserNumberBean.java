package it.vige.businesscomponents.injection.inject.produces;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class UserNumberBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int number;
	private Integer userNumber;
	private int minimum;

	@MaxNumber
	@Inject
	private int maxNumber;

	private int maximum;

	@Random
	@Inject
	private Instance<Integer> randomInt;

	public UserNumberBean() {
	}

	public int getNumber() {
		return number;
	}

	public void setUserNumber(Integer user_number) {
		userNumber = user_number;
	}

	public Integer getUserNumber() {
		return userNumber;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public boolean check() {
		if (userNumber > number) {
			maximum = userNumber - 1;
		}
		if (userNumber < number) {
			minimum = userNumber + 1;
		}
		if (userNumber == number) {
			return true;
		}
		return false;
	}

	@PostConstruct
	public void reset() {
		this.minimum = 0;
		this.userNumber = 0;
		this.maximum = maxNumber;
		this.number = randomInt.get();
	}

	public boolean validateNumberRange(Object value) {
		int input = (Integer) value;

		if (input < minimum || input > maximum) {
			return false;
		}
		return true;
	}
}
