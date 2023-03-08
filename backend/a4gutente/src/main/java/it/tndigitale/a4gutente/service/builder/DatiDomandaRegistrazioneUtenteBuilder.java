package it.tndigitale.a4gutente.service.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class DatiDomandaRegistrazioneUtenteBuilder {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static DatiDomandaRegistrazioneUtenteSintesi from(DomandaRegistrazioneUtente domandaRegistrazioneUtente) {
        DatiAnagrafici datiAnagrafici = new DatiAnagrafici().setCodiceFiscale(domandaRegistrazioneUtente.getCodiceFiscale())
                                                            .setCognome(domandaRegistrazioneUtente.getCognome())
                                                            .setNome(domandaRegistrazioneUtente.getNome())
                                                            .setEmail(domandaRegistrazioneUtente.getEmail())
                                                            .setTelefono(domandaRegistrazioneUtente.getTelefono());
        return new DatiDomandaRegistrazioneUtenteSintesi().setId(domandaRegistrazioneUtente.getId())
                                                          .setIdProtocollo(domandaRegistrazioneUtente.getIdProtocollo())
                                                          .setDataProtocollazione(domandaRegistrazioneUtente.getDtProtocollazione())
                                                          .setDatiAnagrafici(datiAnagrafici)
                                                          .setConfigurato(domandaRegistrazioneUtente.getConfigurato())
                                                          .setDataApprovazione(domandaRegistrazioneUtente.dataApprovazione())
                                                          .setDataRifiuto(domandaRegistrazioneUtente.dataRifiuto())
                                                          .setStato(domandaRegistrazioneUtente.getStato());
    }

    public static RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> from(Page<DomandaRegistrazioneUtente> page) {
        List<DatiDomandaRegistrazioneUtenteSintesi> risultati = page.stream()
                                                                    .map(domanda -> from(domanda))
                                                                    .collect(Collectors.toList());
        return RisultatiPaginati.of(risultati, page.getTotalElements());
    }

}
