package torganizer.core.entities;

public class Team implements IToParticipant {
	private final String name;

	public Team(final String name) {
		this.name = name;
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
}
