package torganizer.core;

public class Team {
	private final String name;

	public Team(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addPlayer(final Player playerA) {
		playerA.setTeam(this);
	}

	@Override
	public String toString() {
		return "[" + name + "]";
	}
}
