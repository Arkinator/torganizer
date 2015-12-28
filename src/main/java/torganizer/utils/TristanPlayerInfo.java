package torganizer.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.core.persistance.orm.TristanInfoOrm;
import torganizer.core.tournaments.TrisTournament;
import torganizer.core.types.StarcraftRace;

public class TristanPlayerInfo implements Comparable<TristanPlayerInfo> {
	private final Player player;
	private final StarcraftRace initialRace;
	private double elo;
	private final List<Double> eloPerRound;
	private final Map<UUID, Integer> roundOfLastEncounter;
	private final List<Integer> strikes;
	private final Map<Integer, StarcraftRace> raceSwitches;
	private boolean isOnHoliday = false;

	public TristanPlayerInfo(final Player player) {
		this.player = player;
		this.eloPerRound = new ArrayList<>();
		this.elo = EloCalculation.defaultEloValue;
		this.roundOfLastEncounter = new HashMap<>();
		this.raceSwitches = new HashMap<>();
		this.strikes = new ArrayList<>();
		this.initialRace = player.getRace();
	}

	public TristanPlayerInfo(final TristanInfoOrm info, final GlobalObjectService globalObjectService) {
		this.player = globalObjectService.getPlayerById(info.getPlayerId());

	}

	public void adjustElo(final double eloAdjustment) {
		eloPerRound.add(this.elo);
		this.elo += eloAdjustment * TrisTournament.eloSpeedupFactor;
	}

	public void adjustElo(final double eloAdjustment, final int round) {
		if (eloPerRound.size() > (round + 1)) {
			throw new RuntimeException("You can not report ELO for a past round");
		}
		if (eloPerRound.size() == (round + 1)) {
			this.elo = eloPerRound.remove(round);
		}
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
		return player.getUid();
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

	public Integer getRoundOfLastEncounter(final UUID opponent, final int round) {
		final Integer result = roundOfLastEncounter.get(opponent);
		if ((result == null) || (result == round)) {
			return null;
		} else {
			return result;
		}
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
		return (getNumberOfStrikes() < 3) && (!isOnHoliday);
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

	public StarcraftRace getRaceForRound(final int round) {
		StarcraftRace race = initialRace;
		int highestRaceSwitchRound = -1;
		for (final Entry<Integer, StarcraftRace> raceSwitch : raceSwitches.entrySet()) {
			if ((raceSwitch.getKey() < round) && (raceSwitch.getKey() > highestRaceSwitchRound)) {
				highestRaceSwitchRound = raceSwitch.getKey();
				race = raceSwitch.getValue();
			}
		}
		return race;
	}

	public void addRaceChangeForRound(final int round, final StarcraftRace newRace) {
		raceSwitches.put(round, newRace);
	}

	public void clearEncounterForRound(final int round) {
		for (final Entry<UUID, Integer> entry : roundOfLastEncounter.entrySet()) {
			if (entry.getValue().equals(round)) {
				roundOfLastEncounter.remove(entry.getKey());
				return;
			}
		}
	}

	public void setHoliday(final boolean isOnHoliday) {
		this.isOnHoliday = isOnHoliday;
	}

	public boolean isOnHoliday() {
		return isOnHoliday;
	}
}
