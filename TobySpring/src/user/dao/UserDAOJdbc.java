package user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import user.domain.Level;
import user.domain.User;
import user.sqlservice.SqlService;

@Component
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
		@Autowired
		private SqlService sqlService;
				
		
		@Autowired
		public void setDataSource(DataSource dataSource) {
			this.jdbcTemplate = new JdbcTemplate(dataSource);
		}
		
		//Can be omitted
		public void setSqlService(SqlService sqlService) {
			this.sqlService = sqlService;
		}

		@Override
		public void add(final User user) {
			// TODO Auto-generated method stub
			this.jdbcTemplate.update(this.sqlService.getSql("userAdd"), 
					user.getId(),user.getName(),user.getPassword(),user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		}
		
		@Override
		public User get(String id) {
			// TODO Auto-generated method stub
			return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), 
					new Object[] {id}, 
					this.userMapper);
		}
		
		@Override
		public List<User> getAll(){
			// TODO Auto-generated method stub
			return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), 
					this.userMapper);
		}
		
		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub
			this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class); 
		}

		@Override
		public void update(User user) {
			// TODO Auto-generated method stub
			this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
					user.getName(),user.getPassword(),user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
		}
		
}
