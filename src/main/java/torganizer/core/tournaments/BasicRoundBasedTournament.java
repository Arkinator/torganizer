package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.GenericMatch;

public abstract class BasicRoundBasedTournament extends AbstractTournament<Player> {
	protected final List<List<BestOfMatchSinglePlayer>> rounds;
	protected final int bestOfMatchLength;
	protected int currentRound;

	public BasicRoundBasedTournament(final int bestOfMatchLength, final List<Player> participantList) {
		super(participantList);
		this.bestOfMatchLength = bestOfMatchLength;
		rounds = new ArrayList<List<BestOfMatchSinglePlayer>>();
		fillRounds();
	}

	protected abstract void fillRounds();

	@Override
	public int getCurrentRound() {
		return currentRound;
	}

	protected BestOfMatchSinglePlayer createNewMatch(final Player playerA, final Player playerB) {
		final BestOfMatchSinglePlayer match = new BestOfMatchSinglePlayer(bestOfMatchLength, playerA, playerB);
		match.addCallbackObject(this);
		return match;
	}

	@Override
	public List<GenericMatch<Player>> getMatchesForRound(final int round) {
		final List<GenericMatch<Player>> list = new ArrayList<GenericMatch<Player>>();
		list.addAll(rounds.get(round));
		return list;
	}

	@Override
	public void callback(final IToEntity sender) {
		final int newRound = calculateActiveRound();
		if (newRound != currentRound) {
			currentRound = newRound;
			fireCallback();
		}
	}

	protected abstract int calculateActiveRound();
}