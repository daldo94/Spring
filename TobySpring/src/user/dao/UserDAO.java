package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import user.domain.User;

public class UserDAO {
		
		private JdbcContext jdbcContext;
		private DataSource dataSource;

		public void setDataSource(DataSource dataSource) {
			this.jdbcContext = new JdbcContext();
			jdbcContext.setDataSource(dataSource);
			this.dataSource = dataSource;
		}

		public void add(final User user) throws SQLException{
			this.jdbcContext.workWithStatementStrategy(
					new StatementStrategy() {				
						@Override
						public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
							// TODO Auto-generated method stub
							PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES(?,?,?)");
							ps.setString(1, user.getId());
							ps.setString(2, user.getName());
							ps.setString(3, user.getPassword());
							return ps;
						}
					});
			
		}
		
		public User get(String id) throws SQLException{
			Connection c = dataSource.getConnection();
			
			PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
			ps.setString(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			
			User user = null;
			if(rs.next()) {
				user = new User();
				user.setId(rs.getString("ID"));
				user.setName(rs.getString("NAME"));
				user.setPassword(rs.getString("PASSWORD"));
			}

			
			rs.close();
			ps.close();
			c.close();
			
			if(user==null) throw new EmptyResultDataAccessException(1);
			
			return user;
		}
		
		public void deleteAll() throws SQLException{
			this.jdbcContext.executeSql("DELETE FROM USERS");
		}
		
		public int getCount() throws SQLException{
			Connection c = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				c = dataSource.getConnection();
				ps = c.prepareStatement("SELECT COUNT(*) FROM USERS");
				
				rs = ps.executeQuery();
				rs.next();
				return rs.getInt(1);
			}catch(SQLException e){
				throw e;
			}finally {
				if(rs!=null) {
					try {
						rs.close();
					}catch(SQLException e) {
						throw e;
					}
				}
				if(ps!=null) {
					try {
						ps.close();
					}catch(SQLException e) {
						throw e;
					}
				}
				if(c!=null) {
					try {
						c.close();
					}catch(SQLException e) {
						throw e;
					}
				}
			}
		}
		
}
