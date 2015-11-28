package torganizer.core.persistance.orm;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ENTITIES", uniqueConstraints = { @UniqueConstraint(columnNames = "ID"), @UniqueConstraint(columnNames = "NAME") })
public class EntityOrm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "NAME", unique = true, nullable = false)
	private String name;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "entity", cascade = CascadeType.ALL)
	private PlayerOrm player;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public PlayerOrm getPlayer() {
		return player;
	}

	public void setPlayer(final PlayerOrm player) {
		this.player = player;
	}
}
