package user.dao;

import java.util.List;

import user.domain.User;

public interface UserDAO {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
}
