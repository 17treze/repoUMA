package it.tndigitale.a4g.soc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g"})
public class SocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocApplication.class, args);
	}

}
