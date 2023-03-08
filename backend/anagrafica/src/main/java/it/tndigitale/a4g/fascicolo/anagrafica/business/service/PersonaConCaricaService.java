package it.tndigitale.a4g.fascicolo.anagrafica.business.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.tndigitale.a4g.fascicolo.anagrafica.business.event.PersoneFisicheConCaricaEvent;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.Sesso;
import static it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.VerificaCodiceFiscale.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.PersoneFisicheConCaricaExtDto;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.shareable.Shareable;
import it.tndigitale.a4g.proxy.client.model.IndirizzoDto;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import java.util.Collection;
import java.util.stream.Collectors;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class PersonaConCaricaService {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private AnagraficaProxyClient anagraficaProxyClient;
    @Autowired
    private PersonaFisicaConCaricaDao personaFisicaConCaricaDao;

    @Transactional
    public void sendEvent(FascicoloModel fascicoloModel, Set<PersonaFisicaConCaricaModel> personaSet) {
        var fascicoloId = fascicoloModel.getId();
        var personaIdSet = personaSet
            .stream()
            .map(PersonaFisicaConCaricaModel::getId)
            .collect(Collectors.toSet());
            
        var data = new PersoneFisicheConCaricaExtDto(fascicoloId, personaIdSet);
        var event = new PersoneFisicheConCaricaEvent(data);
        
        eventBus.publishEvent(event);
    }
    
    @Transactional(propagation = REQUIRES_NEW)
    @Shareable
    public Collection<PersonaFisicaConCaricaModel> persistPersoneFisicheConCarica(PersoneFisicheConCaricaExtDto personeConCarica) {
        return personeConCarica
            .getIdPersonaFisicaConCaricaSet()
            .stream()
            .map(this::persistPersonaFisicaConCarica)
            .collect(Collectors.toList());
    }

    private PersonaFisicaConCaricaModel persistPersonaFisicaConCarica(Long personaId) {
        var personaModel = personaFisicaConCaricaDao
            .findByIdAndIdValidazione(personaId, 0)
            .orElseThrow(() -> new IllegalArgumentException("Persona fisica con carica non trovato"));

        String codiceFiscale = personaModel.getCodiceFiscale();

        var personaDto = anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscale);

        if(personaDto == null) {
            personaModel.setVerificaCodiceFiscale(NON_TROVATO_IN_AT);
        } else {
            personaModel.setVerificaCodiceFiscale(codiceFiscale.equalsIgnoreCase(personaDto.getCodiceFiscale()) ? CORRETTO : PARIX_DIVERSO_DA_AT);
            
            update(personaModel, personaDto);
        }

        return personaFisicaConCaricaDao.save(personaModel);
    }
    
    private static void update(PersonaFisicaConCaricaModel personaModel, PersonaFisicaDto personaDto) {
        personaModel
            .setCodiceFiscale(personaDto.getCodiceFiscale())
            .setNome(personaDto.getAnagrafica().getNome())
            .setCognome(personaDto.getAnagrafica().getCognome())
            .setDataNascita(personaDto.getAnagrafica().getDataNascita())
            .setComuneNascita(personaDto.getAnagrafica().getComuneNascita())
            .setProvinciaNascita(personaDto.getAnagrafica().getProvinciaNascita())
            .setSesso(Sesso.valueOf(personaDto.getAnagrafica().getSesso().getValue()))
            .setDeceduto(personaDto.isDeceduta())
            .setIndirizzo(from(personaDto.getDomicilioFiscale()));
    }
    private static IndirizzoModel from(IndirizzoDto indirizzoDto) {
        return new IndirizzoModel()
            .setCap(indirizzoDto.getCap())
            .setCodiceIstat(indirizzoDto.getCodiceIstat())
            .setComune(indirizzoDto.getComune())
            .setDescrizioneEstesa(indirizzoDto.getDenominazioneEstesa())
            .setFrazione(indirizzoDto.getFrazione())
            .setNumeroCivico(indirizzoDto.getCivico())
            .setProvincia(indirizzoDto.getProvincia())
            .setToponimo(indirizzoDto.getToponimo())
            .setVia(indirizzoDto.getVia());
    }
}
