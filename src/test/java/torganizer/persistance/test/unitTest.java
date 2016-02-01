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
			{ "blazejwa", "887", "Master", "T", "UR" }, { "Broda", "809", "Master", "T", "UR" }, { "Cobaltt", "552", "Gold", "Z", "UR" },
			{ "Colttarren", "359", "Platinum", "P", "UR" }, { "Coltrane", "987", "Bronze", "P", "UR" }, { "DaDaNkEnGiNe", "365", "Gold", "T", "UR" },
			{ "Electric", "423", "Diamond", "Z" }, { "eXiled", "1678", "Diamond", "P" }, { "Exothermic", "532", "Platinum", "Z", "GR" }, { "Fish", "618", "Silver", "R", "GR" },
			{ "FusTup", "2205", "Gold", "Z", "UR" }, { "JuPiteR", "1347", "Platinum", "P", "GR" }, { "Logistic", "468", "Platinum", "Z", "UR" },
			{ "Meristematic", "358", "Silver", "P", "C6" }, { "Miyamori", "624", "Gold", "Z", "BG" }, { "Monk", "893", "Diamond", "T", "UR" }, { "Msyu", "848", "Diamond", "P" },
			{ "Ninkazi", "450", "Master", "T", "RG" }, { "Padula", "475", "Gold", "T", "UR" }, { "Picur", "915", "Diamond", "Z", "UR" }, { "Polar", "508", "Diamond", "R", "UR" },
			{ "Psosa", "2697", "Diamond", "Z", "AI" }, { "RainOnSKy", "897", "Grandmaster", "R", "BG" }, { "SauCeBoSS", "894", "Diamond", "Z", "FG" },
			{ "sMeeZy", "592", "Diamond", "Z", "UR" }, { "Soken", "902", "Gold", "T", "UR" }, { "Sworn", "892", "Master", "Z", "UR" }, { "Synprime", "473", "Diamond", "P", "OG" },
			{ "TheRunedEXP", "1160", "Silver", "Z", "BG" }, { "TheWagon", "249", "Master", "Z", "AI" }, { "Stefan", "594", "Diamond", "T" }, { "Vash", "160", "Gold", "P", "C6" },
			{ "IIIIIIIIIIII(Vintage)", "8535", "Master", "T", "BG" }, { "Whitelion", "1834", "Diamond", "Z", "UR" }, { "XelaWella", "1441", "Diamond", "T", "UR" },
			{ "Xilogh", "401", "Diamond", "Z", "UR" }, { "ShiaLabeouf", "454", "Diamond", "P", "BG" }, { "gdoggcasey", "750", "Diamond", "P", "AI" },
			{ "grimm", "750", "Silver", "P", "UR" }, { "Ransack", "527", "Gold", "T", "UR" } };
	public static String[][] teamInfo = { { "Team Unrivaled", "UR", "team unrivaled", "Unrivaledlogo_std.png" }, //
			{ "All Inspiration", "AI", "all-inspiration", "All-Inspirationlogo_std.png" }, //
			{ "Guns and Roaches", "GR", "guns and roaches", "GunsandRoacheslogo_std.png" }, //
			{ "CTL Team 6", "C6", "ctl team 6", "CT6logo_std.png" }, //
			{ "Outset Gaming", "OG", "outset gaming", "Outsetlogo_std.png" }, //
			{ "Formless Gaming", "FG", "formless gaming", "Formlesslogo_std.png" }, //
			{ "Born Gosu", "BG", "born gosu", "BornGosulogo_std.png" }, //
			{ "Rival Gaming", "RG", "rival gaming", "RivalGaminglogo_std.png" } };

	public static Map<String, UUID> teamMap = new HashMap<>();
	private List<Player> playerList;

	@Test
	@Transactional
	public void testCorrectMatches() {
		addTeams();

		playerList = new ArrayList<>();

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
		tournament.initializeEloValues(globalObjectService, new double[] { 1500., 1600., 1700., 1800., 1900., 2000., 2100. });
		goOnHoliday("Ransack");

		playWeek1();
		playWeek2();
		playWeek3();
		playWeek4();
		playWeek5();
		playMatch(5, "YellOwSky", 2, 1);
		playMatch(5, "Ninkazi", 0, 2);
		giveStrike("Ninkazi");
		playMatch(5, "DaDaNkEnGiNe", 2, 0);
		playMatch(5, "Colttarren", 2, 0);
		playMatch(5, "FusTup", 2, 0);
		playMatch(5, "Xilogh", 2, 1);
		playMatch(5, "Acadien", 0, 2);
		giveStrike("Acadien");
		playMatch(5, "Ransack", 2, 0);
		playMatch(5, "Polar", 0, 2);
		playMatch(5, "Cobaltt", 1, 2);
		playMatch(5, "Psosa", 0, 2);
		giveStrike("Psosa");
		giveStrike("Psosa");
		playMatch(5, "Soken", 2, 0);
		giveStrike("Acadien");
		giveStrike("Acadien");
		tournament.updateNextRound();

		final TrisTournamentPrinter printer = new TrisTournamentPrinter(tournament, globalObjectService);
		final String p = printer.printLiquipediaPage();
		System.out.println(p);
		final Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		final StringSelection data = new StringSelection(p);
		clipBoard.setContents(data, data);

		//
		// p = printer.printLiquipediaPage();
		// System.out.println(p);
		// data = new StringSelection(p);
		// clipBoard.setContents(data, data);
	}

	private void playWeek5() {
		// Week 5
		playMatch(4, "Padula", 0, 2);
		giveStrike("Padula");
		playMatch(4, "Soken", 0, 2);
		playMatch(4, "blazejwa", 2, 0);
		playMatch(4, "Electric", 2, 0);
		playMatch(4, "TheWagon", 0, 2);
		giveStrike("TheWagon");
		giveStrike("TheWagon");
		playMatch(4, "Coltrane", 0, 2);
		playMatch(4, "FusTup", 2, 1);
		playMatch(4, "Xilogh", 0, 2);
		playMatch(4, "Broda", 0, 2);
		giveStrike("Broda");
		callDrawnMatch(4, "sMeeZy");
		giveStrike("sMeeZy");
		giveStrike("Psosa");
		playMatch(4, "Cobaltt", 1, 2);
		playMatch(4, "Ninkazi", 2, 0);
		giveStrike("ShiaLabeouf");
		giveStrike("ShiaLabeouf");
		playMatch(4, "Exothermic", 2, 0);
		callDrawnMatch(4, "Sworn");
		giveStrike("Sworn");
		callDrawnMatch(4, "Apogee");
		giveStrike("Apogee");
		callDrawnMatch(4, "XelaWella");
		giveStrike("XelaWella");
		giveStrike("gdoggcasey");
		callDrawnMatch(4, "Whitelion");
		giveStrike("Whitelion");
		giveStrike("JuPiteR");
		tournament.updateNextRound();
	}

	private void playWeek4() {
		playMatch(3, "Ninkazi", 2, 0);
		playMatch(3, "YellOwSky", 2, 0);
		playMatch(3, "Electric", 0, 2);
		playMatch(3, "Ransack", 2, 0);
		playMatch(3, "Vash", 2, 1);
		playMatch(3, "Cobaltt", 2, 0);
		playMatch(3, "Picur", 2, 0);
		giveStrike("Broda");
		playMatch(3, "Monk", 2, 0);
		playMatch(3, "grimm", 2, 0);
		playMatch(3, "FusTup", 0, 2);
		playMatch(3, "Xilogh", 2, 1);
		playMatch(3, "Synprime", 0, 2);
		giveStrike("Synprime");
		playMatch(3, "Logistic", 0, 2);
		giveStrike("Logistic");
		giveStrike("Logistic");
		giveStrike("Logistic");
		playMatch(3, "Apogee", 0, 2);
		giveStrike("Apogee");
		playMatch(3, "gdoggcasey", 0, 2);
		giveStrike("gdoggcasey");
		callDrawnMatch(3, "TheWagon");
		giveStrike("RainOnSKy");
		callDrawnMatch(3, "Polar");
		giveStrike("Polar");
		giveStrike("XelaWella");
		callDrawnMatch(3, "Athreos");
		giveStrike("Athreos");
		giveStrike("JuPiteR");
		callDrawnMatch(3, "Miyamori");
		giveStrike("Miyamori");
		giveStrike("Exothermic");
		tournament.updateNextRound();
	}

	private void playWeek3() {
		playMatch(2, "DaDaNkEnGiNe", 2, 0);
		playMatch(2, "sMeeZy", 0, 2);
		playMatch(2, "Xilogh", 2, 0);
		playMatch(2, "YellOwSky", 2, 0);
		playMatch(2, "Coltrane", 2, 0);
		giveStrike("TheRunedEXP");
		// GoatDragon drops out
		changeRace("sMeeZy", StarcraftRace.Terran);
		playMatch(2, "Picur", 2, 1);
		playMatch(2, "Fish", 2, 0);
		playMatch(2, "Monk", 2, 1);
		playMatch(2, "FusTup", 0, 2);
		giveStrike("FusTup");
		playMatch(2, "Cobaltt", 0, 2);
		giveStrike("Athreos");
		playMatch(2, "Meristematic", 0, 2);
		giveStrike("Meristematic");
		playMatch(2, "Colttarren", 1, 2);
		playMatch(2, "ShiaLabeouf", 2, 0);
		giveStrike("GoatDragon");
		giveStrike("GoatDragon");
		playMatch(2, "XelaWella", 0, 2);
		giveStrike("XelaWella");
		playMatch(2, "Logistic", 0, 2);
		callDrawnMatch(2, "Broda");
		giveStrike("Broda");
		giveStrike("TheWagon");
		callDrawnMatch(2, "Ninkazi");
		giveStrike("Ninkazi");
		giveStrike("RainOnSKy");
		callDrawnMatch(2, "Sworn");
		giveStrike("Sworn");
		giveStrike("IIIIIIIIIIII(Vintage)");
		callDrawnMatch(2, "gdoggcasey");
		giveStrike("gdoggcasey");
		giveStrike("Synprime");
		callDrawnMatch(2, "Electric");
		giveStrike("JuPiteR");
		callDrawnMatch(2, "Padula");
		giveStrike("Padula");
		giveStrike("Exothermic");
		tournament.updateNextRound();
	}

	private void playWeek2() {
		returnFromHoliday("Ransack");
		playMatch(1, "FusTup", 2, 0);
		playMatch(1, "Psosa", 2, 1);
		playMatch(1, "Cobaltt", 2, 0);
		giveStrike("Vash");
		playMatch(1, "Stefan", 2, 1);
		playMatch(1, "ShiaLabeouf", 2, 0);
		playMatch(1, "TheWagon", 2, 0);
		giveStrike("IIIIIIIIIIII(Vintage)");
		playMatch(1, "Acadien", 2, 1);
		playMatch(1, "Colttarren", 0, 2);
		changeRace("sMeeZy", StarcraftRace.Zerg);
		playMatch(1, "Picur", 2, 0);
		playMatch(1, "DaDaNkEnGiNe", 2, 1);
		playMatch(1, "JuPiteR", 2, 0);
		playMatch(1, "Acadien", 2, 1);
		playMatch(1, "Broda", 2, 0);
		giveStrike("RainOnSKy");
		playMatch(1, "Coltrane", 2, 0);
		giveStrike("TheRunedEXP");
		playMatch(1, "Ninkazi", 2, 0);
		// Sauce drops out
		playMatch(1, "Monk", 2, 0);
		giveStrike("SauCeBoSS");
		giveStrike("SauCeBoSS");
		giveStrike("SauCeBoSS");
		// double w/o meri vs fish
		callDrawnMatch(1, "Meristematic");
		giveStrike("Meristematic");
		giveStrike("Fish");
		// double w/o Padula vs miyamori
		callDrawnMatch(1, "Miyamori");
		giveStrike("Padula");
		// double w/o blazejwa vs sworn
		callDrawnMatch(1, "blazejwa");
		giveStrike("blazejwa");
		giveStrike("Sworn");
		// double w/o syn vs whitelion
		callDrawnMatch(1, "Synprime");
		giveStrike("Synprime");
		giveStrike("Whitelion");
		playMatch(1, "Logistic", 2, 0);
		giveStrike("Athreos");
		// msyu drops out
		playMatch(1, "Polar", 1, 2);
		changeLeague("Padula", StarcraftLeague.Platinum);
		changeLeague("Picur", StarcraftLeague.Master);
		tournament.updateNextRound();
	}

	private void playWeek1() {
		playMatch(0, "Logistic", 2, 1);
		playMatch(0, "Broda", 2, 0);
		playMatch(0, "Stefan", 2, 1);
		playMatch(0, "Sworn", 0, 2);
		changeRace("sMeeZy", StarcraftRace.Terran);
		changeRace("eXiled", StarcraftRace.Zerg);
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
		renamePlayer("eXiled", "GoatDragon", 342, StarcraftLeague.Diamond, StarcraftRace.Zerg, "UR");
		renamePlayer("Msyu", "YellOwSky", 364, StarcraftLeague.Master, StarcraftRace.Protoss, "UR");
	}

	private void renamePlayer(final String oldName, final String newName, final int newCharacterCode, final StarcraftLeague leauge, final StarcraftRace race,
			final String teamShort) {
		final Player p = globalObjectService.getPlayerByName(oldName);
		p.setName(newName);
		p.setBattleNetCode(newCharacterCode);
		p.setLeague(leauge);
		p.setRace(race);
		p.setTeamUid(teamMap.get(teamShort));
		globalObjectService.updateEntity(p);
		// the next step is only for this specific scenario:
		for (final Player player : playerList) {
			if (player.getName().equals(oldName)) {
				player.setName(newName);
				player.setBattleNetCode(newCharacterCode);
				player.setLeague(leauge);
				player.setRace(race);
				player.setTeamUid(teamMap.get(teamShort));
				return;
			}
		}
	}

	private void changeLeague(final String name, final StarcraftLeague newLeague) {
		final Player p = globalObjectService.getPlayerByName(name);
		p.setLeague(newLeague);
		globalObjectService.updateEntity(p);
		// the next step is only for this specific scenario:
		for (final Player player : playerList) {
			if (player.getName().equals(name)) {
				player.setLeague(newLeague);
				return;
			}
		}
	}

	private void returnFromHoliday(final String playerName) {
		final UUID playerUid = globalObjectService.getPlayerByName(playerName).getUid();
		tournament.setPlayerToReturnFromHoliday(playerUid);
		// tournament.updateNextRound();
	}

	private void goOnHoliday(final String playerName) {
		final UUID playerUid = globalObjectService.getPlayerByName(playerName).getUid();
		tournament.setPlayerToHoliday(playerUid);
		tournament.updateNextRound();
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
			if (playerUid.equals(match.getSideA()) || playerUid.equals(match.getSideB())) {
				match.submitResultAdmin(playerUid, scorePlayer, scoreOtherPlayer);
			}
		}
	}
}
