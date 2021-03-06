package torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.ApplicationContextProvider;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.core.persistance.orm.EntityOrm;

public abstract class AbstractToEntity implements IToEntity {
	private List<IToEntity> callbackList = new ArrayList<IToEntity>();
	private final UUID uid;
	private String name;

	private final EntityOrm orm;

	protected AbstractToEntity(final String name) {
		this.uid = UUID.randomUUID();
		this.name = name;
		this.orm = new EntityOrm();
		this.orm.setName(getName());
		this.orm.setUuid(getUid());
	}

	protected AbstractToEntity(final EntityOrm orm) {
		this.orm = orm;
		this.name = orm.getName();
		this.uid = orm.getUuid();
	}

	public AbstractToEntity(final UUID uid, final String name) {
		this.uid = uid;
		this.name = name;
		this.orm = new EntityOrm();
		this.orm.setName(getName());
		this.orm.setUuid(getUid());
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

	public final String getName() {
		return name;
	}

	public final void setName(final String newName) {
		this.name = newName;
		getEntityOrm().setName(newName);
	}

	public EntityOrm getEntityOrm() {
		return orm;
	}

	public static GlobalObjectService getGlobalObjectService() {
		return ApplicationContextProvider.getGlobalObjectService();
	}
}
