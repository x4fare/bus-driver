package com.x4fare.busdriver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class BusDriverApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusDriverApplication.class, args);
	}

}
