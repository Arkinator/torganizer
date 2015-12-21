package torganizer.persistance.test;

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
import org.springframework.transaction.annotation.Transactional;

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
			{ "TheRunedEXP", "1160", "Silver", "Z", "BG" }, { "TheWagon", "249", "Master", "Z", "AI" }, { "Stefan", "594", "Diamond", "T" }, { "Vash", "160", "Gold", "P", "C6" },
			{ "IIIIIIIIIIII(Vintage)", "8535", "Master", "T", "BG" }, { "Whitelion", "1834", "Diamond", "Z", "UR" }, { "XelaWella", "1441", "Diamond", "T", "UR" },
			{ "Xilogh", "401", "Diamond", "Z", "UR" }, { "ShiaLabeouf", "454", "Diamond", "P", "BG" }, { "gdoggcasey", "750", "Diamond", "P", "AI" },
			{ "grimm", "750", "Silver", "P", "UR" } };
	public static String[][] teamInfo = { { "Team Unrivaled", "UR", "team unrivaled", "UnrivaledMini.png" }, //
			{ "All Inspiration", "AI", "all-inspiration", "All-Inspirationlogo_std.png" }, //
			{ "Guns and Roaches", "GR", "guns and roaches", "GunsandRoachesMini.png" }, //
			{ "CTL Team 6", "C6", "ctl team 6", "CT6logo_std.png" }, //
			{ "Outset Gaming", "OG", "outset gaming", "Outsetlogo_std.png" }, //
			{ "Formless Gaming", "FG", "formless gaming", "Formlesslogo_std.png" }, //
			{ "Born Gosu", "BG", "born gosu", "BornGosulogo_std.png" } };

	public static Map<String, UUID> teamMap = new HashMap<>();

	@Test
	@Transactional
	public void testCorrectMatches() {
		addTeams();

		final List<Player> playerList = new ArrayList<>();

		for (final String[] infoStrings : playerInfo) {
			final Player p = new Player(infoStrings[0]);
			p.setBattleNetCode(Integer.parseInt(infoStrings[1]));
			p.setRace(StarcraftRace.parseShort(infoStrings[3]));
			p.setLeague(StarcraftLeague.valueOf(infoStrings[2]));
			playerList.add(p);
			if (infoStrings.length > 4) {
				p.setTeamUid(teamMap.get(infoStrings[4]));
			}
			globalObjectService.addPlayer(p);
		}

		tournament = new TrisTournament(10, 3, playerList, "#UNIT - Season 1");
		final TrisTournamentPrinter printer = new TrisTournamentPrinter(tournament, globalObjectService);
		tournament.initializeEloValues(globalObjectService, new double[] { 1500., 1600., 1700., 1800., 1900., 2000., 2100. });

		playMatch(0, "Logistic", 2, 1);
		playMatch(0, "Broda", 2, 0);
		playMatch(0, "Stefan", 2, 1);
		playMatch(0, "Sworn", 0, 2);
		changeRace("sMeeZy", StarcraftRace.Terran);
		playMatch(0, "sMeeZy", 2, 1);
		playMatch(0, "Xilogh", 2, 0);
		playMatch(0, "Colttarren", 2, 1);
		giveStrike("ShiaLabeouf");
		playMatch(0, "FusTup", 0, 2);
		playMatch(0, "RainOnSKy", 2, 0);
		playMatch(0, "Apogee", 2, 0);
		playMatch(0, "Picur", 2, 0);
		giveStrike("Msyu");
		playMatch(0, "Stefan", 2, 1);
		playMatch(0, "gdoggcasey", 0, 2);
		playMatch(0, "Coltrane", 0, 2);
		playMatch(0, "Vash", 2, 0);
		callDrawnMatch(0, "Acadien");
		giveStrike("Acadien");
		playMatch(0, "Psosa", 2, 0);
		giveStrike("IIIIIIIIIIII(Vintage)");
		playMatch(0, "Padula", 2, 0);
		callDrawnMatch(0, "TheRunedEXP");
		giveStrike("Meristematic");
		giveStrike("TheRunedEXP");
		playMatch(0, "Cobaltt", 2, 0);
		giveStrike("eXiled");
		giveStrike("Monk");
		callDrawnMatch(0, "Monk");
		playMatch(0, "XelaWella", 2, 0);
		giveStrike("Whitelion");

		final String p = printer.printLiquipediaPage();
		System.out.println(p);
		final Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		final StringSelection data = new StringSelection(p);
		clipBoard.setContents(data, data);
	}

	private void callDrawnMatch(final int round, final String playerName) {
		final UUID playerUid = globalObjectService.getPlayerByName(playerName).getUid();
		for (final GenericMatch match : tournament.getMatchesForRound(round)) {
			if (match.getSideA().equals(playerUid) || match.getSideB().equals(playerUid)) {
				match.submitResultAdmin(null, 0, 0);
			}
		}
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

	private void changeRace(final String playerName, final StarcraftRace targetRace) {
		final UUID playerUid = globalObjectService.getPlayerByName(playerName).getUid();
		tournament.addNewRaceChange(playerUid, targetRace);
	}

	private void giveStrike(final String playerName) {
		tournament.giveStrikeToPlayer(globalObjectService.getPlayerByName(playerName).getUid());
	}

	private void playMatch(final int round, final String playerName, final int scorePlayer, final int scoreOtherPlayer) {
		final UUID playerUid = globalObjectService.getPlayerByName(playerName).getUid();
		for (final GenericMatch match : tournament.getMatchesForRound(round)) {
			if (match.getSideA().equals(playerUid) || match.getSideB().equals(playerUid)) {
				match.submitResultAdmin(playerUid, scorePlayer, scoreOtherPlayer);
			}
		}
	}
}
