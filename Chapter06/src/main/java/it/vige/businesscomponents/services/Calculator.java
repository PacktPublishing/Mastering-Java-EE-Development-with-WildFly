package it.vige.businesscomponents.services;

import static it.vige.businesscomponents.services.Operation.DIV;
import static it.vige.businesscomponents.services.Operation.MULTI;
import static it.vige.businesscomponents.services.Operation.SUB;
import static it.vige.businesscomponents.services.Operation.SUM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/calculator")
@Stateless
public class Calculator {

	@GET
	@Path("/sum")
	@Produces(TEXT_PLAIN)
	public double sum(@QueryParam("value") double... values) {
		return execute(SUM, values);
	}

	@GET
	@Path("/sub")
	@Produces(TEXT_PLAIN)
	public double sub(@QueryParam("value") double... values) {
		return execute(SUB, values);

	}

	@POST
	@Path("/multi")
	@Consumes(APPLICATION_JSON)
	@Produces(TEXT_PLAIN)
	public double multi(double... values) {
		return execute(MULTI, values);

	}

	@PUT
	@Path("/div")
	@Produces(APPLICATION_JSON)
	public double divide(double... values) {
		return execute(DIV, values);

	}

	private double[] init(double... values) {
		if (values.length == 0) {
			values = new double[] { 0, 0 };
		} else if (values.length == 1)
			values = new double[] { values[0], 0 };
		return values;
	}

	private double execute(Operation operation, double... values) {
		init(values);
		double result = values[0];
		for (int i = 1; i < values.length; i++)
			result = calculate(operation, result, values[i]);
		return result;
	}

	private double calculate(Operation operation, double result, double value) {
		switch (operation) {
		case SUM:
			result += value;
			break;
		case SUB:
			result -= value;
			break;
		case MULTI:
			result *= value;
			break;
		case DIV:
			result /= value;
			break;
		}
		return result;
	}

}
