package it.tndigitale.a4g.ags;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"it.tndigitale.a4g"})
@SpringBootApplication
public class AgsApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgsApplication.class, args);
	}
}
