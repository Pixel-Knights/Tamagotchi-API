package br.com.pixelknights.tamagochi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TamagochiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TamagochiApplication.class, args);
	}

}
