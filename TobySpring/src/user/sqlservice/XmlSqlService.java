package user.sqlservice;

import java.io.InputStream;
import java.sql.SQLType;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import user.dao.UserDAO;
import user.sqlservice.jaxb.SqlType;
import user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	private String sqlmapFile;
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		// TODO Auto-generated method stub
		try {
			return this.sqlRegistry.findSql(key);
		}catch (SqlNotFoundException e) {
			// TODO: handle exception
			throw new SqlRetrievalFailureException(e);
		}
	}
	@Override
	public void registerSql(String key, String sql) {
		// TODO Auto-generated method stub
		sqlMap.put(key, sql);
		
	}
	@Override
	public String findSql(String key) throws SqlNotFoundException {
		// TODO Auto-generated method stub
		String sql = sqlMap.get(key);
		if(sql==null) throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		else return sql;
	}
	@Override
	public void read(SqlRegistry sqlRegistry) {
		// TODO Auto-generated method stub
		String contextPath = Sqlmap.class.getPackage().getName();
		
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDAO.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
			
		}catch(JAXBException e) {
			throw new RuntimeException(e);
		}
		
	}

}
