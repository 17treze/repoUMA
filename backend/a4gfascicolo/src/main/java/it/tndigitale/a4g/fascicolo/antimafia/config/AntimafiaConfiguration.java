package it.tndigitale.a4g.fascicolo.antimafia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Classe di configurazione per abilitare il contesto Asyncrono di Spring.
 */
@EnableAsync
@Configuration
public class AntimafiaConfiguration {
	
	/**
	 * Specific task executor.
	 *
	 * @return the task executor
	 */
	@Bean(name = "threadMemorizzaAllegatoFamiliariConviventi")
    public TaskExecutor threadMemorizzaAllegatoFamiliariConviventi() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        
        return executor;
    }
}
