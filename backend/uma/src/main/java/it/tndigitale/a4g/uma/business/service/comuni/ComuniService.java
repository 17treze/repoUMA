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
		List<TerritorioAualDto> terreni = territorioClient.getColture(cuaa, LocalDateTime.now());
		if (terreni != null) {
			for (TerritorioAualDto r : terreni) {
				ComuneModel c = comuneDao.findCapofilaComune(r.getCodiProv(), r.getCodiComu());
				if (c != null) {
					ComuneDto cc = new ComuneDto(
							c.getCodiProv(), 
							c.getCodiComu(), 
							c.getDescComu(),
							c.getCodiNcap(),
							c.getCodiComuCapo(),
							c.getCodiCata());
					if (!comuniCapofila.contains(cc)) {
						comuniCapofila.add(cc);
					}
				}
			}
		}
		logger.info("Comuni capofila trovati: " + comuniCapofila.size());
		return comuniCapofila;
	}	
}
