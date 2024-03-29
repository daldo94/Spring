package learningtest.spring.embeddeddb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

public class EmbeddedDbTest {
	EmbeddedDatabase db;
	JdbcTemplate template;

	@Before
	public void setUp() {
		db = new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:/learningtest/spring/embeddeddb/schema.sql")
				.addScript("classpath:/learningtest/spring/embeddeddb/data.sql")
				.build();
		
		template = new JdbcTemplate(db);
	}
	
	@After
	public void tearDown() {
		db.shutdown();
	}
	
	@Test
	public void initData() {
		assertThat(template.queryForObject("SELECT COUNT(*) FROM SQLMAP", Integer.class), is(2));
		
		List<Map<String, Object>> list = template.queryForList("SELECT * FROM SQLMAP ORDER BY KEY_");
		assertThat((String)list.get(0).get("KEY_"), is("KEY1"));
		assertThat((String)list.get(0).get("SQL_"), is("SQL1"));
		assertThat((String)list.get(1).get("KEY_"), is("KEY2"));
		assertThat((String)list.get(1).get("SQL_"), is("SQL2"));
	}
	
	@Test
	public void insert() {
		template.update("INSERT INTO SQLMAP(KEY_, SQL_) VALUES(?,?)", "KEY3", "SQL3");
		assertThat(template.queryForObject("SELECT COUNT(*) FROM SQLMAP", Integer.class), is(3));
	}
}
