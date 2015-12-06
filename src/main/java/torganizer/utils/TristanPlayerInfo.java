package torganizer.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import torganizer.core.tournaments.TrisTournament;

public class TristanPlayerInfo implements Comparable<TristanPlayerInfo> {
	private final UUID player;
	private double elo;
	private double previousElo;
	private final Map<UUID, Integer> roundOfLastEncounter;

	public TristanPlayerInfo(final UUID player) {
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

	public UUID getPlayer() {
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

	public void addEncounter(final UUID opponent, final Integer round) {
		roundOfLastEncounter.put(opponent, round);
	}

	public Integer getRoundOfLastEncounter(final UUID opponent) {
		return roundOfLastEncounter.get(opponent);
	}

	public boolean hasPlayerFacedOpponentBefore(final UUID opponent) {
		return getRoundOfLastEncounter(opponent) != null;
	}

	public double getPreviousElo() {
		return previousElo;
	}
}
