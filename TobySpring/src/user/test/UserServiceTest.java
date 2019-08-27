package user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import user.dao.UserDAO;
import user.domain.Level;
import user.domain.User;
import user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  "/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("dohyun1","�赵��1","1234", Level.BASIC, 49, 0),
				new User("dohyun2","�赵��2","1234", Level.BASIC, 50, 0),
				new User("dohyun3","�赵��3","1234", Level.SILVER, 60, 29),
				new User("dohyun4","�赵��4","1234", Level.SILVER, 60, 30),
				new User("dohyun5","�赵��5","1234", Level.GOLD, 100, 100)
		);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() {
		
		userDAO.deleteAll();
		for(User user : users) {
			userDAO.add(user);
		}
		
		userService.upgradeLevels();
		
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}
	
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDAO.get(user.getId());
		assertThat(userUpdate.getLevel(),is(expectedLevel));
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
}