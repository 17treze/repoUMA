package it.tndigitale.a4g.zootecnia;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g"})
@EnableScheduling
public class ZootecniaApplication {
	public static void main(String[] args) {
        new SpringApplicationBuilder(ZootecniaApplication.class)
	        .run(args);
	}
}
