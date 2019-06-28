package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import user.domain.User;

public class UserDAO {
		
		public void add(User user) throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.jdbc.Driver"); //DB Driver Load
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/spring_db","dohyun","1234");
			
			PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES(?,?,?)");
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			
			ps.executeUpdate();
			
			ps.close();
			c.close();
		}
		
		public User get(String id) throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.jdbc.Driver"); //DB Driver Load
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/spring_db","dohyun","1234");
			
			PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
			ps.setString(1, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next(); //rs가 처음에 null을 가르키기 때문에
			
			User user = new User();
			user.setId(rs.getString("ID"));
			user.setName(rs.getString("NAME"));
			user.setPassword(rs.getString("PASSWORD"));
			
			rs.close();
			ps.close();
			c.close();
			
			return user;
		}
}
