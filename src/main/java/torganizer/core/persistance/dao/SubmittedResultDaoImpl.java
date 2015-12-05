package torganizer.core.persistance.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.persistance.interfaces.SubmittedResultDao;
import torganizer.core.persistance.orm.SubmittedResultOrm;

@Component(value = "submittedResultDao")
public class SubmittedResultDaoImpl extends AbstractDao<SubmittedResultOrm> implements SubmittedResultDao {
	@Autowired
	public SubmittedResultDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class getEntityClass() {
		return SubmittedResultOrm.class;
	}

	@Override
	public SubmittedResultOrm getByResultId(final long id) {
		return (SubmittedResultOrm) getSession().get(getEntityClass(), id);
	}
}
