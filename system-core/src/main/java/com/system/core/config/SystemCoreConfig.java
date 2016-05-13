package com.system.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages = {""})
@PropertySources({ @PropertySource("classpath:DATA-INF/PS-CORE/datasource.properties"), @PropertySource(value = "file:/usr/local/propspace/DATA-INF/PS-CORE/datasource.properties", ignoreResourceNotFound = true) })
@EnableElasticsearchRepositories(basePackages = { "com.propspace.intl.core.repository.nosql", "com.propspace.intl.core.calendar.repository.nosql" }, elasticsearchTemplateRef = "coreElasticSearchTemplate")
@EnableCaching
public class SystemCoreConfig {

	@Autowired
	private Environment env;

//	@Override
//	@Bean(name = PsCoreConfigSupport.PS_CORE_ELASTIC_SEARCH_TEMPLATE)
//	public ElasticsearchTemplate getElasticSearchTemplate() throws Exception {
//		return getElasticsearchTemplateBean(env);
//	}
//
//	@Override
//	@Bean(name = PsCoreConfigSupport.PS_CORE_REDIS_TEMPLATE, destroyMethod="destroyAndCloseConnection")
//	public RedisExtended<String, ?> getReddisTemplateForEntityStatus() throws Exception {
//        RedisTemplate<String, ?> template = getRedisTemplateForEntityReadStatus(env);
//        return this.createRedisExtendedTemplate(template);
//	}

}// End of the class