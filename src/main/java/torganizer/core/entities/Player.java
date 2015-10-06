package torganizer.core.entities;

public class Player extends AbstractToEntity implements IToParticipant {
	private String name;
	private boolean isAdmin;
	private Team team;
	private byte[] passwordHash;
	private String passwordSalt;

	public Player(final String name) {
		this.setName(name);
		this.isAdmin = false;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setAdmin(final boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(final Team team) {
		this.team = team;
	}

	@Override
	public String toString() {
		if (team == null) {
			return name;
		} else {
			return "[" + team + "]" + name;
		}
	}

	@Override
	public boolean equalsOrInTeam(final IToParticipant other) {
		if (super.equals(other)) {
			return true;
		} else {
			if (other instanceof Team) {
				if (getTeam().equals(other)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public void callback(final IToEntity sender) {
		// TODO Auto-generated method stub

	}

	public void setPasswordHash(final byte[] passwordHash) {
		this.passwordHash = passwordHash;
	}

	public byte[] getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordSalt(final String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordSalt() {
		return this.passwordSalt;
	}
}
