package torganizer.core.types;

public enum StarcraftLeague {
	Bronze, Silver, Gold, Platinum, Diamond, Master, Grandmaster;

	public String getLiquipediaImageCode() {
		return "[[File:" + toString() + "Medium.png|17px]]";
	}
}
