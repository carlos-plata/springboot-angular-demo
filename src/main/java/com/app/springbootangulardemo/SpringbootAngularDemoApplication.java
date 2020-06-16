package com.app.springbootangulardemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringbootAngularDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAngularDemoApplication.class, args);
	}

}
