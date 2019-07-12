package user.dao;

public class DaoFactory {
	public UserDAO userDAO() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDAO userDAO = new UserDAO(connectionMaker);
		return userDAO;
	}
}
