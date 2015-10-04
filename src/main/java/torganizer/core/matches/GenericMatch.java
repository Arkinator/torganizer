package torganizer.core.matches;

import torganizer.core.entities.AbstractCallbackObject;
import torganizer.core.entities.IToEntity;
import torganizer.core.entities.IToParticipant;

public abstract class GenericMatch<TYPE extends IToParticipant> extends AbstractCallbackObject implements IToEntity {
	public GenericMatch() {

	}

	public abstract TYPE getWinner();

	public abstract void setSideA(TYPE sideA);

	public abstract TYPE getSideA();

	public abstract void setSideB(TYPE sideB);

	public abstract TYPE getSideB();
}