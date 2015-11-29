package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.IToEntity;
import torganizer.core.entities.IToParticipant;
import torganizer.core.matches.GenericMatch;

public abstract class AbstractTournament<TYPE extends IToParticipant> extends AbstractToEntity implements ITournament<TYPE>, IToEntity {
	private final List<TYPE> participantList;
	private String name;

	public AbstractTournament(final List<TYPE> participantList) {
		this.participantList = new ArrayList<TYPE>();
		this.participantList.addAll(participantList);
		this.name = "Tournament" + getUid();
	}

	public abstract List<GenericMatch<TYPE>> getMatchesForRound(int round);

	public abstract int getCurrentRound();

	public abstract int getNumberOfRounds();

	public List<TYPE> getParticipants() {
		return participantList;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
