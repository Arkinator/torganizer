package torganizer.core.matches;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.IToParticipant;

public abstract class GenericMatch<TYPE extends IToParticipant> implements IToEntity {

	private final List<IToEntity> callbackList = new ArrayList<IToEntity>();

	public abstract TYPE getWinner();

	public abstract void setSideA(TYPE sideA);

	public abstract TYPE getSideA();

	public abstract void setSideB(TYPE sideB);

	public abstract TYPE getSideB();

	public void addCallbackObject(final IToEntity target) {
		callbackList.add(target);
	}

	public void fireCallback() {
		for (final IToEntity target : callbackList) {
			target.callback(this);
		}
	}
}