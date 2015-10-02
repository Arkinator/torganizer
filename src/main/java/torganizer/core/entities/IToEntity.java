package torganizer.core.entities;

public interface IToEntity {
	/**
	 * Returns true either if
	 * - both entities are equal or
	 * - one entity is a team and the other a member of said team
	 *
	 * @param other
	 * @return
	 */
	boolean equalsOrInTeam(IToEntity other);
}
