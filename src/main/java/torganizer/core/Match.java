package torganizer.core;

import java.util.ArrayList;
import java.util.List;

public class Match {
	private Team teamA;
	private Team teamB;
	private final List<Game> sets;

	public Match(final int numberOfSets, final Team teamA, final Team teamB) {
		sets = new ArrayList<Game>();
		for (int i = 0; i < numberOfSets; i++) {
			sets.add(new Game());
		}
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public Object getNumberOfSets() {
		return sets.size();
	}

	public void setPlayer(final int team, final int set, final Player player) {
		if (set >= sets.size()) {
			throw new IllegalSetNumberSpecifiedException("Set-Number '" + set + "' was not found");
		}
		final Game game = sets.get(set);
		if (team == 0) {
			game.setPlayerA(player);
		} else if (team == 1) {
			game.setPlayerB(player);
		} else {
			throw new IllegalTeamNumberSpecifiedException("Team number '" + team + "' could not be used. Only 0 and 1 are allowed");
		}
	}

	public Team getTeamA() {
		return teamA;
	}

	public void setTeamA(final Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setTeamB(final Team teamB) {
		this.teamB = teamB;
	}

	public Game getSet(final int setNumber) {
		return sets.get(setNumber);
	}

	public class IllegalSetNumberSpecifiedException extends RuntimeException {
		private static final long serialVersionUID = -5876494929525138426L;

		public IllegalSetNumberSpecifiedException(final String string) {
			super(string);
		}
	}

	public class IllegalTeamNumberSpecifiedException extends RuntimeException {
		private static final long serialVersionUID = -5876494929525138426L;

		public IllegalTeamNumberSpecifiedException(final String string) {
			super(string);
		}
	}

	public Team getWinner() {
		final int scoreTeamA = getScore(teamA);
		final int scoreTeamB = getScore(teamB);
		if (scoreTeamA == scoreTeamB) {
			return null;
		} else if (scoreTeamA > scoreTeamB) {
			return teamA;
		} else {
			return teamB;
		}
	}

	public int getScore(final Team teamQuery) {
		if (teamQuery == null) {
			return 0;
		}
		int result = 0;
		for (final Game game : sets) {
			final Player winner = game.getWinner();
			if ((winner != null) && teamQuery.equals(winner.getTeam())) {
				result++;
			}
		}
		return result;
	}
}
