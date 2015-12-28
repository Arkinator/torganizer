package torganizer.core.persistance.orm;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRISTAN_INFO")
public class TristanInfoOrm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INFO_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID", nullable = false)
	private TournamentOrm tournament;

	@Column(name = "PLAYER_ID")
	private UUID playerId;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public TournamentOrm getTournament() {
		return tournament;
	}

	public void setTournament(final TournamentOrm tournament) {
		this.tournament = tournament;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(final UUID playerId) {
		this.playerId = playerId;
	}
}
