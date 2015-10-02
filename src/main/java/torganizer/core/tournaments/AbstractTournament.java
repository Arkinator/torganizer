package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.IToEntity;
import torganizer.core.matches.IGenericMatch;

public abstract class AbstractTournament<TYPE extends IToEntity> implements ITournament<TYPE> {
	private final List<TYPE> participantList;

	public AbstractTournament(final List<TYPE> participantList) {
		this.participantList = new ArrayList<TYPE>();
		this.participantList.addAll(participantList);
	}

	public abstract List<IGenericMatch<TYPE>> getMatchesForRound(int round);

	public abstract int getCurrentRound();

	public abstract int getNumberOfRounds();

	public List<TYPE> getParticipants() {
		return participantList;
	}
}
