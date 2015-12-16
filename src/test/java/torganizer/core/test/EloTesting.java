package torganizer.core.test;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

	public static String[][] playerInfo = { { "Acadien", "391", "Diamond", "P", "AI" }, //
			{ "Athreos", "882", "Platinum", "Z", "AI" }, //
			{ "Apogee", "1459", "Diamond", "T", "UR" }, //
			{ "Blaze", "887", "Master", "T", "UR" }, //
			{ "Broda", "809", "Master", "T", "UR" }, //
			{ "Cobaltt", "552", "Gold", "Z", "UR" }, //
			{ "Colttarren", "359", "Platinum", "P", "UR" }, //
			{ "Coltrane", "987", "Bronze", "P", "UR" }, //
			{ "DaDaNkEnGiNe", "365", "Gold", "T", "UR" }, //
			{ "Electric", "423", "Diamond", "Z" }, //
			{ "eXiled", "1678", "Diamond", "P" }, //
			{ "Exothermic", "532", "Platinum", "Z", "GR" }, //
			{ "Fish", "618", "Silver", "R", "GR" }, //
			{ "FusTup", "2205", "Gold", "Z", "UR" }, //
			{ "JuPiteR", "1347", "Platinum", "P", "GR" }, //
			{ "Logistic", "468", "Platinum", "Z", "UR" }, //
			{ "Meristematic", "358", "Silver", "P", "C6" }, //
			{ "Miyamori", "624", "Gold", "Z", "BG" }, //
			{ "Monk", "893", "Diamond", "T", "UR" }, //
			{ "Msyu", "848", "Diamond", "Z" }, //
			{ "Ninkazi", "450", "Master", "T", "GR" }, //
			{ "Padula", "475", "Gold", "T", "UR" }, //
			{ "Picur", "915", "Diamond", "Z", "UR" }, //
			{ "Polar", "508", "Diamond", "R", "UR" }, //
			{ "Psosa", "2697", "Diamond", "Z", "AI" }, //
			{ "RainOnSKy", "897", "Grandmaster", "R", "BG" }, //
			{ "SauCeBoSS", "894", "Diamond", "Z", "FG" }, //
			{ "sMeeZy", "592", "Diamond", "Z", "UR" }, //
			{ "Soken", "902", "Gold", "T", "UR" }, //
			{ "Sworn", "892", "Master", "Z", "UR" }, //
			{ "Synprime", "473", "Diamond", "P", "OG" }, //
			{ "TheRunedEXP", "1160", "Silver", "Z", "BG" }, //
			{ "TheWagon", "249", "Master", "Z", "AI" }, //
			{ "Vespasian", "314", "Diamond", "T" }, //
			{ "Vash", "160", "Gold", "P", "C6" }, //
			{ "IIIIIIIIIIII(Vintage)", "8535", "Master", "T", "BG" }, //
			{ "Whitelion", "1834", "Diamond", "Z", "UR" }, //
			{ "XelaWella", "1441", "Diamond", "T", "UR" }, //
			{ "Xilogh", "401", "Diamond", "Z", "UR" }, //
			{ "Yeezus", "750", "Diamond", "P", "BG" }, //
			// { "gdoggcasey", "750", "Diamond", "P", "AI" }, //
			{ "grimm", "750", "Silver", "P", "UR" } };
	public static String[][] teamInfo = { { "Team Unrivaled", "UR", "team unrivaled", "UnrivaledMini.png" }, //
			{ "All Inspiration", "AI", "all-inspiration", "All-Inspirationlogo_std.png" }, //
			{ "Guns and Roaches", "GR", "guns and roaches", "GunsandRoachesMini.png" }, //
			{ "CTL Team 6", "C6", "ctl team 6", "CT6logo_std.png" }, //
			{ "Outset Gaming", "OG", "outset gaming", "Outsetlogo_std.png" }, //
			{ "Formless Gaming", "FG", "formless gaming", "Formlesslogo_std.png" }, //
			{ "Born Gosu", "BG", "born gosu", "BornGosulogo_std.png" } };
	public static double[] baseEloRatings = new double[] { 1500., 1600., 1700., 1800., 1900., 2000., 2100. };
	public static double[] fakeEloRatings = new double[] { 1500., 1500., 1500., 1500., 1500., 1500., 1500. };

	public static Map<String, UUID> teamMap = new HashMap<>();

	final Map<UUID, Player> playerCache = new HashMap<>();

	@Test
	public void testCorrectMatches() {
		addTeams();

		final List<Player> playerList = new ArrayList<>();

		for (final String[] infoStrings : playerInfo) {
			final Player p = new Player(calcEloRating(StarcraftLeague.valueOf(infoStrings[2])).toString());
			p.setBattleNetCode(Integer.parseInt(infoStrings[1]));
			p.setRace(StarcraftRace.parseShort(infoStrings[3]));
			p.setLeague(StarcraftLeague.valueOf(infoStrings[2]));
			playerList.add(p);
			if (infoStrings.length > 4) {
				p.setTeamUid(teamMap.get(infoStrings[4]));
			}
			globalObjectService.addPlayer(p);
		}

		for (final Player p : playerList) {
			playerCache.put(p.getUid(), p);
		}
		// 83 from 2.8 speedup and 5 repition threshold (100 std)
		// 174 from 7.2 speedup and 4 repition threshold (200)
		// 261 from 14.5 speedup and 7 repition threshold
		// 134.6759047619047 from 8.5 speedup and 5 repition threshold (200)
		// 226.4433333333332 with 0.5 speedup and 7 threshold (all same elo)
		// 123.71476190476183 with 8.599999999999985 speedup and 3 threshold

		// 97.83931707317076 vs 97.23151219512195 vs 105.36092682926828
		final String temp = simulateTournamentWithValues(playerList);
		final Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		final StringSelection data = new StringSelection(temp + "");
		clipBoard.setContents(data, data);
	}

	private String simulateTournamentWithValues(final List<Player> playerList) {
		tournament = new TrisTournament(10, 3, playerList, "#UNIT");
		final TrisTournamentPrinter printer = new TrisTournamentPrinter(tournament, globalObjectService);

		double overallEloDiff = 0;
		tournament.initializeEloValues(playerCache, baseEloRatings);
		for (int i = 0; i < 10; i++) {
			for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(i)) {
				final Player pA = playerCache.get(match.getSideA());
				final Player pB = playerCache.get(match.getSideB());
				if (Math.random() > 0.1) {
					tournament.giveStrikeToPlayer(pA.getUid());
				}
				if ((pA != null) && (pB != null)) {
					final EloCalculation calc = new EloCalculation(Double.parseDouble(pA.getName()), Double.parseDouble(pB.getName()));
					int scoreA = 0;
					int scoreB = 0;

					overallEloDiff += Math.abs(Double.parseDouble(pA.getName()) - Double.parseDouble(pB.getName()));
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
		}

		final String p = printer.printLiquipediaPage();
		return (overallEloDiff / (10 * playerInfo.length * 0.5)) + " ";
		// System.out.println(p);
		// return p;
	}

	private static Random rand = new Random();
	private double c = 0.0;

	private Double calcEloRating(final StarcraftLeague league) {
		final double baseRating = (baseEloRatings[league.ordinal()] * 1.5) - 750;
		c += 0.01;
		return ((int) (baseRating + (200 * rand.nextGaussian()))) + c;
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
