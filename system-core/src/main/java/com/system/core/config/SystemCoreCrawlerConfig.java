package com.system.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.txn.datasource.DataSourceSupport.DataSourceType;
import com.txn.datasource.DatasourceFactory;
import com.txn.datasource.elasticsearch.ElasticSearchDataSource;

@Configuration
@PropertySources({ @PropertySource("classpath:DATA-INF/PS-CORE/datasource.properties") })
@ComponentScan(basePackages = { "com.system.core.controller", "com.system.core.entity", "com.system.core.repository",
		"com.system.core.crawler.support", "com.system.core.service" })
@EnableElasticsearchRepositories(basePackages = {
		"com.system.core.repository" }, elasticsearchTemplateRef = "coreElasticSearchTemplate")
public class SystemCoreCrawlerConfig {

	@Autowired
	private Environment env;

	@Bean(name = "coreElasticSearchTemplate")
	public ElasticsearchTemplate getElasticSearchTemplate() throws Exception {
		return getElasticsearchTemplateBean(env, "core");
	}

	private ElasticsearchTemplate getElasticsearchTemplateBean(Environment environment, String dsName)
			throws Exception {
		return (ElasticsearchTemplate) ((ElasticSearchDataSource) DatasourceFactory
				.getDataSource(DataSourceType.ELASTIC_SEARCH, dsName, env)).getElasticsearchTemplate();
	}

}// End of the class