package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.types.StarcraftRace;
import torganizer.utils.TristanPlayerInfo;

public class TrisTournamentLiquipediStatisticsPrinter extends TrisTournamentLiquipediaBasicPrinter {
	public TrisTournamentLiquipediStatisticsPrinter(final TrisTournament tournament, final Map<UUID, Player> playerCache, final Map<UUID, Team> teamCache,
			final StringBuilder result) {
		super(tournament, playerCache, teamCache, result);
	}

	public void execute() {
		result.append("\n{{Infobox league ");
		result.append("|name=");
		result.append(tournament.getName());
		result.append("|image=");
		result.append("URlogo.png");
		result.append("|organizer=");
		result.append("Team Unrivaled");
		result.append("|server=");
		result.append("NA");
		result.append("|type=");
		result.append("Weekly Online");
		result.append("|format=");
		result.append("ELO-based Swiss with Playoff for Top 8");
		result.append("|sdate=");
		result.append("11.12.2015");
		result.append("|edate=");
		result.append("February 2016");
		result.append("|player_number=");
		result.append(tournament.getParticipants().size());
		result.append("|protoss_number=");
		result.append(getRaceCount(0, StarcraftRace.Protoss));
		result.append("|terran_number=");
		result.append(getRaceCount(0, StarcraftRace.Terran));
		result.append("|zerg_number=");
		result.append(getRaceCount(0, StarcraftRace.Zerg));
		result.append("|random_number=");
		result.append(getRaceCount(0, StarcraftRace.Random));
		result.append("}}\n\n");
	}

	private void printRacialDistribution() {
		result.append("\n{{RaceDist|title=|protoss=");
		result.append(getRaceCount(0, StarcraftRace.Protoss));
		result.append("|terran=");
		result.append(getRaceCount(0, StarcraftRace.Terran));
		result.append("|zerg=");
		result.append(getRaceCount(0, StarcraftRace.Zerg));
		result.append("|random=");
		result.append(getRaceCount(0, StarcraftRace.Random));
		result.append("}}");
	}

	private int getRaceCount(final int round, final StarcraftRace race) {
		int count = 0;
		for (final UUID playerId : tournament.getParticipants()) {
			if (tournament.getInfo(playerId).getRaceForRound(tournament.getCurrentRound()).equals(race)) {
				count++;
			}
		}
		return count;
	}

	public void printDetailedStatistics() {
		printParticipantStatistics();
		// printRacialDistribution();
	}

	private void printParticipantStatistics() {
		result.append("\n===Participants===\n");
		final List<Player> pPlayers = getAllPlayersForRace(StarcraftRace.Protoss);
		final List<Player> tPlayers = getAllPlayersForRace(StarcraftRace.Terran);
		final List<Player> zPlayers = getAllPlayersForRace(StarcraftRace.Zerg);
		final List<Player> rPlayers = getAllPlayersForRace(StarcraftRace.Random);
		printParticipantTableHeader(pPlayers.size(), tPlayers.size(), zPlayers.size(), rPlayers.size());
		while (!(pPlayers.isEmpty() && tPlayers.isEmpty() && zPlayers.isEmpty() && rPlayers.isEmpty())) {
			result.append("|");
			if (!pPlayers.isEmpty()) {
				printPlayerInfo(pPlayers.remove(0));
			}
			result.append("\n|");
			if (!tPlayers.isEmpty()) {
				printPlayerInfo(tPlayers.remove(0));
			}
			result.append("\n|");
			if (!zPlayers.isEmpty()) {
				printPlayerInfo(zPlayers.remove(0));
			}
			result.append("\n|");
			if (!rPlayers.isEmpty()) {
				printPlayerInfo(rPlayers.remove(0));
			}
			result.append("\n|-\n");
		}
		result.append("|}\n");
	}

	private void printPlayerInfo(final Player p) {
		final TristanPlayerInfo info = tournament.getInfo(p.getUid());
		printPlayerStrikes(info, tournament.getNumberOfRounds());
		result.append("{{");
		result.append(info.getRaceForRound(tournament.getCurrentRound()).getLiquipediaCode() + "}} ");
		result.append(p.getLeague().getLiquipediaImageCode());
		result.append(" ");
		printPlayerTeamAndName(p);
	}

	private void printParticipantTableHeader(final int p, final int t, final int z, final int r) {
		result.append("\n{| class=\"wikitable\"\n");
		result.append("|style=\"width:250px;background-color:{{RaceColor|p}};text-align:center;\" | '''{{p}} Protoss ''(" + p + ")'''''\n");
		result.append("|style=\"width:250px;background-color:{{RaceColor|t}};text-align:center;\" | '''{{t}} Terran ''(" + t + ")'''''\n");
		result.append("|style=\"width:250px;background-color:{{RaceColor|z}};text-align:center;\" | '''{{z}} Zerg ''(" + z + ")'''''\n");
		result.append("|style=\"width:250px;background-color:{{RaceColor|r}};text-align:center;\" | '''{{r}} Random ''(" + r + ")'''''\n");
		result.append("|-\n");
	}

	private List<Player> getAllPlayersForRace(final StarcraftRace race) {
		final List<Player> result = new ArrayList<>();
		for (final UUID playerId : tournament.getParticipants()) {
			if (tournament.getInfo(playerId).getRaceForRound(tournament.getCurrentRound()).equals(race)) {
				result.add(playerCache.get(playerId));
			}
		}
		Collections.sort(result, (o1, o2) -> o2.getLeague().compareTo(o1.getLeague()));
		return result;
	}
}
