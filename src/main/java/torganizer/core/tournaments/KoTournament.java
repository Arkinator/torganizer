package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.math.util.MathUtils;

import torganizer.core.entities.IToEntity;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.GenericMatch;

public class KoTournament extends BasicRoundBasedTournament {
	private Map<BestOfMatchSinglePlayer, Integer> matchNumbers;
	private Map<Integer, BestOfMatchSinglePlayer> matchesNumbered;
	private UUID winner = null;

	public KoTournament(final int bestOfMatchLength, final List<UUID> playerList, final String name) {
		super(bestOfMatchLength, playerList, name);
		fillRounds();
	}

	@Override
	protected void fillRounds() {
		final List<UUID> playerList = new ArrayList<UUID>();
		playerList.addAll(getParticipants());
		Collections.shuffle(playerList);
		int playersLeftInTournament = getParticipants().size();
		for (int roundNumber = 0; roundNumber < getNumberOfRounds(); roundNumber++) {
			final List<BestOfMatchSinglePlayer> roundMatches = new ArrayList<BestOfMatchSinglePlayer>();
			for (int matchNumber = 0; matchNumber < (playersLeftInTournament / 2); matchNumber++) {
				if (playerList.size() > 0) {
					roundMatches.add(createNewMatch(playerList.remove(0), playerList.remove(0)));
				} else {
					roundMatches.add(createNewMatch(null, null));
				}
			}
			playersLeftInTournament /= 2;
			rounds.add(roundMatches);
		}
	}

	@Override
	protected BestOfMatchSinglePlayer createNewMatch(final UUID playerA, final UUID playerB) {
		if (matchNumbers == null) {
			initializeInternalDictionaries();
		}
		final BestOfMatchSinglePlayer match = super.createNewMatch(playerA, playerB);
		matchNumbers.put(match, matchNumbers.size());
		matchesNumbered.put(matchesNumbered.size(), match);
		return match;
	}

	private void initializeInternalDictionaries() {
		matchNumbers = new HashMap<BestOfMatchSinglePlayer, Integer>();
		matchesNumbered = new HashMap<Integer, BestOfMatchSinglePlayer>();
	}

	@Override
	public UUID getWinner() {
		return winner;
	}

	public List<BestOfMatchSinglePlayer> getAbstractMatchesForRound(final int round) {
		final List<BestOfMatchSinglePlayer> list = new ArrayList<BestOfMatchSinglePlayer>();
		list.addAll(rounds.get(round));
		return list;
	}

	@Override
	public int getNumberOfRounds() {
		return (int) Math.round(MathUtils.log(2, getNumberOfMatches()));
	}

	private int getNumberOfMatches() {
		return getParticipants().size() - 1;
	}

	public List<UUID> getPlayersForRound(final int roundNumber) {
		final List<UUID> playerList = new ArrayList<UUID>();
		for (final GenericMatch match : getMatchesForRound(roundNumber)) {
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

	@Override
	public void callback(final IToEntity sender) {
		super.callback(sender);
		if (calculateAdvancingPlayers_didAnythingChange(sender)) {
			fireCallback();
		}
	}

	private boolean calculateAdvancingPlayers_didAnythingChange(final IToEntity sender) {
		if (sender instanceof BestOfMatchSinglePlayer) {
			final UUID advancingPlayer = ((BestOfMatchSinglePlayer) sender).getWinner();
			if (advancingPlayer != null) {
				final Integer matchNumber = matchNumbers.get(sender);
				if (matchNumber == null) {
					throw new MatchNotFoundInTournamentException();
				}
				final int nextMatchNumber = calculateAdvancingMatchNumber(matchNumber);
				final BestOfMatchSinglePlayer nextMatch = matchesNumbered.get(nextMatchNumber);
				if (nextMatch == null) {
					winner = advancingPlayer;
				} else {
					if ((matchNumber % 2) != 0) {
						nextMatch.setSideA(advancingPlayer);
					} else {
						nextMatch.setSideB(advancingPlayer);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected int calculateActiveRound() {
		int round = 0;
		for (final List<BestOfMatchSinglePlayer> list : rounds) {
			for (final BestOfMatchSinglePlayer match : list) {
				if (!match.isPlayed()) {
					return round;
				}
			}
			round++;
		}
		return round;
	}

	public int calculateAdvancingMatchNumber(final int matchNumber) {
		return (int) ((getParticipants().size() / 2) - (matchNumber / 2.)) + matchNumber;
	}

	public class MatchNotFoundInTournamentException extends RuntimeException {
		private static final long serialVersionUID = 4058872847663011090L;
	}
}
