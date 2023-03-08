package it.tndigitale.a4g.fascicolo.anagrafica.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.CaaAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;

@Service
public class CaaAgsService {

	@Autowired
	private CaaAgsDao caaAgsDao;
	@Autowired
	private AnagraficaUtenteClient utenteClient;

	public List<SportelloFascicoloDto> getFascicoliEnteUtenteConnesso(TipoDetenzioneAgs tipoDetenzione) {
		// include la ricerca solo sugli sportelli abilitati rispetto all'utente connesso per una questione di migliore performance
		List<String> entiUtenteConnesso = utenteClient.getEntiUtenteConnesso();
		if (CollectionUtils.isEmpty(entiUtenteConnesso)) {
			entiUtenteConnesso = new ArrayList<>();
		}
		return caaAgsDao.getFascicoliCaa(entiUtenteConnesso, tipoDetenzione);
	}

}
