package torganizer.core.types;

public class NullPlayerInfo implements IPlayerShortInfo {
	@Override
	public StarcraftRace getRace() {
		return null;
	}

	@Override
	public StarcraftLeague getLeague() {
		return null;
	}

	@Override
	public double getElo() {
		return 0;
	}
}
