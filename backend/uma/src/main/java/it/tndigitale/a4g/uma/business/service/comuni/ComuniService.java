package it.tndigitale.a4g.uma.business.service.comuni;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.uma.business.persistence.entity.ComuneModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ComuneDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.dto.ComuneDto;
import it.tndigitale.a4g.uma.dto.aual.RecapitoAualDto;
import it.tndigitale.a4g.uma.dto.aual.SoggettoAualDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@Service
public class ComuniService {
	
	@Autowired
	private ComuneDao comuneDao;

	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	
	public List<ComuneDto> getComuniCapofila() {
		List<ComuneModel> comuniModel = comuneDao.findAllCapofila();
		return comuniModel.stream().map(c -> new ComuneDto(
				c.getCodiProv(), 
				c.getCodiComu(), 
				c.getDescComu(),
				c.getCodiNcap(),
				c.getCodiComuCapo(),
				c.getCodiCata())).collect(Collectors.toList());
	}	
	
	public ComuneDto getComuneCapofilaFascicolo(String cuaa) {
		// trova dati richiedente
		SoggettoAualDto soggetto = anagraficaClient.getSoggetto(cuaa);
		if (soggetto != null) {
			for (RecapitoAualDto r : soggetto.getRecapito()) {
				if (r.getTipoRecapito().getCodiTipoReca().equals("57")) {
					// Sede legale
					ComuneModel c = comuneDao.findCapofilaComune(r.getCodiProv(), r.getCodiComu());
					if (c != null) {
						return new ComuneDto(
								c.getCodiProv(), 
								c.getCodiComu(), 
								c.getDescComu(),
								c.getCodiNcap(),
								c.getCodiComuCapo(),
								c.getCodiCata());
					}
				}
			}
		}
		return null;
	}	
}
