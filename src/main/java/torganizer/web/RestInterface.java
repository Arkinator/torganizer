package torganizer.web;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import torganizer.core.entities.Player;
import torganizer.core.persistance.DataSource;
import torganizer.web.data.UserInformation;

@Path("rest")
public class RestInterface implements IRestInterface {
	private static final String attributeName = "blub";
	private static final String saltAttributeName = "salt";

	@Context
	private HttpServletRequest request;

	@Override
	@GET
	@Path("/{param}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMsg(@PathParam("param") final String name) {
		String msg = ": good " + name;
		msg += "\n" + attributeName + " = " + request.getSession().getAttribute(attributeName);
		request.getSession().setAttribute(attributeName, name);

		return msg;
	}

	@GET
	@Path("/getLoginSalt/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getLoginSalt(@PathParam("param") final String username) {
		final Player player = DataSource.getUserByName(username);
		if (player == null) {
			final Long salt = new Random().nextLong();
			request.getSession().setAttribute(saltAttributeName, salt);
			return salt.toString();
		} else {
			return player.getPasswordSalt();
		}
	}

	@POST
	@Path("/doLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public UserInformation doLogin(final String username, final byte[] passwordHashWithSalt) {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("/doLogout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public UserInformation doLogout() {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void createUser(final UserInformation newUserInformation) {
		final Player player = new Player(newUserInformation.username);
		player.setPasswordHash(newUserInformation.passwordHash);
		player.setPasswordSalt(newUserInformation.passwordSalt);
		DataSource.persistEntity(player);
	}
}