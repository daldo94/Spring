package user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import setting.TestApplicationContext;
import user.dao.UserDAO;
import user.domain.Level;
import user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "/applicationContext.xml")
@ContextConfiguration(classes=TestApplicationContext.class)
public class UserDAOTest {
	//@Autowired
	//private ApplicationContext context;
	@Autowired
	private UserDAO userDAO;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {	
		//this.userDAO = context.getBean("useruserDAO",UseruserDAO.class);
		
		this.user1 = new User("dohyun1","±èµµÇö1","1234", Level.BASIC, 1, 0, "kimdohyunlll@naver.com");
		this.user2 = new User("dohyun2","±èµµÇö2","1234", Level.SILVER, 55, 10, "kimdohyunlll@naver.com");
		this.user3 = new User("dohyun3","±èµµÇö3","1234", Level.GOLD, 100, 40, "kimdohyunlll@naver.com");
	}

	@Test
	public void addAndGet() throws SQLException {

		userDAO.deleteAll();
		assertThat(userDAO.getCount(), is(0));
		
		
		userDAO.add(user1);
		userDAO.add(user2);
		assertThat(userDAO.getCount(), is(2));
		
		User userGet1 = userDAO.get(user1.getId());
		checkSameUser(userGet1, user1);
		
		User userGet2 = userDAO.get(user2.getId());
		checkSameUser(userGet2, user2);
		
	}
	
	@Test
	public void count() throws SQLException{
		
		userDAO.deleteAll();
		assertThat(userDAO.getCount(),is(0));
		
		userDAO.add(user1);
		assertThat(userDAO.getCount(),is(1));
		
		userDAO.add(user2);
		assertThat(userDAO.getCount(),is(2));
		
		userDAO.add(user3);
		assertThat(userDAO.getCount(),is(3));
		
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException{

		userDAO.deleteAll();
		assertThat(userDAO.getCount(), is(0));

		userDAO.get("unknown_id");
		
	}
	
	@Test
	public void getAll() throws SQLException{
		userDAO.deleteAll();
		
		List<User> users0 = userDAO.getAll();
		assertThat(users0.size(), is(0));
		
		userDAO.add(user1);
		List<User> users1 = userDAO.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		
		userDAO.add(user2);
		List<User> users2 = userDAO.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		userDAO.add(user3);
		List<User> users3 = userDAO.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
		
		
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
	
	@Test(expected = DataAccessException.class)
	public void duplicateKey() {
		
		userDAO.deleteAll();
		
		userDAO.add(user1);
		userDAO.add(user1);
	}
	
	@Test
	public void update() {
		userDAO.deleteAll();
		
		userDAO.add(user1);
		userDAO.add(user2);
		
		user1.setName("±èµµÇö4");
		user1.setPassword("1234");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		userDAO.update(user1);
		
		User user1Update = userDAO.get(user1.getId());
		checkSameUser(user1, user1Update);
		
		User user2Same = userDAO.get(user2.getId());
		checkSameUser(user2, user2Same);
	}

}
