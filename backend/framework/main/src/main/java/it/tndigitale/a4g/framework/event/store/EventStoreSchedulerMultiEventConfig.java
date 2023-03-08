package it.tndigitale.a4g.framework.event.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class EventStoreSchedulerMultiEventConfig {

    @Bean(name = "eventStoreSchedulerMultiEventPool")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        var threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
          "eventStoreSchedulerMultiEventPool");
        
        return threadPoolTaskScheduler;
    }
}