//package it.tndigitale.a4g.fascicolo.anagrafica.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.WebClientBuilder;
//import it.tndigitale.a4g.framework.security.model.UtenteComponent;
//
//@Configuration
//public class WebClientConfig {
//    @Bean
//    public WebClient createWebClient(
//            @Autowired UtenteComponent utenteCorrente,
//            @Autowired WebClientBuilder webClientBuilder) {
//        return webClientBuilder.buildWith(utenteCorrente::utenza);
//    }
//}
