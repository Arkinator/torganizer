package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.GenericMatch;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.TournamentRoundOrm;
import torganizer.utils.GlobalUtilities;

public abstract class BasicRoundBasedTournament extends AbstractTournament {
	protected final List<List<BestOfMatchSinglePlayer>> rounds;
	protected final int bestOfMatchLength;
	protected int currentRound;

	public BasicRoundBasedTournament(final int bestOfMatchLength, final List<UUID> participantList, final String name) {
		super(participantList, name);
		this.bestOfMatchLength = bestOfMatchLength;
		rounds = new ArrayList<List<BestOfMatchSinglePlayer>>();
		getEntityOrm().getTournament().setBoMatchLength(bestOfMatchLength);
		getEntityOrm().getTournament().setCurrentRound(currentRound);
	}

	public BasicRoundBasedTournament(final EntityOrm entity, final GlobalObjectService globalObjectService) {
		super(entity);
		this.currentRound = entity.getTournament().getCurrentRound();
		this.bestOfMatchLength = entity.getTournament().getBoMatchLength();
		this.rounds = new ArrayList<List<BestOfMatchSinglePlayer>>();
		readMatchesFromDb(entity, globalObjectService);
	}

	private void readMatchesFromDb(final EntityOrm entity, final GlobalObjectService globalObjectService) {
		for (final TournamentRoundOrm round : entity.getTournament().getRounds()) {
			ensureNumberOfRoundsExistInField(round.getRoundInTournament());
			for (final UUID matchUid : round.getMatches()) {
				rounds.get(round.getRoundInTournament()).add(globalObjectService.getBestOfMatchById(matchUid));
			}
		}
	}

	private void ensureNumberOfRoundsExistInField(final Integer value) {
		for (int i = rounds.size(); i <= value; i++) {
			rounds.add(new ArrayList<>());
		}
	}

	protected abstract void fillRounds();

	@Override
	public int getCurrentRound() {
		return currentRound;
	}

	protected BestOfMatchSinglePlayer createNewMatch(final UUID playerAId, final UUID playerBId) {
		final long t1 = System.nanoTime();
		final Player playerA = getGlobalObjectService().getPlayerById(playerAId);
		final long t2 = System.nanoTime();
		final Player playerB = getGlobalObjectService().getPlayerById(playerBId);
		final long t3 = System.nanoTime();
		final BestOfMatchSinglePlayer match = new BestOfMatchSinglePlayer(bestOfMatchLength, playerA, playerB, GlobalUtilities.createNewSetName(this));
		match.addCallbackObject(this);
		final long t4 = System.nanoTime();
		System.out.println("\n\ta: " + GlobalUtilities.formatNanoTimeDiff(t1, t2) + //
				"\n\tb: " + GlobalUtilities.formatNanoTimeDiff(t2, t3) + //
				"\ngesamt: " + GlobalUtilities.formatNanoTimeDiff(t1, t4));
		return match;
	}

	@Override
	public List<GenericMatch> getMatchesForRound(final int round) {
		final List<GenericMatch> list = new ArrayList<GenericMatch>();
		list.addAll(rounds.get(round));
		return list;
	}

	public List<BestOfMatchSinglePlayer> getBestOfMatchesForRound(final int round) {
		final List<BestOfMatchSinglePlayer> list = new ArrayList<BestOfMatchSinglePlayer>();
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