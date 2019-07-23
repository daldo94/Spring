package user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

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
		
		User user = new User();
		user.setId("dohyun");
		user.setName("±èµµÇö");
		user.setPassword("1234");
		
		dao.add(user);
		
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
		
		
	}

}
