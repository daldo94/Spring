package user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import user.dao.UserDAO;
import user.domain.Level;
import user.domain.User;
import user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  "/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired
	MailSender mailSender;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("dohyun1","�赵��1","1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "kimdohyunlll@naver.com"),
				new User("dohyun2","�赵��2","1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "kimdohyunlll@naver.com"),
				new User("dohyun3","�赵��3","1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1, "kimdohyunlll@naver.com"),
				new User("dohyun4","�赵��4","1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD, "kimdohyunlll@naver.com"),
				new User("dohyun5","�赵��5","1234", Level.GOLD, 100, Integer.MAX_VALUE, "kimdohyunlll@naver.com")
		);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		
		userDAO.deleteAll();
		for(User user : users) {
			userDAO.add(user);
		}
		
		MockMailSender mockMailSender = new MockMailSender();
		userService.setMailSender(mockMailSender);
		
		userService.upgradeLevels();
		
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		
		List<String> request = mockMailSender.getRequest();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(0).getEmail()));
		assertThat(request.get(1), is(users.get(1).getEmail()));
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDAO.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(),is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(),is(user.getLevel()));
		}
	}
	
	@Test
	public void add() {
		userDAO.deleteAll();
		User userWithLevel = users.get(4); //gold
		User userWithoutLevel = users.get(0); //BASIC - > NULL
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		
		User userWithLevelRead = userDAO.get(userWithLevel.getId());
		User userWithoutLevelRead = userDAO.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
		
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDAO(this.userDAO);
		testUserService.setTransactionManager(transactionManager);
		testUserService.setMailSender(mailSender);
		
		userDAO.deleteAll();
		for(User user : users) userDAO.add(user);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e) {
			
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	
	static class TestUserService extends UserService{
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		@Override
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
	}
	
	static class TestUserServiceException extends RuntimeException{
		
	}
	
	static class MockMailSender implements MailSender{
		private List<String> requests = new ArrayList<String>();
		
		
		public List<String> getRequest(){
			return requests;
		}
		@Override
		public void send(SimpleMailMessage mailMessage) throws MailException {
			// TODO Auto-generated method stub
			requests.add(mailMessage.getTo()[0]);
		}

		@Override
		public void send(SimpleMailMessage[] mailMessage) throws MailException {
			// TODO Auto-generated method stub
			
		}
		
	}
}
