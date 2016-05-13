/**
 * @File: PropspaceDatasourceFactory.java
 */
package com.txn.datasource;

import org.springframework.core.env.Environment;

import com.txn.datasource.DataSourceSupport.DataSourceType;
import com.txn.datasource.elasticsearch.ElasticSearchDataSource;
import com.txn.datasource.redis.RedisDataSource;

/**
 * The factory class will generate different datasources based on the parameters
 */
public class DatasourceFactory {

	/**
	 * Factory Method
	 * 
	 * @param dsType
	 * @param dsName
	 * @param env
	 * @param packagesToScan
	 * @return
	 * @throws Exception
	 */
	public static Object getDataSource(DataSourceType dsType, String dsName, Environment env, String... packagesToScan) throws Exception {

		switch (dsType) {

			case ELASTIC_SEARCH:
				return getElasticSearchDataSource(dsName, env);
			case REDIS:
				return getRedisDataSource(dsName, env);
			case REDISENTITYSTATUS:
				return getRedisDataSourceForEntity(dsName, env);
			case HBASE:
				return getHbaseDataSource(dsName, env);
		

			default:
				throw new Exception("Couldn't identify the datasource of type: " + dsType.name());

		}

	}

	/**
	 * HBASE
	 * 
	 * @param dsName
	 */
	private static Object getHbaseDataSource(String dsName, Environment env) {
		// TODO Needed Implementation
		return null;
	}

	/**
	 * REDIS
	 * 
	 * @param dsName
	 */
	private static RedisDataSource getRedisDataSource(String dsName, Environment env) {
		return new RedisDataSource(env, dsName, false);
	}

	/**
	 * REDIS for entity read status
	 * 
	 * @param dsName
	 */
	private static RedisDataSource getRedisDataSourceForEntity(String dsName, Environment env) {
		return new RedisDataSource(env, dsName, true);
	}

	/**
	 * ELASTIC SEARCH
	 * 
	 * @param dsName
	 */
	private static ElasticSearchDataSource getElasticSearchDataSource(String dsName, Environment env) {
		return new ElasticSearchDataSource(env, dsName);
	}


}// End of the class