package torganizer.core.persistance.orm;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import torganizer.core.types.StarcraftLeague;
import torganizer.core.types.StarcraftRace;

@Entity
@Table(name = "PLAYERS")
public class PlayerOrm {
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "entity") )
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private EntityOrm entity;

	@Column(name = "ADMIN")
	private Boolean admin;

	@Column(name = "BNETCODE")
	private int battleNetCode;

	@Column(name = "RACE")
	private StarcraftRace race;

	@Column(name = "LEAGUE")
	private StarcraftLeague league;

	@Column(name = "TEAM")
	private UUID team;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public EntityOrm getEntity() {
		return entity;
	}

	public void setEntity(final EntityOrm entity) {
		this.entity = entity;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(final Boolean admin) {
		this.admin = admin;
	}

	public int getBattleNetCode() {
		return battleNetCode;
	}

	public void setBattleNetCode(final int battleNetCode) {
		this.battleNetCode = battleNetCode;
	}

	public StarcraftRace getRace() {
		return race;
	}

	public void setRace(final StarcraftRace race) {
		this.race = race;
	}

	public StarcraftLeague getLeague() {
		return league;
	}

	public void setLeague(final StarcraftLeague league) {
		this.league = league;
	}

	public UUID getTeam() {
		return team;
	}

	public void setTeam(final UUID team) {
		this.team = team;
	}
}
