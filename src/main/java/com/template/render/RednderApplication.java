package com.template.render;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.template.render.repository")
public class RednderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RednderApplication.class, args);
	}

}
