package com.system.main.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.system.core.config.SystemCoreConfig;
import com.system.main.gateway.config.GatewayWebAppConfig;

@SpringBootApplication(exclude = {MongoDataAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class,
		DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, ElasticsearchAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({ SystemCoreConfig.class, GatewayWebAppConfig.class})
public class GatewayInternational {

	public static void main(String[] args) {
		SpringApplication.run(GatewayInternational.class, args);
	}

}
