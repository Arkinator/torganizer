package torganizer.core.matches;

import java.util.UUID;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.IToEntity;
import torganizer.core.persistance.orm.EntityOrm;

public abstract class GenericMatch extends AbstractToEntity implements IToEntity {

	public GenericMatch(final String name) {
		super(name);
	}

	public GenericMatch(final EntityOrm entity) {
		super(entity);
	}

	public GenericMatch(final UUID uid) {
		super(uid, "Match" + uid);
	}

	public abstract UUID getWinner();

	public abstract void setSideA(UUID sideA);

	public abstract UUID getSideA();

	public abstract void setSideB(UUID sideB);

	public abstract UUID getSideB();
}