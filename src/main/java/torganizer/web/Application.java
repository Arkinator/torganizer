package torganizer.web;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource(locations = { "/spring-module.xml" })
public class Application {
	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataSource dataSource() {
		// no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
		final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		final EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).build();
		return db;
	}
}