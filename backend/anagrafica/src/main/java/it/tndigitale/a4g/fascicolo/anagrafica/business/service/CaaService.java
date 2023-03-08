package it.tndigitale.a4g.fascicolo.anagrafica.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CentroAssistenzaAgricolaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CentroAssistenzaAgricolaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.SportelloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.CentroAssistenzaAgricolaDtoBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.CentroAssistenzaAgricolaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;
import it.tndigitale.a4g.framework.support.CustomCollectors;

@Service
public class CaaService {
	private static Logger logger = LoggerFactory.getLogger(CaaService.class);
	@Autowired
	private SportelloDao sportelloDao;
	@Autowired
	private AnagraficaUtenteClient anagraficaUtenteClient;
	
	@Autowired
	private FascicoloDao fascicoloDao;
	
	@Autowired
	private MandatoService mandatoService;
	
	@Autowired
	private CentroAssistenzaAgricolaDao caaDao;

	/**
	 * @author B.Conetta
	 * @return get informazioni del caa connesso con gli sportelli a cui è abilitato
	 * 
	 */
	public CentroAssistenzaAgricolaDto getCentroAssistenzaAgricoloUtenteConnesso() {
		// recupera enti abilitati 
		List<String> entiUtenteConnesso = anagraficaUtenteClient.getEntiUtenteConnesso();
		List<SportelloModel> sportelloModelList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entiUtenteConnesso))  {
			List<Long> enti = entiUtenteConnesso
					.stream()
					.map(Long::valueOf)
					.collect(Collectors.toList());

			sportelloModelList = sportelloDao.findByIdentificativoIn(enti);
		}
		CentroAssistenzaAgricolaModel caa;
		if (!CollectionUtils.isEmpty(entiUtenteConnesso)) {
			caa = sportelloModelList.stream()
					.map(SportelloModel::getCentroAssistenzaAgricola)
					.distinct()
					.collect(CustomCollectors.toSingleton());
		} else {
			// la lista degli enti è vuota - 204 No Content
			logger.warn("Attenzione: Non ci sono sportelli abilitati per l'operatore connesso");
			return null;
		}
		return new CentroAssistenzaAgricolaDtoBuilder()
				.withSoggetto(caa)
				.withSportelli(sportelloModelList)
				.build();
	}

	public Boolean verificaSportelloIsAbilitato(Long idSportelloAGS) {
		List<String> entiUtenteConnesso = anagraficaUtenteClient.getEntiUtenteConnesso();
		if (!CollectionUtils.isEmpty(entiUtenteConnesso)) {
			return entiUtenteConnesso.stream().anyMatch(
					ente -> ente.equals(String.valueOf(idSportelloAGS)));
		}
		return Boolean.FALSE;
	}

	// Le aziende visualizzate devono essere soltanto quelle per cui ho i diritti di gestione del fascicolo
	// Posso visualizzare mandati e fascicoli solo per gli enti (sportelli) abilitati
	public List<String> getSportelliAbilitatiCaa() {
		return anagraficaUtenteClient.getEntiUtenteConnesso();
	}

	public boolean isAccessoCaa(final FascicoloModel fascicoloModel) {
		Optional<MandatoModel> detenzioneCorrenteOpt = mandatoService.getMandatoCorrente(fascicoloModel);
		if (!detenzioneCorrenteOpt.isPresent()) {
			return false;
		}
		MandatoModel mandatoModel = detenzioneCorrenteOpt.get();
		CentroAssistenzaAgricolaDto caaConnesso = getCentroAssistenzaAgricoloUtenteConnesso();
		if (caaConnesso == null) return false;
		List<Long> identificativiSportelliAbilitati = caaConnesso.getSportelli().stream()
				.map(SportelloCAADto::getIdentificativo)
				.collect(Collectors.toList());
		return identificativiSportelliAbilitati.contains(mandatoModel.getSportello().getIdentificativo());
	}
	
	public boolean isAccessoCaa(final String cuaa, final Integer idValidazione) {
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
		if (!fascicoloModelOpt.isPresent()) {
			return true;
		}
		return isAccessoCaa(fascicoloModelOpt.get());
	}

	public List<CentroAssistenzaAgricolaDto> getAllCaaWhitSportelli() {
		List<CentroAssistenzaAgricolaDto> caaWhitSportelli = new ArrayList<>();
		List<CentroAssistenzaAgricolaModel> centroAssistenzaAgricolaModelList = caaDao.findAll();
		centroAssistenzaAgricolaModelList.forEach(caa -> {
			List<SportelloModel> sportelloModelList = sportelloDao.findByCentroAssistenzaAgricola_id(caa.getId());
			if(sportelloModelList != null && !sportelloModelList.isEmpty()) {
				CentroAssistenzaAgricolaDto caaDto = new CentroAssistenzaAgricolaDtoBuilder()
						.withSoggetto(caa)
						.withSportelli(sportelloModelList)
						.build();
				caaWhitSportelli.add(caaDto);
			}
		});

		return caaWhitSportelli;
	}
}
