package it.tndigitale.a4gistruttoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4gistruttoria","it.tndigitale.a4g"})
@EnableRetry
@EnableScheduling
public class A4gistruttoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(A4gistruttoriaApplication.class, args);
	}
}
