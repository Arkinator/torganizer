package torganizer.actor;

import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.web.data.UserInformation;

public class AddUserCommand extends BasicObjectAction<Boolean> {
	private final UserInformation userInfo;

	public AddUserCommand(final UserInformation userInfo, final GlobalObjectService globalObjectService) {
		super(globalObjectService);
		this.userInfo = userInfo;
	}

	@Override
	public Boolean execute() {
		final Player player = getPlayerFromUserInfo();
		getGlobalObjectService().addPlayer(player);
		return true;
	}

	private Player getPlayerFromUserInfo() {
		final Player player = new Player(userInfo.username);
		return player;
	}
}
