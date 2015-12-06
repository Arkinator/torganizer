package torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team extends AbstractToEntity implements IToParticipant {
	private UUID owner;
	private final List<UUID> lieutenants;

	public Team(final String name) {
		super(name);
		this.lieutenants = new ArrayList<UUID>();
	}

	public void addPlayer(final Player newPlayer) {
		newPlayer.setTeamUid(this.getUid());
	}

	@Override
	public String toString() {
		return "[" + getName() + "]";
	}

	@Override
	public boolean equalsOrInTeam(final IToParticipant other) {
		if (super.equals(other)) {
			return true;
		} else {
			if (other instanceof Player) {
				if (((Player) other).getTeamUid().equals(this.getUid())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(final UUID owner) {
		this.owner = owner;
	}

	public List<UUID> getLieutenants() {
		return lieutenants;
	}

	public boolean isLieutenant(final UUID player) {
		return getLieutenants().contains(player);
	}

	public void addLieutenant(final UUID player) {
		lieutenants.add(player);
	}

	@Override
	public void callback(final IToEntity sender) {

	}
}
