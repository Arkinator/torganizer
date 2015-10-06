package torganizer.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("RESTEasyHelloWorld")
public class RestInterface {
	@GET
	@Path("/{param}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMsg(@PathParam("param") final String name) {
		final String msg = "Rest say: good " + name;
		return msg;
	}
}