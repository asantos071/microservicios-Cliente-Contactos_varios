package com.app;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "com.controller, com.model, com.service")
public class MicroservicioClienteContactoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioClienteContactoApplication.class, args);
	}

	@Bean
	public RestTemplate template(){
		return new RestTemplate();
	}

	@Bean
	public Executor executor(){
	  return new ThreadPoolTaskExecutor();
	}

}
