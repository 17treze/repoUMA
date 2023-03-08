package it.tndigitale.a4gistruttoria.service;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.DatiDomandaRicerca;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;

import java.util.List;

public interface RicercaDomandeService {
    RisultatiPaginati<DatiDomandaRicerca> ricercaDomandeUniche(IstruttoriaDomandaUnicaFilter filter, Paginazione paginazione, Ordinamento ordinamento);

    List<StatoDomanda> getListaStatiDomande();

    List<Integer> getListaAnniCampagne();
}
