package com.demo.demo20200625.spread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Demo20200625Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo20200625Application.class, args);
	}

}
