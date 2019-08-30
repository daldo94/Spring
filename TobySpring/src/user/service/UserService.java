package user.service;

import user.domain.User;

public interface UserService {
	public void add(User user);
	public void upgradeLevels();
}
