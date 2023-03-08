package it.tndigitale.a4gistruttoria.service;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.DatiDomandaRicerca;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.service.builder.RicercaDomandaUnicaBuilder;
import it.tndigitale.a4gistruttoria.specification.DomandaUnicaRicercaSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RicercaDomandeServiceImpl implements RicercaDomandeService {

    private static final Logger logger = LoggerFactory.getLogger(RicercaDomandeServiceImpl.class);

    @Autowired
    private DomandaUnicaDao domandaUnicaDao;

    @Override
    public RisultatiPaginati<DatiDomandaRicerca> ricercaDomandeUniche(IstruttoriaDomandaUnicaFilter filter, Paginazione paginazione, Ordinamento sort) {
        try {
            Page<DomandaUnicaModel> domande = domandaUnicaDao.findAll(DomandaUnicaRicercaSpecificationBuilder.searchDomandeUnicheByFilters(filter), PageableBuilder.build().from(paginazione, sort));
            List<DatiDomandaRicerca> domandaRicercaList = domande.stream().map(domanda -> RicercaDomandaUnicaBuilder.buildFromQuery(domanda, filter)).collect(Collectors.toList());
            return RisultatiPaginati.of(domandaRicercaList, domande.getTotalElements());
        } catch (Exception e) {
            logger.error("Errore nella ricerca delle domande uniche");
            throw e;
        }
    }

    @Override
    public List<StatoDomanda> getListaStatiDomande() {
        try {
            return domandaUnicaDao.getListaStatiDomande();
        } catch (Exception e) {
            logger.error("Errore nella ricerca degli stati domanda");
            throw e;
        }
    }

    @Override
    public List<Integer> getListaAnniCampagne() {
        try {
            return domandaUnicaDao.getListaAnniCampagne();
        } catch (Exception e) {
            logger.error("Errore nel recupero della lista anni delle domande");
            throw e;
        }
    }
}
