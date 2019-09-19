package user.sqlservice;

import java.io.InputStream;
import java.sql.SQLType;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import user.dao.UserDAO;
import user.sqlservice.jaxb.SqlType;
import user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public XmlSqlService() {
		String contextPath = Sqlmap.class.getPackage().getName();
		
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDAO.class.getResourceAsStream("sqlmap.xml");
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
			
		}catch(JAXBException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		// TODO Auto-generated method stub
		String sql = sqlMap.get(key);
		if(sql==null) throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		else return sql;
	}

}
