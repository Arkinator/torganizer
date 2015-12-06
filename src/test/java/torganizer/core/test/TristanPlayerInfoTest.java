package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.utils.TristanPlayerInfo;

public class TristanPlayerInfoTest {
	@Test
	public void calculateEloTest() {
		final Player playerA = new Player("kfldösa");
		final Player playerB = new Player("fsdafdsa");
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA.getUid());
		final TristanPlayerInfo playerBInfo = new TristanPlayerInfo(playerB.getUid());
		playerAInfo.adjustElo(10);
		assertTrue(playerAInfo.getElo() > playerBInfo.getElo());
	}

	@Test
	public void testSorting() {
		final Player playerA = new Player("kfldösa");
		final Player playerB = new Player("fsdafdsa");
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA.getUid());
		final TristanPlayerInfo playerBInfo = new TristanPlayerInfo(playerB.getUid());
		playerAInfo.setElo(1000.);
		playerBInfo.setElo(500.);
		assertTrue(playerAInfo.compareTo(playerBInfo) > 0);
	}

	@Test
	public void testSortingInList() {
		final Player playerA = new Player("kfldösa");
		final Player playerB = new Player("fsdafdsa");
		final TristanPlayerInfo playerAInfo = new TristanPlayerInfo(playerA.getUid());
		final TristanPlayerInfo playerBInfo = new TristanPlayerInfo(playerB.getUid());
		playerAInfo.setElo(1000.);
		playerBInfo.setElo(500.);
		final List<TristanPlayerInfo> list = new ArrayList<>();
		list.add(playerBInfo);
		list.add(playerAInfo);
		list.sort(null);
		assertEquals(playerBInfo, list.get(0));
		assertEquals(playerAInfo, list.get(1));
	}
}
