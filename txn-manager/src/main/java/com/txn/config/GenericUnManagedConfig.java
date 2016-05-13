package com.txn.config;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.txn.datasource.DataSourceSupport.DataSourceType;
import com.txn.datasource.DatasourceFactory;
import com.txn.datasource.elasticsearch.ElasticSearchDataSource;
import com.txn.datasource.redis.RedisDataSource;
import com.txn.datasource.redis.RedisExtended;

public abstract class GenericUnManagedConfig {
	
	static final Logger LOGGER = LoggerFactory.getLogger(GenericUnManagedConfig.class);

	private String projectKey;
	private String[] jpaPackagesToScan;
	
	private ElasticSearchDataSource elasticSearchDataSource;
	private RedisDataSource redisDataSource;
	
	public GenericUnManagedConfig(String projectKey, String[] jpaPackagesToScan) {
		this.projectKey = projectKey;
		this.jpaPackagesToScan = jpaPackagesToScan;
	}
	
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
	protected ElasticsearchTemplate getElasticsearchTemplateBean(Environment env) throws Exception {
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
	
    @SuppressWarnings({"unchecked","rawtypes"})
	protected RedisExtended<String, ?> createRedisExtendedTemplate(RedisTemplate<String,?> template){
        RedisExtended<String, ?> extended = new RedisExtended();
        extended.setConnectionFactory(template.getConnectionFactory());
        extended.setDefaultSerializer(template.getDefaultSerializer());
        extended.setEnableDefaultSerializer(template.isEnableDefaultSerializer());
        extended.setExposeConnection(template.isExposeConnection());
        extended.setHashKeySerializer(template.getHashKeySerializer());
        extended.setHashValueSerializer(template.getHashValueSerializer());
        extended.setKeySerializer(template.getKeySerializer());
        extended.setStringSerializer(template.getStringSerializer());
        return extended;
	}
	
}// End of the class
