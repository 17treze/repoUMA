package it.tndigitale.a4g.srt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g"})
public class SrtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrtApplication.class, args);
	}
	
}
