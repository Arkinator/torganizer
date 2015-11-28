package torganizer.core.persistance.orm;

import torganizer.core.entities.Player;

public class OrmFactory {
	public static PlayerOrm getPlayerOrm(final Player player) {
		final PlayerOrm result = new PlayerOrm();
		result.id = player.getUid();
		result.name = player.getName();
		return result;
	}

	public static Player getPlayerBo(final PlayerOrm orm) {
		final Player result = new Player(orm.name, orm.id);
		return result;
	}
}
