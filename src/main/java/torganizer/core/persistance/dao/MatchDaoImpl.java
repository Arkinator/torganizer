package torganizer.core.persistance.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.persistance.interfaces.MatchDao;
import torganizer.core.persistance.orm.MatchOrm;

@Component(value = "matchDao")
public class MatchDaoImpl extends AbstractDao<MatchOrm> implements MatchDao {
	@Autowired
	public MatchDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<MatchOrm> getAllMatches() {
		return getAllEntities();
	}

	@Override
	protected Class getEntityClass() {
		return MatchOrm.class;
	}
}
