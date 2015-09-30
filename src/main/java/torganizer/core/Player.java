package torganizer.core;

public class Player {
	private String name;
	private boolean isAdmin;
	private Team team;

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
}
