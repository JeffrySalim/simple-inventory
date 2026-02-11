package com.example.simple_inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SimpleInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleInventoryApplication.class, args);
	}


}
