package setting;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

import user.dao.UserDAO;
import user.dao.UserDAOJdbc;
import user.service.DummyMailSender;
import user.service.UserService;
import user.service.UserServiceImpl;
import user.sqlservice.OxmSqlService;
import user.sqlservice.SqlRegistry;
import user.sqlservice.SqlService;
import user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import user.test.UserServiceTest.TestUserService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "user")
@Import(SqlServiceContext.class)
public class AppContext {
	//@Resource DataSource embeddedDatabase;
	@Autowired 
	UserDAO userDAO;
	
	/**
	 * DB Connection and Transaction
	 */
	@Bean
	public DataSource dataSource(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/spring_db");
		dataSource.setUsername("dohyun");
		dataSource.setPassword("1234");
		
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}

	/**
	 * Application logic and Test
	 */
	/*//Using @Repository auto bean enrollment 
	@Bean
	public UserDAO userDAO(){
		return new UserDAOJdbc();
	}*/
	
	/*
	@Bean//Using @Service auto bean enrollment
	public UserService userService() {
		UserServiceImpl service = new UserServiceImpl();
		service.setUserDAO(this.userDAO);
		service.setMailSender(mailSender());
		return service;
	}
	*/

}