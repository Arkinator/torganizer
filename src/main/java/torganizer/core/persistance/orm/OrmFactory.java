package torganizer.core.persistance.orm;

import org.springframework.stereotype.Component;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

@Component(value = "ormFactory")
public class OrmFactory {
	public PlayerOrm getPlayerOrm(final Player p, final EntityOrm entityOrm) {
		entityOrm.setName(p.getName());
		final PlayerOrm player = new PlayerOrm();
		player.setEntity(entityOrm);
		entityOrm.setPlayer(player);
		player.setId(entityOrm.getId());
		player.setAdmin(p.isAdmin());
		return player;
	}

	public Player getPlayerBo(final EntityOrm orm) {
		return new Player(orm.getPlayer());
	}

	public EntityOrm getEntityOrm(final AbstractToEntity entity) {
		final EntityOrm result = new EntityOrm();
		result.setName(entity.getName());
		result.setUuid(entity.getUid());
		return result;
	}
}
