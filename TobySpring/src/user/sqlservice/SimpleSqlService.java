package user.sqlservice;

import java.util.Map;

public class SimpleSqlService implements SqlService {
	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		// TODO Auto-generated method stub
		String sql = sqlMap.get(key);
		if(sql==null) {
			throw new SqlRetrievalFailureException(key + "에 대한 sql을 찾을 수 없습니다.");
		}else {
			return sql;
		}
	}
	
}
