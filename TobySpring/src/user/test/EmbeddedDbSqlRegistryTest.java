package user.test;

import static org.junit.Assert.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import user.sqlservice.updatable.SqlUpdateFailureException;
import user.sqlservice.updatable.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{

	EmbeddedDatabase db;
	
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		// TODO Auto-generated method stub
		db = new EmbeddedDatabaseBuilder()
				.setType(HSQL).addScript("classpath:user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
		
		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(db);
		
		return embeddedDbSqlRegistry;
	}
	
	@After
	public void tearDown() {
		db.shutdown();
	}
	
	@Test
	public void transactionalUpdate() {
		checkFindResult("SQL1", "SQL2", "SQL3");
		
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1","Modified1");
		sqlmap.put("KEY9999!@#$","Modified9999");
		
		try {
			sqlRegistry.updateSql(sqlmap);
			fail();
		}catch(SqlUpdateFailureException e) {
			
		}
		
		checkFindResult("SQL1", "SQL2", "SQL3");
	}

}
