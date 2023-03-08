package it.tndigitale.a4g.proxy.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.proxy.dto.AntimafiaEsitoDto;
import it.tndigitale.a4g.proxy.dto.builder.AntimafiaEsitoBuilder;
import it.tndigitale.a4g.proxy.repository.esiti.dao.AntimafiaDescrizioneEsitiDao;
import it.tndigitale.a4g.proxy.repository.esiti.dao.AntimafiaEsitiDao;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaDescrizioneEsitiModel;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaEsitiModel;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;

@Service
public class EsitoAntimafiaService {

	@Autowired
	private AntimafiaEsitiDao daoAntimafiaEsiti;

	@Autowired
	private AntimafiaDescrizioneEsitiDao daoAntimafiaDescrizioneEsiti;
	
	@Autowired
	private DichiarazioneAntimafiaDao dichiarazioneAntimafiaDao;

	public AntimafiaEsitoDto getEsitoAntimafia (String cuaa) {
		List<AntimafiaEsitiModel> esiti = daoAntimafiaEsiti.findByIdCuaa(cuaa);
		if (CollectionUtils.isEmpty(esiti)) {
			return null;
		}
		AntimafiaEsitiModel esito = Collections.max(esiti, Comparator.comparing(c -> c.getId().getDtElaborazione()));
		AntimafiaDescrizioneEsitiModel esitoDescrizione = daoAntimafiaDescrizioneEsiti.findByCodice(esito.getCodice());
		AntimafiaEsitoDto dto = AntimafiaEsitoBuilder.from(esito, esitoDescrizione);
		
		List<AabaantiTab> dichiarazioniSincronizzate = dichiarazioneAntimafiaDao.findByCuaa(cuaa);
		if (!CollectionUtils.isEmpty(dichiarazioniSincronizzate)) {
			AabaantiTab dichiarazioneRecente = Collections.max(dichiarazioniSincronizzate, Comparator.comparing(AabaantiTab::getDataInizVali));
			if (dichiarazioneRecente != null && dichiarazioneRecente.getDataInizVali() != null) {
				dto.setDtValidita(LocalDateConverter.fromDateTime(dichiarazioneRecente.getDataInizVali()));
			}
		}
		return dto;
	}
	
	
	public List<AntimafiaEsitoDto> getEsitiAntimafia (List<String> cuaaList) {
		List<AntimafiaEsitoDto> result = new ArrayList<>();
		cuaaList.forEach(cuaa -> result.add(getEsitoAntimafia(cuaa)));
		return result;
	}
}
