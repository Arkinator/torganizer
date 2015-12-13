package torganizer.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import torganizer.core.tournaments.TrisTournament;

public class TristanPlayerInfo implements Comparable<TristanPlayerInfo> {
	private final UUID player;
	private double elo;
	private final List<Double> eloPerRound;
	private final Map<UUID, Integer> roundOfLastEncounter;
	private final List<Integer> strikes;

	public TristanPlayerInfo(final UUID player) {
		this.player = player;
		this.eloPerRound = new ArrayList<>();
		this.elo = EloCalculation.defaultEloValue;
		this.roundOfLastEncounter = new HashMap<>();
		this.strikes = new ArrayList<>();
	}

	public void adjustElo(final double eloAdjustment) {
		eloPerRound.add(this.elo);
		this.elo += eloAdjustment * TrisTournament.eloSpeedupFactor;
	}

	public double getElo() {
		return elo;
	}

	public void setElo(final double elo) {
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

	public boolean hasPlayerFacedOpponentBeforeInNMatches(final UUID opponent, final int threshold, final int round) {
		final Integer lastEncounter = getRoundOfLastEncounter(opponent);
		if (lastEncounter == null) {
			return false;
		}
		return (round - lastEncounter) <= threshold;
	}

	public double getEloForRound(final int round) {
		if (round >= eloPerRound.size()) {
			return elo;
		}
		return eloPerRound.get(round);
	}

	public void addStrike(final int round) {
		strikes.add(round);
	}

	public boolean isEligibleToPlay() {
		return getNumberOfStrikes() < 3;
	}

	public boolean isEligibleToPlay(final int round) {
		return getNumberOfStrikes(round) < 3;
	}

	public int getNumberOfStrikes() {
		return strikes.size();
	}

	public int getNumberOfStrikes(final int round) {
		int numberOfStrikes = 0;
		for (final Integer strike : strikes) {
			if (strike < round) {
				numberOfStrikes++;
			}
		}
		return numberOfStrikes;
	}
}
