package user.main;

import java.sql.SQLException;

import user.dao.ConnectionMaker;
import user.dao.DConnectionMaker;
import user.dao.UserDAO;
import user.domain.User;

public class UserFunctionTestMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		UserDAO dao = new UserDAO(connectionMaker);
		User user = new User();
		user.setId("dohyun");
		user.setName("�赵��");
		user.setPassword("1234");
		
		dao.add(user);
		
		System.out.println(user.getId() + "��� ����");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + "��ȸ ����");
	}

}
