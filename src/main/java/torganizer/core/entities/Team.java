package torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;

public class Team implements IToParticipant {
	private final String name;
	private Player owner;
	private final List<Player> lieutenants;

	public Team(final String name) {
		this.name = name;
		this.lieutenants = new ArrayList<Player>();
	}

	public String getName() {
		return name;
	}

	public void addPlayer(final Player newPlayer) {
		newPlayer.setTeam(this);
	}

	@Override
	public String toString() {
		return "[" + name + "]";
	}

	@Override
	public boolean equalsOrInTeam(final IToParticipant other) {
		if (super.equals(other)) {
			return true;
		} else {
			if (other instanceof Player) {
				if (((Player) other).getTeam().equals(this)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(final Player owner) {
		this.owner = owner;
	}

	public List<Player> getLieutenants() {
		return lieutenants;
	}

	public boolean isLieutenant(final Player player) {
		return getLieutenants().contains(player);
	}

	public void addLieutenant(final Player player) {
		lieutenants.add(player);
	}
}
