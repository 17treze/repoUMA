package it.tndigitale.a4g.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g.proxy", "it.tndigitale.a4g.framework", "it.tndigit.iotrentino"})
@EnableAsync
public class ProxyApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
}
