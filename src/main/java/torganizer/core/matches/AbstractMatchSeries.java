package torganizer.core.matches;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.IToEntity;

public abstract class AbstractMatchSeries<BELIGERENT extends IToEntity, SET extends IGenericMatch<?>> //
		extends AbstractMatch<BELIGERENT> //
		implements IGenericMatch<BELIGERENT> {
	private final List<SET> sets;

	public AbstractMatchSeries(final int numberOfSets, final BELIGERENT sideA, final BELIGERENT sideB) {
		super(sideA, sideB);
		sets = new ArrayList<SET>();
		for (int i = 0; i < numberOfSets; i++) {
			sets.add(constructNewSet());
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

	public BELIGERENT getWinner() {
		final int scoreSideA = getScore(getSideA());
		final int scoreSideB = getScore(getSideB());
		if (scoreSideA == scoreSideB) {
			return null;
		} else if (scoreSideA > scoreSideB) {
			return getSideA();
		} else {
			return getSideB();
		}
	}

	public int getScore(final BELIGERENT beligerent) {
		if (beligerent == null) {
			return 0;
		}
		int result = 0;
		for (final SET set : sets) {
			if (beligerent.equalsOrInTeam(set.getWinner())) {
				result++;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + getScore(getSideA()) + ":" + getScore(getSideB()) + ")";
	}
}
