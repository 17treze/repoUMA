package it.tndigitale.a4g.proxy.services;

import javax.annotation.PostConstruct;

import it.tndigitale.a4g.proxy.config.WSBasicAuthenticationPreemptiveSupport;
import it.tndigitale.a4g.proxy.dto.mapper.PersonaFisicaMapper;
import it.tndigitale.a4g.proxy.dto.mapper.PersonaGiuridicaMapper;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import it.tndigitale.ws.isvalidazioneanagrafe.ISAutenticazione;
import it.tndigitale.ws.isvalidazioneanagrafe.ISCodifica;
import it.tndigitale.ws.isvalidazioneanagrafe.IdentificativoFiscale;
import it.tndigitale.ws.isvalidazioneanagrafe.RichiestaRichiestaRispostaSincronaRicercaAnagraficaAll;
import it.tndigitale.ws.isvalidazioneanagrafe.RispostaRichiestaRispostaSincronaRicercaAnagraficaAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service
public class AnagrafeTributariaServiceImpl implements AnagrafeTributariaService {

    private static final Logger log = LoggerFactory.getLogger(AnagrafeTributariaServiceImpl.class);

    @Autowired
    private AnagrafeTributariaWS anagrafeTributariaWS;
    
    @Override
    @Deprecated
    public RispostaRichiestaRispostaSincronaRicercaAnagraficaAll caricaAnagraficaPersonaFisica(String codiceFiscale) throws Exception {
        return anagrafeTributariaWS.retryableCaricaAnagrafica(codiceFiscale);
    }
    @Override
    public PersonaFisicaDto getPersonaFisica(String codiceFiscale) throws Exception {
        var response = anagrafeTributariaWS.retryableCaricaAnagrafica(codiceFiscale);
        
        ISCodifica esito = response.getEsito();
        String codiceEsito = esito.getCodice().getValue();
        String descrizioneEsito = esito.getDescrizione().getValue();
        
        switch(codiceEsito) {
            case "XX02": //codice fiscale non censito
                return null;
            case "999":
                throw new Exception(descrizioneEsito);
        }
        
        return PersonaFisicaMapper.fromAnagrafeTributaria(response);
    }
    @Override
    public PersonaGiuridicaDto getPersonaGiuridica(String codiceFiscale) throws Exception {
        var response = anagrafeTributariaWS.retryableCaricaAnagrafica(codiceFiscale);
        
        ISCodifica esito = response.getEsito();
        String codiceEsito = esito.getCodice().getValue();
        String descrizioneEsito = esito.getDescrizione().getValue();
        
        switch(codiceEsito) {
            case "S010":
                return null;
            case "999":
                throw new Exception(descrizioneEsito);
        }
        
        return PersonaGiuridicaMapper.fromAnagrafeTributaria(response);
    }

    @Component
    protected static class AnagrafeTributariaWS extends WSBasicAuthenticationPreemptiveSupport {
        
        @Value("${anagrafetributaria.uri}")
        private String wsUri;

        // Codice fiscale abilitato alla lettura anagrafe tributaria
        // (RGNPLA67A63E783V e' del referente di APPAG, Paola Rogani)
        // 20210830: PGLPLG72E23C352Q e' il cf usato da ags per accedere ad anagrafe tributaria; questo
        //  cf funziona correttamente anche per reperire le informazioni sulle persone giuridiche (quella di Paola Rogani
        //  al momento di questa nota funziona solo per persone fisiche).
        //	@Value("${anagrafetributaria.cfauth:RGNPLA67A63E783V}")
        @Value("${anagrafetributaria.cfauth:PGLPLG72E23C352Q}")
        private String cfAutenticazione;

        @Value("${anagrafetributaria.username}")
        private String wsUsername;

        @Value("${anagrafetributaria.password}")
        private String wsPassword;
        
        @PostConstruct
        private void buildWebTemplate() throws Exception {
            super.buildWebTemplate("it.tndigitale.ws.isvalidazioneanagrafe", wsUri, wsUsername, wsPassword);
        }
        
        @Retryable(include = Exception.class, maxAttempts = 10, backoff = @Backoff(100))
        protected RispostaRichiestaRispostaSincronaRicercaAnagraficaAll retryableCaricaAnagrafica(String codiceFiscale) throws Exception {
            var response = caricaAnagrafica(codiceFiscale);

            String codiceEsito = response
                .getEsito()
                .getCodice()
                .getValue();

            switch(codiceEsito) {
                case "S013": //servizio non disponibile
                    String message = String.format("Errore nel servizio di anagrafica tributaria per il soggetto %s non è censito in Anagrafe Tributaria. %s", codiceFiscale, codiceEsito);

                    log.error(message);

                    throw new Exception(message);
            }

            return response;
        }
        @Recover
        protected RispostaRichiestaRispostaSincronaRicercaAnagraficaAll recoverCaricaAnagrafica(Exception e, String codiceFiscale) {
            return caricaAnagrafica(codiceFiscale);
        }
    
        private RispostaRichiestaRispostaSincronaRicercaAnagraficaAll caricaAnagrafica(String codiceFiscale) {
            var request = new RichiestaRichiestaRispostaSincronaRicercaAnagraficaAll();

            IdentificativoFiscale identificativo = new IdentificativoFiscale();
            switch(codiceFiscale.length()) {
                case 11: //persone giuridiche 
                    identificativo.setCodiceFiscalePersonaGiuridica(codiceFiscale);
                    break;
                case 16: //persone fisiche
                    identificativo.setCodiceFiscalePersonaFisica(codiceFiscale);
                    break;
                default:
                    String message = String.format("Chiamato servizio anagrafe tributaria con con codice fiscale invalido %s", codiceFiscale);

                    log.error(message);

                    throw new IllegalArgumentException(message);
            }

            ISAutenticazione autenticazione = new ISAutenticazione();
            autenticazione.setUtenteCF(cfAutenticazione);

            request.setIdentificativo(identificativo);
            request.setUtente(autenticazione);

            var response = (RispostaRichiestaRispostaSincronaRicercaAnagraficaAll) getWebServiceTemplate().marshalSendAndReceive(wsUri, request);

            return response;
        }
    }
}