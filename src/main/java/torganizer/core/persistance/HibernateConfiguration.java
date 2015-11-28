package torganizer.core.persistance;

//@Configuration
//@EnableTransactionManagement
// @ComponentScan({ "com.websystique.springmvc.configuration" })
public class HibernateConfiguration {
	// @Autowired
	// private Environment environment;
	//
	// @Bean
	// public LocalSessionFactoryBean sessionFactory() {
	// final LocalSessionFactoryBean sessionFactory = new
	// LocalSessionFactoryBean();
	// sessionFactory.setDataSource(dataSource());
	// sessionFactory.setPackagesToScan(new String[] { "torganizer.core" });
	// sessionFactory.setHibernateProperties(hibernateProperties());
	// return sessionFactory;
	// }
	//
	// @Bean
	// public DataSource dataSource() {
	// final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	// dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
	// dataSource.setUrl("jdbc:hsqldb:file:data.hsql");
	// dataSource.setUsername("sa");
	// dataSource.setPassword("");
	// return dataSource;
	// }
	//
	// private Properties hibernateProperties() {
	// final Properties properties = new Properties();
	// properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
	// return properties;
	// }
	//
	// @Bean
	// @Autowired
	// public HibernateTransactionManager transactionManager(final
	// SessionFactory s) {
	// final HibernateTransactionManager txManager = new
	// HibernateTransactionManager();
	// txManager.setSessionFactory(s);
	// return txManager;
	// }
}