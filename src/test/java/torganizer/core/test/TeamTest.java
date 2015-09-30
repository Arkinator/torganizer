package torganizer.core.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.Player;
import torganizer.core.Team;

public class TeamTest {
	private static Player playerA;
	private static Player playerB;

	@BeforeClass
	public static void intialize() {
		playerA = new Player("playerA");
		playerB = new Player("playerB");
	}

	@Test
	public void createTeam() {
		final String teamName = "fjkdslajfwepioc";
		final Team team = new Team(teamName);
		assertEquals(teamName, team.getName());
	}

	@Test
	public void addPlayerToTeam() {
		final Team team = new Team("teamName");
		team.addPlayer(playerA);
		team.addPlayer(playerB);
		assertEquals(team, playerA.getTeam());
	}
}
