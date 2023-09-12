package it.tndigitale.a4g.uma.business.service.comuni;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.uma.business.persistence.entity.ComuneModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ComuneDao;
import it.tndigitale.a4g.uma.dto.ComuneDto;

@Service
public class ComuniService {
	
	@Autowired
	private ComuneDao comuneDao;
	
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
	
}
