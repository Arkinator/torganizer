package torganizer.core.matches;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.entities.IToEntity;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.MatchOrm;

public abstract class AbstractMatchSeries<SET extends GenericMatch> extends AbstractMatch {
	private final List<SET> sets;

	public AbstractMatchSeries(final int numberOfSets, final UUID sideA, final UUID sideB, final String name) {
		super(sideA, sideB, name);
		sets = new ArrayList<SET>();
		getEntityOrm().getMatch().setSets(new ArrayList<>());
		for (int i = 0; i < numberOfSets; i++) {
			final SET set = constructNewSet();
			addNewSet(set);
		}
		getEntityOrm().getMatch().setNumberOfSets(numberOfSets);
	}

	private void addNewSet(final SET set) {
		set.addCallbackObject(this);
		sets.add(set);
		getEntityOrm().getMatch().getSets().add(set.getEntityOrm());
	}

	public AbstractMatchSeries(final MatchOrm match) {
		super(match);
		sets = new ArrayList<SET>();
		for (final EntityOrm setOrm : match.getSets()) {
			final SET set = (SET) MatchFactory.constructMatchFromOrm(setOrm);
			sets.add(set);
			set.addCallbackObject(this);
		}
	}

	/**
	 * Constructor for a new set. Just instantiate a new Object, may set
	 * appropriate values for Side, Player, Color, Date, whatever
	 *
	 * @return
	 */
	public abstract SET constructNewSet();

	public int getNumberOfSets() {
		return sets.size();
	}

	public SET getSet(final int setNumber) {
		if (setNumber >= sets.size()) {
			throw new IllegalSetNumberSpecifiedException("GameSet-Number '" + setNumber + "' was not found");
		}
		return sets.get(setNumber);
	}

	@Override
	public void callback(final IToEntity sender) {
		super.callback(sender);
		checkIfNewPlayersAreKnown();
	}

	private void checkIfNewPlayersAreKnown() {
		if ((getSet(0).getSideA() == null) && (getSideA() != null)) {
			sets.forEach(set -> set.setSideA(getSideA()));
		}
		if ((getSet(0).getSideB() == null) && (getSideB() != null)) {
			sets.forEach(set -> set.setSideB(getSideB()));
		}
	}

	@Override
	public UUID calculateWinner() {
		final int scoreSideA = getScoreSideA();
		final int scoreSideB = getScoreSideB();
		if (scoreSideA == scoreSideB) {
			return null;
		} else if (scoreSideA > scoreSideB) {
			return getSideA();
		} else {
			return getSideB();
		}
	}

	public int getScoreSideA() {
		int result = 0;
		for (final SET set : sets) {
			if ((set.getSideA() != null) && set.getSideA().equals(set.getWinner())) {
				result++;
			}
		}
		return result;
	}

	public int getScoreSideB() {
		int result = 0;
		for (final SET set : sets) {
			if ((set.getSideB() != null) && set.getSideB().equals(set.getWinner())) {
				result++;
			}
		}
		return result;
	}

	/**
	 * 1.0 means sideA won 100% of the sets
	 *
	 * @return
	 */
	public double getFinalScore() {
		final int scoreA = getScoreSideA();
		final int scoreB = getScoreSideB();
		return scoreA / (scoreA + scoreB);
	}

	@Override
	public String toString() {
		return super.toString() + "(" + getScoreSideA() + ":" + getScoreSideB() + ")";
	}

	protected List<GenericMatch> getSets() {
		return (List<GenericMatch>) sets;
	}
}
