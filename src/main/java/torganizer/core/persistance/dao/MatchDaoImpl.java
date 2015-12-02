package torganizer.core.persistance.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.persistance.interfaces.MatchDao;
import torganizer.core.persistance.orm.MatchOrm;
import torganizer.exceptions.NotYetImplementedException;

@Transactional
@Component(value = "matchDao")
public class MatchDaoImpl implements MatchDao {
	private final SessionFactory sessionFactory;

	@Autowired
	public MatchDaoImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Long persist(final MatchOrm entity) {
		final Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(entity);
	}

	@Override
	public MatchOrm getById(final UUID id) {
		throw new NotYetImplementedException();
	}

	@Override
	public MatchOrm getByName(final String name) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<MatchOrm> getAllMatches() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(MatchOrm.class);
		final List<MatchOrm> persons = criteria.list();
		return persons;
	}
}
