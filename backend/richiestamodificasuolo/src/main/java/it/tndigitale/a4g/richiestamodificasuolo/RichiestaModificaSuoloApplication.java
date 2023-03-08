package it.tndigitale.a4g.richiestamodificasuolo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"it.tndigitale.a4g","it.tndigitale.a4g.richiestamodificasuolo"})
public class RichiestaModificaSuoloApplication {

	public static void main(String[] args) {
		SpringApplication.run(RichiestaModificaSuoloApplication.class, args);
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}
}
