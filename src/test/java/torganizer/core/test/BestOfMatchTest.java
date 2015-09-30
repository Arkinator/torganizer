package torganizer.core.test;

import org.junit.Test;

import torganizer.core.BestOfMatch;
import torganizer.core.Player;

public class BestOfMatchTest {
	@Test
	public void simpleTest() {
		final BestOfMatch set = new BestOfMatch();
	}

	@Test
	public void simpleSetTest() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatch set = new BestOfMatch(playerA, playerB);

	}
}
