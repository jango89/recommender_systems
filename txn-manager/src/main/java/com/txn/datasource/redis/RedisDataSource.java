/**
 * @File: PropspaceRedisDataSource.java
 */
package com.txn.datasource.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author shahinkonadath
 */
public class RedisDataSource {
	
	private Environment env;
	private RedisTemplate<String, ?> redisTemplate;
	private String dsName;
	
	private static final Logger propLogger = LoggerFactory.getLogger(RedisDataSource.class);
	
	/**
	 * @param env
	 */
	public RedisDataSource(Environment env, String dsName) {
		super();
		this.env = env;
		this.dsName = dsName;
		this.redisTemplate = createRedisTemplate();
	}
	
	public RedisDataSource(Environment env, String dsName, Boolean isEntityStatus) {
		super();
		this.env = env;
		this.dsName = dsName;
		if (!isEntityStatus)
			this.redisTemplate = createRedisTemplate();
		else
			this.redisTemplate = createRedisTemplateForEntity();
	}
	
	/**
	 * @return
	 */
	private RedisTemplate<String, ?> createRedisTemplate() {
		redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(getRedisConnectionFactory());
		redisTemplate.afterPropertiesSet();
		String serializer = env.getProperty(dsName + ".redis.stringKeySerializer");
		if (serializer != null && serializer.equals("true")) {
			redisTemplate.setKeySerializer(new StringRedisSerializer());
		}
		return redisTemplate;
	}
	
	/**
	 * @return
	 */
	private RedisTemplate<String, ?> createRedisTemplateForEntity() {
		redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(getRedisConnectionFactory());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
		
		return redisTemplate;
	}
	
	/**
	 * @return
	 */
	private RedisConnectionFactory getRedisConnectionFactory() {
		
		JedisPoolConfig poolConfig = new JedisPoolConfig(); // Leaving to default settings as of now
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(poolConfig);
		redisConnectionFactory.setHostName(env.getProperty(dsName + ".redis.host"));
		redisConnectionFactory.setPort(Integer.valueOf(env.getProperty(dsName + ".redis.port")));
		redisConnectionFactory.setPassword("");
		redisConnectionFactory.afterPropertiesSet();
		propLogger.info("JEDIS CONNECTION: {}, host: {}, port: {}", redisConnectionFactory.getConnection(), env.getProperty(dsName + ".redis.host"), env.getProperty(dsName + ".redis.port"));
		return redisConnectionFactory;
		
	}
	
	/**
	 * @return the redisTemplate
	 */
	public RedisTemplate<String, ?> getRedisTemplate() {
		return redisTemplate;
	}
	
}// End of the class