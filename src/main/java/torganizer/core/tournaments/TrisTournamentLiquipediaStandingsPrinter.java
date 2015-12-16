package torganizer.core.tournaments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.utils.TristanPlayerInfo;

public class TrisTournamentLiquipediaStandingsPrinter extends TrisTournamentLiquipediaBasicPrinter {
	public TrisTournamentLiquipediaStandingsPrinter(final TrisTournament tournament, final Map<UUID, Player> playerCache, final Map<UUID, Team> teamCache,
			final StringBuilder result) {
		super(tournament, playerCache, teamCache, result);
	}

	public String execute(final int round) {
		final DecimalFormat df = new DecimalFormat("#.0");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		printLiquipediaHeaderStandings();
		int place = 1;
		for (final TristanPlayerInfo info : produceStandingForRound(round + 1)) {
			final Player player = playerCache.get(info.getPlayer());
			if (place <= 8) {
				result.append(" bgcolor=\"#B8F2B8\" ");
			}
			result.append("\n|");
			result.append(place++);
			result.append("||{{");
			result.append(info.getRaceForRound(tournament.getCurrentRound()).getLiquipediaCode());
			result.append("}}||");
			result.append(player.getLeague().getLiquipediaImageCode());
			result.append("||");
			printPlayerTeamAndName(player);
			printPlayerStrikes(info, round + 1);
			result.append("||");
			result.append(player.getBattleNetCode());
			result.append("||'''");
			result.append(df.format(info.getEloForRound(round + 1)));
			result.append("'''\n|-");
		}
		result.append("\n|}");
		return result.toString();
	}

	private List<TristanPlayerInfo> produceStandingForRound(final int round) {
		final List<TristanPlayerInfo> result = new ArrayList<TristanPlayerInfo>();
		tournament.getStanding().forEach(info -> {
			if (info.isEligibleToPlay(round)) {
				result.add(info);
			}
		});
		Collections.sort(result, (o1, o2) -> Double.compare(o2.getEloForRound(round), o1.getEloForRound(round)));
		return result;
	}

	private void printLiquipediaHeaderStandings() {
		result.append("{|class=\"wikitable sortable\" style=\"width:500px\"\n");
		result.append("!colspan=15|Current Standing\n");
		result.append("|-\n");
		result.append("!width=5px|<!-- Rank -->\n");
		result.append("!width=5px|<!-- Race -->\n");
		result.append("!width=5px|<!-- League -->\n");
		result.append("!width=100px|ID\n");
		result.append("!width=10px|SC2-Code\n");
		result.append("!width=25px|ELO\n");
		result.append("|-");
	}
}
