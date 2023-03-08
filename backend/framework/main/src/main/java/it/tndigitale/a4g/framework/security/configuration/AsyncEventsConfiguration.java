package it.tndigitale.a4g.framework.security.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
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
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

/**
 * Classe di configurazione per abilitare il contesto Asincrono con context di Spring nella gestione dei processi.
 * Il SecurityContext viene impostato ad un'utenza tecnica .
 */
@Configuration
@EnableAsync //abilitazione eventi asicroni. E' necessario decorare con @Async il listener
public class AsyncEventsConfiguration {
	
	@Value("${it.tndigit.event.scheduler.username:USER_SCHEDULER_EVENT}")
	private String utenzaTecnica;

	@Bean(name = DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	public Executor threadGestioneProcessi() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		List<GrantedAuthority> authorities = new ArrayList<>();
		Authentication authentication = new UsernamePasswordAuthenticationToken(utenzaTecnica, "",	authorities);
		context.setAuthentication(authentication);
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("GestioneEventiAsincroniThread-");
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor (executor, context);
	}
}
