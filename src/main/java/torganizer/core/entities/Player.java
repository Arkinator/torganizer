package torganizer.core.entities;

import java.util.UUID;

import torganizer.core.persistance.orm.PlayerOrm;
import torganizer.core.types.StarcraftLeague;
import torganizer.core.types.StarcraftRace;
import torganizer.web.data.UserInformation;

public class Player extends AbstractToEntity implements IToParticipant {
	private boolean isAdmin;
	private UUID teamUid;
	private byte[] passwordHash;
	private String passwordSalt;
	private int gmtOffset;
	private int battleNetCode;
	private StarcraftRace race;
	private StarcraftLeague league;

	public Player(final String name) {
		super(name);
		this.isAdmin = false;
		getEntityOrm().setPlayer(new PlayerOrm());
		getEntityOrm().getPlayer().setEntity(getEntityOrm());
		getEntityOrm().getPlayer().setAdmin(false);
	}

	public Player(final PlayerOrm orm) {
		super(orm.getEntity());
		this.isAdmin = orm.getAdmin();
		this.battleNetCode = orm.getBattleNetCode();
		this.race = orm.getRace();
		this.league = orm.getLeague();
		this.teamUid = orm.getTeam();
	}

	public void setAdmin(final boolean isAdmin) {
		this.isAdmin = isAdmin;
		getEntityOrm().getPlayer().setAdmin(isAdmin);
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public UUID getTeamUid() {
		return teamUid;
	}

	public void setTeamUid(final UUID teamUid) {
		this.teamUid = teamUid;
		getEntityOrm().getPlayer().setTeam(teamUid);
	}

	@Override
	public String toString() {
		if (teamUid == null) {
			return getName();
		} else {
			return "[" + teamUid + "]" + getName();
		}
	}

	@Override
	public boolean equalsOrInTeam(final IToParticipant other) {
		if (super.equals(other)) {
			return true;
		} else {
			if (other instanceof Team) {
				if (teamUid.equals(other.getUid())) {
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

	public UserInformation extractUserInformation() {
		final UserInformation userInfo = new UserInformation();
		userInfo.username = getName();
		return userInfo;
	}

	public void setTimezoneOffset(final int gmtOffset) {
		this.gmtOffset = gmtOffset;
	}

	public int getTimezoneOffset() {
		return gmtOffset;
	}

	public int getBattleNetCode() {
		return battleNetCode;
	}

	public void setBattleNetCode(final int battleNetCode) {
		this.battleNetCode = battleNetCode;
		getEntityOrm().getPlayer().setBattleNetCode(battleNetCode);
	}

	public StarcraftRace getRace() {
		return race;
	}

	public void setRace(final StarcraftRace race) {
		this.race = race;
		getEntityOrm().getPlayer().setRace(race);
	}

	public StarcraftLeague getLeague() {
		return league;
	}

	public void setLeague(final StarcraftLeague league) {
		this.league = league;
		getEntityOrm().getPlayer().setLeague(league);
	}
}
