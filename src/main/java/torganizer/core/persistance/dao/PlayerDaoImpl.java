package torganizer.core.persistance.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.orm.PlayerOrm;
import torganizer.exceptions.NotYetImplementedException;

@Transactional
@Component(value = "playerDao")
public class PlayerDaoImpl implements PlayerDao {
	private final SessionFactory sessionFactory;

	@Autowired
	public PlayerDaoImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Long persist(final PlayerOrm entity) {
		final Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(entity);
	}

	@Override
	public PlayerOrm getById(final UUID id) {
		throw new NotYetImplementedException();
	}

	@Override
	public PlayerOrm getByName(final String name) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<PlayerOrm> getAllPlayers() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(PlayerOrm.class);
		final List<PlayerOrm> persons = criteria.list();
		return persons;
	}
}
