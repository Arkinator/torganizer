package torganizer.core.matches;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.IToParticipant;

public abstract class AbstractMatch<TYPE extends IToParticipant> extends GenericMatch<TYPE> {
	private TYPE sideA;
	private TYPE sideB;
	private TYPE winner = null;

	public AbstractMatch(final TYPE sideA, final TYPE sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
	}

	public AbstractMatch() {
	}

	@Override
	public TYPE getWinner() {
		return winner;
	}

	@Override
	public void setSideA(final TYPE sideA) {
		this.sideA = sideA;
	}

	@Override
	public TYPE getSideA() {
		return sideA;
	}

	@Override
	public void setSideB(final TYPE sideB) {
		this.sideB = sideB;
	}

	@Override
	public TYPE getSideB() {
		return sideB;
	}

	@Override
	public String toString() {
		return sideA + " vs " + sideB;
	}

	public void callback(final IToEntity sender) {
		refresh();
	}

	public void refresh() {
		final TYPE newWinner = calculateWinner();
		if (newWinner != winner) {
			winner = newWinner;
			fireCallback();
		}
	}

	public abstract TYPE calculateWinner();
}
