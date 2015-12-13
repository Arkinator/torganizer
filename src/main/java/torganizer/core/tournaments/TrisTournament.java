package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.utils.EloCalculation;
import torganizer.utils.TristanPlayerInfo;

/**
 * The format is called Tristan
 *
 * @author jules
 *
 */
public class TrisTournament extends BasicRoundBasedTournament {
	public static double eloSpeedupFactor = 8.;
	public static int playerRepititionThreshold = 5;
	private final int numberOfRounds;
	private final List<TristanPlayerInfo> standings;
	private final Map<UUID, TristanPlayerInfo> infoMap;

	public TrisTournament(final int numberOfRounds, final int bestOfSeriesLength, final List<UUID> playerList, final String name) {
		super(bestOfSeriesLength, playerList, name);
		this.numberOfRounds = numberOfRounds;
		standings = new ArrayList<TristanPlayerInfo>();
		playerList.forEach(player -> standings.add(new TristanPlayerInfo(player)));
		infoMap = new HashMap<>();
		standings.forEach(info -> infoMap.put(info.getPlayer(), info));
		fillRounds();
	}

	@Override
	public UUID getWinner() {
		return null;
	}

	@Override
	protected int calculateActiveRound() {
		int activeRound = 0;
		for (final List<BestOfMatchSinglePlayer> list : rounds) {
			for (final BestOfMatchSinglePlayer match : list) {
				if (match.getWinner() == null) {
					return activeRound;
				}
			}
			activeRound++;
		}
		return activeRound;
	}

	protected int calculateNextRound() {
		int nextRound = 0;
		for (final List<BestOfMatchSinglePlayer> list : rounds) {
			boolean roundIsActive = false;
			for (final BestOfMatchSinglePlayer match : list) {
				if ((match.getWinner() != null) && !match.isBye()) {
					roundIsActive = true;
					break;
				}
			}
			if (!roundIsActive) {
				return nextRound;
			}
			nextRound++;
		}
		return nextRound;
	}

	@Override
	public int getNumberOfRounds() {
		return numberOfRounds;
	}

	@Override
	protected void fillRounds() {
		rounds.add(createMatchesForRoundAccordingToStanding(0));
		for (int i = 1; i < numberOfRounds; i++) {
			rounds.add(createRound());
		}
	}

	private List<BestOfMatchSinglePlayer> createMatchesForRoundAccordingToStanding(final int round) {
		final List<UUID> playerList = new ArrayList<UUID>();
		if (standings == null) {
			playerList.addAll(this.getParticipants());
			Collections.shuffle(playerList);
			// account for uneven amount of players
			playerList.add(null);
			final List<BestOfMatchSinglePlayer> matchesForRound = new ArrayList<BestOfMatchSinglePlayer>();
			for (int i = 0; i < calculateMatchesPerRound(playerList.size()); i++) {
				final UUID playerA = playerList.remove(0);
				final UUID playerB = playerList.remove(0);
				matchesForRound.add(createNewMatch(playerA, playerB));
			}
			return matchesForRound;
		} else {
			standings.forEach(info -> {
				if (info.isEligibleToPlay()) {
					playerList.add(info.getPlayer());
				}
			});
			final List<BestOfMatchSinglePlayer> matchesForRound = new ArrayList<BestOfMatchSinglePlayer>();
			final int numMatches = calculateMatchesPerRound(playerList.size());
			for (int i = 0; i < numMatches; i++) {
				final UUID playerA = playerList.remove(0);
				final UUID playerB = seekOpponentForPlayer(playerA, playerList);
				matchesForRound.add(createNewMatch(playerA, playerB));
			}
			return matchesForRound;
		}
	}

	private UUID seekOpponentForPlayer(final UUID player, final List<UUID> playerList) {
		UUID bestMatch = null;
		int distance = -1;
		final TristanPlayerInfo playerInfo = infoMap.get(player);
		for (final UUID possibleOpponent : playerList) {
			if (!playerInfo.hasPlayerFacedOpponentBeforeInNMatches(possibleOpponent, playerRepititionThreshold, getCurrentRound())) {
				playerList.remove(possibleOpponent);
				return possibleOpponent;
			}
			final int newDistance = (getCurrentRound() - playerInfo.getRoundOfLastEncounter(possibleOpponent));
			if (distance < newDistance) {
				bestMatch = possibleOpponent;
				distance = newDistance;
			}
		}
		playerList.remove(bestMatch);
		return bestMatch;
	}

	private List<BestOfMatchSinglePlayer> createRound() {
		final List<BestOfMatchSinglePlayer> matchesForRound = new ArrayList<BestOfMatchSinglePlayer>();
		for (int i = 0; i < calculateMatchesPerRound(getParticipants().size()); i++) {
			matchesForRound.add(createNewMatch(null, null));
		}
		return matchesForRound;
	}

	private int calculateMatchesPerRound(final int numOfPlayers) {
		if ((numOfPlayers % 2) == 0) {
			return numOfPlayers / 2;
		} else {
			// always take a bye for the extra player in case of an uneven
			// number of participants
			return (numOfPlayers / 2) + 1;
		}
	}

	public void setInitialEloValue(final UUID player, final double initialEloValue) {
		infoMap.get(player).setElo(initialEloValue);
	}

	public void updateNextRound() {
		final int nextRound = calculateNextRound();
		if (nextRound >= numberOfRounds) {
			return;
		}
		standings.sort(Collections.reverseOrder());
		rounds.set(nextRound, createMatchesForRoundAccordingToStanding(nextRound));
	}

	@Override
	public void callback(final IToEntity sender) {
		super.callback(sender);
		if (sender instanceof BestOfMatchSinglePlayer) {
			updateEloAfterMatchWasPlayed((BestOfMatchSinglePlayer) sender);
		}
		updateNextRound();
	}

	private void updateEloAfterMatchWasPlayed(final BestOfMatchSinglePlayer sender) {
		if (sender.getWinner() != null) {
			final TristanPlayerInfo infoA = infoMap.get(sender.getSideA());
			final TristanPlayerInfo infoB = infoMap.get(sender.getSideB());
			infoA.addEncounter(sender.getSideB(), getCurrentRound());
			infoB.addEncounter(sender.getSideA(), getCurrentRound());

			final EloCalculation elo = new EloCalculation(infoA.getElo(), infoB.getElo());
			elo.setFactualResult(sender.getFinalScore());
			infoA.adjustElo(elo.getPlayerAAdjustment());
			infoB.adjustElo(elo.getPlayerBAdjustment());
		}
	}

	public List<TristanPlayerInfo> getStanding() {
		return standings;
	}

	public void initializeEloValues(final GlobalObjectService globalObjectService, final double[] initialEloValues) {
		for (final TristanPlayerInfo info : this.standings) {
			final Player p = globalObjectService.getPlayerById(info.getPlayer());
			info.setElo(initialEloValues[p.getLeague().ordinal()]);
		}
		updateNextRound();
	}

	public void initializeEloValues(final Map<UUID, Player> playerCache, final double[] initialEloValues) {
		for (final TristanPlayerInfo info : this.standings) {
			final Player p = playerCache.get(info.getPlayer());
			info.setElo(initialEloValues[p.getLeague().ordinal()]);
		}
		updateNextRound();
	}

	public TristanPlayerInfo getInfo(final UUID key) {
		return infoMap.get(key);
	}

	public void giveStrikeToPlayer(final UUID uid) {
		infoMap.get(uid).addStrike(getCurrentRound());
	}
}
