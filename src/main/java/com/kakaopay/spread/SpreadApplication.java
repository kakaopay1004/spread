package com.kakaopay.spread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreadApplication.class, args);
	}

}
