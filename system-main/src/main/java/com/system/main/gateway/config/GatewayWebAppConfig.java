package com.system.main.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.txn.datasource.DataSourceSupport.DataSourceType;
import com.txn.datasource.DatasourceFactory;
import com.txn.datasource.redis.RedisDataSource;

@Configuration
@EnableWebMvc
@EnableCaching
@ComponentScan(basePackages = {""})
@PropertySources({ @PropertySource("classpath:DATA-INF/GATEWAY/application.properties"), @PropertySource(value = "file:/usr/local/propspace/DATA-INF/GATEWAY/application.properties", ignoreResourceNotFound = true) })
public class GatewayWebAppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;
	private RedisDataSource redisDataSource; // Caching in spring
					 									// web-app can be done
														// only globally

	/**
	 * Global Cache Manager
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public CacheManager cacheManager() throws Exception {
		RedisCacheManager cacheManager = new RedisCacheManager(getRedisDataSource("gateway").getRedisTemplate());
		cacheManager.setTransactionAware(true);
		cacheManager.afterPropertiesSet();
		cacheManager.setUsePrefix(true);
		return cacheManager;
	}

	/**
	 * REDIS
	 * 
	 * @return the redisDataSource
	 * @throws Exception
	 */
	private RedisDataSource getRedisDataSource(String dsName) throws Exception {
		return (RedisDataSource) DatasourceFactory.getDataSource(DataSourceType.REDIS, dsName, env);
	}

}