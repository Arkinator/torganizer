package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;

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
		assertEquals(team.getUid(), playerA.getTeamUid());
	}

	@Test
	public void setOwnerAndCheck() {
		final String teamName = "fjkdslajfwepioc";
		final Team team = new Team(teamName);
		assertNull(team.getOwner());
		team.setOwner(playerA.getUid());
		assertEquals(playerA.getUid(), team.getOwner());
	}

	@Test
	public void addLieutenantAndCheck() {
		final String teamName = "fjkdslajfwepioc";
		final Team team = new Team(teamName);
		assertEquals(0, team.getLieutenants().size());
		assertFalse(team.isLieutenant(playerA.getUid()));
		team.addLieutenant(playerA.getUid());
		assertEquals(1, team.getLieutenants().size());
		assertTrue(team.isLieutenant(playerA.getUid()));
	}
}
