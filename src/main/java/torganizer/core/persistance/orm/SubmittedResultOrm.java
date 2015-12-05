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
@Table(name = "SUBMITTED_RESULT")
public class SubmittedResultOrm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RESULT_ID", unique = true, nullable = false)
	private Integer resultId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID", nullable = false)
	private MatchOrm match;

	@Column(name = "WINNER")
	private UUID winner;

	@Column(name = "SUBMITTER")
	private UUID submitter;

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(final Integer resultId) {
		this.resultId = resultId;
	}

	public MatchOrm getMatch() {
		return match;
	}

	public void setMatch(final MatchOrm match) {
		this.match = match;
	}

	public UUID getWinner() {
		return winner;
	}

	public void setWinner(final UUID winner) {
		this.winner = winner;
	}

	public UUID getSubmitter() {
		return submitter;
	}

	public void setSubmitter(final UUID submitter) {
		this.submitter = submitter;
	}
}
