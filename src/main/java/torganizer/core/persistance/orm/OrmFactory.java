package torganizer.core.persistance.orm;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

public class OrmFactory {
	public static PlayerOrm getPlayerOrm(final Player p, final EntityOrm entityOrm) {
		entityOrm.setName(p.getName());
		final PlayerOrm player = new PlayerOrm();
		player.setEntity(entityOrm);
		entityOrm.setPlayer(player);
		player.setId(entityOrm.getId());
		return player;
	}

	public static Player getPlayerBo(final PlayerOrm orm) {
		final Player result = new Player(orm.getEntity().getName(), orm.getEntity().getId());
		return result;
	}

	public static EntityOrm getEntityOrm(final AbstractToEntity entity) {
		final EntityOrm result = new EntityOrm();
		result.setName(entity.getName());
		return result;
	}
}
