package com.ansar.moneymanaer_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MoneymanagerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneymanagerApiApplication.class, args);
	}

}
