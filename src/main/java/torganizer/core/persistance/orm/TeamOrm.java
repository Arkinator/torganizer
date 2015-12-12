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

@Entity
@Table(name = "TEAMS")
public class TeamOrm {
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "entity") )
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private EntityOrm entity;

	@Column(name = "OWNER")
	private UUID owner;

	@Column(name = "SHORTNAME")
	private String shortname;

	@Column(name = "FLAGCODE")
	private String flagcode;

	@Column(name = "LIQUIPEDIANAME")
	private String liquipediaName;

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

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(final UUID owner) {
		this.owner = owner;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(final String shortname) {
		this.shortname = shortname;
	}

	public String getFlagcode() {
		return flagcode;
	}

	public void setFlagcode(final String flagcode) {
		this.flagcode = flagcode;
	}

	public String getLiquipediaName() {
		return liquipediaName;
	}

	public void setLiquipediaName(final String liquipediaName) {
		this.liquipediaName = liquipediaName;
	}
}
