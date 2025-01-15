package com.jdc.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.jdc.spring.model.BaseRepositoryImpl;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
@PropertySource(value = "/jwt-token.properties")
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class AnnouncementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnouncementApiApplication.class, args);
	}

}
