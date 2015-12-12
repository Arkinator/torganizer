package torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.TeamOrm;

public class Team extends AbstractToEntity implements IToParticipant {
	private UUID owner;
	private final List<UUID> lieutenants;
	private String shortName;
	private String liquipediaFlagCode;
	private String liquipediaName;

	public Team(final String name) {
		super(name);
		this.lieutenants = new ArrayList<UUID>();
		getEntityOrm().setTeam(new TeamOrm());
		getEntityOrm().getTeam().setEntity(getEntityOrm());
	}

	public Team(final EntityOrm entity) {
		super(entity);
		this.lieutenants = new ArrayList<UUID>();
		this.shortName = getEntityOrm().getTeam().getShortname();
		this.liquipediaFlagCode = getEntityOrm().getTeam().getFlagcode();
		this.liquipediaName = getEntityOrm().getTeam().getLiquipediaName();
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
		getEntityOrm().getTeam().setOwner(owner);
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
		getEntityOrm().getTeam().setShortname(shortName);
	}

	public String getLiquipediaFlagCode() {
		return liquipediaFlagCode;
	}

	public void setLiquipediaFlagCode(final String liquipediaFlagCode) {
		this.liquipediaFlagCode = liquipediaFlagCode;
		getEntityOrm().getTeam().setFlagcode(liquipediaFlagCode);
	}

	public String getLiquipediaName() {
		return liquipediaName;
	}

	public void setLiquipediaName(final String liquipediaName) {
		this.liquipediaName = liquipediaName;
		getEntityOrm().getTeam().setLiquipediaName(liquipediaName);
	}
}
