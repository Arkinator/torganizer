package torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractToEntity implements IToEntity {
	private List<IToEntity> callbackList = new ArrayList<IToEntity>();
	private UUID uid;

	protected AbstractToEntity() {
		this.uid = UUID.randomUUID();
	}

	protected AbstractToEntity(final UUID id) {
		this.uid = id;
	}

	public void addCallbackObject(final IToEntity target) {
		callbackList.add(target);
	}

	public void fireCallback() {
		for (final IToEntity target : callbackList) {
			target.callback(this);
		}
	}

	@Override
	public UUID getUid() {
		return uid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractToEntity other = (AbstractToEntity) obj;
		if (uid == null) {
			if (other.uid != null) {
				return false;
			}
		} else if (!uid.equals(other.uid)) {
			return false;
		}
		return true;
	}

	public List<IToEntity> getCallbackList() {
		return callbackList;
	}

	public void setCallbackList(final List<IToEntity> callbackList) {
		this.callbackList = callbackList;
	}

	public void setUid(final UUID uid) {
		this.uid = uid;
	}

	public abstract String getName();
}
