package torganizer.core.persistance.orm;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.types.TournamentType;

@Entity
@Table(name = "TOURNAMENT_ROUND")
public class TournamentRoundOrm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROUND_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID", nullable = false)
	private TournamentOrm tournament;

	@Column(name = "ROUND_IN_TOURNAMENT")
	private Integer roundInTournament;

	@ElementCollection
	@CollectionTable(name = "TOURNAMENT_ROUND_MATCHES", joinColumns = @JoinColumn(name = "ID") )
	@Column(name = "MATCH_ID")
	private List<UUID> matches;

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

	public Integer getRoundInTournament() {
		return roundInTournament;
	}

	public void setRoundInTournament(final Integer roundInTournament) {
		this.roundInTournament = roundInTournament;
	}

	public List<UUID> getMatches() {
		return matches;
	}

	public void setMatches(final List<UUID> matches) {
		this.matches = matches;
	}
}
