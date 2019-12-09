package com.PSOOP.PROGETTO.SCHIAVONI.OOP;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.service.ServiceAzAgr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);
	}

}
