package torganizer.core.persistance.orm;

import org.springframework.stereotype.Component;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.Game;
import torganizer.core.types.MatchType;

@Component(value = "ormFactory")
public class OrmFactory {
	public PlayerOrm getPlayerOrm(final Player p) {
		final EntityOrm entityOrm = getEntityOrm(p);
		final PlayerOrm player = new PlayerOrm();
		player.setEntity(entityOrm);
		entityOrm.setPlayer(player);
		player.setAdmin(p.isAdmin());
		return player;
	}

	public EntityOrm getEntityOrm(final AbstractToEntity entity) {
		final EntityOrm result = new EntityOrm();
		result.setName(entity.getName());
		result.setUuid(entity.getUid());
		return result;
	}

	public MatchOrm getGameOrm(final Game game) {
		final EntityOrm entityOrm = getEntityOrm(game);
		final MatchOrm gameOrm = new MatchOrm();
		gameOrm.setEntity(entityOrm);
		entityOrm.setMatch(gameOrm);
		gameOrm.setSideASubmittedWinner(game.getSideASubmittedWinner());
		gameOrm.setSideBSubmittedWinner(game.getSideBSubmittedWinner());
		gameOrm.setAdminSubmittedWinner(game.getAdminSubmittedWinner());
		gameOrm.setWinner(game.getWinner());
		gameOrm.setSideAId(game.getSideA());
		gameOrm.setSideBId(game.getSideB());
		gameOrm.setType(MatchType.GAME);
		return gameOrm;
	}
}
