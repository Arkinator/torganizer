package torganizer.core.persistance.objectservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.entities.Player;
import torganizer.core.matches.AbstractMatch;
import torganizer.core.matches.Game;
import torganizer.core.matches.SubmittedGameResult;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.interfaces.MatchDao;
import torganizer.core.persistance.interfaces.SubmittedResultDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.MatchOrm;
import torganizer.core.persistance.orm.SubmittedResultOrm;
import torganizer.core.types.MatchType;
import torganizer.exceptions.NotYetImplementedException;

@Transactional
@Component(value = "matchObjectService")
public class MatchObjectService {
	private PlayerObjectService playerObjectService;
	private EntityObjectService entityObjectService;
	private final EntityDao entityDao;
	private final MatchDao matchDao;
	private final SubmittedResultDao submittedResultDao;

	@Autowired
	public MatchObjectService(final PlayerObjectService playerObjectService, final EntityObjectService entityObjectService, final EntityDao entityDao, final MatchDao matchDao,
			final SubmittedResultDao submittedResultDao) {
		this.playerObjectService = playerObjectService;
		this.entityObjectService = entityObjectService;
		this.entityDao = entityDao;
		this.matchDao = matchDao;
		this.submittedResultDao = submittedResultDao;
	}

	public void addGame(final Game game) {
		entityObjectService.createEntity(game);
		final EntityOrm entityOrm = entityDao.getById(game.getUid());
		addGame(game, entityOrm);
	}

	public PlayerObjectService getPlayerObjectService() {
		return playerObjectService;
	}

	public void setPlayerObjectService(final PlayerObjectService playerObjectService) {
		this.playerObjectService = playerObjectService;
	}

	public EntityObjectService getEntityObjectService() {
		return entityObjectService;
	}

	public void setEntityObjectService(final EntityObjectService entityObjectService) {
		this.entityObjectService = entityObjectService;
	}

	public AbstractMatch<?> getMatchById(final UUID gameId) {
		return getMatchBo(entityDao.getById(gameId));
	}

	private void addGame(final Game game, final EntityOrm entityOrm) {
		addMatchOrm(game, entityOrm);
		final MatchOrm matchOrm = entityDao.getById(entityOrm.getUuid()).getMatch();
		matchOrm.setType(MatchType.GAME);
		matchOrm.setSubmittedResults(new ArrayList<>());
		for (final SubmittedGameResult submittedResult : game.getSubmittedResults()) {
			final SubmittedResultOrm resultOrm = getSubmittedResultOrm(submittedResult, matchOrm);
			matchOrm.getSubmittedResults().add(resultOrm);
		}
		matchDao.update(matchOrm);
	}

	private SubmittedResultOrm getSubmittedResultOrm(final SubmittedGameResult submittedResult, final MatchOrm matchOrm) {
		final SubmittedResultOrm resultOrm = new SubmittedResultOrm();
		resultOrm.setSubmitter(submittedResult.getSubmitter().getUid());
		resultOrm.setWinner(submittedResult.getWinner().getUid());
		resultOrm.setMatch(matchOrm);
		return submittedResultDao.persist(resultOrm);
	}

	private Game getGameBo(final EntityOrm entity) {
		final Game game = new Game();
		game.setSideA(playerObjectService.getPlayerById(entity.getMatch().getSideAId()));
		game.setSideB(playerObjectService.getPlayerById(entity.getMatch().getSideBId()));
		final EntityOrm winner = entityDao.getById(entity.getMatch().getWinner());
		if (winner != null) {
			game.setWinner(playerObjectService.getPlayerById(entity.getMatch().getWinner()));
		}
		if (entity.getMatch().getSubmittedResults() != null) {
			game.setSubmittedResults(loadSubmittedResultsForMatch(entity.getMatch()));
		}
		return game;
	}

	private List<SubmittedGameResult> loadSubmittedResultsForMatch(final MatchOrm match) {
		final List<SubmittedGameResult> result = new ArrayList<>();
		for (final SubmittedResultOrm resultOrm : match.getSubmittedResults()) {
			result.add(createSubmittedResultBo(resultOrm));
		}
		return result;
	}

	private SubmittedGameResult createSubmittedResultBo(final SubmittedResultOrm resultOrm) {
		final Player submitter = playerObjectService.getPlayerById(resultOrm.getSubmitter());
		final Player winner = playerObjectService.getPlayerById(resultOrm.getWinner());
		return new SubmittedGameResult(submitter, winner);
	}

	private void addMatchOrm(final AbstractMatch<?> match, final EntityOrm entityOrm) {
		final MatchOrm result = new MatchOrm();
		result.setEntity(entityOrm);
		result.setSideAId(match.getSideA().getUid());
		result.setSideBId(match.getSideB().getUid());
		if (match.getWinner() != null) {
			result.setWinner(match.getWinner().getUid());
		}
		entityOrm.setMatch(result);
		matchDao.persist(result);
	}

	private AbstractMatch<?> getMatchBo(final EntityOrm entity) {
		if (entity.getMatch() == null) {
			throw new NullPointerException();
		}
		if (entity.getMatch().getType() == MatchType.GAME) {
			return getGameBo(entity);
		}
		if (entity.getMatch().getType() == MatchType.SINGLE_PLAYER_MATCH_SERIES) {
			return getGameBo(entity);
		}
		throw new NotYetImplementedException("Was the Match type correctly set? (=" + entity.getMatch().getType() + ")");
	}
}
