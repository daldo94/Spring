package user.service;

import java.util.List;

import user.domain.User;

public interface UserService {
	public void add(User user);
	public User get(String id);
	public List<User> getAll();
	public void deleteAll();
	public void update(User user);
	
	public void upgradeLevels();
}
