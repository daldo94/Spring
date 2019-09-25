package user.sqlservice;

public interface SqlRegistry {
	public void registerSql(String key, String sql);
	public String findSql(String key) throws SqlNotFoundException;
}
