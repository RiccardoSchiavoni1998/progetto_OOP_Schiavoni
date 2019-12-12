package com.PSOOP.PROGETTO.SCHIAVONI.OOP;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.service.ServiceAzAgr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
/**
 * @author Riccardo Schiavoni
 *
 */
@SpringBootApplication
public class Application {
	/**

	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);
	}

}
