package torganizer.core.persistance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

public class DataSource {
	private static IDataSource dataSource;

	static {
		final ApplicationContext context = new ClassPathXmlApplicationContext("spring-module.xml");

		dataSource = (IDataSource) context.getBean("datasourceBean");
	}

	public static void persistEntity(final AbstractToEntity entity) {
		dataSource.persistEntity(entity);
	}

	public static AbstractToEntity getEntity(final long playerId) {
		return dataSource.findEntityById(playerId);
	}

	public static Player getUserByName(final String username) {
		return dataSource.findUserByName(username);
	}
}
