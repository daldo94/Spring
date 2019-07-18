package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import user.domain.User;

public class UserDAO {
		
		private DataSource dataSource;

		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public void add(User user) throws SQLException{
			Connection c = dataSource.getConnection();
			
			PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES(?,?,?)");
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			
			ps.executeUpdate();
			
			ps.close();
			c.close();
		}
		
		public User get(String id) throws SQLException{
			Connection c = dataSource.getConnection();
			
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
