package torganizer.core.matches;

import torganizer.core.entities.IToParticipant;

public abstract class AbstractMatch<TYPE extends IToParticipant> extends GenericMatch<TYPE> {
	private TYPE sideA;
	private TYPE sideB;

	public AbstractMatch(final TYPE sideA, final TYPE sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
	}

	public AbstractMatch() {
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
}
