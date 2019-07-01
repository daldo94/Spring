package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
        Class.forName("com.mysql.jdbc.Driver"); //DB Driver Load
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/spring_db","dohyun","1234");
        return c;
	}
	
}
