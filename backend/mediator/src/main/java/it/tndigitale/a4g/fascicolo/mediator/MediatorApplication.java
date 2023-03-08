package it.tndigitale.a4g.fascicolo.mediator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"it.tndigitale.a4g"})
@EnableScheduling
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class MediatorApplication {
//	Questo microservizio non dispone di un layer db. 
//	Per escludere il layer db ed avviare il microservizio col custom framework si deve procedere in due passi:
//	1. annotare il SpringBootApplication con:
//			@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
//	2. utilizzare il classifier "no-db" della dipendenza a framework, a partire dalla versione 2.4.3:
//	<dependency>
//		<groupId>it.tndigitale.a4g</groupId>
//		<artifactId>framework</artifactId>
//		<version>${a4g.framework.version}</version>
//		<classifier>no-db</classifier>
//	</dependency>
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(MediatorApplication.class)
		.run(args);
	}
}
