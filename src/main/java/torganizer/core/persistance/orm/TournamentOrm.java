package torganizer.core.persistance.orm;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import torganizer.core.types.TournamentType;

@Entity
@Table(name = "TOURNAMENTS")
public class TournamentOrm {
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "entity") )
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private EntityOrm entity;

	@Column(name = "TYPE", nullable = false)
	private TournamentType type;

	@Column(name = "CURRENT_ROUND")
	private Integer currentRound;

	@Column(name = "NUMBER_OF_ROUNDS")
	private Integer numberOfRounds;

	@Column(name = "BO_MATCH_LENGTH")
	private Integer boMatchLength;

	@ElementCollection
	@CollectionTable(name = "TOURNAMENTPLAYERS", joinColumns = @JoinColumn(name = "ID") )
	@Column(name = "PLAYER_ID")
	private List<UUID> participantList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tournament", cascade = { CascadeType.ALL })
	private List<TournamentRoundOrm> rounds;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tournament")
	private List<TristanInfoOrm> tristanInfos;

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

	public TournamentType getType() {
		return type;
	}

	public void setType(final TournamentType type) {
		this.type = type;
	}

	public List<UUID> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(final List<UUID> player) {
		this.participantList = player;
	}

	public Integer getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(final Integer currentRound) {
		this.currentRound = currentRound;
	}

	public Integer getBoMatchLength() {
		return boMatchLength;
	}

	public void setBoMatchLength(final Integer boMatchLength) {
		this.boMatchLength = boMatchLength;
	}

	public List<TournamentRoundOrm> getRounds() {
		return rounds;
	}

	public void setRounds(final List<TournamentRoundOrm> rounds) {
		this.rounds = rounds;
	}

	public Integer getNumberOfRounds() {
		return numberOfRounds;
	}

	public void setNumberOfRounds(final Integer numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

	public List<TristanInfoOrm> getTristanInfos() {
		return tristanInfos;
	}

	public void setTristanInfos(final List<TristanInfoOrm> tristanInfos) {
		this.tristanInfos = tristanInfos;
	}
}
