package torganizer.core.tournaments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.utils.TristanPlayerInfo;

public class TrisTournamentLiquipediaBasicPrinter {
	protected static final String strikeSymbolLink = "[[File:Ambox-warning-32px.png|17px]] ";
	protected final TrisTournament tournament;
	protected final Map<UUID, Player> playerCache;
	protected final Map<UUID, Team> teamCache;
	protected final StringBuilder result;
	protected final DecimalFormat df;

	public TrisTournamentLiquipediaBasicPrinter(final TrisTournament tournament, final Map<UUID, Player> playerCache, final Map<UUID, Team> teamCache, final StringBuilder result) {
		this.playerCache = playerCache;
		this.teamCache = teamCache;
		this.tournament = tournament;
		this.result = result;
		df = new DecimalFormat("#.0");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
	}

	protected void printPlayerStrikes(final TristanPlayerInfo info, final int round) {
		for (int i = 0; i < info.getNumberOfStrikes(round); i++) {
			result.append(strikeSymbolLink);
		}
	}

	protected void printPlayerTeamAndName(final Player player) {
		if (player.getTeamUid() != null) {
			result.append("[[File:");
			result.append(teamCache.get(player.getTeamUid()).getLiquipediaFlagCode());
			result.append("]]");
		}
		result.append(player.getName());
	}
}