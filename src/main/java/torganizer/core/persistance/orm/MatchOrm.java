package torganizer.core.persistance.orm;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import torganizer.core.types.MatchType;
import torganizer.core.types.StarcraftLeague;
import torganizer.core.types.StarcraftRace;

@Entity
@Table(name = "MATCHES")
public class MatchOrm {
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "entity") )
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private EntityOrm entity;

	@Column(name = "SIDEA")
	private UUID sideAId;

	@Column(name = "SIDEB")
	private UUID sideBId;

	@Column(name = "SIDEA_RACE")
	private StarcraftRace sideARace;

	@Column(name = "SIDEB_RACE")
	private StarcraftRace sideBRace;

	@Column(name = "SIDEA_LEAGUE")
	private StarcraftLeague sideALeague;

	@Column(name = "SIDEB_LEAGUE")
	private StarcraftLeague sideBLeague;

	@Column(name = "WINNER")
	private UUID winner;

	@Column(name = "TYPE")
	private MatchType type;

	@Column(name = "SIDEA_WINNER")
	private UUID sideASubmittedWinner;

	@Column(name = "SIDEB_WINNER")
	private UUID sideBSubmittedWinner;

	@Column(name = "ADMIN_WINNER")
	private UUID adminSubmittedWinner;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID", unique = true)
	private List<EntityOrm> sets;

	@Column(name = "NUMBER_OF_SETS")
	private int numberOfSets;

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

	public UUID getSideAId() {
		return sideAId;
	}

	public void setSideAId(final UUID sideAId) {
		this.sideAId = sideAId;
	}

	public UUID getSideBId() {
		return sideBId;
	}

	public void setSideBId(final UUID sideBId) {
		this.sideBId = sideBId;
	}

	public UUID getWinner() {
		return winner;
	}

	public void setWinner(final UUID winner) {
		this.winner = winner;
	}

	public MatchType getType() {
		return type;
	}

	public void setType(final MatchType type) {
		this.type = type;
	}

	public UUID getSideASubmittedWinner() {
		return sideASubmittedWinner;
	}

	public void setSideASubmittedWinner(final UUID sideASubmittedWinner) {
		this.sideASubmittedWinner = sideASubmittedWinner;
	}

	public UUID getSideBSubmittedWinner() {
		return sideBSubmittedWinner;
	}

	public void setSideBSubmittedWinner(final UUID sideBSubmittedWinner) {
		this.sideBSubmittedWinner = sideBSubmittedWinner;
	}

	public UUID getAdminSubmittedWinner() {
		return adminSubmittedWinner;
	}

	public void setAdminSubmittedWinner(final UUID adminSubmittedWinner) {
		this.adminSubmittedWinner = adminSubmittedWinner;
	}

	public List<EntityOrm> getSets() {
		return sets;
	}

	public void setSets(final List<EntityOrm> sets) {
		this.sets = sets;
	}

	public int getNumberOfSets() {
		return numberOfSets;
	}

	public void setNumberOfSets(final int numberOfSets) {
		this.numberOfSets = numberOfSets;
	}

	public StarcraftRace getSideARace() {
		return sideARace;
	}

	public void setSideARace(final StarcraftRace sideARace) {
		this.sideARace = sideARace;
	}

	public StarcraftRace getSideBRace() {
		return sideBRace;
	}

	public void setSideBRace(final StarcraftRace sideBRace) {
		this.sideBRace = sideBRace;
	}

	public StarcraftLeague getSideALeague() {
		return sideALeague;
	}

	public void setSideALeague(final StarcraftLeague sideALeague) {
		this.sideALeague = sideALeague;
	}

	public StarcraftLeague getSideBLeague() {
		return sideBLeague;
	}

	public void setSideBLeague(final StarcraftLeague sideBLeague) {
		this.sideBLeague = sideBLeague;
	}
}
