package com.template.render;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RednderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RednderApplication.class, args);
	}

}
