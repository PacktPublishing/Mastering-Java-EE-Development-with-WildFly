package it.vige.businesscomponents.services.components;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/simple")
@Stateless
public class SimpleService {

	@GET
	@Path("/valuesget")
	@Produces(TEXT_PLAIN)
	public String valuesget(@QueryParam("OrderID") String orderId, @QueryParam("UserName") String userName) {
		return orderId + "-" + userName;

	}

	@POST
	@Path("/values")
	@Produces(TEXT_PLAIN)
	public Response values(String inputMessage) {
		return ok("Order successfully placed ").status(OK).build();
	}

}
