package user.main;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import user.dao.DAOFactory;
import user.dao.UserDAO;
import user.domain.User;

public class UserFunctionTestMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
        //Using Java Bean for DI
        //ApplicationContext context = new AnnotationConfigApplicationContext(DAOFactory.class);
		
		
		//Using XML for DI
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

		
		UserDAO dao = context.getBean("userDAO",UserDAO.class);
		
		User user = new User();
		user.setId("dohyun");
		user.setName("�赵��");
		user.setPassword("1234");
		
		dao.add(user);
		
		System.out.println(user.getId() + " ��� ����");
		
		
		User user2 = dao.get(user.getId());
		
		if(!user.getName().equals(user2.getName())) {
			System.out.println("�׽�Ʈ ���� (name)");
		}else if(!user.getPassword().equals(user2.getPassword())) {
			System.out.println("�׽�Ʈ ���� (password)");
		}else {
			System.out.println(user2.getId() + " ��ȸ ����");
		}
		
	}

}
