package it.tndigitale.a4gistruttoria.service.builder;

import it.tndigitale.a4gistruttoria.dto.DatiDomandaRicerca;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.dto.RelateModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RicercaDomandaUnicaBuilder {

    public static DatiDomandaRicerca buildFromQuery(DomandaUnicaModel domanda, IstruttoriaDomandaUnicaFilter filter) {
        DatiDomandaRicerca datiDomandaRicerca = new DatiDomandaRicerca();
        datiDomandaRicerca.setNumeroDomanda(domanda.getNumeroDomanda());
        datiDomandaRicerca.setCompanyDescription(domanda.getRagioneSociale());
        datiDomandaRicerca.setCuaa(domanda.getCuaaIntestatario());
        datiDomandaRicerca.setYear(domanda.getCampagna());
        datiDomandaRicerca.setState(domanda.getStato().name());
        datiDomandaRicerca.setRelates(new ArrayList<>());
        List<IstruttoriaModel> istruttorieDomanda = domanda.getA4gtLavorazioneSostegnos().stream().filter(istruttoriaModel -> istruttoriaModel.getDomandaUnicaModel().getId().equals(domanda.getId())).collect(Collectors.toList());
        if (filter.getSostegno() != null) {
            istruttorieDomanda = istruttorieDomanda.stream().filter(istruttoriaModel -> istruttoriaModel.getSostegno().equals(filter.getSostegno())).collect(Collectors.toList());
        }
        if (filter.getTipo() != null) {
            istruttorieDomanda = istruttorieDomanda.stream().filter(istruttoriaModel -> istruttoriaModel.getTipologia().equals(filter.getTipo())).collect(Collectors.toList());
        }
        istruttorieDomanda.forEach(istruttoriaModel -> datiDomandaRicerca.getRelates().add(new RelateModel(istruttoriaModel.getSostegno(), istruttoriaModel.getTipologia(), istruttoriaModel.getId())));
        Collections.sort(datiDomandaRicerca.getRelates());
        return datiDomandaRicerca;
    }
}
