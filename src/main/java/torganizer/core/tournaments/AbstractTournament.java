package torganizer.core.tournaments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.IToEntity;
import torganizer.core.matches.GenericMatch;

public abstract class AbstractTournament extends AbstractToEntity implements ITournament, IToEntity {
	private final List<UUID> participantList;

	public AbstractTournament(final List<UUID> participantList, final String name) {
		super(name);
		this.participantList = new ArrayList<UUID>();
		this.participantList.addAll(participantList);
	}

	public abstract List<GenericMatch> getMatchesForRound(int round);

	public abstract int getCurrentRound();

	public abstract int getNumberOfRounds();

	public List<UUID> getParticipants() {
		return participantList;
	}
}
