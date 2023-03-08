package it.tndigitale.a4g.proxy.ags.bdn;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@EnableAsync
public class ProxyAgsBdnApplication {
	private static Logger logger = LoggerFactory.getLogger(ProxyAgsBdnApplication.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ProxyAgsBdnApplication.class, args);
		logger.debug("started");
	}
	
}
