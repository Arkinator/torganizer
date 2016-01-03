package torganizer.core.types;

import torganizer.core.entities.Player;
import torganizer.core.persistance.orm.MatchOrm;
import torganizer.exceptions.NotYetImplementedException;

public class PlayerShortInfo implements IPlayerShortInfo {
	private enum PlayerSideMapping {
		sideA, sideB
	}

	private final StarcraftRace race;
	private final StarcraftLeague league;
	private final Double elo;
	private final PlayerSideMapping side;
	private final MatchOrm match;

	public static IPlayerShortInfo createSideAInfo(final Player player, final MatchOrm match) {
		if (player == null) {
			return new NullPlayerInfo();
		}
		return new PlayerShortInfo(player, PlayerSideMapping.sideA, match);
	}

	public static IPlayerShortInfo createSideBInfo(final Player player, final MatchOrm match) {
		if (player == null) {
			return new NullPlayerInfo();
		}
		return new PlayerShortInfo(player, PlayerSideMapping.sideB, match);
	}

	public static IPlayerShortInfo loadSideAInfo(final MatchOrm match) {
		return new PlayerShortInfo(PlayerSideMapping.sideA, match);
	}

	public static IPlayerShortInfo loadSideBInfo(final MatchOrm match) {
		return new PlayerShortInfo(PlayerSideMapping.sideB, match);
	}

	private PlayerShortInfo(final Player player, final PlayerSideMapping side, final MatchOrm match) {
		this.race = player.getRace();
		this.league = player.getLeague();
		this.elo = null;
		this.side = side;
		this.match = match;
		updateOrm();
	}

	private PlayerShortInfo(final PlayerSideMapping side, final MatchOrm match) {
		if (side == PlayerSideMapping.sideA) {
			this.race = match.getSideARace();
			this.league = match.getSideALeague();
		} else {
			this.race = match.getSideBRace();
			this.league = match.getSideBLeague();
		}
		this.elo = null;
		this.side = side;
		this.match = match;
	}

	private void updateOrm() {
		if (side == PlayerSideMapping.sideA) {
			match.setSideALeague(getLeague());
			match.setSideARace(getRace());
		} else {
			match.setSideBLeague(getLeague());
			match.setSideBRace(getRace());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see torganizer.core.types.IPlayerShortInfo#getRace()
	 */
	@Override
	public StarcraftRace getRace() {
		return race;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see torganizer.core.types.IPlayerShortInfo#getLeague()
	 */
	@Override
	public StarcraftLeague getLeague() {
		return league;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see torganizer.core.types.IPlayerShortInfo#getElo()
	 */
	@Override
	public double getElo() {
		if (elo == null) {
			throw new NotYetImplementedException("ELO not yet bound to player! (look at the constructor, smartass)");
		}
		return elo;
	}
}
