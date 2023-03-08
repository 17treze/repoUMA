package it.tndigitale.a4g.uma.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4g.uma.GeneralFactory;

@Configuration
public class ServiceLocatorsConfig {

	@SuppressWarnings("rawtypes")
	@Bean("generalFactory")
	public FactoryBean inizializzaGeneralFactory() {
		ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
		factoryBean.setServiceLocatorInterface(GeneralFactory.class);
		return factoryBean;
	}	
}
