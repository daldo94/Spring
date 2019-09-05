package user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import user.dao.UserDAO;
import user.domain.Level;
import user.domain.User;
import user.service.TransactionHandler;
import user.service.TxProxyFactoryBean;
import user.service.UserService;
import user.service.UserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  "/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	ApplicationContext context;
	@Autowired
	private UserService userService;
	//@Autowired
	//private UserServiceImpl userServiceImpl;
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
				new User("dohyun1","±èµµÇö1","1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "kimdohyunlll@naver.com"),
				new User("dohyun2","±èµµÇö2","1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "kimdohyunlll@naver.com"),
				new User("dohyun3","±èµµÇö3","1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1, "kimdohyunlll@naver.com"),
				new User("dohyun4","±èµµÇö4","1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD, "kimdohyunlll@naver.com"),
				new User("dohyun5","±èµµÇö5","1234", Level.GOLD, 100, Integer.MAX_VALUE, "kimdohyunlll@naver.com")
		);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDAO mockUserDAO = new MockUserDAO(this.users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDAO.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "dohyun2", Level.SILVER);
		checkUserAndLevel(updated.get(1), "dohyun4", Level.GOLD);

		
		List<String> request = mockMailSender.getRequest();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(0).getEmail()));
		assertThat(request.get(1), is(users.get(1).getEmail()));
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDAO mockUserDAO = mock(UserDAO.class);
		when(mockUserDAO.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDAO, times(2)).update(any(User.class));
		verify(mockUserDAO).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(is(Level.SILVER)));
		verify(mockUserDAO).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(is(Level.GOLD)));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {		
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDAO(this.userDAO);
		testUserService.setMailSender(mailSender);
		
		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService) txProxyFactoryBean.getObject();
		
		userDAO.deleteAll();
		for(User user : users) userDAO.add(user);
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e) {
			
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	
	static class TestUserService extends UserServiceImpl{
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
	
	static class MockUserDAO implements UserDAO{
		private List<User> users;
		private List<User> updated = new ArrayList();
		
		private MockUserDAO(List<User> users) {
			// TODO Auto-generated constructor stub
			this.users = users;
		}
		
		public List<User> getUpdated(){
			return this.updated;
		}
		
		@Override
		public List<User> getAll() {
			// TODO Auto-generated method stub
			return this.users;
		}
		
		@Override
		public void update(User user) {
			// TODO Auto-generated method stub
			updated.add(user);
		}
		
		@Override
		public void add(User user) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
			
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}


		
		
	}
}
