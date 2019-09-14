package user.sqlservice;

public interface SqlService {
	public String getSql(String key) throws SqlRetrievalFailureException;
}
