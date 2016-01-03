package torganizer.core.persistance.objectservice;

import java.util.UUID;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.Game;
import torganizer.core.tournaments.TrisTournament;

public interface GlobalObjectService {
	Player getPlayerById(UUID id);

	void addEntity(AbstractToEntity entity);

	Player getPlayerByName(String name);

	void addPlayer(Player player);

	void addGame(Game game);

	void addMatch(BestOfMatchSinglePlayer match);

	Game getGameById(UUID gameId);

	BestOfMatchSinglePlayer getBestOfMatchById(UUID gameId);

	void updateEntity(AbstractToEntity entity);

	void addTeam(Team t);

	Team getTeamById(UUID teamId);

	TrisTournament getTournamentById(UUID tournamentId);
}