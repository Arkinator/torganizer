package torganizer.core.matches;

import torganizer.core.entities.IToEntity;

public interface IGenericMatch<TYPE extends IToEntity> {

	public abstract TYPE getWinner();

	public abstract void setSideA(TYPE sideA);

	public abstract TYPE getSideA();

	public abstract void setSideB(TYPE sideB);

	public abstract TYPE getSideB();
}