package se.magnus.microservices.core.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("se.magnus")
public class ReivewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReivewServiceApplication.class, args);
	}

}
