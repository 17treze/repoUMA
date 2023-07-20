package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneInProprioModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.specification.FascicoloSpecificationBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DetenzioneDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.DetenzioneInProprioBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.MandatoBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper.FascicoloMapper;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;

@Service
public class RicercaFascicoloService {
	private static final Logger logger = LoggerFactory.getLogger(RicercaFascicoloService.class);
	
	@Autowired
	private FascicoloDao fascicoloDao;
	
	@Autowired
	private CaaService caaService;
	
	@Autowired
	private DetenzioneService detenzioneService;
	
	public RisultatiPaginati<FascicoloDto> ricerca(FascicoloFilter criteri, Paginazione paginazione,
			Ordinamento ordinamento) {
		//		criteri.setEntiUtenteConnesso(caaService.getSportelliAbilitatiCaa());
		criteri.setEntiUtenteConnesso(new ArrayList<String>());
		Page<FascicoloModel> page = fascicoloDao.findAll(FascicoloSpecificationBuilder.getFilter(criteri),
				PageableBuilder.build().from(paginazione, ordinamento));
		List<FascicoloDto> risultati = FascicoloMapper.from(page);
		if (risultati != null) {
			risultati.forEach(fascicoloDto -> {
				Optional<FascicoloModel> fascicoloModelOptional = fascicoloDao
						.findByCuaaAndIdValidazione(fascicoloDto.getCuaa(), 0);
				if (fascicoloModelOptional.isPresent()) {
					FascicoloModel fascicoloModel = fascicoloModelOptional.get();
					Optional<DetenzioneModel> detenzioneCorrenteOpt = detenzioneService
							.getDetenzioneCorrente(fascicoloModel);
					if (detenzioneCorrenteOpt.isPresent()) {
						DetenzioneModel detenzioneModel = detenzioneCorrenteOpt.get();
						DetenzioneDto detenzioneDto = null;
						if (detenzioneModel instanceof MandatoModel) {
							detenzioneDto = MandatoBuilder.from((MandatoModel) detenzioneModel);
						}
						else if (detenzioneModel instanceof DetenzioneInProprioModel) {
							detenzioneDto = DetenzioneInProprioBuilder.from((DetenzioneInProprioModel) detenzioneModel);
						}
						fascicoloDto.setDetenzione(detenzioneDto);
					}
					else {
						fascicoloDto.setDetenzione(null);
					}
				}
			});
		}
		else {
			risultati = new ArrayList<>();
		}
		return RisultatiPaginati.of(risultati, page.getTotalElements());
	}
	
	public FascicoloModel getFascicoloModel(final String cuaa, final int idValidazione) throws EntityNotFoundException {
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
		if (!fascicoloModelOpt.isPresent()) {
			throw new EntityNotFoundException(String.format("Fascicolo CUAA %s non trovato", cuaa));
		}
		return fascicoloModelOpt.get();
	}
	
	public FascicoloDto getFascicoloDto(final String cuaa, final int idValidazione) throws Exception {
		FascicoloDto dto = FascicoloMapper.fromFascicolo(getFascicoloModel(cuaa, idValidazione));
		
		Optional<LocalDate> dataUltimaValidazioneOpt = fascicoloDao.getDataUltimaValidazione(cuaa);
		if (dataUltimaValidazioneOpt.isPresent()) {
			dto.setDataUltimaValidazione(dataUltimaValidazioneOpt.get());
		}
		
		return dto;
	}
	
	public FascicoloDto getFascicoloPerRevocaImmediata(final String cuaa) throws Exception {
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		if (!fascicoloModelOpt.isPresent()) {
			String err = String.format("Fascicolo CUAA %s non trovato", cuaa);
			logger.error(err);
			throw new EntityNotFoundException(err);
		}
		return FascicoloMapper.fromFascicolo(fascicoloModelOpt.get());
	}
}
