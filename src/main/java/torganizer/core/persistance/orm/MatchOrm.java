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
}
