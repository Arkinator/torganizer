package torganizer.core.tournaments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.utils.EloCalculation;
import torganizer.utils.TristanPlayerInfo;

/**
 * The format is called Tristan
 *
 * @author jules
 *
 */
public class TrisTournament extends BasicRoundBasedTournament {
	public static final double eloSpeedupFactor = 7.;
	private final int numberOfRounds;
	private final List<TristanPlayerInfo> standings;
	private final Map<Player, TristanPlayerInfo> infoMap;

	public TrisTournament(final int numberOfRounds, final int bestOfSeriesLength, final List<Player> playerList) {
		super(bestOfSeriesLength, playerList);
		this.numberOfRounds = numberOfRounds;
		standings = new ArrayList<TristanPlayerInfo>();
		playerList.forEach(player -> standings.add(new TristanPlayerInfo(player)));
		infoMap = new HashMap<>();
		standings.forEach(info -> infoMap.put(info.getPlayer(), info));
		fillRounds();
	}

	@Override
	public Player getWinner() {
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
		rounds.add(createMatchesForRoundAccordingToStanding());
		for (int i = 1; i < numberOfRounds; i++) {
			rounds.add(createRound());
		}
	}

	private List<BestOfMatchSinglePlayer> createMatchesForRoundAccordingToStanding() {
		final List<Player> playerList = new ArrayList<Player>();
		if (standings == null) {
			playerList.addAll(this.getParticipants());
			Collections.shuffle(playerList);
			// account for uneven amount of players
			playerList.add(null);
			final List<BestOfMatchSinglePlayer> matchesForRound = new ArrayList<BestOfMatchSinglePlayer>();
			for (int i = 0; i < calculateMatchesPerRound(); i++) {
				matchesForRound.add(createNewMatch(playerList.remove(0), playerList.remove(0)));
			}
			return matchesForRound;
		} else {
			standings.forEach(info -> playerList.add(info.getPlayer()));
			final List<BestOfMatchSinglePlayer> matchesForRound = new ArrayList<BestOfMatchSinglePlayer>();
			for (int i = 0; i < calculateMatchesPerRound(); i++) {
				final Player player = playerList.remove(0);
				matchesForRound.add(createNewMatch(player, seekOpponentForPlayer(player, playerList)));
			}
			return matchesForRound;
		}
	}

	private Player seekOpponentForPlayer(final Player player, final List<Player> playerList) {
		Player bestMatch = null;
		int distance = -1;
		final TristanPlayerInfo playerInfo = infoMap.get(player);
		for (final Player possibleOpponent : playerList) {
			if (!playerInfo.hasPlayerFacedOpponentBefore(possibleOpponent)) {
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
		for (int i = 0; i < calculateMatchesPerRound(); i++) {
			matchesForRound.add(createNewMatch(null, null));
		}
		return matchesForRound;
	}

	private int calculateMatchesPerRound() {
		if ((getParticipants().size() % 2) == 0) {
			return getParticipants().size() / 2;
		} else {
			// always take a bye for the extra player in case of an uneven
			// number of participants
			return (getParticipants().size() / 2) + 1;
		}
	}

	public void setInitialEloValue(final Player player, final double initialEloValue) {
		infoMap.get(player).setElo(initialEloValue);
	}

	public void updateNextRound() {
		final int nextRound = calculateNextRound();
		if (nextRound >= numberOfRounds) {
			return;
		}
		standings.sort(Collections.reverseOrder());
		rounds.set(nextRound, createMatchesForRoundAccordingToStanding());
	}

	@Override
	public void callback(final IToEntity sender) {
		super.callback(sender);
		if (sender instanceof BestOfMatchSinglePlayer) {
			addFacedPreviousOpponent((BestOfMatchSinglePlayer) sender);
		}
		updateNextRound();
	}

	private void addFacedPreviousOpponent(final BestOfMatchSinglePlayer sender) {
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

	public String printStanding() {
		final StringBuilder result = new StringBuilder();
		int place = 1;
		for (final TristanPlayerInfo info : standings) {
			result.append(place++);
			result.append(".\t");
			result.append(info.getPlayer().getName());
			result.append(" (");
			result.append(info.getElo());
			result.append(")\n");
		}
		return result.toString();
	}

	public String printMatchesForRound(final int i) {
		final StringBuilder result = new StringBuilder();
		final DecimalFormat df = new DecimalFormat("#.00");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		for (final BestOfMatchSinglePlayer match : rounds.get(i)) {
			result.append(match.getSideA().getName());
			result.append(" vs ");
			result.append(match.getSideB().getName());
			result.append(" ");
			result.append(match.getScore(match.getSideA()));
			result.append("-");
			result.append(match.getScore(match.getSideB()));
			result.append(" (");
			final EloCalculation elo = new EloCalculation(infoMap.get(match.getSideA()).getPreviousElo(), infoMap.get(match.getSideB()).getPreviousElo());
			elo.setFactualResult(match.getFinalScore());
			final double deltaElo = elo.getPlayerBAdjustment() * TrisTournament.eloSpeedupFactor;
			result.append(df.format(deltaElo));
			result.append(")\n");
		}
		return result.toString();
	}
}
