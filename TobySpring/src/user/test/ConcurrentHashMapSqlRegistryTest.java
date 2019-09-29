package user.test;

import user.sqlservice.updatable.ConcurrentHashMapSqlRegistry;
import user.sqlservice.updatable.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		// TODO Auto-generated method stub
		return new ConcurrentHashMapSqlRegistry();
	}

}
