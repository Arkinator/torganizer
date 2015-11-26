package torganizer.core.tournaments;

import java.util.List;

import torganizer.core.entities.Player;

public class TrisTournament extends BasicRoundBasedTournament {
	public TrisTournament(final int bestOfSeriesLength, final List<Player> playerList) {
		super(bestOfSeriesLength, playerList);
	}

	@Override
	public Player getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int calculateActiveRound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfRounds() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void fillRounds() {
		// TODO Auto-generated method stub

	}
}
