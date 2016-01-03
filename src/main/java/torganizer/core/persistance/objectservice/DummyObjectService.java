package torganizer.core.persistance.objectservice;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.Game;
import torganizer.core.tournaments.TrisTournament;

public class DummyObjectService implements GlobalObjectService {
	private final List<AbstractToEntity> entities;
	private final HashMap<UUID, AbstractToEntity> idMap;

	public DummyObjectService(final List<AbstractToEntity> entities) {
		this.entities = entities;
		this.idMap = new HashMap<>();
		entities.forEach(e -> idMap.put(e.getUid(), e));
	}

	@Override
	public Player getPlayerById(final UUID id) {
		return (Player) idMap.get(id);
	}

	@Override
	public void addEntity(final AbstractToEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Player getPlayerByName(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPlayer(final Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGame(final Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMatch(final BestOfMatchSinglePlayer match) {
		// TODO Auto-generated method stub

	}

	@Override
	public Game getGameById(final UUID gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BestOfMatchSinglePlayer getBestOfMatchById(final UUID gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateEntity(final AbstractToEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTeam(final Team t) {
		// TODO Auto-generated method stub

	}

	@Override
	public Team getTeamById(final UUID teamId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TrisTournament getTournamentById(final UUID tournamentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
