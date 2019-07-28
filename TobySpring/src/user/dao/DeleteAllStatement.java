package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = c.prepareStatement("DELETE FROM USERS");
		return ps;
	}

}
