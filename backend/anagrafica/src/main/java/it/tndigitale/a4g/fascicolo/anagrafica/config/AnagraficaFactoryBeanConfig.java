package it.tndigitale.a4g.fascicolo.anagrafica.config;

import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateFactoryFactory;

@Configuration
public class AnagraficaFactoryBeanConfig {
	
    
    @Bean("emailTemplateFactory")
    public ServiceLocatorFactoryBean inizializzaEmailTemplateFactory() {
	    ServiceLocatorFactoryBean locator = new ServiceLocatorFactoryBean();
	    locator.setServiceLocatorInterface(EmailTemplateFactoryFactory.class);
        return locator;
    }

}
