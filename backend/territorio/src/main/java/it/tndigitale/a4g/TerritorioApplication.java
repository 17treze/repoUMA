package it.tndigitale.a4g;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g"})
@EnableScheduling
public class TerritorioApplication {
	public static void main(String[] args) {
        new SpringApplicationBuilder(TerritorioApplication.class)
	        .run(args);
	}
}
