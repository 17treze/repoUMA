package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;
import it.tndigitale.a4gistruttoria.repository.model.EsitoMantenimentoPascolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalcoloAnomalieMantenimentoSupport {

    @Autowired
    private ObjectMapper mapper;

    public EsitoMantenimentoPascolo from(String esitoMan,
                                          IstruttoriaModel istruttoria,
                                          List<VariabileCalcolo> variabiliInput,
                                          DatiSintesi datiSintesiPascolo,
                                          A4gtPascoloParticella pascolo) throws CalcoloSostegnoException {
        return new EsitoMantenimentoPascolo()
                .setEsitoMan(esitoMan)
                .setIstruttoria(istruttoria)
                .setDatiInput(generaDatiInput(variabiliInput))
                .setDatiOutput(generaDatiOutput(datiSintesiPascolo))
                .setPascoloParticella(pascolo);
    }

    private String generaDatiInput(List<VariabileCalcolo> variabiliInput) throws CalcoloSostegnoException {
        try {
            return mapper.writeValueAsString(variabiliInput);
        } catch (JsonProcessingException e1) {
            throw new CalcoloSostegnoException("Errore scrivendo i dati delle variabili in input/output per l'istruttoria del pascolo ", e1);
        }
    }

    private String generaDatiOutput(DatiSintesi datiSintesiPascolo) throws CalcoloSostegnoException {
        try {
            return mapper.writeValueAsString(datiSintesiPascolo);
        } catch (JsonProcessingException e1) {
            throw new CalcoloSostegnoException("Errore scrivendo i dati delle variabili in input/output per l'istruttoria del pascolo ", e1);
        }
    }

}
