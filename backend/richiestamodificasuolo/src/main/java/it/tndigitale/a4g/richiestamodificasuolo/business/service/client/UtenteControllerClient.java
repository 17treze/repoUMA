package it.tndigitale.a4g.richiestamodificasuolo.business.service.client;

import java.util.List;

import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.utente.client.api.UtenteControllerApi;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.web.client.RestClientException;

@Component
public class UtenteControllerClient extends AbstractClient {

    @Value("${it.tndigit.a4g.utente.url}")
    private String urlUtente;
    
    public List<String> entiAbilitati() {
    	return getUtenteControllerApi().getEntiUsingGET();
    }
    public List<String> getProfili(String nome) {
        RappresentaIlModelloPerRappresentareUnUtenteDelSistema utente = getUtenteControllerApi().caricaUsingGET(nome);
        
        if(utente == null) {
            return Collections.emptyList();
        }
        
        return utente
            .getProfili()
            .stream()
            .map(RappresentaIlModelloDelProfiloDiUnUtente::getIdentificativo)
            .collect(Collectors.toList());
    }

    public RappresentaIlModelloPerRappresentareUnUtenteDelSistema getUtente(String nominativo) {
        return getUtenteControllerApi().caricaUsingGET(nominativo);
    }

    private UtenteControllerApi getUtenteControllerApi() {
        return restClientProxy(UtenteControllerApi.class, urlUtente);
    }
}