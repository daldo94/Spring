package user.sqlservice;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {

	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	@Override
	public void registerSql(String key, String sql) {
		// TODO Auto-generated method stub
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		// TODO Auto-generated method stub
		String sql = sqlMap.get(key);
		if(sql==null) {
			throw new SqlNotFoundException(key + "�� �̿��ؼ� SQL�� ã�� �� �����ϴ�.");
		}else {
			return sql;	
		}
	}

}
