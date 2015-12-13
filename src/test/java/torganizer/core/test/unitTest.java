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
import torganizer.core.matches.GenericMatch;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.core.tournaments.TrisTournament;
import torganizer.core.tournaments.TrisTournamentPrinter;
import torganizer.core.types.StarcraftLeague;
import torganizer.core.types.StarcraftRace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-module.xml" })
public class unitTest {
	@Autowired
	private GlobalObjectService globalObjectService;
	private TrisTournament tournament;

	public static String[][] playerInfo = { { "Acadien", "391", "Diamond", "P", "AI" }, { "Athreos", "882", "Platinum", "Z", "AI" }, { "Apogee", "1459", "Diamond", "T", "UR" },
			{ "Blaze", "887", "Master", "T", "UR" }, { "Broda", "809", "Master", "T", "UR" }, { "Cobaltt", "552", "Gold", "Z", "UR" },
			{ "Colttarren", "359", "Platinum", "P", "UR" }, { "Coltrane", "987", "Bronze", "P", "UR" }, { "DaDaNkEnGiNe", "365", "Gold", "T", "UR" },
			{ "Electric", "423", "Diamond", "Z" }, { "eXiled", "1678", "Diamond", "P" }, { "Exothermic", "532", "Platinum", "Z", "GR" }, { "Fish", "618", "Silver", "R", "GR" },
			{ "FusTup", "2205", "Gold", "Z", "UR" }, { "JuPiteR", "1347", "Platinum", "P", "GR" }, { "Logistic", "468", "Platinum", "Z", "UR" },
			{ "Meristematic", "358", "Silver", "P", "C6" }, { "Miyamori", "624", "Gold", "Z", "BG" }, { "Monk", "893", "Diamond", "T", "UR" }, { "Msyu", "848", "Diamond", "Z" },
			{ "Ninkazi", "450", "Master", "T", "GR" }, { "Padula", "475", "Gold", "T", "UR" }, { "Picur", "915", "Diamond", "Z", "UR" }, { "Polar", "508", "Diamond", "R", "UR" },
			{ "Psosa", "2697", "Diamond", "Z", "AI" }, { "RainOnSKy", "897", "Grandmaster", "R", "BG" }, { "SauCeBoSS", "894", "Diamond", "Z", "FG" },
			{ "sMeeZy", "592", "Diamond", "Z", "UR" }, { "Soken", "902", "Gold", "T", "UR" }, { "Sworn", "892", "Master", "Z", "UR" }, { "Synprime", "473", "Diamond", "P", "OG" },
			{ "TheRunedEXP", "1160", "Silver", "Z", "BG" }, { "TheWagon", "249", "Master", "Z", "AI" }, { "Vespasian", "314", "Diamond", "T" },
			{ "Vash", "160", "Gold", "P", "C6" }, { "IIIIIIIIIIII(Vintage)", "8535", "Master", "T", "BG" }, { "Whitelion", "1834", "Diamond", "Z", "UR" },
			{ "XelaWella", "1441", "Diamond", "T", "UR" }, { "Xilogh", "401", "Diamond", "Z", "UR" }, { "Yeezus", "750", "Diamond", "P", "BG" },
			{ "gdoggcasey", "750", "Diamond", "P", "AI" }, { "grimm", "750", "Silver", "P", "UR" } };
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
		System.out.println(printer.printStanding());
		tournament.initializeEloValues(globalObjectService, new double[] { 1500., 1600., 1700., 1800., 1900., 2000., 2100. });
		playMatch(0, "Logistic", 2, 1);
		playMatch(0, "Broda", 2, 0);
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
