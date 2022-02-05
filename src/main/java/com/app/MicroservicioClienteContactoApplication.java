package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.controller, com.model")
public class MicroservicioClienteContactoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioClienteContactoApplication.class, args);
	}

	@Bean
	public RestTemplate template(){
		return new RestTemplate();
	}

}
