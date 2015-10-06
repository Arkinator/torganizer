package torganizer.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import torganizer.web.data.UserInformation;

public interface IUserRestInterface {
	@GET
	@Path("/getLoginSalt/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getLoginSalt(@PathParam("param") final String username);

	@POST
	@Path("/doLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserInformation doLogin(final UserInformation userInfo);

	@GET
	@Path("/doLogout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	UserInformation doLogout();

	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	void createUser(UserInformation newUserInformation);

	@GET
	@Path("/getUserInfo/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserInformation getUserInfo();
}
