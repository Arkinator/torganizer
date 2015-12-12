package torganizer.core.test;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.GenericMatch;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.core.tournaments.TrisTournament;
import torganizer.core.tournaments.TrisTournamentPrinter;
import torganizer.core.types.StarcraftLeague;
import torganizer.core.types.StarcraftRace;
import torganizer.utils.EloCalculation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-module.xml" })
public class EloTesting {
	@Autowired
	private GlobalObjectService globalObjectService;
	private TrisTournament tournament;

	public static String[][] playerInfo = { //
			{ "2000", "391", "Silver", "P", "AI" }, //
			{ "1900", "882", "Platinum", "Z", "AI" }, //
			{ "1800", "1459", "Diamond", "T", "UR" }, //
			{ "1700", "887", "Master", "T", "UR" }, //
			{ "1600", "809", "Master", "T", "UR" }, //
			{ "1500", "552", "Gold", "Z", "UR" }, //
			{ "1850", "359", "Platinum", "P", "UR" }, //
			{ "1650", "987", "Bronze", "P", "UR" }, //
			{ "1750", "365", "Gold", "T", "UR" }, //
			{ "1950", "365", "Gold", "T", "UR" } };
	public static String[][] teamInfo = { { "Team Unrivaled", "UR", "team unrivaled", "UnrivaledMini.png" }, //
			{ "All Inspiration", "AI", "all-inspiration", "All-Inspirationlogo_std.png" }, //
			{ "Guns and Roaches", "GR", "guns and roaches", "GunsandRoachesMini.png" }, //
			{ "CTL Team 6", "C6", "ctl team 6", "CT6logo_std.png" }, //
			{ "Outset Gaming", "OG", "outset gaming", "Outsetlogo_std.png" }, //
			{ "Formless Gaming", "FG", "formless gaming", "Formlesslogo_std.png" }, //
			{ "Born Gosu", "BG", "born gosu", "BornGosulogo_std.png" } };

	public static Map<String, UUID> teamMap = new HashMap<>();

	@Test
	public void testCorrectMatches() {
		addTeams();

		final List<UUID> playerList = new ArrayList<>();

		for (final String[] infoStrings : playerInfo) {
			final Player p = new Player(infoStrings[0]);
			p.setBattleNetCode(Integer.parseInt(infoStrings[1]));
			p.setRace(StarcraftRace.parseShort(infoStrings[3]));
			p.setLeague(StarcraftLeague.valueOf(infoStrings[2]));
			playerList.add(p.getUid());
			if (infoStrings.length > 4) {
				p.setTeamUid(teamMap.get(infoStrings[4]));
			}
			globalObjectService.addPlayer(p);
		}

		tournament = new TrisTournament(10, 3, playerList, "#UNIT");
		final TrisTournamentPrinter printer = new TrisTournamentPrinter(tournament, globalObjectService);

		tournament.initializeEloValues(globalObjectService, new double[] { 1500., 1600., 1700., 1800., 1900., 2000., 2100. });
		for (int i = 0; i < 10; i++) {
			for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(i)) {
				final Player pA = globalObjectService.getPlayerById(match.getSideA());
				final Player pB = globalObjectService.getPlayerById(match.getSideB());
				final EloCalculation calc = new EloCalculation(Double.parseDouble(pA.getName()), Double.parseDouble(pB.getName()));
				int scoreA = 0;
				int scoreB = 0;
				for (int j = 0; j < 3; j++) {
					if (Math.random() < calc.getExpectedScore()) {
						scoreA++;
					} else {
						scoreB++;
					}
					if ((scoreA == 2) || (scoreB == 2)) {
						break;
					}
				}
				match.submitResultAdmin(pA.getUid(), scoreA, scoreB);
			}
		}
		final String p = printer.printLiquipediaPage();
		System.out.println(p);
		final Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		final StringSelection data = new StringSelection(p);
		clipBoard.setContents(data, data);
	}

	private void addTeams() {
		for (final String[] strings : teamInfo) {
			final Team t = new Team(strings[0]);
			t.setShortName(strings[1]);
			t.setLiquipediaFlagCode(strings[3]);
			t.setLiquipediaName(strings[2]);
			globalObjectService.addTeam(t);
			teamMap.put(t.getShortName(), t.getUid());
		}
	}

	private void playMatch(final int round, final String playerName, final int scorePlayer, final int scoreOtherPlayer) {
		final UUID playerUid = globalObjectService.getPlayerByName(playerName).getUid();
		for (final GenericMatch match : tournament.getMatchesForRound(0)) {
			if (match.getSideA().equals(playerUid) || match.getSideB().equals(playerUid)) {
				match.submitResultAdmin(playerUid, scorePlayer, scoreOtherPlayer);
			}
		}
	}
}
