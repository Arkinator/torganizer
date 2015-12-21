package torganizer.core.tournaments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.utils.EloCalculation;
import torganizer.utils.TristanPlayerInfo;

public class TrisTournamentPrinter {
	private final TrisTournament tournament;
	private final Map<UUID, Player> playerCache = new HashMap<>();
	private final Map<UUID, Team> teamCache = new HashMap<>();

	public TrisTournamentPrinter(final TrisTournament tournament, final GlobalObjectService globalObjectService) {
		this.tournament = tournament;
		for (final UUID playerId : tournament.getParticipants()) {
			final Player p = globalObjectService.getPlayerById(playerId);
			playerCache.put(playerId, p);
			if (p.getTeamUid() != null) {
				teamCache.put(p.getTeamUid(), globalObjectService.getTeamById(p.getTeamUid()));
			}
		}
	}

	public String printStanding() {
		final StringBuilder result = new StringBuilder();
		int place = 1;
		for (final TristanPlayerInfo info : tournament.getStanding()) {
			result.append(place++);
			result.append(".\t");
			result.append(playerCache.get(info.getPlayer()).getName());
			result.append(" (");
			result.append(info.getElo());
			result.append(")\n");
		}
		return result.toString();
	}

	public String printMatchesForRound(final int round) {
		final StringBuilder result = new StringBuilder();
		final DecimalFormat df = new DecimalFormat("#.0");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(round)) {
			result.append(playerCache.get(match.getSideA()).getName());
			result.append(" vs ");
			if (match.getSideB() != null) {
				result.append(playerCache.get(match.getSideB()).getName());
			}
			result.append(" ");
			result.append(match.getScoreSideA());
			result.append("-");
			result.append(match.getScoreSideB());
			result.append(" (");
			if (match.getSideB() != null) {
				final EloCalculation elo = new EloCalculation(tournament.getInfo(match.getSideA()).getEloForRound(round),
						tournament.getInfo(match.getSideB()).getEloForRound(round));
				if ((match.getScoreSideA() == 0) && (match.getScoreSideB() == 0)) {
					elo.setFactualResult(0);
				} else {
					elo.setFactualResult(match.getFinalScore());
				}
				final double deltaElo = elo.getPlayerBAdjustment() * TrisTournament.eloSpeedupFactor;
				result.append(df.format(deltaElo));
			}
			result.append(")\n");
		}
		return result.toString();
	}

	public String printLiquipediaPage() {
		final StringBuilder result = new StringBuilder();
		final TrisTournamentLiquipediStatisticsPrinter statistics = new TrisTournamentLiquipediStatisticsPrinter(tournament, playerCache, teamCache, result);
		final TrisTournamentLiquipediaMatchInfoPrinter printer = new TrisTournamentLiquipediaMatchInfoPrinter(tournament, playerCache, teamCache, result);
		final TrisTournamentLiquipediaStandingsPrinter standingsPrinter = new TrisTournamentLiquipediaStandingsPrinter(tournament, playerCache, teamCache, result);

		statistics.execute();
		result.append("=");
		result.append(tournament.getName());
		result.append("=\n");
		result.append("{{tabs start|title1=Overview");
		for (int i = 1; i <= tournament.getNumberOfRounds(); i++) {
			result.append("|title" + (i + 1) + "=Week " + i);
		}
		result.append("}}\n");
		result.append("\n{{tab 1}}\n\n==Overview==\n");
		result.append("===Current Standings===\n");
		standingsPrinter.execute(tournament.getCurrentRound() + 1);
		statistics.printDetailedStatistics();

		for (int i = 0; i < tournament.getNumberOfRounds(); i++) {
			result.append("\n{{tab " + (i + 2) + "}}\n\n==Results Week " + (i + 1) + "==\n");
			result.append("===Standings===\n");
			standingsPrinter.execute(i);
			result.append("\n===Matches===\n");
			printer.execute(i);
		}
		return result.toString();
	}
}
