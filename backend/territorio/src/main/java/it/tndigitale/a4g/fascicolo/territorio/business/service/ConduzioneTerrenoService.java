package it.tndigitale.a4g.fascicolo.territorio.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity.TipoConduzioneModel;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.SottotipoConduzioneDao;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.TipoConduzioneDao;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.AgsStoredFunctionException;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.ConduzioneTerrenoDao;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.CaratteristicheZonaDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.ConduzioneSianDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.ParticellaSianDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.SottotipoConduzioneDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.SottotipoDocumentoDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.TipoConduzioneDto;
import it.tndigitale.a4g.territorio.business.service.client.ProxyClient;

@Service
public class ConduzioneTerrenoService {
	@Autowired private ProxyClient proxyClient;
	@Autowired private ConduzioneTerrenoDao conduzioneTerrenoDao;

	@Autowired
	private TipoConduzioneDao tipoConduzioneDao;

	@Autowired
	private SottotipoConduzioneDao sottotipoConduzioneDao;

	public List<ConduzioneSianDto> getConduzioneTerreni(String cuaa) {
		var resultList = new ArrayList<it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.ConduzioneSianDto>();
		var resultFromProxy = proxyClient.leggiConsistenzaUsingGET(cuaa);
		if (resultFromProxy == null) {
			return resultList;
		}

		for (it.tndigitale.a4g.proxy.client.model.ConduzioneDto conduzione: resultFromProxy) {
			var particellaDtoResult = new ParticellaSianDto();
			BeanUtils.copyProperties(conduzione.getDatiParticella(), particellaDtoResult);
			var caratteristicheZonaDtoResult = new CaratteristicheZonaDto();
			BeanUtils.copyProperties(conduzione.getCaratteristicheZona(), caratteristicheZonaDtoResult);
			ConduzioneSianDto result = new ConduzioneSianDto();
			BeanUtils.copyProperties(conduzione, result);
			result.setCaratteristicheZona(caratteristicheZonaDtoResult);
			result.setDatiParticella(particellaDtoResult);
			resultList.add(result);
		}
		return resultList;
	}

	// FAS-ANA-03.4
	public void salvaConduzioneTerreni(String cuaa, List<ConduzioneSianDto> conduzioneDtoList) throws SQLException, JsonProcessingException, AgsStoredFunctionException {
		short result = conduzioneTerrenoDao.salvaConduzioneTerreno(cuaa, conduzioneDtoList);
		if (result == 1) {
			//			errore in stored function
			throw new AgsStoredFunctionException("Stored function AGS_SINCRO_FASCICOLO.SINC_CONSISTENZA_SIAN fallita: cuaa [" + cuaa + "] - esito [" + result + "]");
		}
	}

	public List<TipoConduzioneDto> getElencoTipologieConduzione() {
		return tipoConduzioneDao.findAll().stream().map((TipoConduzioneModel model) -> {
			var result = new TipoConduzioneDto();
			BeanUtils.copyProperties(model, result);
			return result;
		}).collect(Collectors.toList());
	}

	public List<SottotipoConduzioneDto> getElencoSottoipologieConduzione(Long idTipologia) {
		var tipo = new TipoConduzioneModel();
		tipo.setId(idTipologia);
		Optional<TipoConduzioneModel> byid = tipoConduzioneDao.findById(idTipologia);
		if(byid.isEmpty()) {
			return new ArrayList<>();
		}
		tipo = byid.get();
		return sottotipoConduzioneDao.findByTipoConduzione(tipo).stream().map((model) -> {
			var result = new SottotipoConduzioneDto();
			BeanUtils.copyProperties(model, result);
			result.setIdTipologia(idTipologia);
			return result;
		}).collect(Collectors.toList());
	}
	
	
	public List<SottotipoDocumentoDto> getElencoSottotipoDocumenti (Long idSottoTipoConduzione) {
		var sottotipoConduzioneOpt = sottotipoConduzioneDao.findById(idSottoTipoConduzione);
		if(sottotipoConduzioneOpt.isEmpty()) {
			return new ArrayList<>();
		}

		var sottotipoConduzione = sottotipoConduzioneOpt.get();
		var relazioneDocumenti = sottotipoConduzione.getSottotipodocumenti();
		var stream = relazioneDocumenti.stream();
		return stream.map(model -> {
			var result = new SottotipoDocumentoDto();
			BeanUtils.copyProperties(model, result);
			result.setIdDocumentoConduzione(model.getDocumentoConduzione().getId());
			result.setSottotipoConduzione(idSottoTipoConduzione);
			result.setDescrizione(model.getDocumentoConduzione().getDescrizione());
			var dipendenze = model.getDocumentoDipendenzaPrimario();
			if(!dipendenze.isEmpty()) {
				Long[] docs = dipendenze.stream().map( curr -> curr.getDocumentoConduzione().getId()).toArray(Long[]::new);
				result.setDocumentoDipendenza(docs);
			}
			return result;
		}).collect(Collectors.toList());
	}
}
