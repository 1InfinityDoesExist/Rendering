package com.template.render;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
@EnableMongoRepositories(basePackages = { "com.template.render.repository.mongo" })
@EnableElasticsearchRepositories(basePackages = "com.template.render.repository.es")
public class RednderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RednderApplication.class, args);
	}

}
