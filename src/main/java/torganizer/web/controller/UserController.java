package torganizer.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import torganizer.actor.AddUserCommand;
import torganizer.actor.GetUserInfoCommand;
import torganizer.actor.ListUsersAction;
import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.web.data.UserInformation;

@RestController
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private GlobalObjectService globalObjectService;
	@Autowired
	private PlayerDao playerDao;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public UserInformation addUser(@RequestBody(required = true) final UserInformation userInfo) {
		if (new AddUserCommand(userInfo, globalObjectService).execute()) {
			return new GetUserInfoCommand(userInfo.username, globalObjectService).execute();
		} else {
			throw new UnknownUserCreationError();
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public List<UserInformation> listAllUsers() {
		return new ListUsersAction(playerDao).execute();
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public static class UnknownUserCreationError extends RuntimeException {
		private static final long serialVersionUID = -345413036496562217L;
	}
}
