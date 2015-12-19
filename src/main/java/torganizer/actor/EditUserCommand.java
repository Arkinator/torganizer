package torganizer.actor;

import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.web.data.UserInformation;

public class EditUserCommand extends BasicObjectAction<Boolean> {
	private final UserInformation userInfo;
	private final UUID uid;

	public EditUserCommand(final UUID uid, final UserInformation userInfo, final GlobalObjectService globalObjectService) {
		super(globalObjectService);
		this.uid = uid;
		this.userInfo = userInfo;
	}

	@Override
	public Boolean execute() {
		final Player p = getGlobalObjectService().getPlayerById(uid);
		if (performChangesOnPlayer(p)) {
			getGlobalObjectService().updateEntity(p);
			return true;
		} else {
			return false;
		}
	}

	private boolean performChangesOnPlayer(final Player p) {
		if (userInfo.username != null) {
			p.setName(userInfo.username);
		}
		return true;
	}
}
