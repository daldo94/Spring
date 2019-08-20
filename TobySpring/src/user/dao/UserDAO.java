package user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import user.domain.User;

public class UserDAO {
		
		private JdbcTemplate jdbcTemplate;
		private DataSource dataSource;

		public void setDataSource(DataSource dataSource) {
			this.jdbcTemplate = new JdbcTemplate(dataSource);
			this.dataSource = dataSource;
		}

		public void add(final User user) throws SQLException{
			this.jdbcTemplate.update("INSERT INTO USERS(ID, NAME, PASSWORD) VALUES(?,?,?)", user.getId(),user.getName(),user.getPassword());
		}
		
		public User get(String id) throws SQLException{
			return this.jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE ID = ?", 
					new Object[] {id}, 
					new RowMapper<User>() {
						@Override
						public User mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							User user = new User();
							user.setId(rs.getString("ID"));
							user.setName(rs.getString("NAME"));
							user.setPassword(rs.getString("PASSWORD"));
							return user;
						}
					
			});
		}
		
		public List<User> getAll(){
			return this.jdbcTemplate.query("SELECT * FROM USERS ORDER BY ID", 
					new RowMapper<User>() {
						@Override
						public User mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							User user = new User();
							user.setId(rs.getString("ID"));
							user.setName(rs.getString("NAME"));
							user.setPassword(rs.getString("PASSWORD"));
							return user;
						}
			});
		}
		
		public void deleteAll() throws SQLException{
			this.jdbcTemplate.update("DELETE FROM USERS");
		}
		
		public int getCount() {
			return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USERS", Integer.class); 
		}
		
}
