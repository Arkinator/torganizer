package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.util.MathUtils;

import torganizer.core.entities.Player;
import torganizer.core.matches.AbstractMatch;
import torganizer.core.matches.BestOfMatchSinglePlayer;

public class KoTournament extends AbstractTournament<Player> {

	private final List<List<AbstractMatch<Player>>> rounds;
	private final int bestOfMatchLength;

	public KoTournament(final int bestOfMatchLength, final List<Player> playerList) {
		super(playerList);
		this.bestOfMatchLength = bestOfMatchLength;
		rounds = new ArrayList<List<AbstractMatch<Player>>>();
		fillRounds();
	}

	private void fillRounds() {
		final List<Player> playerList = new ArrayList<Player>();
		playerList.addAll(getParticipants());
		Collections.shuffle(playerList);
		int playersLeftInTournament = getParticipants().size();
		for (int roundNumber = 0; roundNumber < getNumberOfRounds(); roundNumber++) {
			final List<AbstractMatch<Player>> roundMatches = new ArrayList<AbstractMatch<Player>>();
			for (int matchNumber = 0; matchNumber < (playersLeftInTournament / 2); matchNumber++) {
				if (playerList.size() > 0) {
					roundMatches.add(new BestOfMatchSinglePlayer(bestOfMatchLength, playerList.remove(0), playerList.remove(0)));
				} else {
					roundMatches.add(new BestOfMatchSinglePlayer(bestOfMatchLength, null, null));
				}
			}
			playersLeftInTournament /= 2;
			rounds.add(roundMatches);
		}
	}

	public Player getWinner() {
		return null;
	}

	@Override
	public List<AbstractMatch<Player>> getMatchesForRound(final int round) {
		return rounds.get(round);
	}

	@Override
	public int getCurrentRound() {
		return 0;
	}

	@Override
	public int getNumberOfRounds() {
		return (int) Math.round(MathUtils.log(2, getNumberOfMatches()));
	}

	private int getNumberOfMatches() {
		return getParticipants().size() - 1;
	}

	public List<Player> getPlayersForRound(final int roundNumber) {
		final List<Player> playerList = new ArrayList<Player>();
		for (final AbstractMatch<Player> match : getMatchesForRound(0)) {
			playerList.add(match.getSideA());
			playerList.add(match.getSideB());
		}
		return playerList;
	}
}
