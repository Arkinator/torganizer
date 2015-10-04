package torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCallbackObject implements IToEntity {
	private final List<IToEntity> callbackList = new ArrayList<IToEntity>();

	public void addCallbackObject(final IToEntity target) {
		callbackList.add(target);
	}

	public void fireCallback() {
		for (final IToEntity target : callbackList) {
			target.callback(this);
		}
	}
}
