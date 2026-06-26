package com.hypertrophy.hypertrophy_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HypertrophyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HypertrophyApiApplication.class, args);
	}

}
