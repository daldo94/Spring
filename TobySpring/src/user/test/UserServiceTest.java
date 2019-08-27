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
import static user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

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
				new User("dohyun1","±èµµÇö1","1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("dohyun2","±èµµÇö2","1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("dohyun3","±èµµÇö3","1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
				new User("dohyun4","±èµµÇö4","1234", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("dohyun5","±èµµÇö5","1234", Level.GOLD, 100, Integer.MAX_VALUE)
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
		
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
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
}
