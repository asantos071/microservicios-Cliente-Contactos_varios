package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.controller, com.model")
public class MicroservicioClienteContactoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioClienteContactoApplication.class, args);
	}

	@Bean
	public RestTemplate template() {
		BasicAuthenticationInterceptor intercep = new BasicAuthenticationInterceptor("admin", "admin");
        RestTemplate template = new RestTemplate();
		template.getInterceptors().add(intercep);
		return  template;
	}

}
