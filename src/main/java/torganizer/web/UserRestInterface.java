package torganizer.web;

import java.util.Arrays;
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

@Path("user")
public class UserRestInterface implements IUserRestInterface {
	private static final String SALT = "salt";
	private static final String LOGGED_IN_PLAYER = "user";

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/getLoginSalt/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getLoginSalt(@PathParam("param") final String username) {
		final Player player = DataSource.getUserByName(username);
		if (player == null) {
			final Long salt = new Random().nextLong();
			request.getSession().setAttribute(SALT, salt.toString());
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
	public UserInformation doLogin(final UserInformation userInfo) {
		final Player player = DataSource.getUserByName(userInfo.username);
		if (player != null) {
			if (Arrays.equals(userInfo.passwordHash, player.getPasswordHash())) {
				request.getSession().setAttribute(LOGGED_IN_PLAYER, player);
			}
		}
		return ((Player) (request.getSession().getAttribute(LOGGED_IN_PLAYER))).extractUserInformation();
	}

	@GET
	@Path("/doLogout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public UserInformation doLogout() {
		request.getSession().setAttribute(LOGGED_IN_PLAYER, null);
		return null;
	}

	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void createUser(final UserInformation newUserInformation) {
		final Player player = new Player(newUserInformation.username);
		player.setPasswordHash(newUserInformation.passwordHash);
		player.setPasswordSalt((String) request.getSession().getAttribute(SALT));
		DataSource.persistEntity(player);
	}

	@GET
	@Path("/getUserInfo/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public UserInformation getUserInfo() {
		final Player player = (Player) request.getSession().getAttribute(LOGGED_IN_PLAYER);
		if (player == null) {
			return null;
		} else {
			return player.extractUserInformation();
		}
	}
}