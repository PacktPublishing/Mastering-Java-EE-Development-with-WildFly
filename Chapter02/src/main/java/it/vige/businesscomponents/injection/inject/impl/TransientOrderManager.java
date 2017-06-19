package it.vige.businesscomponents.injection.inject.impl;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.TransientReference;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;

@SessionScoped
@Typed(Comment.class)
public class TransientOrderManager implements Comment<String>, Serializable {

	private static final long serialVersionUID = -368372211937274036L;
	private Order order;

	public TransientOrderManager() {
	}

	@Inject
	public TransientOrderManager(@TransientReference Order order) {
		this.order = order;
	}

	@Inject
	public void initialize(@TransientReference Order order) {
		this.order = order;
	}

	@Override
	public Order getOrder() {
		return order;
	}
}