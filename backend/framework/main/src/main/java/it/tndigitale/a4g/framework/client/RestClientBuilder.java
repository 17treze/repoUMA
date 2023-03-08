package it.tndigitale.a4g.framework.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Component
public class RestClientBuilder {
	protected ClientServiceBuilder restTemplateBuilder;
	
	protected UtenteComponent utenteCorrente;
	
	
	private final static Logger log = LoggerFactory.getLogger(RestClientBuilder.class);
	
	public <T extends RestApiClient<T>> BuilderClientService<T> from(Class<T> clazzClient) {
		return new BuilderClientService<T>(clazzClient);
	}
	
	@Autowired
    public RestClientBuilder setRestTemplateBuilder(ClientServiceBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        return this;
    }
	
	@Autowired
    public RestClientBuilder setRestTemplateBuilder(UtenteComponent utenteCorrente) {
        this.utenteCorrente = utenteCorrente;
        return this;
    }
	
	/**
	 * Builder del client rest che deve implentare {@link RestApiClient}
	 * @author Lorenzo Martinelli
	 *
	 * @param <T> il tipo di client da implementare
	 */
	public class BuilderClientService<T extends RestApiClient<T>> {
		protected Class<T> newClass;
        protected String basePath;

        BuilderClientService(Class<T> newClass) {
            this.newClass = newClass;
        }

        /**
         * Definisce il context da richiamare
         * @param basePath
         * @return
         */
        public BuilderClientService<T> setBasePath(String basePath) {
            this.basePath = basePath;
            return this;
        }

        /**
         * Istanzia il Builder ed il rest template per istanziare il client.
         * @return
         */
        public T newInstance() {
            return newInstance(newClass, RestClientBuilder.this.newRestTemplate());
        }

        protected T newInstance(Class<T> newClass, RestTemplate restTemplate) {
            return (T) RestClientBuilder.this.newInstance(newClass)
                            .setRestTemplate(restTemplate)
                            .setBasePath(basePath);
        }
    }
	
	protected <T> T newInstance(Class<T> classTarget) {
        try {
            return classTarget.newInstance();
        } catch (Exception e) {
        	log.error("non riesco a creare la classe " + classTarget, e);
            throw new RuntimeException(e);
        }
    }
	
	protected RestTemplate newRestTemplate() {
		return restTemplateBuilder.buildWith(() -> utenteCorrente.utenza());
    }
}
