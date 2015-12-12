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
		final DecimalFormat df = new DecimalFormat("#.00");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(round)) {
			result.append(playerCache.get(match.getSideA()).getName());
			result.append(" vs ");
			result.append(playerCache.get(match.getSideB()).getName());
			result.append(" ");
			result.append(match.getScoreSideA());
			result.append("-");
			result.append(match.getScoreSideB());
			result.append(" (");
			final EloCalculation elo = new EloCalculation(tournament.getInfo(match.getSideA()).getPreviousElo(), tournament.getInfo(match.getSideB()).getPreviousElo());
			if ((match.getScoreSideA() == 0) && (match.getScoreSideB() == 0)) {
				elo.setFactualResult(0);
			} else {
				elo.setFactualResult(match.getFinalScore());
			}
			final double deltaElo = elo.getPlayerBAdjustment() * TrisTournament.eloSpeedupFactor;
			result.append(df.format(deltaElo));
			result.append(")\n");
		}
		return result.toString();
	}

	public String printLiquipediaPage() {
		final StringBuilder result = new StringBuilder();
		result.append("=#UNIT - Season 1=\n==Standings==\n");
		result.append(printStandingsLiquipedia(0));
		result.append("\n==Matches==\n");
		result.append("{{tabs start");
		for (int i = 1; i <= tournament.getNumberOfRounds(); i++) {
			result.append("|title" + i + "=Week " + i);
		}
		result.append("}}\n");
		for (int i = 0; i < tournament.getNumberOfRounds(); i++) {
			result.append("\n{{tab " + (i + 1) + "}}\n\n===Results Week " + (i + 1) + "===\n");
			result.append(printMatchesLiquipedia(i));
		}
		return result.toString();
	}

	public String printStandingsLiquipedia(final int round) {
		final StringBuilder result = new StringBuilder();
		final DecimalFormat df = new DecimalFormat("#.00");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		printLiquipediaHeaderStandings(result);
		int place = 1;
		for (final TristanPlayerInfo info : tournament.getStanding()) {
			final Player player = playerCache.get(info.getPlayer());
			result.append("|");
			result.append(place++);
			result.append("||{{");
			result.append(player.getRace().getLiquipediaCode());
			result.append("}}||");
			result.append(player.getLeague().getLiquipediaImageCode());
			result.append("||");
			result.append(printPlayerInfo(player));
			result.append("||");
			result.append(player.getBattleNetCode());
			result.append("||'''");
			result.append(df.format(info.getElo()));
			result.append("'''\n|-\n");
		}
		result.append("|}");
		return result.toString();
	}

	private String printPlayerInfo(final Player player) {
		String result = "";
		if (player.getTeamUid() != null) {
			result += "[[File:" + teamCache.get(player.getTeamUid()).getLiquipediaFlagCode() + "]]";
		}
		result += player.getName();
		return result;
	}

	private void printLiquipediaHeaderStandings(final StringBuilder result) {
		result.append("{|class=\"wikitable sortable\" style=\"width:500px\"\n");
		result.append("!colspan=15|Current Standing\n");
		result.append("|-\n");
		result.append("!width=5px|<!-- Rank -->\n");
		result.append("!width=5px|<!-- Race -->\n");
		result.append("!width=5px|<!-- League -->\n");
		result.append("!width=100px|ID\n");
		result.append("!width=10px|SC2-Code\n");
		result.append("!width=25px|ELO\n");
		result.append("|-\n");
	}

	public String printMatchesLiquipedia(final int round) {
		final StringBuilder result = new StringBuilder();
		final DecimalFormat df = new DecimalFormat("#.00");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		printLiquipediaHeaderMatches(result, round);
		for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(round)) {
			if (((match.getSideA() == null) || (match.getSideB() == null))) {
				break;
			}
			final Player playerA = playerCache.get(match.getSideA());
			final Player playerB = playerCache.get(match.getSideB());
			final TristanPlayerInfo infoA = tournament.getInfo(match.getSideA());
			final TristanPlayerInfo infoB = tournament.getInfo(match.getSideB());
			final EloCalculation elo = new EloCalculation(tournament.getInfo(match.getSideA()).getPreviousElo(), tournament.getInfo(match.getSideB()).getPreviousElo());
			final boolean isPlayed = (match.getScoreSideA() != 0) || (match.getScoreSideB() != 0);
			if (isPlayed) {
				elo.setFactualResult(((double) match.getScoreSideA()) / (match.getScoreSideA() + match.getScoreSideB()));
			} else {
				elo.setFactualResult(0.);
			}

			final double deltaElo = elo.getPlayerAAdjustment() * TrisTournament.eloSpeedupFactor;

			String cellDividerA = "||";
			String cellDividerB = "||";
			if (isPlayed) {
				if (match.getWinner().equals(match.getSideA())) {
					cellDividerA = "||style=\"background: #B8F2B8;\"|";
					cellDividerB = "||style=\"background: #F2B8B8;\"|";
				} else {
					cellDividerB = "||style=\"background: #B8F2B8;\"|";
					cellDividerA = "||style=\"background: #F2B8B8;\"|";
				}
			}
			result.append(cellDividerA.substring(1));
			result.append(infoA.getPreviousElo());
			result.append(cellDividerA);
			if (playerA.getTeamUid() != null) {
				result.append("[[File:");
				result.append(teamCache.get(playerA.getTeamUid()).getLiquipediaFlagCode());
				result.append("]]");
			}
			result.append(cellDividerA);
			result.append(playerA.getName());
			result.append(" ");
			result.append(playerA.getLeague().getLiquipediaImageCode());
			result.append(" {{");
			result.append(playerA.getRace().getLiquipediaCode() + "}}");
			result.append("||style=\"text-align:center;\"|");
			if (isPlayed) {
				result.append("'''" + match.getScoreSideA() + " : " + match.getScoreSideB() + "'''");
			} else {
				result.append(" vs ");
			}
			result.append(cellDividerB + "{{");
			result.append(playerB.getRace().getLiquipediaCode() + "}} ");
			result.append(playerB.getLeague().getLiquipediaImageCode());
			result.append(" ");
			result.append(playerB.getName());
			result.append(cellDividerB);
			if (playerB.getTeamUid() != null) {
				result.append("[[File:");
				result.append(teamCache.get(playerB.getTeamUid()).getLiquipediaFlagCode());
				result.append("]]");
			}
			result.append(cellDividerB);
			result.append(infoB.getPreviousElo());
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
		result.append("|}");
		return result.toString();
	}

	private void printLiquipediaHeaderMatches(final StringBuilder result, final int round) {
		result.append("{|class=\"wikitable sortable\"\n");
		result.append("!colspan=15|Matches for Round " + (round + 1) + "\n");
		result.append("|-\n");
		result.append("!width=10px|ELO A\n");
		result.append("!width=20px|<!--Team A-->\n");
		result.append("!width=200px style=\"text-align: center;\"|ID A\n");
		result.append("!width=25px class=\"unsortable\" |<!-- vs -->\n");
		result.append("!width=200px|ID B\n");
		result.append("!width=20px|<!--Team B-->\n");
		result.append("!width=10px|ELO B\n");
		result.append("!width=10px|Δ Elo\n");
		result.append("|-\n");
	}
}
