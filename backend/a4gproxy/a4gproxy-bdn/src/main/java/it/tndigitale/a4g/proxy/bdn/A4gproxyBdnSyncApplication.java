package it.tndigitale.a4g.proxy.bdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"it.tndigitale.a4g.framework" , "it.tndigitale.a4g.proxy.bdn"})
public class A4gproxyBdnSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(A4gproxyBdnSyncApplication.class, args);
	}

}
