package torganizer.utils;

import java.util.HashMap;
import java.util.Map;

import torganizer.core.entities.Player;
import torganizer.core.tournaments.TrisTournament;

public class TristanPlayerInfo implements Comparable<TristanPlayerInfo> {
	private final Player player;
	private double elo;
	private double previousElo;
	private final Map<Player, Integer> roundOfLastEncounter;

	public TristanPlayerInfo(final Player player) {
		this.player = player;
		this.elo = EloCalculation.defaultEloValue;
		this.roundOfLastEncounter = new HashMap<>();
	}

	public void adjustElo(final double eloAdjustment) {
		this.elo += eloAdjustment * TrisTournament.eloSpeedupFactor;
	}

	public double getElo() {
		return elo;
	}

	public void setElo(final double elo) {
		this.previousElo = elo;
		this.elo = elo;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public int compareTo(final TristanPlayerInfo o) {
		return Double.compare(elo, o.getElo());
	}

	@Override
	public String toString() {
		return "[player=" + player + ", elo=" + elo + "]";
	}

	public void addEncounter(final Player opponent, final Integer round) {
		roundOfLastEncounter.put(opponent, round);
	}

	public Integer getRoundOfLastEncounter(final Player opponent) {
		return roundOfLastEncounter.get(opponent);
	}

	public boolean hasPlayerFacedOpponentBefore(final Player opponent) {
		return getRoundOfLastEncounter(opponent) != null;
	}

	public double getPreviousElo() {
		return previousElo;
	}
}
