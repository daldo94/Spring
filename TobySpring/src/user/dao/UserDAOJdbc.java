package user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import user.domain.Level;
import user.domain.User;

public class UserDAOJdbc implements UserDAO {
		
		private JdbcTemplate jdbcTemplate;
		private RowMapper<User> userMapper = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setId(rs.getString("ID"));
				user.setName(rs.getString("NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setLevel(Level.valueOf(rs.getInt("LEVEL")));
				user.setLogin(rs.getInt("LOGIN"));
				user.setRecommend(rs.getInt("RECOMMEND"));
				user.setEmail(rs.getString("EMAIL"));
				return user;
			}
		};
		private Map<String, String> sqlMap;
				

		public void setDataSource(DataSource dataSource) {
			this.jdbcTemplate = new JdbcTemplate(dataSource);
		}
		
		public void setSqlMap(Map<String,String> sqlMap) {
			this.sqlMap = sqlMap;
		}

		@Override
		public void add(final User user) {
			// TODO Auto-generated method stub
			this.jdbcTemplate.update(this.sqlMap.get("add"), 
					user.getId(),user.getName(),user.getPassword(),user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		}
		
		@Override
		public User get(String id) {
			// TODO Auto-generated method stub
			return this.jdbcTemplate.queryForObject(this.sqlMap.get("get"), 
					new Object[] {id}, 
					this.userMapper);
		}
		
		@Override
		public List<User> getAll(){
			// TODO Auto-generated method stub
			return this.jdbcTemplate.query(this.sqlMap.get("getAll"), 
					this.userMapper);
		}
		
		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub
			this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.jdbcTemplate.queryForObject(this.sqlMap.get("getCount"), Integer.class); 
		}

		@Override
		public void update(User user) {
			// TODO Auto-generated method stub
			this.jdbcTemplate.update(this.sqlMap.get("update"),
					user.getName(),user.getPassword(),user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
		}
		
}
