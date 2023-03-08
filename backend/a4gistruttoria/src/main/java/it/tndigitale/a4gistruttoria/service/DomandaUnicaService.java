package it.tndigitale.a4gistruttoria.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.DomandaUnicaRicercaFilter;
import it.tndigitale.a4gistruttoria.dto.domandaunica.Istruttoria;
import it.tndigitale.a4gistruttoria.dto.domandaunica.IstruttoriaFilter;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiDomandaUnica;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiPagamento;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.service.builder.DomandaUnicaBuilder;
import it.tndigitale.a4gistruttoria.service.support.DomandaUnicaSupport;
import it.tndigitale.a4gistruttoria.specification.DomandaUnicaSpecificationBuilder;
import javassist.NotFoundException;

@Service
public class DomandaUnicaService {

	private static Logger logger = LoggerFactory.getLogger(DomandaUnicaService.class);
	
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	
	@Autowired
	private DomandaUnicaSupport domandaUnicaSupport;

	/**
	 * Recupera tutte le istruttorie di una Domanda Unica con le relative anomalie
	 * @param idDomanda - temporaneo a monte del refactoring saldi
	 */
	public List<Istruttoria> getIstruttorie(Long idDomanda) throws NotFoundException {
		Optional<DomandaUnicaModel> domandaUnicaModelTempOpt = domandaUnicaDao.findById(idDomanda);
		if(!domandaUnicaModelTempOpt.isPresent()) {
			return Collections.emptyList();
		}
		DomandaUnicaModel domanda = domandaUnicaModelTempOpt.get();
		return domandaUnicaSupport.getIstruttorieBy(domanda);
	}
	
	public List<Istruttoria> getIstruttorie(BigDecimal numeroDomanda) {
		DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(numeroDomanda);
		if (domanda == null) {
			return Collections.emptyList();
		}
		return domandaUnicaSupport.getIstruttorieBy(domanda);
	}
	
	/**
	 * Recupera tutte le istruttorie di una Domanda Unica con le relative anomalie
	 * @param istruttoriaFilter
	 * @return Lista di Istruttorie - contentente la lista delle istruttorie con le relative anomalie
	 */
	@Deprecated
	public List<Istruttoria> getIstruttorie(IstruttoriaFilter istruttoriaFilter) {
		if (istruttoriaFilter.getCuaa() == null || istruttoriaFilter.getCampagna() == null) {
			throw new IllegalArgumentException("Cuaa o anno non valorizzati");
		}
		// TODO: query da rifattorizzare a valle del refactor dei saldi.
		DomandaUnicaModel domanda = domandaUnicaDao.findByCuaaIntestatarioAndCampagna(istruttoriaFilter.getCuaa(), istruttoriaFilter.getCampagna());
		return domandaUnicaSupport.getIstruttorieBy(domanda);
	}
	
	/**
	 * Recupera i dati sui pagamenti per idDomanda
	 * @param idDomanda
	 * @return
	 */
	public SintesiPagamento getSintesiPagamento(Long idDomanda) {
		Optional<DomandaUnicaModel> domandaUnicaModelTempOpt = domandaUnicaDao.findById(idDomanda);
		if(!domandaUnicaModelTempOpt.isPresent()) {
			logger.info("Nessuna domanda trovata con id {}", idDomanda);
			return null;
		}
		DomandaUnicaModel domanda = domandaUnicaModelTempOpt.get();
		return domandaUnicaSupport.calcolaSintesiPagamentoFrom(domanda);
	}
	
	/**
	 * Recupera i dati sui pagamenti per numero domanda
	 * @param numeroDomanda
	 * @return
	 */
	public SintesiPagamento getSintesiPagamento(BigDecimal numeroDomanda) {
		DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(numeroDomanda);
		if (domanda == null) {
			return null;
		}
		return domandaUnicaSupport.calcolaSintesiPagamentoFrom(domanda);
	}
	
	public Long countDomande(DomandaUnicaRicercaFilter criteri)  {
		return domandaUnicaDao.count(DomandaUnicaSpecificationBuilder.getDomandaUnicaFilter(criteri));
	}

	public RisultatiPaginati<SintesiDomandaUnica> ricerca(DomandaUnicaRicercaFilter filter,
			Paginazione paginazione, Ordinamento ordinamento) {
		Page<DomandaUnicaModel> page = domandaUnicaDao.findAll(
				DomandaUnicaSpecificationBuilder.getDomandaUnicaFilter(filter),
				PageableBuilder.build().from(paginazione, ordinamento));
		return DomandaUnicaBuilder.sintesiFrom(page);
	}
}
