package it.tndigitale.a4gistruttoria.config;

import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4gistruttoria.service.businesslogic.avvio.InizializzaDatiIstruttoreFactory;
import it.tndigitale.a4gistruttoria.service.businesslogic.avvio.VerificatoreIstruttoriaFactory;

@Configuration
public class IstruttoriaFactoryBeanConfig {
	
    
    @Bean("verificatoreIstruttoriaFactory")
    public ServiceLocatorFactoryBean verificatoreIstruttoriaFactory() {
	    ServiceLocatorFactoryBean locator = new ServiceLocatorFactoryBean();
	    locator.setServiceLocatorInterface(VerificatoreIstruttoriaFactory.class);
        return locator;
    }
    
    @Bean("inizializzaDatiIstruttoreFactory")
    public ServiceLocatorFactoryBean inizializzaDatiIstruttoreFactory() {
	    ServiceLocatorFactoryBean locator = new ServiceLocatorFactoryBean();
	    locator.setServiceLocatorInterface(InizializzaDatiIstruttoreFactory.class);
        return locator;
    }

}
