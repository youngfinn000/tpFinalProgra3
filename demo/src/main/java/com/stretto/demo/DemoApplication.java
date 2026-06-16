package com.stretto.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		Environment env = context.getEnvironment();
		String port = env.getProperty("server.port", "8080");

		System.out.println("""

				========================================
				 Stretto API levantada correctamente
				 Swagger UI: http://localhost:%s/swagger-ui.html
				========================================
				""".formatted(port));
	}
}
