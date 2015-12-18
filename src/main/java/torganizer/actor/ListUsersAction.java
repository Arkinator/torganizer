package torganizer.actor;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.web.data.UserInformation;

public class ListUsersAction extends BasicAction<List<UserInformation>> {
	private final PlayerDao playerDao;

	public ListUsersAction(final PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Override
	public List<UserInformation> execute() {
		final List<UserInformation> result = new ArrayList<UserInformation>();
		playerDao.getAllPlayers().forEach(p -> result.add(new UserInformation(p)));
		return result;
	}

}
