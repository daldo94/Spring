package setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import user.dao.UserDAO;
import user.service.DummyMailSender;
import user.service.UserService;
import user.test.UserServiceTest.TestUserService;

@Configuration
public class TestAppContext {
	@Autowired
	UserDAO userDAO;
	
	@Bean
	public UserService testUserService() {
		return new TestUserService();
	}
	
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
}
