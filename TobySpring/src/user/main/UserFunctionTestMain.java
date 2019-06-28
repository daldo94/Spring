package user.main;

import java.sql.SQLException;

import user.dao.UserDAO;
import user.domain.User;

public class UserFunctionTestMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		UserDAO dao = new UserDAO();
		
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
	}

}
