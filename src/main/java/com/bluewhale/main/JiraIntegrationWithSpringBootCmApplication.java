package com.bluewhale.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.bluewhale")
public class JiraIntegrationWithSpringBootCmApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiraIntegrationWithSpringBootCmApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
