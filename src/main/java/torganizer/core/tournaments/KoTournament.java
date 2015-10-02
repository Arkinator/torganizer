package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.util.MathUtils;

import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.IGenericMatch;

public class KoTournament extends AbstractTournament<Player> {

	private final List<List<BestOfMatchSinglePlayer>> rounds;
	private final int bestOfMatchLength;

	public KoTournament(final int bestOfMatchLength, final List<Player> playerList) {
		super(playerList);
		this.bestOfMatchLength = bestOfMatchLength;
		rounds = new ArrayList<List<BestOfMatchSinglePlayer>>();
		fillRounds();
	}

	private void fillRounds() {
		final List<Player> playerList = new ArrayList<Player>();
		playerList.addAll(getParticipants());
		Collections.shuffle(playerList);
		int playersLeftInTournament = getParticipants().size();
		for (int roundNumber = 0; roundNumber < getNumberOfRounds(); roundNumber++) {
			final List<BestOfMatchSinglePlayer> roundMatches = new ArrayList<BestOfMatchSinglePlayer>();
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
	public List<IGenericMatch<Player>> getMatchesForRound(final int round) {
		final List<IGenericMatch<Player>> list = new ArrayList<IGenericMatch<Player>>();
		list.addAll(rounds.get(round));
		return list;
	}

	public List<BestOfMatchSinglePlayer> getAbstractMatchesForRound(final int round) {
		final List<BestOfMatchSinglePlayer> list = new ArrayList<BestOfMatchSinglePlayer>();
		list.addAll(rounds.get(round));
		return list;
	}

	@Override
	public int getCurrentRound() {
		int round = 0;
		for (final List<BestOfMatchSinglePlayer> list : rounds) {
			for (final BestOfMatchSinglePlayer match : list) {
				if (match.getWinner() == null) {
					return round;
				}
			}
			round++;
		}
		return round - 1;
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
		for (final IGenericMatch<Player> match : getMatchesForRound(roundNumber)) {
			playerList.add(match.getSideA());
			playerList.add(match.getSideB());
		}
		return playerList;
	}

	@Override
	public String toString() {
		String result = "";
		for (final List<BestOfMatchSinglePlayer> list : rounds) {
			for (final BestOfMatchSinglePlayer match : list) {
				result += match + "\t";
			}
			result += "\n";
		}
		return result;
	}
}
