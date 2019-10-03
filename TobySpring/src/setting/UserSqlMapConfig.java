package setting;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import user.dao.UserDAO;

public class UserSqlMapConfig implements SqlMapConfig {

	@Override
	public Resource getSqlMapResource() {
		// TODO Auto-generated method stub
		return new ClassPathResource("sqlmap.xml",UserDAO.class);
	}

}
