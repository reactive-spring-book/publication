package com.example.production;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(PublicationProperties.class)
public class PublicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicationApplication.class, args);
	}

	@Bean
	InitializingBean logRequiredProperties(PublicationProperties properties) {
		return () -> log.info(properties.toString());
	}

}
