package br.com.flaviohblima.rinhabackend3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;

@Validated
@SpringBootApplication
public class Rinhabackend3Application {

	public static void main(String[] args) {
		SpringApplication.run(Rinhabackend3Application.class, args);
	}

}
