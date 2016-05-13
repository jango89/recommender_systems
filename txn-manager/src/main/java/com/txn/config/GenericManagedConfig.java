package com.txn.config;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.txn.datasource.DataSourceSupport.DataSourceType;
import com.txn.datasource.DatasourceFactory;
import com.txn.datasource.elasticsearch.ElasticSearchDataSource;
import com.txn.datasource.redis.RedisDataSource;


@EnableTransactionManagement
public abstract class GenericManagedConfig {
	
	private String projectKey;
	private String[] jpaPackagesToScan;
	
	private ElasticSearchDataSource elasticSearchDataSource;
	private RedisDataSource redisDataSource;
	
	/**
	 * @param projectKey
	 * @param jpaPackagesToScan
	 */
	public GenericManagedConfig(String projectKey, String[] jpaPackagesToScan) {
		this.projectKey = projectKey;
		this.jpaPackagesToScan = jpaPackagesToScan;
	}
	
	//Protected abstract Methods
	public abstract LocalContainerEntityManagerFactoryBean getEntityManagerFactory() throws Exception;
	
	public abstract PlatformTransactionManager getTransactionManager() throws Exception;
	
	public abstract MongoTemplate getMongoTemplate() throws Exception;
	
	public abstract ElasticsearchTemplate getElasticSearchTemplate() throws Exception;
	
	public abstract RedisTemplate<String, ?> getReddisTemplate() throws Exception;
	
	public abstract RedisTemplate<String, ?> getReddisTemplateForEntityStatus() throws Exception;
	
	public abstract Producer<String, ?> getKafkaProducer() throws Exception;
	
	/**
	 * @param env
	 * @return
	 * @throws Exception
	 */
	protected ElasticsearchTemplate getElasticsearchTemplateBean(Environment env) throws Exception{
		return getElasticSearchDataSource(env).getElasticsearchTemplate();
	}

	/**
	 * 4.Redis Template
	 * 
	 * @return
	 * @throws Exception
	 */
	protected RedisTemplate<String, ?> getRedisTemplate(Environment env) throws Exception {
		return getRedisDataSource(env).getRedisTemplate();
	}


	/**
	 * 4.Redis Template
	 * 
	 * @return
	 * @throws Exception
	 */
	protected RedisTemplate<String, ?> getRedisTemplateForEntityReadStatus(Environment env) throws Exception {
		return getRedisDataSourceForEntity(env).getRedisTemplate();
	}


	
	/**
	 * @param env
	 * @return
	 * @throws Exception
	 */
	private ElasticSearchDataSource getElasticSearchDataSource(Environment env) throws Exception {
		return null == elasticSearchDataSource ? (ElasticSearchDataSource) DatasourceFactory.getDataSource(DataSourceType.ELASTIC_SEARCH, projectKey, env, jpaPackagesToScan) : elasticSearchDataSource;
	}
	
	/**
	 * REDIS
	 * 
	 * @return the redisDataSource
	 * @throws Exception
	 */
	private RedisDataSource getRedisDataSource(Environment env) throws Exception {
		if (null == redisDataSource) {
			redisDataSource = (RedisDataSource) DatasourceFactory.getDataSource(DataSourceType.REDIS, projectKey, env);
		}
		return redisDataSource;
	}
	
	/**
	 * REDIS
	 * 
	 * @return the redisDataSource
	 * @throws Exception
	 */
	private RedisDataSource getRedisDataSourceForEntity(Environment env) throws Exception {
		if (null == redisDataSource) {
			redisDataSource = (RedisDataSource) DatasourceFactory.getDataSource(DataSourceType.REDISENTITYSTATUS, projectKey, env);
		}
		return redisDataSource;
	}
	
}//End of the class