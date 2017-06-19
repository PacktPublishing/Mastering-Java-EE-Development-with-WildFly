package it.vige.businesscomponents.news;

import static io.undertow.util.Headers.CONTENT_TYPE_STRING;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;

public class Param {

	private String resourceId;

	@FormParam("data")
	private String data;

	@HeaderParam(CONTENT_TYPE_STRING)
	private String header;

	@PathParam("id")
	public void setResourceId(String id) {
		resourceId = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

}
