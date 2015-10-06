package torganizer.core.persistance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import torganizer.core.entities.AbstractToEntity;

public class DataSource {
	private static IDataSource dataSource;

	static {
		final ApplicationContext context = new ClassPathXmlApplicationContext("spring-Module.xml");

		dataSource = (IDataSource) context.getBean("datasourceBean");
	}

	public static void persistEntity(final AbstractToEntity entity) {
		dataSource.persistEntity(entity);
	}

	public static AbstractToEntity getEntity(final long playerId) {
		return dataSource.findEntityById(playerId);
	}

}
