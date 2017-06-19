package it.vige.businesscomponents.services.otherhttp;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("/receiver")
@Stateless
@Logged
public class HttpReceiver {

	@OPTIONS
	@Path("/options")
	@Produces(TEXT_PLAIN)
	public double options() {
		return 88.99;
	}

	@DELETE
	@Path("/delete")
	@Produces(TEXT_PLAIN)
	public double delete() {
		return 99.66;
	}

	@GET
	@Path("/header")
	public String header(@HeaderParam("my_new_header") String my_new_header) {
		return my_new_header;
	}

	@GET
	@Path("/headerWithContext")
	@Produces(TEXT_PLAIN)
	public String headerWithContext(@Context HttpHeaders headers) {
		return headers.getRequestHeader("my_new_header").get(0);
	}

}
