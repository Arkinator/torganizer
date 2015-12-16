package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.types.StarcraftRace;
import torganizer.utils.TristanPlayerInfo;

public class TristanPlayerInfoTest {
	@Test
	public void calculateEloTest() {
		final Player playerA = new Player("kfldösa");
		final Player playerB = new Player("fsdafdsa");
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA);
		final TristanPlayerInfo playerBInfo = new TristanPlayerInfo(playerB);
		playerAInfo.adjustElo(10);
		assertTrue(playerAInfo.getElo() > playerBInfo.getElo());
	}

	@Test
	public void testSorting() {
		final Player playerA = new Player("kfldösa");
		final Player playerB = new Player("fsdafdsa");
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA);
		final TristanPlayerInfo playerBInfo = new TristanPlayerInfo(playerB);
		playerAInfo.setElo(1000.);
		playerBInfo.setElo(500.);
		assertTrue(playerAInfo.compareTo(playerBInfo) > 0);
	}

	@Test
	public void testSortingInList() {
		final Player playerA = new Player("kfldösa");
		final Player playerB = new Player("fsdafdsa");
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA);
		final TristanPlayerInfo playerBInfo = new TristanPlayerInfo(playerB);
		playerAInfo.setElo(1000.);
		playerBInfo.setElo(500.);
		final List<TristanPlayerInfo> list = new ArrayList<>();
		list.add(playerBInfo);
		list.add(playerAInfo);
		list.sort(null);
		assertEquals(playerBInfo, list.get(0));
		assertEquals(playerAInfo, list.get(1));
	}

	@Test
	public void testRaceChange() {
		final Player playerA = new Player("kfldösa");
		playerA.setRace(StarcraftRace.Terran);
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA);
		assertEquals(StarcraftRace.Terran, playerAInfo.getRaceForRound(0));
		assertEquals(StarcraftRace.Terran, playerAInfo.getRaceForRound(1));
		playerAInfo.addRaceChangeForRound(0, StarcraftRace.Zerg);
		assertEquals(StarcraftRace.Terran, playerAInfo.getRaceForRound(0));
		assertEquals(StarcraftRace.Zerg, playerAInfo.getRaceForRound(1));
	}
}
