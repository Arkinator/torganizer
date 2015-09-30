package torganizer.core.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import torganizer.core.Player;
import torganizer.core.Team;

public class EntityTest {
	@Test
	public void testPlayerIsPartOfTeam() {
		final Player player = new Player("name");
		final Team team = new Team("name");
		team.addPlayer(player);
		assertTrue(team.equalsOrInTeam(player));
		assertTrue(player.equalsOrInTeam(team));
	}
}
