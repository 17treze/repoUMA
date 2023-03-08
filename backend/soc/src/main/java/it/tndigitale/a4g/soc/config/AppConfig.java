package it.tndigitale.a4g.soc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:capitoli.properties"})
public class AppConfig {
}