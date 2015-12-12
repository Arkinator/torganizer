package torganizer.core.types;

public enum StarcraftRace {
	Terran, Zerg, Protoss, Random, Switcher;

	public static StarcraftRace parseShort(final String shortRaceCode) {
		switch (shortRaceCode) {
		case "T":
			return StarcraftRace.Terran;
		case "Z":
			return StarcraftRace.Zerg;
		case "P":
			return StarcraftRace.Protoss;
		case "R":
			return StarcraftRace.Random;
		case "S":
			return StarcraftRace.Switcher;
		default:
			throw new UnrecognizedValueException("Unknown Race Short: '" + shortRaceCode + "'");
		}
	}

	public static class UnrecognizedValueException extends RuntimeException {
		private static final long serialVersionUID = -4740199930877608596L;

		public UnrecognizedValueException(final String string) {
			super(string);
		}
	}

	public String getLiquipediaCode() {
		switch (this) {
		case Terran:
			return "t";
		case Zerg:
			return "z";
		case Protoss:
			return "p";
		default:
			return "r";
		}
	}
}
