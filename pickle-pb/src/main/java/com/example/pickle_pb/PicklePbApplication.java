package com.example.pickle_pb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.example.pickle_pb", "com.example.real_common"})
@EnableJpaAuditing
public class PicklePbApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicklePbApplication.class, args);
	}

}
