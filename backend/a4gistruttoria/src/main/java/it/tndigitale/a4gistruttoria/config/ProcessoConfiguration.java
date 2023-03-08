package it.tndigitale.a4gistruttoria.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

/**
 * Classe di configurazione per abilitare il contesto Asyncrono di Spring nella gestione dei processi.
 */
@Configuration
@EnableAsync
public class ProcessoConfiguration {

	@Bean("threadGestioneProcessi")
	public Executor threadGestioneProcessi() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("ProcessoThread-");
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor (executor);
	}

}
