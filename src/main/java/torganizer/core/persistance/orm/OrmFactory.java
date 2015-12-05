package torganizer.core.persistance.orm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.persistance.interfaces.EntityDao;

@Component(value = "ormFactory")
public class OrmFactory {
	private final EntityDao entityDao;

	@Autowired(required = true)
	public OrmFactory(final EntityDao entityDao) {
		this.entityDao = entityDao;
	}

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
		final Player result = new Player(orm.getName(), orm.getUuid());
		result.setAdmin(orm.getPlayer().getAdmin());
		return result;
	}

	public EntityOrm getEntityOrm(final AbstractToEntity entity) {
		final EntityOrm result = new EntityOrm();
		result.setName(entity.getName());
		result.setUuid(entity.getUid());
		return result;
	}
}
