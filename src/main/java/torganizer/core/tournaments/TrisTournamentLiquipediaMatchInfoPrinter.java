package torganizer.core.tournaments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.utils.EloCalculation;
import torganizer.utils.TristanPlayerInfo;

public class TrisTournamentLiquipediaMatchInfoPrinter {
	private static final String strikeSymbolLink = "[[File:Ambox-warning-32px.png|17px]] ";
	private final TrisTournament tournament;
	private final Map<UUID, Player> playerCache;
	private final Map<UUID, Team> teamCache;
	private StringBuilder result = new StringBuilder();
	private final DecimalFormat df;

	public TrisTournamentLiquipediaMatchInfoPrinter(final TrisTournament tournament, final Map<UUID, Player> playerCache, final Map<UUID, Team> teamCache) {
		this.playerCache = playerCache;
		this.teamCache = teamCache;
		this.tournament = tournament;
		df = new DecimalFormat("#.0");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
	}

	public String execute(final int round) {
		result = new StringBuilder();
		printLiquipediaHeaderMatches(round);
		for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(round)) {
			printMatchInfo(match, round);
		}
		result.append("|}");
		return result.toString();
	}

	private void printMatchInfo(final BestOfMatchSinglePlayer match, final int round) {
		final Player playerA = playerCache.get(match.getSideA());
		final Player playerB = playerCache.get(match.getSideB());
		if ((playerA == null) && (playerB == null)) {
			return;
		}
		if (playerA == null) {
			printBye(round, match, playerB);
			return;
		}
		if (playerB == null) {
			printBye(round, match, playerA);
			return;
		}
		final TristanPlayerInfo infoA = tournament.getInfo(match.getSideA());
		final TristanPlayerInfo infoB = tournament.getInfo(match.getSideB());
		final EloCalculation elo = new EloCalculation(tournament.getInfo(match.getSideA()).getEloForRound(round), tournament.getInfo(match.getSideB()).getEloForRound(round));
		final boolean isPlayed = (match.getScoreSideA() != 0) || (match.getScoreSideB() != 0);
		if (isPlayed) {
			elo.setFactualResult(((double) match.getScoreSideA()) / (match.getScoreSideA() + match.getScoreSideB()));
		} else {
			elo.setFactualResult(0.);
		}

		final double deltaElo = elo.getPlayerAAdjustment() * TrisTournament.eloSpeedupFactor;

		String styleCellA = "";
		String styleCellB = "";
		if (isPlayed) {
			if (match.getWinner().equals(match.getSideA())) {
				styleCellA = "background: #B8F2B8;";
				styleCellB = "background: #F2B8B8;";
			} else {
				styleCellB = "background: #B8F2B8;";
				styleCellA = "background: #F2B8B8;";
			}
		}
		final String cellDividerA = "||style=\"" + styleCellA + "\"|";
		final String cellDividerB = "||style=\"" + styleCellB + "\"|";
		result.append(cellDividerA.substring(1));
		result.append(df.format(infoA.getEloForRound(round)));
		result.append(cellDividerA);
		printPlayerTeamInfo(playerA);
		result.append(" ||style=\"" + styleCellA + " text-align:right;\"|");
		result.append(playerA.getName());
		result.append(" ");
		result.append(playerA.getLeague().getLiquipediaImageCode());
		result.append(" {{");
		result.append(playerA.getRace().getLiquipediaCode() + "}}");
		printPlayerStrikes(infoA, round);
		result.append(" ||style=\"text-align:center;\"|");
		if (isPlayed) {
			result.append("'''" + match.getScoreSideA() + " : " + match.getScoreSideB() + "'''");
		} else {
			result.append(" vs ");
		}
		result.append(cellDividerB);
		printPlayerStrikes(infoB, round);
		result.append("{{");
		result.append(playerB.getRace().getLiquipediaCode() + "}} ");
		result.append(playerB.getLeague().getLiquipediaImageCode());
		result.append(" ");
		result.append(playerB.getName());
		result.append(cellDividerB);
		printPlayerTeamInfo(playerB);
		result.append(cellDividerB);
		result.append(df.format(infoB.getEloForRound(round)));
		result.append(" ||");
		if (!isPlayed) {
			elo.setFactualResult(0);
			result.append("(" + df.format(-deltaElo) + ")");
		} else {
			elo.setFactualResult(match.getFinalScore());
			result.append("'''" + df.format(deltaElo) + "'''");
		}
		result.append("\n|-\n");
	}

	private void printBye(final int round, final BestOfMatchSinglePlayer match, final Player player) {
		final TristanPlayerInfo info = tournament.getInfo(player.getUid());
		final EloCalculation elo = new EloCalculation(tournament.getInfo(player.getUid()).getEloForRound(round), tournament.getInfo(player.getUid()).getEloForRound(round));
		final boolean isPlayed = (match.getScoreSideA() != 0) || (match.getScoreSideB() != 0);
		elo.setFactualResult(((double) match.getScoreSideA()) / (match.getScoreSideA() + match.getScoreSideB()));

		final double deltaElo = elo.getPlayerAAdjustment() * TrisTournament.eloSpeedupFactor;

		final String styleCellA = "background: #B8F2B8;";
		final String styleCellB = "background: #F2B8B8;";
		final String cellDividerA = "||style=\"" + styleCellA + "\"|";
		final String cellDividerB = "||style=\"" + styleCellB + "\"|";
		result.append(cellDividerA.substring(1));
		result.append(df.format(info.getEloForRound(round)));
		result.append(cellDividerA);
		printPlayerTeamInfo(player);
		result.append("||style=\"" + styleCellA + " text-align:right;\"|");
		result.append(player.getName());
		result.append(" ");
		result.append(player.getLeague().getLiquipediaImageCode());
		result.append(" {{");
		result.append(player.getRace().getLiquipediaCode() + "}}");
		printPlayerStrikes(info, round);
		result.append("||style=\"text-align:center;\"|");
		if (isPlayed) {
			result.append("'''" + match.getScoreSideA() + " : " + match.getScoreSideB() + "'''");
		} else {
			result.append(" vs ");
		}
		result.append(cellDividerB);
		result.append(" ");
		result.append(cellDividerB);
		result.append(" ");
		result.append(cellDividerB);
		result.append(" ");
		result.append("||");
		if (!isPlayed) {
			elo.setFactualResult(0);
			result.append("(" + df.format(-deltaElo) + ")");
		} else {
			elo.setFactualResult(match.getFinalScore());
			result.append("'''" + df.format(deltaElo) + "'''");
		}
		result.append("\n|-\n");
	}

	private void printPlayerStrikes(final TristanPlayerInfo info, final int round) {
		for (int i = 0; i < info.getNumberOfStrikes(round); i++) {
			result.append(strikeSymbolLink);
		}
	}

	private void printPlayerTeamInfo(final Player player) {
		if (player.getTeamUid() != null) {
			result.append("{{TeamPart/");
			result.append(teamCache.get(player.getTeamUid()).getLiquipediaName());
			result.append("}}");
		} else {
			result.append(" ");
		}
	}

	private void printLiquipediaHeaderMatches(final int round) {
		result.append("{|class=\"wikitable sortable\"\n");
		result.append("!colspan=15|Matches for Round " + (round + 1) + "\n");
		result.append("|-\n");
		result.append("!width=10px|ELO A\n");
		result.append("!width=20px|<!--Team A-->\n");
		result.append("!width=200px|ID A\n");
		result.append("!width=35px class=\"unsortable\" |<!-- vs -->\n");
		result.append("!width=200px|ID B\n");
		result.append("!width=20px|<!--Team B-->\n");
		result.append("!width=10px|ELO B\n");
		result.append("!width=10px|diff Elo\n");
		result.append("|-\n");
	}
}