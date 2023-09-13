package it.tndigitale.a4g.uma.business.service.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.uma.business.persistence.entity.DistributoreModel;
import it.tndigitale.a4g.uma.business.persistence.repository.DistributoriDao;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;

@Component
public class UmaUtenteClient extends AbstractClient {

	@Autowired
	private DistributoriDao distributoriDao;
	
	private static final Logger logger = LoggerFactory.getLogger(UmaAnagraficaClient.class);

	// Methods from Controllers
	public DistributoreDto getDistributoreById(Long identificativoDistributore) {
		Optional<DistributoreModel> optDistributore = distributoriDao.findByIdentificativo(identificativoDistributore);
		if (optDistributore.isPresent()) {
			DistributoreModel c = optDistributore.get();
			return new DistributoreDto(
					c.getId(), 
					c.getDenominazione(),
					c.getComune(),
					c.getIndirizzo(),
					c.getProvincia(),
					c.getIdentificativo());
		}
		return null;
	}

	public List<DistributoreDto> getDistributori() {
		List<DistributoreModel> distributoriModel = distributoriDao.findAll();
		return distributoriModel.stream().map(c -> new DistributoreDto(
				c.getId(), 
				c.getDenominazione(),
				c.getComune(),
				c.getIndirizzo(),
				c.getProvincia(),
				c.getIdentificativo())).collect(Collectors.toList());
	}
	
}
