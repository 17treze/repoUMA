package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.legacy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDtoPaged;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.ModoPagamentoAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;

@Service
public class FascicoloAgsService {

	@Autowired
	private FascicoloAgsDao fascicoloAgsDao;
	@Autowired
	private Clock clock;
	
	public FascicoloAgsDto getFascicolo(String cuaa, LocalDateTime date) {
		if (date == null) {
			date = clock.now();
		}
		return fascicoloAgsDao.getFascicolo(cuaa, date);
	}

	public FascicoloAgsDto getFascicolo(String cuaa) {
		return getFascicolo(cuaa, clock.now());
	}

	public RisultatiPaginati<FascicoloAgsDtoPaged> ricercaFascicolo(FascicoloAgsFilter filter, Paginazione paginazione, Ordinamento ordinamento) {
		Pageable pageable = PageableBuilder.build().from(paginazione, ordinamento);
		List<FascicoloAgsDtoPaged> getfascicoliPaginati = fascicoloAgsDao.getFascicoli(filter, pageable, ordinamento);
		Optional<Integer> totalOpt = getfascicoliPaginati.stream().map(FascicoloAgsDtoPaged::getTotale).findAny();
		return RisultatiPaginati.of(getfascicoliPaginati, totalOpt.isPresent() ? Long.valueOf(totalOpt.get()) : 0L);
	}

	public FascicoloAgsDto getFascicolo(Long id) {
		return fascicoloAgsDao.getFascicolo(id, clock.now());
	}

	public FascicoloAgsDto getFascicoloDaMigrare(String cuaa) {
		return fascicoloAgsDao.getFascicoloDaMigrare(cuaa, clock.now());
	}

	public List<ModoPagamentoAgsDto> getModiPagamentoDaMigrare(String cuaa) {
		return fascicoloAgsDao.getModiPagamentoDaMigrare(cuaa, clock.now());
	}
	
	public MovimentoValidazioneFascicoloAgsDto getMovimentiValidazioneFascicolo(Long id, Long campagna) {
		return fascicoloAgsDao.getMovimentiValidazioneFascicolo(id, campagna);
	}
}
