package user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOFactory {
	@Bean
	public UserDAO userDAO() {
		UserDAO userDAO = new UserDAO();
		userDAO.setConnectionMaker(connectionMaker());
		return userDAO;
	}
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
}
