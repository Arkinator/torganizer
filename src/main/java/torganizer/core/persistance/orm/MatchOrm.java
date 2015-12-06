package torganizer.core.persistance.orm;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import torganizer.core.types.MatchType;

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

	@Column(name = "WINNER")
	private UUID winner;

	@Column(name = "TYPE")
	private MatchType type;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "match")
	private List<SubmittedResultOrm> submittedResults;

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

	public List<SubmittedResultOrm> getSubmittedResults() {
		return submittedResults;
	}

	public void setSubmittedResults(final List<SubmittedResultOrm> submittedResults) {
		this.submittedResults = submittedResults;
	}
}