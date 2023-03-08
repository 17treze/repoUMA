package it.tndigitale.a4g.richiestamodificasuolo.config;

import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneFactory;

@Configuration
public class IstruttoriaFactoryBeanConfig {
	
    
    @Bean("azioneLavorazioneFactory")
    public ServiceLocatorFactoryBean azioneLavorazioneFactory() {
	    ServiceLocatorFactoryBean locator = new ServiceLocatorFactoryBean();
	    locator.setServiceLocatorInterface(AzioneLavorazioneFactory.class);
        return locator;
    }

}
