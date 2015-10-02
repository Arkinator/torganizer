package torganizer.core.matches;

import torganizer.core.entities.IToEntity;

public abstract class AbstractMatch<TYPE extends IToEntity> implements IGenericMatch<TYPE> {
	private TYPE sideA;
	private TYPE sideB;

	public AbstractMatch(final TYPE sideA, final TYPE sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
	}

	public AbstractMatch() {
	}

	public void setSideA(final TYPE sideA) {
		this.sideA = sideA;
	}

	public TYPE getSideA() {
		return sideA;
	}

	public void setSideB(final TYPE sideB) {
		this.sideB = sideB;
	}

	public TYPE getSideB() {
		return sideB;
	}

	@Override
	public String toString() {
		return sideA + " vs " + sideB;
	}
}
