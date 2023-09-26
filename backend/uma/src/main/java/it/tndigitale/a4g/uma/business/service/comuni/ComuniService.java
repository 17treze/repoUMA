package it.tndigitale.a4g.uma.business.service.comuni;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.uma.business.persistence.entity.ComuneModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ComuneDao;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.dto.ComuneDto;
import it.tndigitale.a4g.uma.dto.aual.TerritorioAualDto;

@Service
public class ComuniService {

	private static final Logger logger = LoggerFactory.getLogger(ComuniService.class);

	@Autowired
	private ComuneDao comuneDao;

	@Autowired
	private UmaTerritorioClient territorioClient;
	
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
	
	public List<ComuneDto> getComuneCapofilaFascicolo(String cuaa) {
		List<ComuneDto> comuniCapofila = new ArrayList<ComuneDto>();
		logger.info("Chiamato servizio terreni: " + cuaa);
		List<TerritorioAualDto> terreni = territorioClient.getColture(cuaa, LocalDateTime.now());
		if (terreni != null && !terreni.isEmpty()) {
			List<String> comuniTerreni = new ArrayList<String>();
			for (TerritorioAualDto r : terreni) {
				comuniTerreni.add(r.getCodiProv() + r.getCodiComu());
			}
			logger.info("Comuni terreni: " + comuniTerreni);
			if (!comuniTerreni.isEmpty()) {
				List<ComuneModel> cList = comuneDao.findCapofilaComuni(comuniTerreni);
				if (cList != null) {
					for (ComuneModel c : cList) {
						comuniCapofila.add(new ComuneDto(
								c.getCodiProv(), 
								c.getCodiComu(), 
								c.getDescComu(),
								c.getCodiNcap(),
								c.getCodiComuCapo(),
								c.getCodiCata()));
					}
				}
			}
		}
		logger.info("Comuni capofila trovati: " + comuniCapofila.toString());
		return comuniCapofila;
	}	
}
