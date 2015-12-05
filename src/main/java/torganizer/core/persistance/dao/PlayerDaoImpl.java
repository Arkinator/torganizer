package torganizer.core.persistance.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.orm.PlayerOrm;

@Component(value = "playerDao")
public class PlayerDaoImpl extends AbstractDao<PlayerOrm> implements PlayerDao {
	@Autowired
	public PlayerDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<PlayerOrm> getAllPlayers() {
		return getAllEntities();
	}

	@Override
	protected Class getEntityClass() {
		return PlayerOrm.class;
	}
}
