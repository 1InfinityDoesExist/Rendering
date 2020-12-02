package com.template.render;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
//@EnableMongoRepositories(basePackages = { "com.template.render.repository.mongo" })
//@EnableElasticsearchRepositories(basePackages = "com.template.render.repository.es")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class })
public class RednderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RednderApplication.class, args);
	}

}
