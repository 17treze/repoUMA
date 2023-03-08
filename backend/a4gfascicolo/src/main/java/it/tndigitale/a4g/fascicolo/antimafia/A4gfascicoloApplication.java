package it.tndigitale.a4g.fascicolo.antimafia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g"})
@EnableScheduling
public class A4gfascicoloApplication {

	public static void main(String[] args) {
		SpringApplication.run(A4gfascicoloApplication.class, args);
	}

}
