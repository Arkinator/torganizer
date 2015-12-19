package torganizer.actor;

import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.web.data.UserInformation;

public class GetUserInfoCommand extends BasicObjectAction<UserInformation> {
	private String userName;
	private UUID uid;

	public GetUserInfoCommand(final String userName, final GlobalObjectService globalObjectService) {
		super(globalObjectService);
		this.userName = userName;
	}

	public GetUserInfoCommand(final UUID uid, final GlobalObjectService globalObjectService) {
		super(globalObjectService);
		this.uid = uid;
	}

	@Override
	public UserInformation execute() {
		Player p = null;
		if (userName != null) {
			p = getGlobalObjectService().getPlayerByName(userName);
			if (p == null) {
				throw new UserNotFoundException("The Player '" + userName + "' was not found");
			}
		} else if (uid != null) {
			p = getGlobalObjectService().getPlayerById(uid);
			if (p == null) {
				throw new UserNotFoundException("The Player '" + uid + "' was not found");
			}
		} else {
			throw new UnknownUserReferenceException("Neither a name nor an uid was set, a player info can therefore not be retrieved");
		}
		return new UserInformation(p);
	}

	public class UnknownUserReferenceException extends RuntimeException {
		private static final long serialVersionUID = 463233230189028748L;

		public UnknownUserReferenceException(final String string) {
			super(string);
		}
	}

	public class UserNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 4363301166435682994L;

		public UserNotFoundException(final String string) {
			super(string);
		}
	}
}
