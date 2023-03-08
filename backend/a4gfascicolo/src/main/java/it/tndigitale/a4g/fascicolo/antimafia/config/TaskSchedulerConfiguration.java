package it.tndigitale.a4g.fascicolo.antimafia.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.scheduling.DelegatingSecurityContextSchedulingTaskExecutor;

/**
 * @author ITE3279
 * Classe di configurazione per abilitare il contesto asincrono (@async) di Spring nella gestione dei processi legati ad uno scheduler.
 * Include la gestione della sicurezza attraverso un utente di servizio (detto anche utenza tecnica)
 */
@Configuration
@EnableAsync
public class TaskSchedulerConfiguration {
	
	@Value("${verificaperiodica.utenzatecnica.username}")
	private String utenzaTecnica;

	@Bean("threadGestioneProcessiScheduler")
	public Executor threadGestioneProcessi() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		List<GrantedAuthority> authorities = new ArrayList<>();
		Authentication authentication = new UsernamePasswordAuthenticationToken(utenzaTecnica, "",	authorities);
		context.setAuthentication(authentication);
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("ThreadGestioneProcessiScheduler-");
		executor.initialize();
		return new DelegatingSecurityContextSchedulingTaskExecutor(executor, context);
	}

}
