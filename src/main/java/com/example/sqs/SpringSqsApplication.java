package com.example.sqs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringSqsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringSqsApplication.class, args);
	}
}
