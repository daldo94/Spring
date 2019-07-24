package user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import user.dao.UserDAO;
import user.domain.User;

public class UserDAOTest {

	@Test
	public void addAndGet() throws SQLException {
        //Using Java Bean for DI
        //ApplicationContext context = new AnnotationConfigApplicationContext(DAOFactory.class);
		
		
		//Using XML for DI
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

		
		UserDAO dao = context.getBean("userDAO",UserDAO.class);
		User user1 = new User("dohyun1","±èµµÇö","1234");
		User user2 = new User("dohyun2","±èµµÇö","1234");
		
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userGet1 = dao.get(user1.getId());
		assertThat(userGet1.getName(), is(user1.getName()));
		assertThat(userGet1.getPassword(), is(user1.getPassword()));
		
		User userGet2 = dao.get(user2.getId());
		assertThat(userGet2.getName(), is(user2.getName()));
		assertThat(userGet2.getPassword(), is(user2.getPassword()));
		
		
	}
	
	@Test
	public void count() throws SQLException{
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO",UserDAO.class);
		User user1 = new User("dohyun1","±èµµÇö","1234");
		User user2 = new User("dohyun2","±èµµÇö","1234");
		User user3 = new User("dohyun3","±èµµÇö","1234");
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(),is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(),is(3));
		
		
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDAO dao = context.getBean("userDAO",UserDAO.class);
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
	}

}
