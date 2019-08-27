package user.dao;

import java.util.List;

import user.domain.User;

public interface UserDAO {
	public void add(User user);
	public User get(String id);
	public List<User> getAll();
	public void deleteAll();
	public int getCount();
	public void update(User user);
}
