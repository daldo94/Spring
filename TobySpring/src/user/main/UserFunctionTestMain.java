package user.main;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import user.dao.CountingConnectionMaker;
import user.dao.DaoFactory;
import user.dao.UserDAO;
import user.domain.User;

public class UserFunctionTestMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		//Using DI
		//ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//Using DL
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		UserDAO dao = context.getBean("userDAO",UserDAO.class);
		
		User user = new User();
		user.setId("dohyun");
		user.setName("김도현");
		user.setPassword("1234");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + "조회 성공");
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());
	}

}
