package com.example.pickle_customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.example.pickle_customer", "com.example.real_common"})
@EnableJpaAuditing
public class PickleCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickleCustomerApplication.class, args);
	}

}
