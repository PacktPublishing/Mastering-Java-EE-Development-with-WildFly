package it.vige.businesscomponents.news;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import javax.ejb.Stateless;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/simple")
@Stateless
public class SimpleService {

	@POST
	@Path("{id}")
	@Produces(TEXT_HTML)
	public String values(@BeanParam Param param) {
		return param.getResourceId() + "|" + param.getData() + "|" + param.getHeader();
	}

	@POST
	@Path("form")
	@Produces(TEXT_HTML)
	public String values(@FormParam("data") String data) {
		return data;
	}

}
